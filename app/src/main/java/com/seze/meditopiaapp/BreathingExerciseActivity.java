package com.seze.meditopiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;

import java.util.List;

public class BreathingExerciseActivity extends AppCompatActivity {

    private VideoView mVideoView;
    private TextView mGuideTextView;
    private RatingBar mExerciseRatingBar;
    private Button mBackButton;
    private static final String USER_ID_KEY = "package com.seze.meditopiaapp.userIdKey";
    private static final String PREFERENCES_KEY = "package com.seze.meditopiaapp.PREFERENCES_KEY";

    private User mUser;
    private Rating mRating;
    private MeditopiaDAO mMeditopiaDAO;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_breathing_exercise);

        mVideoView = findViewById(R.id.videoView);
        mGuideTextView = findViewById(R.id.guideTextView);
        mExerciseRatingBar = findViewById(R.id.exerciseRatingBar);
        mBackButton = findViewById(R.id.backButton);


        // set the video path
//        String videoPath = "android.resource://" + getPackageName() + "/" + R.raw.breathing_exercise;
//        mVideoView.setVideoURI(Uri.parse(videoPath));
//        mVideoView.start();

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

        // set the rating bar listener
        mExerciseRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                if(fromUser){
                    if(mRating != null){
                        mRating.setBreathingExerciseRating(rating);
                        mMeditopiaDAO.update(mRating);
                    }
                }
            }
        });

        // set the back button listener
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public void onBackPressed() {
        // code to handle back button press
        // maybe show an alert dialog to confirm if the user wants to exit the exercise
        // then stop the video and finish the activity
        mVideoView.stopPlayback();
        super.onBackPressed();
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
        Intent intent = new Intent(context, BreathingExerciseActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(USER_ID_KEY, userId);
        context.startActivity(intent);

        return  intent;
    }
}
