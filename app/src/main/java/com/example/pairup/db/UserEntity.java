package com.example.pairup.db;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "user")
public class UserEntity {

    @PrimaryKey(autoGenerate = true)
    public long id;

    @ColumnInfo(name = "gmail")
    public String gmail;

    @ColumnInfo(name = "password")
    public String password;

    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "biography")
    public String biography;

    @ColumnInfo(name = "languages")
    public String languages;

    @ColumnInfo(name = "color")
    public String color;

    public UserEntity() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGmail() {
        return gmail;
    }

    public void setGmail(String gmail) {
        this.gmail = gmail;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBiography() { return biography; }

    public void setBiography(String biography) {
        this.biography = biography;
    }

    public String getLanguages() {
        return languages;
    }

    public void setLanguages(String languages) { this.languages = languages; }

    public String getColor() { return color; }

    public void setColor(String color) { this.color = color; }
}
