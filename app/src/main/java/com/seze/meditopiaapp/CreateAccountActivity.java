package com.seze.meditopiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;

import java.util.List;

public class CreateAccountActivity extends AppCompatActivity {

    private EditText mNewUsername;
    private EditText mNewPassword;
    private Button createAccountButton;
    private Button backToMainButton;
    private Subscription mSubscription;
    private Rating mRating;
    private MeditopiaDAO mMeditopiaDAO;
    private String mUsername;
    private String mPassword;
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);

        getDatabase();
        wireupDisplay();
    }

    private void wireupDisplay() {
        mNewUsername= findViewById(R.id.createAccountUsernameEditText);
        mNewPassword= findViewById(R.id.createAccountPasswordEditText);
        createAccountButton = findViewById(R.id.createAccountButton);
        backToMainButton = findViewById(R.id.createAccountBackButton);

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createAccountButtonClicked();
            }
        });

        backToMainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                backToMainButtonClicked();
            }
        });
    }

    private void createAccountButtonClicked() {

        getValuesFromDisplay();



        if (!userExists(mUsername)) {
            accountCreation(mUsername, mPassword);
            subscriptionCreation();
            ratingCreation();
            Intent intent = LandingPage.intentFactory(getApplicationContext(), mUser.getUserId());
            startActivity(intent);
        } else {
            Toast.makeText(this, "This username already exists", Toast.LENGTH_SHORT).show();
        }
    }

    private void backToMainButtonClicked() {
        Intent intent = MainActivity.intentFactory(this);
        startActivity(intent);
    }

    private void getValuesFromDisplay() {
        if (mNewUsername!= null && mNewPassword!= null) {
            mUsername = mNewUsername.getText().toString();
            mPassword = mNewPassword.getText().toString();
        } else {
            Toast.makeText(this, "Please enter a username and password", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    private boolean userExists(String username) {
        if (username == null) {
            return false;
        }
        List<User> users = mMeditopiaDAO.getAllUsers();
        for (User user : users) {
            if (username.equals(user.getUsername())) {
                return true;
            }
        }
        return false;
    }

    private void accountCreation(String username, String password) {
        User user = new User(username, password, false);
        mMeditopiaDAO.insert(user);
        mUser = mMeditopiaDAO.getUserByUsername(username);
    }

    private void subscriptionCreation(){
       int mUserId = mUser.getUserId();
        mSubscription = new Subscription(false, false, false, mUserId);
        mMeditopiaDAO.insert(mSubscription);
    }

    private void ratingCreation(){
        int mUserId = mUser.getUserId();
        mRating = new Rating(0,0,0,mUserId);
        mMeditopiaDAO.insert(mRating);
    }

    private void getDatabase(){
        mMeditopiaDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MeditopiaDAO();
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent (context, CreateAccountActivity.class);
        return intent;
    }

}
