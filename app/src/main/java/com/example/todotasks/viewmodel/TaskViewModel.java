package com.example.todotasks.viewmodel;

import android.app.Application;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.hilt.lifecycle.ViewModelInject;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelStoreOwner;

import com.example.todotasks.model.Task;
import com.example.todotasks.onClickListener;
import com.example.todotasks.repository.TaskRepository;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

public class TaskViewModel extends ViewModel  {
private TaskRepository repository;
LiveData<List<Task>> allTasks;
public Task mainTask;
private static TaskViewModel viewModelInstance;

@ViewModelInject
@Inject
    public TaskViewModel( TaskRepository repository) {
        //repository= new TaskRepository(application);
    this.repository=repository;
        allTasks=repository.getAllTasks();


    }
  /*  public Task getTaskAt(int position){
    List<Task> tasks = (List<Task>) allTasks;
    return tasks.get(position);
    }*/
    public LiveData<List<Task>> getAllTasks(){return allTasks;}
    public void insertTask(Task task){repository.insertTask(task);}
    public void updateTask(Task task){repository.updateTask(task);}
    public void deleteTask(Task task){repository.deleteTask(task);}
    public void deleteAll(){repository.deleteAll();}

    public void setMainTask(Task mainTask){

        this.mainTask=mainTask;
    }

    public Task getMainTask(){
        if(mainTask==null){

            }else {Log.i(mainTask.getTitle(),mainTask.getDetails());
            }

        return mainTask;}
        public static TaskViewModel getViewModelInstance(ViewModelStoreOwner owner){
        if(viewModelInstance==null){
            return viewModelInstance= new ViewModelProvider(owner).get(TaskViewModel.class);
        }else {return viewModelInstance;}

        }


}
