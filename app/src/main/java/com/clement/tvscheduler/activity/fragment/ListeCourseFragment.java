package com.clement.tvscheduler.activity.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.clement.tvscheduler.R;
import com.clement.tvscheduler.AppConstants;
import com.clement.tvscheduler.activity.adapter.CoursesAdapter;
import com.clement.tvscheduler.object.Achat;
import com.clement.tvscheduler.task.achat.AddAchatTask;
import com.clement.tvscheduler.task.achat.ListAchatTask;
import com.clement.tvscheduler.task.achat.ListSuggestAchatTask;
import com.clement.tvscheduler.task.achat.RemoveAchatTask;

import java.util.List;

public class ListeCourseFragment extends BaseFragment{


    private ListView listViewAchats;
    /**
     * the button to add the purchase
     */
    private Button achatAjoutBtn;
    /**
     * The drop down to list the potential items
     */
    private AutoCompleteTextView achatAjoutEdt;


    private View fragmentView;


    /**
     * The suggested list for the pruchase
     */
    private ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.liste_course, container, false);
        init(v);
        this.fragmentView = v;
        Log.d(AppConstants.ACTIVITY_TAG__TAG, "Passage sur on createView de CourseFragment");
        ListAchatTask listAchatTask = new ListAchatTask(this);
        listAchatTask.execute();
        getSuggest();
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


    public Object getSystemService(String layoutInflaterService) {
        return getActivity().getSystemService(layoutInflaterService);
    }


    /**
     * Intitialistation des composants
     */
    private void init(View v) {
        listViewAchats = (ListView) v.findViewById(R.id.list_courses_lst);
        achatAjoutEdt = (AutoCompleteTextView) v.findViewById(R.id.achat_ajout_edt);
    }

    /**
     * Called once the achat are retrived from the database
     *
     * @param achats
     */

    public void setAchats(List<Achat> achats) {
        ListAdapter listAdapter = new CoursesAdapter(achats, this, listViewAchats);

        listViewAchats.setAdapter(listAdapter);
        listViewAchats.setEmptyView(fragmentView.findViewById(R.id.empty_courses_view));
        achatAjoutBtn = (Button) fragmentView.findViewById(R.id.achat_ajout_btn);
        achatAjoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(AppConstants.ACTIVITY_TAG__TAG, "Click sur le bouton ajout");
                Achat achat = new Achat();
                achat.setName(achatAjoutEdt.getText().toString());
                AddAchatTask addAchatTask = new AddAchatTask(ListeCourseFragment.this, achat);
                addAchatTask.execute();
            }
        });
    }

    /**
     * Called once the suggestion list is retrived from the database
     *
     * @param suggestions
     */

    public void setSuggestAchats(List<String> suggestions) {
        adapter = new ArrayAdapter<String>(getActivity(),
                android.R.layout.simple_dropdown_item_1line, suggestions);
        achatAjoutEdt.setAdapter(adapter);
    }

     /**
     * This asks a confirmation before removing an item
     *
     * @param achatId
     * @param achatName
     */
    public void askConfirmationBeforeRemoving(final String achatId, String achatName) {
        AlertDialog alert = new AlertDialog.Builder(getActivity())
                .setTitle("Confirmation")
                .setMessage("Etes vous sur de vouloir supprimer " + achatName + " ?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        RemoveAchatTask removeAchatTask = new RemoveAchatTask(ListeCourseFragment.this, achatId);
                        removeAchatTask.execute();
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    /**
     * This retrieve the purchase recorded
     */
    public void refreshListeAchat() {
        ListAchatTask listAchatTask = new ListAchatTask(this);
        listAchatTask.execute();
        achatAjoutEdt.setText("");
    }

    /**
     * This retrieve the purchase recorded
     */
    public void getSuggest() {
        ListSuggestAchatTask listAchatTask = new ListSuggestAchatTask(this);
        listAchatTask.execute();

    }

    /**
     *
     */
    public void achatTermine() {
        ListAchatTask listAchatTask = new ListAchatTask(this);
        listAchatTask.execute();
    }


}

