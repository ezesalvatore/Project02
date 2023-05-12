package com.seze.meditopiaapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;

import java.util.List;

public class AdminActivity extends AppCompatActivity {

    private TextView mRatingLogTextView;
    private EditText mDeleteUserEditText;
    private Button mDeleteUserButton;
    private EditText mAddAdminEditText;
    private Button mAddAdminButton;
    private MeditopiaDAO mMeditopiaDAO;
    private String mUsername;
    private static final String USER_ID_KEY = "package com.seze.meditopiaapp.userIdKey";
    private static final String PREFERENCES_KEY = "package com.seze.meditopiaapp.PREFERENCES_KEY";
    private User mUser;
    private User mDeletedUser;
    private Subscription mSubscription;
    private Rating mRating;
    private String mAdminUsername;
    private User mNewAdmin;
    private Button mBackButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        mRatingLogTextView = findViewById(R.id.ratingLogTextView);
        mDeleteUserEditText = findViewById(R.id.deleteUserEditText);
        mDeleteUserButton = findViewById(R.id.deleteUserButton);
        mAddAdminEditText = findViewById(R.id.addAdminEditText);
        mAddAdminButton = findViewById(R.id.addAdminButton);
        mBackButton = findViewById(R.id.backButton);


        getDatabase();
        int mUserId = getUserId();
        loginUser(mUserId);

        mRatingLogTextView.setMovementMethod(new ScrollingMovementMethod());


        refreshAdminPage();

        mDeleteUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mUsername = mDeleteUserEditText.getText().toString();
                if(checkForUserInDatabase()){
                   deleteUser();
               }
                refreshAdminPage();
            }
        });

        mAddAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAdminUsername = mAddAdminEditText.getText().toString();
                addAdmin(mAdminUsername);
                refreshAdminPage();
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

    private void refreshAdminPage() {

        List<Rating> ratingLogs = mMeditopiaDAO.getAllRatingsLogs();

        mRatingLogTextView.setText("");

        mRatingLogTextView.append("Rating Log:\n\n");

        for (Rating ratingLog : ratingLogs) {
            mRatingLogTextView.append("User ID: " + ratingLog.getUserId() + "\n");
            mRatingLogTextView.append("Breathing Exercise Rating: " + ratingLog.getBreathingExerciseRating() + "\n");
            mRatingLogTextView.append("Relaxation Exercise Rating: " + ratingLog.getRelaxationExerciseRating() + "\n");
            mRatingLogTextView.append("Soothing Music Rating: " + ratingLog.getSoothingMusicRating() + "\n");
            mRatingLogTextView.append("--------------------------------------------------\n\n");
        }
    }

    private void addAdmin(String username) {
        if (username.isEmpty()) {
            Toast.makeText(this, "Please enter a username", Toast.LENGTH_SHORT).show();
            return;
        }

        mNewAdmin = mMeditopiaDAO.getUserByUsername(username);
        if (mNewAdmin != null) {
            // User already exists, show an error message and return
            Toast.makeText(this, "User already exists", Toast.LENGTH_SHORT).show();
            return;
        }

        User newAdmin = new User(username, "admin123", true);
        mMeditopiaDAO.insert(newAdmin);

        int newAdminId = newAdmin.getUserId();

        Subscription FreeAdmin = new Subscription(true, true, true, newAdminId);
        mMeditopiaDAO.insert(FreeAdmin);
        Rating FiveStars = new Rating(5, 5, 5, newAdminId);
        mMeditopiaDAO.insert(FiveStars);

        Toast.makeText(this, "Admin added", Toast.LENGTH_SHORT).show();
    }


    private void deleteUser(){
        int userId = mDeletedUser.getUserId();

        List<Subscription> subscriptions = mMeditopiaDAO.getAllSubscriptionLogs();
        for (Subscription subscription : subscriptions) {
            if (subscription.getUserId() == userId) {
                mSubscription = subscription;
                break;
            }
        }

        List<Rating> ratings = mMeditopiaDAO.getAllRatingsLogs();
        for (Rating rating : ratings) {
            if (rating.getUserId() == userId) {
                mRating = rating;
                break;
            }
        }

        mMeditopiaDAO.delete(mDeletedUser);
        mMeditopiaDAO.delete(mSubscription);
        mMeditopiaDAO.delete(mRating);

        Toast.makeText(this, mUsername + " deleted successfully!", Toast.LENGTH_SHORT).show();

    }

    private boolean checkForUserInDatabase(){
        mDeletedUser = mMeditopiaDAO.getUserByUsername(mUsername);
        if(mDeletedUser == null){
            Toast.makeText(this, "No user " + mUsername + " found", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private int getUserId() {
        int userId = getIntent().getIntExtra(USER_ID_KEY, -1);

        if (userId == -1) {
            SharedPreferences preferences = getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
            userId = preferences.getInt(USER_ID_KEY, -1);
        }

        return userId;
    }

    private void loginUser(int userId) {
        mUser = mMeditopiaDAO.getUserByUserId(userId);
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent (context, AdminActivity.class);
        intent.putExtra(USER_ID_KEY, userId);
        return intent;
    }

    private void getDatabase(){
        mMeditopiaDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MeditopiaDAO();
    }

}