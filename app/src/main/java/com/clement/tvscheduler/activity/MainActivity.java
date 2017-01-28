package com.clement.tvscheduler.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.clement.tvscheduler.R;
import com.clement.tvscheduler.TodosAdapter;
import com.clement.tvscheduler.dialog.PinDialog;
import com.clement.tvscheduler.task.BaseTask;
import com.clement.tvscheduler.task.CreditTask;
import com.clement.tvscheduler.task.ListTodoTask;
import com.clement.tvscheduler.task.PunitionTask;
import com.clement.tvscheduler.task.TVStatusTask;
import com.clement.tvscheduler.object.Todo;

import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity extends AppCompatActivity implements ConnectedActivity {

    public final static String TAG = "MainActivity";

    public static final DateFormat datFormatSimple;

    static {
        datFormatSimple = new SimpleDateFormat("EEE dd, HH:mm", Locale.FRANCE);
        datFormatSimple.setTimeZone(TimeZone.getTimeZone("Europe/Paris"));
    }


    DecimalFormat midPinFormat = new DecimalFormat("00");

    private Button tvOn;

    private Button tvOff;

    private Button tvCredit30;

    private Button punition;

    private Button prive;

    private Button recompense;

    private Button tvCredit60;

    private TextView remainingTimeView;

    private TextView relayStatusView;

    private TextView tvStatusView;

    private TextView nextCreditView;

    private TextView consumedTodayView;

    private ListView listViewTodos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        init();
        TVStatusTask tvStatusTask = new TVStatusTask(this);
        Log.d(TAG, "Passage sur on create");
        Toolbar myToolbar = (Toolbar) findViewById(R.id.my_toolbar);

        /**
         * Création de la toolbar
         */
        myToolbar.setTitle("Distribaffe");
        myToolbar.setSubtitle("Pour enfants gentils et méchants");
        setSupportActionBar(myToolbar);
        //  Cheecking tasks
        tvStatusTask.execute();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent i;
        switch (item.getItemId()) {
            case R.id.create_new:
                i = new Intent(MainActivity.this, CreateTaskActivity.class);
                startActivity(i);
                return true;
            case R.id.liste_course:
                i = new Intent(MainActivity.this, ListeCourseActivity.class);
                startActivity(i);
                return true;
            default:
                return super.onOptionsItemSelected(item);

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        TVStatusTask tvStatusTask = new TVStatusTask(this);
        Log.d(TAG, "Passage sur on resume");
        tvStatusTask.execute();

        ListTodoTask listTodoTask = new ListTodoTask(this);
        listTodoTask.execute();
    }

    /**
     * Intitialistation des composants
     */
    private void init() {
        tvOn = (Button) findViewById(R.id.button_on);
        tvOff = (Button) findViewById(R.id.button_off);
        tvCredit30 = (Button) findViewById(R.id.button_30);
        tvCredit60 = (Button) findViewById(R.id.button_60);
        punition = (Button) findViewById(R.id.button_punition);
        prive = (Button) findViewById(R.id.button_prive);
        recompense = (Button) findViewById(R.id.button_recompense);
        remainingTimeView = (TextView) findViewById(R.id.remainingTime_view);
        relayStatusView = (TextView) findViewById(R.id.relayStatus_view);
        nextCreditView = (TextView) findViewById(R.id.nextCredit_view);
        consumedTodayView = (TextView) findViewById(R.id.consumedToday_view);
        tvStatusView = (TextView) findViewById(R.id.tvStatus_view);
        listViewTodos = (ListView) findViewById(R.id.listTodos);

        tvOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV ON");
                requestServerCredit(-2);

            }
        });
        tvOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV Off");
                requestServerCredit(-1);
            }
        });
        tvCredit30.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV 30");
                requestServerCredit(1800);
            }
        });
        tvCredit60.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur TV 60");
                requestServerCredit(3600);
            }
        });
        punition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur punition");
                requestServerPunition(-20);
            }
        });
        prive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur punition");
                requestServerPunition(-1000);

            }
        });

        recompense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG, "Click sur punition");
                requestServerPunition(20);
            }
        });

    }

    /**
     * @param message
     */
    public void showMessage(String message) {
        Context context = getApplicationContext();
        CharSequence text = message;
        int duration = Toast.LENGTH_SHORT;
        Toast toast = Toast.makeText(context, text, duration);
        toast.show();
    }

    /**
     * @param timeRemaining
     */

    public void setTimeRemaining(String timeRemaining) {
        remainingTimeView.setText(timeRemaining);
    }

    public void setRelayStatus(String relayStatus) {
        relayStatusView.setText(relayStatus);
    }

    public void setConsumedToday(String relayStatus) {
        consumedTodayView.setText(relayStatus);
    }

    public void setNextCredit(Date nextCredit, Integer numberOfMinutes) {
        nextCreditView.setText(numberOfMinutes.toString() + "mn credite le " + datFormatSimple.format(nextCredit));
    }

    public void setTvStatus(String tvStatus) {
        tvStatusView.setText(tvStatus);
    }


    /**
     * Reauest a credit to the server
     *
     * @param credit
     */
    void requestServerCredit(int credit) {
        int netType;
        CreditTask creditTask = new CreditTask(MainActivity.this, credit);
        enterPin(creditTask);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Reauest a credit to the server
     *
     * @param punition
     */
    void requestServerPunition(int punition) {
        PunitionTask puntionTask = new PunitionTask(MainActivity.this, punition);
        enterPin(puntionTask);
    }

    /**
     * This creates a dialog to enter the pin
     */
    public void enterPin(BaseTask asyncTask) {
        Double d = Math.ceil(Math.random() * 100);
        int random = d.intValue();
        String midPin = midPinFormat.format(random);
        PinDialog newFragment = new PinDialog();
        newFragment.setMidPin(midPin);
        newFragment.setBaseTask(asyncTask);
        FragmentManager fm = getSupportFragmentManager();
        newFragment.show(fm, "pin");
    }

    /**
     * This is a callback function after the right pin has been entered.
     *
     * @param midPin
     * @param valueEntered
     * @param asyncTask
     */
    public void checkPin(String midPin, String valueEntered, BaseTask asyncTask) {
        String expectedResult = "1" + midPin + "1";
        if (expectedResult.equals(valueEntered)) {
            Log.i(TAG, "La bonne valeur a ete entree");
            asyncTask.execute();
        } else {
            Log.i(TAG, "La mauvaise valeur a ete entree");
        }
    }


    public void setTodos(List<Todo> todos) {
        ListAdapter listAdapter = new TodosAdapter(todos, this, listViewTodos);
        listViewTodos.setAdapter(listAdapter);
        listViewTodos.setEmptyView(findViewById(R.id.empty_todos_view));


    }

}