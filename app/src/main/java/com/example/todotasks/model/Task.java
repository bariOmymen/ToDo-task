package com.example.todotasks.model;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;
import androidx.room.Update;

@Entity(tableName = "tasks table")
public class Task {
    @PrimaryKey(autoGenerate = true)
    private int id;
    @NonNull
    @ColumnInfo(name = "tasks")
    private String title;
    private String details;
    private double lat;
    private double lon;
    private String taskAddress;

    public Task(String title, String details,double lat,double lon, String address){this.title=title;
    this.details=details;
    this.lat=lat;
    this.lon=lon;
    this.taskAddress=address;
    }

    public String getTitle() {
        return this.title;
    }

    public String getDetails() {
        return this.details;
    }

    public double getLat() {
        return this.lat;
    }

    public double getLon() {
        return this.lon;
    }

    public String getTaskAddress(){return this.taskAddress;}


    public void setDetails(String details) {
        this.details = details;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setTaskAddress(String address){this.taskAddress=address;}
    @Ignore
    public Task(String title, String details,double lat,double lon){this.title=title;
        this.details=details;
        this.lat=lat;
        this.lon=lon;
    this.id=id;
    }

    public int getId(){return this.id;}
    public void setId(int id){this.id=id;}

    public Task(){}
}
