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

public class MeditationLibraryActivity extends AppCompatActivity {

    private TextView mTitleTextView;
    private Button mBreathingExerciseButton;
    private Button mSoothingMusicButton;
    private Button mRelaxationExerciseButton;
    private Button mBackButton;
    private MeditopiaDAO mMeditopiaDAO;
    private static final String USER_ID_KEY = "package com.seze.meditopiaapp.userIdKey";
    private static final String PREFERENCES_KEY = "package com.seze.meditopiaapp.PREFERENCES_KEY";
    private User mUser;
    private Subscription mSubscription;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meditation_library);

        mTitleTextView = findViewById(R.id.meditationLibraryTitle);
        mBreathingExerciseButton = findViewById(R.id.breathingExerciseButton);
        mSoothingMusicButton = findViewById(R.id.soothingMusicButton);
        mRelaxationExerciseButton = findViewById(R.id.relaxationExerciseButton);
        mBackButton = findViewById(R.id.backButton);

        getDatabase();
        int mUserId = getUserId();
        loginUser(mUserId);

        List<Subscription> subscriptions = mMeditopiaDAO.getAllSubscriptionLogs();
        for (Subscription subscription : subscriptions) {
            if (subscription.getUserId() == mUserId) {
                mSubscription = subscription;
                break;
            }
        }

        mBreathingExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean isSubscribed = mSubscription.isBreathingExercisePremium();
                if(isSubscribed){
                    Intent intent = BreathingExerciseActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }else{
                    Intent intent = Get_A_Subscription.intentFactory(getApplicationContext(),mUser.getUserId());
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                    finish();
                }
            }
        });

        mSoothingMusicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when soothingMusicButton is clicked
            }
        });

        mRelaxationExerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Code to execute when relaxationExerciseButton is clicked
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPage.intentFactory(getApplicationContext(),mUser.getUserId());
                startActivity(intent);
                finish();
            }
        });
    }

    private void getDatabase(){
        mMeditopiaDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MeditopiaDAO();
    }

    private int getUserId() {
        int userId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (userId == -1) {
            SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
            userId = preferences.getInt(USER_ID_KEY, -1);
        }

        return userId;
    }

    private void loginUser(int userId){
        mUser = mMeditopiaDAO.getUserByUserId(userId);
    }


    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, MeditationLibraryActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(USER_ID_KEY, userId);
        context.startActivity(intent);

        return  intent;
    }

}