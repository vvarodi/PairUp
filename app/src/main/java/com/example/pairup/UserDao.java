package com.example.pairup;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void registerUser(UserEntity UserEntity);

    @Query ("SELECT * from user where gmail=(:gmail) and password=(:password)")
    UserEntity login(String gmail, String password);

}
