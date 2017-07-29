package com.clement.tvscheduler.activity.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.support.v4.view.MotionEventCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.clement.tvscheduler.R;
import com.clement.tvscheduler.TVSchedulerConstants;
import com.clement.tvscheduler.activity.TaskListActivityI;
import com.clement.tvscheduler.object.Task;
import com.clement.tvscheduler.task.task.UpdateTodoTask;

import java.util.List;

/**
 * Created by cleme on 30/10/2016.
 */
public class TasksAdapter implements ListAdapter {

    private List<Task> tasks;

    private TaskListActivityI mainActivity;

    private ListView listViewTodos;

    public TasksAdapter(List<Task> tasks, TaskListActivityI mainActivity, ListView parentView) {
        this.tasks = tasks;
        this.mainActivity = mainActivity;
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

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final View rowView;
        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) mainActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            rowView = inflater.inflate(R.layout.task_item, null);
            rowView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    int action = MotionEventCompat.getActionMasked(event);

                    switch (action) {
                        case (MotionEvent.ACTION_DOWN):
                            Log.d(TVSchedulerConstants.DEBUG_TAG, "Action was DOWN in " + position);
                            positionStartSwiping = position;
                            positionStartSwipingX = event.getX();

                            return true;
                        case (MotionEvent.ACTION_MOVE):
                            //     Log.d(DEBUG_TAG, "Action was MOVE for  " + position + " at X=" + event.getX());
                            return true;
                        case (MotionEvent.ACTION_UP):
                            Log.d(TVSchedulerConstants.DEBUG_TAG, "Action was UP in " + position);
                            if (position == positionStartSwiping) {
                                if (event.getX() - positionStartSwipingX > 0) {
                                    Log.d(TVSchedulerConstants.DEBUG_TAG, "The swipe was done left to right");
                                    Task task = tasks.get(position);
                                    Log.d(TVSchedulerConstants.DEBUG_TAG, "Suppression de " + task.getName());
                                    mainActivity.askConfirmationBeforeRemoving(task.getId(), task.getName());
                                }
                            }
                            return true;
                        case (MotionEvent.ACTION_CANCEL):
                            Log.d(TVSchedulerConstants.DEBUG_TAG, "Action was CANCEL in " + position);
                            /** In case the swipe could not end, we reset the swipe*/
                            positionStartSwiping = -1;
                            return true;
                        case (MotionEvent.ACTION_OUTSIDE):
                            Log.d(TVSchedulerConstants.DEBUG_TAG, "Movement occurred outside bounds " +
                                    "of current screen element");
                            /** In case the swipe could not end, we reset the swipe*/
                            positionStartSwiping = -1;
                            return true;
                        default:
                            return rowView.onTouchEvent(event);
                    }
                }
            });
        } else {
            rowView = convertView;
        }

        TextView nameTextView = (TextView) rowView.findViewById(R.id.taskLabel);
        TextView ownerTextView = (TextView) rowView.findViewById(R.id.taskOwner);
        nameTextView.setText(tasks.get(position).getName());
        ownerTextView.setText(tasks.get(position).getOwner());
        final Task task = tasks.get(position);
        final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
        checkBox.setChecked(task.getDone());
        checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                task.setDone(checkBox.isChecked());
                UpdateTodoTask updateTodoTask = new UpdateTodoTask(mainActivity, task);
                updateTodoTask.execute();
                Log.i(TVSchedulerConstants.ACTIVITY_TAG__TAG, "Click sur la tâche " + task.getId());
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
