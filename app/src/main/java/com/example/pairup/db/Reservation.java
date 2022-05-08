package com.example.pairup.db;

import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Junction;
import androidx.room.PrimaryKey;
import androidx.room.Relation;

import com.example.pairup.db.EventEntity;
import com.example.pairup.db.UserEntity;

import java.util.List;

@Entity(primaryKeys = {"id_user", "id_event"})
public class Reservation {

    @ColumnInfo(name="id_user", index = true)
    public long id_user;

    @ColumnInfo(name="id_event", index = true)
    public long id_event;

    public Reservation(long id_user, long id_event) {
        this.id_user = id_user;
        this.id_event = id_event;
    }
}
