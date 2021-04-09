package com.example.todotasks.viewmodel;

import android.content.Context;
import android.os.Build;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import com.example.todotasks.model.Task;
import com.example.todotasks.repository.TaskRepository;
import com.example.todotasks.taskdb.TaskDao;
import com.example.todotasks.taskdb.TaskRoomDataBase;
import com.google.common.base.Verify;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.robolectric.annotation.Config;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

@RunWith(AndroidJUnit4.class)

public class viewModelTest {

    TaskViewModel viewModel;

    TaskRepository repository;

    @Mock
    Observer<List<Task>> observer;
    List<Task> mTasks;



    @Rule
    public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();


    private TaskRoomDataBase dataBase;
    private TaskDao dao;
    Context context;


    @Before
    public void setUp() throws InterruptedException {
        context= InstrumentationRegistry.getInstrumentation().getContext();

        dataBase= Room.inMemoryDatabaseBuilder(context
                ,TaskRoomDataBase.class)
                .allowMainThreadQueries()
                .build();
        dao=dataBase.taskDao();
        repository=new TaskRepository(dataBase);
        viewModel= new TaskViewModel(repository);
       viewModel.insertTask(new Task("this","details",7.0,0.0,"this add"));
        mTasks = LiveDataTestUtils.getValue(viewModel.getAllTasks());

    }
    @After
    public void packUp(){
        dataBase.close();
    }
    @Test
    public void isNull(){
        assertNotNull(viewModel);
        assertNotNull(repository);

    }
    @Test
    public void viewModelTest() throws InterruptedException {


        assertThat(mTasks.size(),is(1));

        //  test.add(new Task("this","details",7.0,0.0,"this add"));
        //viewModel.insertTask(new Task("this","details",7.0,0.0,"this add"));
        // assertThat(mTasks.get(0),is(test.get(0)));
        //when(viewModel.getAllTasks()).thenReturn((LiveData<List<Task>>) test);
        // viewModel.insertTask(test.get(0));


    }
    @Test
    public void upDataTask() throws InterruptedException {
        viewModel.insertTask(new Task("this","details",7.0,0.0,"this add"));
        mTasks = LiveDataTestUtils.getValue(viewModel.getAllTasks());
        mTasks.get(0).setTaskAddress("add");

        assertThat(mTasks.get(0).getTaskAddress(),is("add"));
    }
    @Test
    public void deleteTask(){
        dao.deleteAll();
        assertEquals(0, mTasks.size());
    }

}

