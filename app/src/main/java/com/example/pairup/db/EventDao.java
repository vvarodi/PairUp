package com.example.pairup.db;

import androidx.room.Dao;
import androidx.room.Query;

import java.util.List;

@Dao
public interface EventDao {

    @Query("SELECT * from event")
    List<EventEntity> getAllEvents();

}
