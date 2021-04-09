package com.example.todotasks.module;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.lifecycle.ViewModelStore;
import androidx.lifecycle.ViewModelStoreOwner;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todotasks.model.Task;
import com.example.todotasks.repository.TaskRepository;
import com.example.todotasks.taskdb.TaskDao;
import com.example.todotasks.taskdb.TaskRoomDataBase;
import com.example.todotasks.viewmodel.TaskViewModel;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;


@Module
@InstallIn(ApplicationComponent.class)
public class DataBaseModule {
    static TaskRoomDataBase one;
    static Context context;
    @Provides
    @Singleton
    public static TaskRoomDataBase dataBaseProvider(Application application){
       context=application.getApplicationContext();
         one = Room.databaseBuilder(application,TaskRoomDataBase.class,"image_database")
                .fallbackToDestructiveMigration()
                 //.allowMainThreadQueries()
                .addCallback(callbackRoom)
                .build();
        return one;
    }
    @Provides
    @Singleton
    public static TaskDao taskDaoProvider(TaskRoomDataBase db){
        return db.taskDao();
    }
    public static RoomDatabase.Callback callbackRoom = new RoomDatabase.Callback(){
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new TaskAsyncTask(one).execute();
        }
    };

    public static class TaskAsyncTask extends AsyncTask<Void,Void,Void> {
        private TaskDao dao;
        @Inject
        TaskAsyncTask(TaskRoomDataBase dataBase){dao=dataBase.taskDao();}

        @Override
        protected Void doInBackground(Void... voids) {


           /* SharedPreferences sharedPreferences = context.getSharedPreferences("com.example.todotasks", Context.MODE_PRIVATE);
            Log.i("shared", String.valueOf(sharedPreferences.getBoolean("check", false)));
            if (!sharedPreferences.getBoolean("check", false)) {
                dao.deleteAll();

                Task task = new Task("example", "example detail", 0.0, 0.0,"Task Address");
                dao.insert(task);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean("check", true);
                editor.apply();
                Log.i("shared", String.valueOf(sharedPreferences.getBoolean("check", false)));
            }*/

            return null;
        }
    }
    @Provides
    @Singleton
    public static TaskViewModel viewModelProvider(){
        TaskViewModel viewModel =new ViewModelProvider((ViewModelStoreOwner) context.getApplicationContext()).get(TaskViewModel.class);
        return viewModel;
    }

}
