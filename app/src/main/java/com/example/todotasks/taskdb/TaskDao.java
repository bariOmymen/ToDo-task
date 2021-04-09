package com.example.todotasks.taskdb;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;

import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.todotasks.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
@Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Task task);
@Query("SELECT * FROM `tasks table` ORDER BY tasks ASC")
    LiveData<List<Task>> getAllTasks();
@Update(onConflict = OnConflictStrategy.REPLACE)
    void updateTask(Task task);
@Delete
    void deleteTask(Task task);
@Query("DELETE FROM `tasks table`")
    void deleteAll();

}
