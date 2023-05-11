package com.seze.meditopiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView titleTextView;
    private Button loginButton;
    private Button createAccountButton;
    private MeditopiaDAO mMeditopiaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getDatabase();

        List<User> users = mMeditopiaDAO.getAllUsers();
        if(users.size() <= 0){
            User MeditopiaAdmin = new User("MeditopiaAdmin", "admin123", true);
            Subscription FreeAdmin = new Subscription(true,true,true,1);
            Rating FiveStars = new Rating(5,5,5,1);
            mMeditopiaDAO.insert(MeditopiaAdmin);
            mMeditopiaDAO.insert(FreeAdmin);
            mMeditopiaDAO.insert(FiveStars);
            User ToastyBanana = new User("ToastyBanana ", "MamaCita123", false);
            Subscription ToastyBananaS = new Subscription(true,true,true,2);
            Rating ToastyBananaR = new Rating(3, 2, 5, 2);
            mMeditopiaDAO.insert(ToastyBanana);
            mMeditopiaDAO.insert(ToastyBananaS);
            mMeditopiaDAO.insert(ToastyBananaR);
            User WittyKitty = new User("WittyKitty", "BaconEater", false);
            Subscription WittyKittyS = new Subscription(true, false,false,3);
            Rating WittyKittyR = new Rating(1.5f, 4, 3.5f, 3);
            mMeditopiaDAO.insert(WittyKitty);
            mMeditopiaDAO.insert(WittyKittyS);
            mMeditopiaDAO.insert(WittyKittyR);
            User LuckyColty = new User("LuckyColty", "trashcan1",false);
            Subscription LuckyColtyS = new Subscription(false,false,true, 4);
            Rating LuckyColtyR = new Rating(3.5f,5, 2.5f, 4);
            mMeditopiaDAO.insert(LuckyColty);
            mMeditopiaDAO.insert(LuckyColtyS);
            mMeditopiaDAO.insert(LuckyColtyR);

        }

        wireupDisplay();
    }

    private void wireupDisplay() {
        titleTextView = findViewById(R.id.titleTextView);
        loginButton = findViewById(R.id.loginButton);
        createAccountButton = findViewById(R.id.createAccountButton);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginButtonClicked();
            }
        });

        createAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccountButtonClicked();
            }
        });
    }

    public void loginButtonClicked() {
        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);
    }

    public void createAccountButtonClicked() {
        Intent intent = CreateAccountActivity.intentFactory(this);
        startActivity(intent);
    }

    public static Intent intentFactory(Context context) {
        Intent intent = new Intent (context, MainActivity.class);
        return intent;
    }

    private void getDatabase(){
        mMeditopiaDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MeditopiaDAO();
    }

}


