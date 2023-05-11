package com.seze.meditopiaapp.DB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.seze.meditopiaapp.Rating;
import com.seze.meditopiaapp.Subscription;
import com.seze.meditopiaapp.User;

import java.util.List;
@Dao
public interface MeditopiaDAO {
    @Insert
    void insert(User... users);

    @Update
    void update(User... users);

    @Delete
    void delete(User... users);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE)
    List<User> getAllUsers();

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + "  WHERE mUsername = :username")
    User getUserByUsername(String username);

    @Query("SELECT * FROM " + AppDataBase.USER_TABLE + "  WHERE mUserId = :userId ")
    User getUserByUserId(int userId);


    @Insert
    void insert(Subscription subscription);

    @Update
    void update(Subscription subscription);

    @Delete
    void delete(Subscription subscription);

    @Query("SELECT * FROM " + AppDataBase.SUBSCRIPTION_TABLE + " WHERE mUserId = :subscriptionId")
    Subscription getBySubscriptionId(int subscriptionId);

    @Query("SELECT * FROM " + AppDataBase.SUBSCRIPTION_TABLE)
    List<Subscription> getAllSubscriptionLogs();

    @Insert
    void insert(Rating rating);

    @Update
    void update(Rating rating);

    @Delete
    void delete(Rating rating);

    @Query("SELECT * FROM " + AppDataBase.RATING_TABLE)
    List<Rating> getAllRatingsLogs();
}




