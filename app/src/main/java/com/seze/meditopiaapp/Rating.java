package com.seze.meditopiaapp;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.seze.meditopiaapp.DB.AppDataBase;

@Entity(tableName = AppDataBase.RATING_TABLE,
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "mUserId",
                childColumns = "mUserId",
                onDelete = ForeignKey.CASCADE))

public class Rating {
    @PrimaryKey(autoGenerate = true)
    private int mRatingId;

    private float mBreathingExerciseRating;

    private float mRelaxationExerciseRating;

    private float mSoothingMusicRating;

    private int mUserId; // foreign key to User table



    public Rating(float breathingExerciseRating, float relaxationExerciseRating, float soothingMusicRating, int userId) {
        mBreathingExerciseRating = breathingExerciseRating;
        mRelaxationExerciseRating = relaxationExerciseRating;
        mSoothingMusicRating = soothingMusicRating;
        mUserId = userId;
    }

    public int getRatingId() {
        return mRatingId;
    }

    public void setRatingId(int ratingId) {
        mRatingId = ratingId;
    }

    public float getBreathingExerciseRating() {
        return mBreathingExerciseRating;
    }

    public void setBreathingExerciseRating(float breathingExerciseRating) {
        mBreathingExerciseRating = breathingExerciseRating;
    }

    public float getRelaxationExerciseRating() {
        return mRelaxationExerciseRating;
    }

    public void setRelaxationExerciseRating(float relaxationExerciseRating) {
        mRelaxationExerciseRating = relaxationExerciseRating;
    }

    public float getSoothingMusicRating() {
        return mSoothingMusicRating;
    }

    public void setSoothingMusicRating(float soothingMusicRating) {
        mSoothingMusicRating = soothingMusicRating;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        mUserId = userId;
    }
}
