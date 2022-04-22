package com.example.pairup.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    void NewEvent(EventEntity EventEntity);

    @Query("SELECT * FROM event ORDER BY strftime('%Y-%d-%m', day) DESC")
    List<EventEntity> getAllEvents();

}
