package com.clement.task.activity.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioGroup;

import com.clement.task.R;
import com.clement.task.AppConstants;
import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.activity.adapter.TasksAdapter;
import com.clement.task.object.Task;
import com.clement.task.task.task.AddTodoTask;
import com.clement.task.task.task.ListTodoTask;
import com.clement.task.task.task.RemoveTodoTask;

import java.util.List;

/**
 * Created by cleme on 29/07/2017.
 */

public class TaskFragment extends BaseFragment implements TaskListFragmentI {


    private Button createTaskBtn;

    private EditText todoAjoutEdt;

    private ListView listViewTasks;

    private RadioGroup radioGrouOwner;

    private CheckBox checkBoxTemporary;

    private View fragmentView;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_tasks, container, false);
        init(v);
        this.fragmentView = v;
        Log.d(AppConstants.ACTIVITY_TAG__TAG, "Passage sur on createView de TaskFragment");
        refreshTaskList();
        return v;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.create_new:
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }


    /**
     * Intitialistation des composants
     */
    private void init(View v) {
        createTaskBtn = (Button) v.findViewById(R.id.todo_ajout_btn);

        todoAjoutEdt = (EditText) v.findViewById(R.id.todo_ajout_edt);
        createTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Task task = new Task();
                task.setName(todoAjoutEdt.getText().toString());
                task.setTemporary(checkBoxTemporary.isChecked());
                Integer idSelectedOwned = radioGrouOwner.getCheckedRadioButtonId();
                if (idSelectedOwned == R.id.idCesarRadio) {
                    task.setOwner("César");
                } else if (idSelectedOwned == R.id.idHomeRadio) {
                    task.setOwner("Home");
                } else {
                    task.setOwner("Home");
                }

                AddTodoTask addTodoTask = new AddTodoTask(TaskFragment.this, task);
                addTodoTask.execute();
            }
        });

        listViewTasks = (ListView) v.findViewById(R.id.listTasks);
        radioGrouOwner = (RadioGroup) v.findViewById(R.id.idOwnerRadioGroup);
        radioGrouOwner.check(R.id.idHomeRadio);
        checkBoxTemporary = (CheckBox) v.findViewById(R.id.checkboxTemporary);

    }

    /**
     * Action to be performed after a task is saved.
     */
    public void taskEnregistre() {
        todoAjoutEdt.setText("");
        radioGrouOwner.check(R.id.idHomeRadio);
        checkBoxTemporary.setChecked(false);
        refreshTaskList();

    }

    public void setTasks(List<Task> tasks) {
        ListAdapter listAdapter = new TasksAdapter(tasks, this, listViewTasks);
        listViewTasks.setAdapter(listAdapter);
        listViewTasks.setEmptyView(fragmentView.findViewById(R.id.empty_tasks_view));
    }


    @Override
    public void askConfirmationBeforeRemoving(final String id, String name) {
        {
            AlertDialog alert = new AlertDialog.Builder(getActivity())
                    .setTitle("Confirmation")
                    .setMessage("Etes vous sur de vouloir supprimer " + name + " ?")
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            RemoveTodoTask removeAchatTask = new RemoveTodoTask(TaskFragment.this, id);
                            removeAchatTask.execute();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
    }

    @Override
    public void showMessage(String message) {
    }

    @Override
    public void refreshTaskList() {
        checkSync();


        ListTodoTask listTodoTask = new ListTodoTask(this);
        listTodoTask.execute();
    }

    /**
     * Check if some elements require a synchronization
     */
    private void checkSync() {

        List<Task> tasks = getTaskSQLiteHelper().listTasks(true);
        if (tasks.size() > 0) {
            Log.d(AppConstants.ACTIVITY_TAG__TAG, "There are some task remainin to sync");
        }else {
            Log.d(AppConstants.ACTIVITY_TAG__TAG, "No tasks to sync");
        }
    }

    @Override
    public void setTodos(List<Task> tasks) {
        ListAdapter listAdapter = new TasksAdapter(tasks, this, listViewTasks);
        listViewTasks.setAdapter(listAdapter);
        listViewTasks.setEmptyView(fragmentView.findViewById(R.id.empty_tasks_view));
    }
}
