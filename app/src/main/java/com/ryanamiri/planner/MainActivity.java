package com.ryanamiri.planner;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.TimePickerDialog;
import android.database.Cursor;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private EditText taskName;
    private EditText taskTime;
    private ListView tasksView;
    private TextView dateText;
    private TextView tipText;

    private TimePickerDialog timePicker;

    private SQLite db;
    private ArrayList<Task> tasks;

    private String[] tips = {"Set goals correctly. Set goals that are achievable and measurable.", "Prioritize wisely. Prioritize tasks based on importance and urgency.", "Set a time limit to complete a task.", "Take a break between tasks.", "Organize yourself.", "Remove non-essential tasks/activities.", "Plan ahead."};

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskName = findViewById(R.id.taskName);
        taskTime = findViewById(R.id.taskTime);
        tasksView = findViewById(R.id.tasks);
        dateText = findViewById(R.id.dateText);
        tipText = findViewById(R.id.tipText);

        Calendar currentTime = Calendar.getInstance();
        int day = currentTime.get(Calendar.DAY_OF_MONTH);
        int month = currentTime.get(Calendar.MONTH) + 1;
        int year = currentTime.get(Calendar.YEAR);
        dateText.setText(month + "/" + day + "/" + year);

        tipText.setText("Tip: " + tips[new Random().nextInt(tips.length)]);

        initializeTimePicker();

        db = new SQLite(this);

        //db.reset();

        try {
            setTasks();
        } catch (ParseException e) {
            Log.d("TASKS", e.toString());
        }

    }

    /*public void initializeTimer(){
        handler.removeCallbacks(runnable);
        handler.postDelayed(runnable = new Runnable() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            public void run() {
                handler.postDelayed(runnable, delay);
                Log.d("TASKS", "Here");
                for(Task task: tasks){
                    if(task.getElapsed() >= -60000 && task.getElapsed() <= 60000){
                        Log.d("TASKS", "Task " + task.getId() + " is notified.");
                        NotificationManager notif=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                        Notification notify=new Notification.Builder(getApplicationContext()).setContentTitle("Start your task!").setContentText("It's time to start your task \"" + task.getName() + "\"!").setSmallIcon(R.drawable.ic_launcher_foreground).build();
                        notify.flags |= Notification.FLAG_AUTO_CANCEL;
                        notif.notify(0, notify);
                    }
                }
            }
        }, delay);
    }*/

    public void sort(){
        Task[] sorted = tasks.toArray(new Task[0]);
        for (int i = 0; i < sorted.length; i++) {
            for (int j = i+1; j < sorted.length; j++) {
                if ( (sorted[i].getElapsed() > sorted[j].getElapsed()) && (i != j) ) {
                    Task temp = sorted[j];
                    sorted[j] = sorted[i];
                    sorted[i] = temp;
                }
            }
        }
        tasks = new ArrayList<>(Arrays.asList(sorted));
    }

    /*
     * Method setTasks Method can be called anytime to retrieve new tasks and reformat the tasks according to the view.
     */
    @SuppressLint("Range")
    public void setTasks() throws ParseException {
         tasks = new ArrayList<Task>();
         Cursor res = db.getData();
         while(res.moveToNext()){
             Log.d("DATABASE", res.getString(0) + " " + res.getString(1) + " " + res.getString(2));
             tasks.add(new Task(res.getString(0), res.getString(1), res.getString(2)));
         }

        sort();

        tasksView.setAdapter(new ListAdapter(this, this, R.layout.list_item, tasks));

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void initializeTimePicker() {
        Calendar currentTime = Calendar.getInstance();
        int hour = currentTime.get(Calendar.HOUR_OF_DAY);
        int minute = currentTime.get(Calendar.MINUTE);

        timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker v, int selectedHour, int selectedMinute) {
                String AM_PM;
                if(selectedHour == 0){
                    selectedHour = 12;
                    AM_PM = "AM";
                }
                else if(selectedHour < 12) {
                    AM_PM = "AM";
                }
                else if(selectedHour == 12){
                    AM_PM = "PM";
                }
                else {
                    selectedHour -= 12;
                    AM_PM = "PM";
                }
                if (selectedMinute < 10)
                    taskTime.setText(selectedHour + ":0" + selectedMinute + " " + AM_PM );
                else
                    taskTime.setText(selectedHour + ":" + selectedMinute + " " + AM_PM );
            }
        }, hour, minute, false);
        timePicker.setTitle("Select Time");
    }

    public void pickTime(View v){
        timePicker.show();
    }

    /*
     * Method addTask, This method is ran when the add task button is pressed, this method then handles adding the task.
     *
     * @param v, The current view (screen).
     */
    public void addTask(View v) throws ParseException {
        if(taskName.getText().toString().equals("") || taskTime.getText().toString().equals(""))
            return;
        boolean result = db.insert(taskName.getText().toString(), taskTime.getText().toString());
        if (result) {
            setTasks();
            Log.d("DATABASE", "Task has been added successfully.");
        }
        else
            Log.d("DATABASE", "Task failed to add.");

    }

    public SQLite getDB(){
        return db;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }
}