package com.example.pairup.db;


import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "event")
public class EventEntity {

    @PrimaryKey(autoGenerate = true)
    Integer id;

    @ColumnInfo(name="language")
    String language;
}
