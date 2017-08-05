package com.clement.task.activity.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.clement.task.R;
import com.clement.task.AppConstants;
import com.clement.task.activity.TaskListFragmentI;
import com.clement.task.object.Task;
import com.clement.task.task.task.UpdateTodoTask;

import java.util.List;

/**
 * Created by cleme on 30/10/2016.
 */
public class TasksAdapter implements ListAdapter {

    private List<Task> tasks;

    private TaskListFragmentI taskFragment;

    private ListView listViewTodos;

    public TasksAdapter(List<Task> tasks, TaskListFragmentI mainActivity, ListView parentView) {
        this.tasks = tasks;
        this.taskFragment = mainActivity;
        this.listViewTodos = parentView;
    }

    static int positionStartSwiping = -1;

    static float positionStartSwipingX = -1F;


    @Override
    public boolean areAllItemsEnabled() {
        return true;
    }

    @Override
    public boolean isEnabled(int position) {
        return true;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver observer) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver observer) {
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    /**
     * This retrieves a view for the Task
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
/**
 * First the view rpresenting the task is created
 */
        final View rowView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) taskFragment
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.task_item, null);

        } else {
            rowView = convertView;
        }
/**
 * The fields of a task are created.
 */
        final Task task = tasks.get(position);

        TextView nameTextView = (TextView) rowView.findViewById(R.id.taskLabel);
        TextView ownerTextView = (TextView) rowView.findViewById(R.id.taskOwner);
        nameTextView.setText(task.getName());
        ownerTextView.setText(task.getOwner());

        /**
         * Then a the actions associated with a task are managed.
         */

       // Handler mHandler = new Handler(Looper.getMainLooper());

        final GestureDetector gdt = new GestureDetector(taskFragment.getContext(), new TaskGestureListener(taskFragment, task.getId(), task.getName()));

        rowView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return gdt.onTouchEvent(event);
            }
        });


        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        checkBox.setChecked(task.getDone());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setDone(checkBox.isChecked());
                UpdateTodoTask updateTodoTask = new UpdateTodoTask(taskFragment, task);
                updateTodoTask.execute();
                Log.i(AppConstants.ACTIVITY_TAG__TAG, "Click sur la tâche " + task.getId());
            }
        });

        return rowView;
    }

    @Override
    public int getItemViewType(int position) {
        return 0;
    }

    /**
     * Returns the number of different biew in the list (In case of similar element this is 1
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return tasks.isEmpty();
    }
}
