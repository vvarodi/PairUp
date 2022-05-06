package com.example.pairup.db;


import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

@Dao
public interface UserDao {

    @Insert
    void registerUser(UserEntity UserEntity);

    @Query ("SELECT * from user where gmail=(:gmail) and password=(:password)")
    UserEntity login(String gmail, String password);

    @Query ("SELECT * from user where gmail=(:gmail)")
    UserEntity getCurrentUser(String gmail);

    @Query ("UPDATE user SET biography=:new_biography where gmail=(:gmail)")
    void updateBiography(String gmail, String new_biography);

    @Query ("UPDATE user SET languages=:new_languages where gmail=(:gmail)")
    void updateLanguages(String gmail, String new_languages);

    @Query ("UPDATE user SET color=:new_color where gmail=(:gmail)")
    void updateColor(String gmail, String new_color);


}
