package com.example.pairup.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void NewEvent(EventEntity EventEntity);

    @Query("SELECT * FROM event ORDER BY substr(day,7,4)||substr(day,4,2)||substr(day,1,2)")
    List<EventEntity> getAllEvents();

}
