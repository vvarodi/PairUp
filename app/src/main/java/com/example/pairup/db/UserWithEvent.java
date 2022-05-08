package com.example.pairup.db;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class UserWithEvent {
    @Embedded public UserEntity user;
    @Relation(
            parentColumn = "id_user",
            entityColumn = "id_event",
            associateBy = @Junction(value = Reservation.class)
    )
    public List<EventEntity> events;
}