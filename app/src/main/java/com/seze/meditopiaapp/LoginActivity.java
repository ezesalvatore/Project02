package com.seze.meditopiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;

import java.util.List;

public class LoginActivity extends AppCompatActivity {
    private EditText mUsernameEditText;
    private EditText mPasswordEditText;
    private Button mLoginButton;
    private MeditopiaDAO mMeditopiaDAO;
    private String mUsername;
    private String mPassword;
    private User mUser;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getDatabase();
        wireupDisplay();
    }

    private void getValuesFromDisplay(){
        mUsername = mUsernameEditText.getText().toString();
        mPassword = mPasswordEditText.getText().toString();
    }

    private void wireupDisplay(){
        mUsernameEditText = findViewById(R.id.usernameEditText);
        mPasswordEditText = findViewById(R.id.passwordEditText);
        mLoginButton = findViewById(R.id.loginButton);

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                getValuesFromDisplay();

                if(checkForUserInDatabase()){
                    if(!validatePassword()){
                        Toast.makeText(LoginActivity.this, "Invalid password", Toast.LENGTH_SHORT).show();
                    } else {
                        Intent intent = LandingPage.intentFactory(getApplicationContext(),mUser.getUserId());
                        startActivity(intent);
                        finish();
                    }
                }
            }
        });
    }
    private boolean checkForUserInDatabase(){
        mUser = mMeditopiaDAO.getUserByUsername(mUsername);
        if(mUser == null){
            Toast.makeText(this, "no user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
    private boolean validatePassword(){
        return mUser.getPassword().equals(mPassword);
    }


    private void getDatabase(){
        mMeditopiaDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MeditopiaDAO();
    }


    public static Intent intentFactory(Context context) {
         Intent intent = new Intent (context, LoginActivity.class);
        return intent;
    }
}



