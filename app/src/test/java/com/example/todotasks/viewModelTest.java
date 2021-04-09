package com.example.todotasks;

import android.content.Context;
import android.os.Build;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.room.Room;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.SmallTest;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

import com.example.todotasks.model.Task;
import com.example.todotasks.repository.TaskRepository;
import com.example.todotasks.taskdb.TaskDao;
import com.example.todotasks.taskdb.TaskRoomDataBase;
import com.example.todotasks.ui.MainActivity;
import com.example.todotasks.viewmodel.TaskViewModel;

import junit.framework.TestCase;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runner.manipulation.Ordering;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.util.ArrayList;
import java.util.List;


import javax.inject.Inject;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import com.example.todotasks.LiveDataTestUtils;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(JUnit4.class)


public class viewModelTest {

        TaskViewModel viewModel;

TaskRepository repository;


        List<Task> mTasks;



        @Rule
        public InstantTaskExecutorRule taskExecutorRule = new InstantTaskExecutorRule();


        private TaskRoomDataBase dataBase;
        private TaskDao dao;
    Context context;

        @Before
        public void setUp(){

            context= InstrumentationRegistry.getInstrumentation().getTargetContext();
           dataBase= Room.inMemoryDatabaseBuilder(context
                   ,TaskRoomDataBase.class)
                   .allowMainThreadQueries()
                   .build();
           dao=dataBase.taskDao();
           repository=new TaskRepository(dataBase);
            viewModel= new TaskViewModel(repository);
        }
        @Test
    public void isNull(){
            assertNotNull(viewModel);
            assertNotNull(repository);

        }
        @Test
            public void viewModelTest() throws InterruptedException {
            viewModel.insertTask(new Task("this","details",7.0,0.0,"this add"));
                List<Task> test = LiveDataTestUtils.getValue(viewModel.getAllTasks());

                assertFalse(test.isEmpty());

              //  test.add(new Task("this","details",7.0,0.0,"this add"));
                //viewModel.insertTask(new Task("this","details",7.0,0.0,"this add"));
       // assertThat(mTasks.get(0),is(test.get(0)));
            //when(viewModel.getAllTasks()).thenReturn((LiveData<List<Task>>) test);
           // viewModel.insertTask(test.get(0));


        }

    }

