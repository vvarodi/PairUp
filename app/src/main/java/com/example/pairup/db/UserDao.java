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

    @Query ("SELECT * from user where id_user=(:id)")
    UserEntity getCurrentUserById(long id);

    @Query ("SELECT * from user where gmail=(:gmail)")
    UserEntity getCurrentUser(String gmail);

    @Query ("UPDATE user SET biography=:new_biography where id_user=(:id)")
    void updateBiography(Long id, String new_biography);

    @Query ("UPDATE user SET languages=:new_languages where id_user=(:id)")
    void updateLanguages(Long id, String new_languages);

    @Query ("UPDATE user SET color=:new_color where id_user=(:id)")
    void updateColor(Long id, String new_color);


}
