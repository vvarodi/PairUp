package com.example.pairup.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;


@Entity(tableName = "event", foreignKeys = {
        @ForeignKey(
        entity = UserEntity.class,
        parentColumns = "id_user",
        childColumns = "id_event",
        onDelete = ForeignKey.CASCADE,
        onUpdate = ForeignKey.CASCADE
    )
})
public class EventEntity {

    @PrimaryKey(autoGenerate = true)
    public long id_event;

    @ColumnInfo(name="id_user")
    public long id_user;

    @ColumnInfo(name="day")
    public String day;

    @ColumnInfo(name="time")
    public String time;

    @ColumnInfo(name="location")
    public String location;

    @ColumnInfo(name="language")
    public String language;

    @ColumnInfo(name="members")
    public int members;

    @ColumnInfo(name="full")
    public boolean full;

    public EventEntity() {
    }

    public long getId_event() {
        return id_event;
    }

    public void setId_event(long id_event) {
        this.id_event = id_event;
    }

    public long getId_user() {
        return id_user;
    }

    public void setId_user(long id_user) {
        this.id_user = id_user;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public int getMembers() {
        return members;
    }

    public void setMembers(int members) {
        this.members = members;
    }

    public boolean isFull() {
        return full;
    }

    public void setFull(boolean full) {
        this.full = full;
    }
}
