package com.seze.meditopiaapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AlertDialog;
import androidx.room.Room;

import android.widget.Toast;

import com.seze.meditopiaapp.DB.AppDataBase;
import com.seze.meditopiaapp.DB.MeditopiaDAO;

import java.util.List;

public class SubscriptionActivity extends AppCompatActivity {

    private TextView subscriptionTitleTextView;
    private Button mButtonBreathingExercise;
    private TextView breathingExercisePremiumDescriptionTextView;
    private Button mButtonSoothingMusic;
    private TextView soothingMusicPremiumDescriptionTextView;
    private Button mButtonRelaxationExercise;
    private TextView relaxationButtonDescription;

    private Subscription mSubscription;
    private MeditopiaDAO mMeditopiaDAO;
    private Button mButtonBacktoMainMenu;
    private static final String USER_ID_KEY = "package com.seze.meditopiaapp.userIdKey";
    private static final String PREFERENCES_KEY = "package com.seze.meditopiaapp.PREFERENCES_KEY";
    private User mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subscription);

        // Initialize UI elements
        subscriptionTitleTextView = findViewById(R.id.subscriptionTitleTextView);
        mButtonBreathingExercise = findViewById(R.id.breathingExercisePremiumButton);
        breathingExercisePremiumDescriptionTextView = findViewById(R.id.breathingExercisePremiumDescriptionTextView);
        mButtonSoothingMusic = findViewById(R.id.soothingMusicPremiumButton);
        soothingMusicPremiumDescriptionTextView = findViewById(R.id.soothingMusicPremiumDescriptionTextView);
        mButtonRelaxationExercise = findViewById(R.id.relaxationButton);
        relaxationButtonDescription = findViewById(R.id.relaxationButtonDescription);
        mButtonBacktoMainMenu = findViewById(R.id.backButton);



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

        setSubscriptionTitle();


        mButtonBreathingExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SubscriptionActivity.this);
                builder.setMessage("Would you like to subscribe to breathing exercise premium?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSubscription.setBreathingExercisePremium(true);
                                mMeditopiaDAO.update(mSubscription);
                                mButtonBreathingExercise.setText("SUBSCRIBED");
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSubscription.setBreathingExercisePremium(false);
                                mMeditopiaDAO.update(mSubscription);
                                mButtonBreathingExercise.setText("UNSUBSCRIBED");
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                            }
                        })
                        .show();
            }
        });

        mButtonSoothingMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder builder = new AlertDialog.Builder(SubscriptionActivity.this);
                builder.setMessage("Would you like to subscribe to soothing music premium?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSubscription.setSoothingMusicPremium(true);
                                mMeditopiaDAO.update(mSubscription);
                                mButtonSoothingMusic.setText("SUBSCRIBED");
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSubscription.setSoothingMusicPremium(false);
                                mMeditopiaDAO.update(mSubscription);
                                mButtonSoothingMusic.setText("UNSUBSCRIBED");
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                            }
                        })
                        .show();
            }
        });

        mButtonRelaxationExercise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(SubscriptionActivity.this);
                builder.setMessage("Would you like to subscribe to relaxation exercise premium?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSubscription.setRelaxationExercisePremium(true);
                                mMeditopiaDAO.update(mSubscription);
                                mButtonRelaxationExercise.setText("SUBSCRIBED");
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mSubscription.setRelaxationExercisePremium(false);
                                mMeditopiaDAO.update(mSubscription);
                                mButtonRelaxationExercise.setText("UNSUBSCRIBED");
                            }
                        })
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                            }
                        })
                        .show();
            }
        });
        mButtonBacktoMainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = LandingPage.intentFactory(getApplicationContext(),mUser.getUserId());
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

    private void loginUser(int userId){
        mUser = mMeditopiaDAO.getUserByUserId(userId);
    }

    private void setSubscriptionTitle() {
        String username = mUser.getUsername();
        String subscriptionTitle = username + " : Meditopia Subscription";
        subscriptionTitleTextView.setText(subscriptionTitle);
    }

    private void getDatabase(){
        mMeditopiaDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MeditopiaDAO();
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, SubscriptionActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(USER_ID_KEY, userId);
        context.startActivity(intent);

        return  intent;
    }


}