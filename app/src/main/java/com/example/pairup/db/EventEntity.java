package com.example.pairup.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "event")
public class EventEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name="day")
    public String day;

    @ColumnInfo(name="title")
    public String title;

    public EventEntity() {
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
