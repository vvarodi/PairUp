package com.example.pairup;


import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {UserEntity.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    private static final String DB_NAME = "pairup.db";
    private static volatile AppDatabase appDatabase;

    public abstract UserDao userDao();

    public static synchronized AppDatabase getUserDatabase(Context context) {
        if (appDatabase == null) {
            appDatabase = create(context);
        }
        return appDatabase;
    }

    private static AppDatabase create(Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME)
                .allowMainThreadQueries()
                .build();
    }


}
