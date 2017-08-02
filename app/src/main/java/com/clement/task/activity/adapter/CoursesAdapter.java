package com.clement.task.activity.adapter;

import android.content.Context;
import android.database.DataSetObserver;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.clement.task.R;
import com.clement.task.AppConstants;
import com.clement.task.activity.fragment.CourseFragment;
import com.clement.task.object.Achat;
import com.clement.task.task.achat.EndAchatTask;
import com.clement.task.task.achat.UpdateAchatTask;

import java.util.List;

/**
 * Created by cleme on 30/10/2016.
 */
public class CoursesAdapter implements ListAdapter {

    private List<Achat> achats;

    private CourseFragment listeCourseActivity;

    private ListView listView;

    static int positionStartSwiping = -1;

    static float positionStartSwipingX = -1F;


    public CoursesAdapter(List<Achat> achats, CourseFragment listeCourseActivity, ListView parentView) {
        this.achats = achats;
        this.listeCourseActivity = listeCourseActivity;
        this.listView = parentView;
    }

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
        return achats.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return achats.get(position);
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
        /** The layout of the item in the list*/
        final View rowView;

        if (convertView == null) {
            LayoutInflater inflater = (LayoutInflater) listeCourseActivity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            if (position < achats.size()) {
                rowView = inflater.inflate(R.layout.achat_item, null);
            } else {
                rowView = inflater.inflate(R.layout.end_achat_item, null);
            }
        } else {
            rowView = convertView;
        }
        /**
         * In case we want to tag a course as done.
         .*/

        // This test determine is ze qre not on the finir ñes course button;
        if (position < achats.size()) {
            TextView textView = (TextView) rowView.findViewById(R.id.label);
            textView.setText(achats.get(position).getName());
            final Achat achat = achats.get(position);
            final CheckBox checkBox = (CheckBox) rowView.findViewById(R.id.checkBox);
            checkBox.setChecked(achat.getDone());
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    achat.setDone(checkBox.isChecked());
                    UpdateAchatTask updateAchatTask = new UpdateAchatTask(listeCourseActivity, achat);
                    updateAchatTask.execute();
                    Log.i(AppConstants.ACTIVITY_TAG__TAG, "Click sur la course " + achat.getId());
                }
            });

            /**
             * Then a the actions associated with a task are managed.
             */

            final GestureDetector gdt = new GestureDetector(rowView.getContext(), new CourseGestureListener(listeCourseActivity, achat.getId(), achat.getName()));

            rowView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    return gdt.onTouchEvent(event);
                }
            });

        }
        /**
         * In case we are on the end course bouton.
         */
        else {
            Button textView = (Button) rowView.findViewById(R.id.end_achat_btn);
            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    EndAchatTask endAchatTask = new EndAchatTask(listeCourseActivity);
                    endAchatTask.execute();
                    Log.i(AppConstants.ACTIVITY_TAG__TAG, "Click sur la tâche end achat");
                }
            });
        }
        return rowView;
    }

    /**
     * There are two type:
     * <p>
     * The items themselves
     * The end shopping button
     *
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        /**
         * it the item represent an achat
         */
        if (position < achats.size()) {
            return 0;
        }
        /**
         * Else this is the last item --> The button
         */
        else {
            return 1;
        }
    }

    /**
     * Returns the number of different biew in the list (In case of similar element this is 1
     *
     * @return
     */
    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public boolean isEmpty() {
        return achats.isEmpty();
    }
}
