package com.example.todotasks.taskdb;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.todotasks.model.Task;

@Database(entities = {Task.class},version = 3,exportSchema = false)
public abstract class TaskRoomDataBase extends RoomDatabase {
    public abstract TaskDao taskDao();


}
