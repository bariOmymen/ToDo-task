package com.example.todotasks.viewmodel;


import androidx.arch.core.executor.testing.InstantTaskExecutorRule;

import androidx.lifecycle.Observer;


import com.example.todotasks.model.Task;
import com.example.todotasks.repository.TaskRepository;
import com.example.todotasks.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;


import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(JUnit4.class)
public class TaskViewModelTest {

    TaskViewModel viewModel;
    @Mock
    TaskRepository repository;

    @Mock
    Observer<List<Task>> observer;
    List<Task> mTasks;

@Rule
public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();


    @Before
public void setUp(){
       // viewModel= new TaskViewModel(repository);
   // viewModel.getAllTasks().observeForever(observer);
}
@Test
public void notNull(){
    assertNull(repository);
}
/*@Test
    public void viewModelTest(){
        List<Task> test = new ArrayList<>();
        test.add(new Task("this","details",7.0,0.0,"this add"));
        test.add(new Task("this","details",7.0,0.0,"this add"));
        viewModel.insertTask(new Task("this","details",7.0,0.0,"this add"));
assertThat(mTasks.get(0),is(test.get(0)));
    //when(viewModel.getAllTasks()).thenReturn((LiveData<List<Task>>) test);
    viewModel.insertTask(test.get(0));
    verify(observer).onChanged(test);
}*/
@After public void cleanUp(){
        viewModel=null;
        observer=null;
}
}