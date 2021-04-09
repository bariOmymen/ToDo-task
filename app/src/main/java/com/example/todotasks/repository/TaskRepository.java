package com.example.todotasks.repository;

import android.app.Application;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;

import com.example.todotasks.model.Task;
import com.example.todotasks.taskdb.TaskDao;
import com.example.todotasks.taskdb.TaskRoomDataBase;

import java.util.List;

import javax.inject.Inject;
@VisibleForTesting
public class TaskRepository {
    private LiveData<List<Task>> allTasks;
    private TaskDao taskDao;
    @Inject
    public TaskRepository(TaskRoomDataBase db){

    taskDao=db.taskDao();
    allTasks=taskDao.getAllTasks();
}
public LiveData<List<Task>> getAllTasks(){return this.allTasks;}
public void insertTask(Task task){new InsertAsyncTask(taskDao).execute(task);}
public void deleteTask(Task task){new DeleteAsyncTask(taskDao).execute(task);}
public void updateTask(Task task){new UpdateAsyncTask(taskDao).execute(task);}
public void deleteAll(){new DeleteAll(taskDao).execute();}

public static class InsertAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao dao;
        InsertAsyncTask(TaskDao taskDao1){dao=taskDao1;}
    @Override
    protected Void doInBackground(Task... tasks) {
        dao.insert(tasks[0]);
        return null;
    }
}
public static class UpdateAsyncTask extends AsyncTask<Task,Void,Void>{
        private TaskDao dao;
        UpdateAsyncTask(TaskDao taskDao2){dao=taskDao2;}
    @Override
    protected Void doInBackground(Task... tasks) {
        dao.updateTask(tasks[0]);
        return null;
    }
}
public static class DeleteAsyncTask extends AsyncTask<Task,Void,Void>{
        TaskDao dao;
        DeleteAsyncTask(TaskDao taskDao3){dao=taskDao3;}
    @Override
    protected Void doInBackground(Task... tasks) {
        dao.deleteTask(tasks[0]);
        Log.i("Worked","deleteTask");
        return null;
    }
}
public static class DeleteAll extends AsyncTask<Void,Void,Void>{
        TaskDao deleteDao;
DeleteAll(TaskDao deleteDao){
    this.deleteDao=deleteDao;
}
    @Override
    protected Void doInBackground(Void... voids) {
    deleteDao.deleteAll();
        return null;
    }
}
}
