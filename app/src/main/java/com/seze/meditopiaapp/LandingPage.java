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

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;

import java.util.List;

public class LandingPage extends AppCompatActivity {
    private TextView mWelcomeTextView;
    private TextView mUsernameWelcomename;
    private Button mSubscriptionButton;
    private Button mMeditationLibraryButton;
    private Button mLogoutButton;
    private Button mAdminButton;
    private MeditopiaDAO mMeditopiaDAO;
    private static final String USER_ID_KEY = "package com.seze.meditopiaapp.userIdKey";
    private static final String PREFERENCES_KEY = "package com.seze.meditopiaapp.PREFERENCES_KEY";
    private int mUserId = -1;

    private User mUser;

    private SharedPreferences mPreferences = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing_page);
        mWelcomeTextView = findViewById(R.id.welcomeTextView);
        mUsernameWelcomename = findViewById(R.id.usernameTextView);
        mSubscriptionButton = findViewById(R.id.subscriptionButton);
        mMeditationLibraryButton = findViewById(R.id.meditationLibraryButton);
        mLogoutButton = findViewById(R.id.logoutButton);
        mAdminButton = findViewById(R.id.adminButton);

        getDatabase();
        checkForUser();
        addUserToPreference(mUserId);
        loginUser(mUserId);
        setWelcomeText();


        // Set click listeners for buttons
        mSubscriptionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = SubscriptionActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        mMeditationLibraryButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = MeditationLibraryActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                finish();
            }
        });

        mLogoutButton = findViewById(R.id.logoutButton);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logoutUser();
            }
        });


        mAdminButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = AdminActivity.intentFactory(getApplicationContext(),mUser.getUserId());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
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

    private void checkForUser() {
        mUserId= getIntent().getIntExtra(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }


        if(mPreferences == null){
            getPrefs();
        }

        mUserId = mPreferences.getInt(USER_ID_KEY, -1);

        if(mUserId != -1){
            return;
        }


        List<User> users = mMeditopiaDAO.getAllUsers();

        Intent intent = LoginActivity.intentFactory(this);
        startActivity(intent);

    }

    private void loginUser(int userId) {
        mUser = mMeditopiaDAO.getUserByUserId(userId);
    }

    private void getPrefs() {
        mPreferences = this.getSharedPreferences(PREFERENCES_KEY, Context.MODE_PRIVATE);
    }

    public static Intent intentFactory(Context context, int userId) {
        Intent intent = new Intent (context, LandingPage.class);
        intent.putExtra(USER_ID_KEY, userId);

        return intent;
    }

    private void setWelcomeText() {
        String username = mUser.getUsername();
        String welcomeText = "Welcome, " + username + "!";
        mUsernameWelcomename.setText(welcomeText);

        boolean isAdmin = mUser.isAdmin();
        if (isAdmin) {
            mAdminButton.setVisibility(View.VISIBLE);
        } else {
            mAdminButton.setVisibility(View.GONE);
        }
    }

    private void addUserToPreference(int userId) {
        if(mPreferences == null){
            getPrefs();
        }
        SharedPreferences.Editor editor = mPreferences.edit();
        editor.putInt(USER_ID_KEY, userId);

    }

    private void logoutUser() {
        mUserId = -1;
        mUser = null;
        addUserToPreference(-1);
        Intent intent = MainActivity.intentFactory(this);
        startActivity(intent);
        finish();
    }

}