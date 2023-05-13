package com.seze.meditopiaapp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.content.Context;

import androidx.room.Room;
import androidx.test.InstrumentationRegistry;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;


import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class UserDatabaseTest {

    private MeditopiaDAO meditopiaDAO;
    private AppDataBase appDatabase;

    @Before
    public void createDb() {
        Context context = ApplicationProvider.getApplicationContext();
        appDatabase = Room.inMemoryDatabaseBuilder(context, AppDataBase.class).build();
        meditopiaDAO = appDatabase.MeditopiaDAO();
    }

    @After
    public void closeDb() {
        appDatabase.close();
    }

    @Test
    public void insertUser() throws Exception {
        User user = new User("A","A",false);
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setAdmin(false);
        meditopiaDAO.insert(user);
        User loaded = meditopiaDAO.getUserByUserId(1);
        assertEquals(user.getUsername(), loaded.getUsername());
    }

    @Test
    public void updateUser() throws Exception {
        User user = new User("A","B",false);
        user.setUsername("jane_doe");
        user.setPassword("password123");
        user.setAdmin(false);
        meditopiaDAO.insert(user);
        User loaded = meditopiaDAO.getUserByUserId(1);
        loaded.setUsername("jane_smith");
        meditopiaDAO.update(loaded);
        User updated = meditopiaDAO.getUserByUserId(1);
        assertEquals("jane_smith", updated.getUsername());
    }

    @Test
    public void deleteUser() throws Exception {
        User user = new User("A","B",false);
        user.setUsername("john_doe");
        user.setPassword("password123");
        user.setAdmin(false);
        meditopiaDAO.insert(user);
        meditopiaDAO.delete(user);
        User deletedUser = meditopiaDAO.getUserByUserId(user.getUserId());
        assertNull(deletedUser);

    }


}



