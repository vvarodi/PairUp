package com.example.pairup.db;

import androidx.room.Embedded;
import androidx.room.Junction;
import androidx.room.Relation;

import java.util.List;

public class EventWithUsers {
    @Embedded public EventEntity event;
    @Relation(
            parentColumn = "id_event",
            entityColumn = "id_user",
            associateBy = @Junction(value = Reservation.class)
    )
    public List<UserEntity> users;
}