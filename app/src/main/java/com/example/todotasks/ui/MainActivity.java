package com.example.todotasks.ui;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;

import com.example.todotasks.BuildConfig;

import com.example.todotasks.GeofenceBroadcast;
import com.example.todotasks.adpter.ListAdapter;
import com.example.todotasks.R;
import com.example.todotasks.model.Task;
import com.example.todotasks.viewmodel.TaskViewModel;
import com.example.todotasks.onClickListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.StrictMode;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;

import static com.example.todotasks.adpter.ListAdapter.number;

@AndroidEntryPoint
public class MainActivity extends AppCompatActivity implements onClickListener {
    public static RecyclerView recyclerView;
    private static final String PRIMARY_CHANNEL_ID = "channelId";
    private static String Action = "com.example.android.todotasks.ACTION_UPDATE_NOTIFICATION";
    public ListAdapter listAdapter;
   public static TaskViewModel viewModel;
    GeofenceBroadcast geofenceBroadcastReceiver = new GeofenceBroadcast();



    List<Task> tasks2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        createChannel();
        registerReceiver(geofenceBroadcastReceiver, new IntentFilter(Action));

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        recyclerView = findViewById(R.id.recyclerView);
        listAdapter = new ListAdapter(this, this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(listAdapter);

        viewModel = TaskViewModel.getViewModelInstance(this);
        viewModel.getAllTasks().observe(this, new Observer<List<Task>>() {
            @Override
            public void onChanged(List<Task> tasks) {
                listAdapter.setList(tasks);
            }
        });

        ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT |
                ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder,
                                  @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Task task = listAdapter.getTaskAtPosition(viewHolder.getAdapterPosition());
                Toast.makeText(getApplicationContext(), "Task " + task.getTitle() + " is Deleting", Toast.LENGTH_LONG).show();
                viewModel.deleteTask(task);
            }
        });
        helper.attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), AddTasksActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);


            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    public void createChannel() {
        NotificationManager notificationManager;
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(PRIMARY_CHANNEL_ID, "Task Notification", NotificationManager.IMPORTANCE_HIGH);
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.getLightColor();
            channel.setDescription("Notification From ToDo Tasks");
            notificationManager.createNotificationChannel(channel);
        }

    }


    @Override
    public void onClick(Task task, View view) {

        Toast.makeText(getApplicationContext(),task.getTitle()+"fuck this shit",Toast.LENGTH_SHORT).show();
        if(task==null){
            Log.i("MaIN TASK shit","IS NULL");
        }else {Log.i("MaIN TASK shit","IS not NULL");}
        viewModel.setMainTask(task);
        Intent intent = new Intent(getApplicationContext(), AddTasksActivity.class);
        intent.putExtra(number,1);
        startActivity(intent);
    }
}