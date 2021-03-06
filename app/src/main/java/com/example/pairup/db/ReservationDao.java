package com.example.pairup.db;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Transaction;

import java.util.List;

@Dao
public interface ReservationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertUser(UserEntity userEntity);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    public long insertEvent(EventEntity eventEntity);
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insertReservation(Reservation reservation);

    @Transaction
    @Query("SELECT * FROM event")
    public List<EventWithUsers> getEventsWithUsers();

    @Transaction
    @Query("SELECT * FROM event WHERE language like :lang")
    public List<EventWithUsers> getEventsWithUsersByLanguage(String lang);

    @Transaction
    @Query("SELECT * FROM user where id_user=(:id)")
    public List<UserWithEvent> getUsersWithEvents(long id);

}
