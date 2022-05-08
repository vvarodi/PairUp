package com.example.pairup.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface EventDao {

    @Insert
    long NewEvent(EventEntity EventEntity);

    @Query("SELECT * FROM event WHERE id_event=(:id)")
    EventEntity getEvent(long id);

    @Query("SELECT * FROM event ORDER BY substr(day,7,4)||substr(day,4,2)||substr(day,1,2)")
    List<EventEntity> getAllEvents();

}
