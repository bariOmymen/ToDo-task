package com.example.todotasks.module;


import android.app.Application;

import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.example.todotasks.model.Task;
import com.example.todotasks.viewmodel.TaskViewModel;

import java.util.List;

import javax.inject.Inject;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class TaskList {


    @Provides
public LiveData<List<Task>> TaskList(TaskViewModel taskViewModel){

return taskViewModel.getAllTasks();
    }
}
