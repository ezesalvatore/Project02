package com.seze.meditopiaapp.DB;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import android.content.Context;

import com.seze.meditopiaapp.Rating;
import com.seze.meditopiaapp.Subscription;
import com.seze.meditopiaapp.User;

@Database(entities = {User.class, Subscription.class, Rating.class}, version = 1)
public abstract class AppDataBase extends RoomDatabase {

    public static final String DATABASE_NAME = "user-db";

    public static final String USER_TABLE = "username_table";
    public static final String SUBSCRIPTION_TABLE ="subscription_table";
    public static final String RATING_TABLE ="rating_table";

    private static volatile AppDataBase instance;

    public abstract MeditopiaDAO MeditopiaDAO();

    public static synchronized AppDataBase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                            AppDataBase.class, DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return instance;
    }
}

