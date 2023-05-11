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

public class Get_A_Subscription extends AppCompatActivity {

    private TextView mLockedTextView;
    private TextView mSubscribeTextView;
    private Button mSubscribeButton;

    private static final String USER_ID_KEY = "package com.seze.meditopiaapp.userIdKey";
    private static final String PREFERENCES_KEY = "package com.seze.meditopiaapp.PREFERENCES_KEY";
    private User mUser;
    private MeditopiaDAO mMeditopiaDAO;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_get_asubscription);

        mLockedTextView = findViewById(R.id.lockedTextView);
        mSubscribeTextView = findViewById(R.id.subscribeTextView);
        mSubscribeButton = findViewById(R.id.subscribeButton);

        getDatabase();
        int mUserId = getUserId();
        loginUser(mUserId);

        mSubscribeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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

    private void loginUser(int userId){
        mUser = mMeditopiaDAO.getUserByUserId(userId);
    }


    private void getDatabase(){
        mMeditopiaDAO = Room.databaseBuilder(this, AppDataBase.class, AppDataBase.DATABASE_NAME)
                .allowMainThreadQueries()
                .build()
                .MeditopiaDAO();
    }

    public static Intent intentFactory(Context context, int userId){
        Intent intent = new Intent(context, Get_A_Subscription.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra(USER_ID_KEY, userId);
        context.startActivity(intent);

        return  intent;
    }


}