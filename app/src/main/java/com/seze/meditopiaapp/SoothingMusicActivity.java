package com.seze.meditopiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;

import java.util.List;

public class SoothingMusicActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private TextView mTitleTextView;
    private RatingBar mRatingBar;
    private Button mBackButton;
    private static final String USER_ID_KEY = "package com.seze.meditopiaapp.userIdKey";
    private static final String PREFERENCES_KEY = "package com.seze.meditopiaapp.PREFERENCES_KEY";
    private User mUser;
    private Rating mRating;
    private MeditopiaDAO mMeditopiaDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_soothing_music);

        mVideoView = findViewById(R.id.videoView);
        mTitleTextView = findViewById(R.id.soothingMusicTextView);
        mRatingBar = findViewById(R.id.ratingBar);
        mBackButton = findViewById(R.id.backButton);

        getDatabase();
        int mUserId = getUserId();
        loginUser(mUserId);
        List<Rating> ratings = mMeditopiaDAO.getAllRatingsLogs();
        for (Rating rating : ratings) {
            if (rating.getUserId() == mUserId) {
                mRating = rating;
                break;
            }
        }

        mRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    if(mRating != null){
                        mRating.setSoothingMusicRating(rating);
                        mMeditopiaDAO.update(mRating);
                    }
                }
            }
        });

        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent intent = MeditationLibraryActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

    }
    private int getUserId() {
        int userId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (userId == -1) {
            SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
            userId = preferences.getInt(USER_ID_KEY, -1);
        }

        return userId;
    }
    private void getDatabase(){
        mMeditopiaDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MeditopiaDAO();
    }

    private void loginUser(int userId){
        mUser = mMeditopiaDAO.getUserByUserId(userId);
    }


    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, SoothingMusicActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(USER_ID_KEY, userId);
        context.startActivity(intent);

        return  intent;
    }
}