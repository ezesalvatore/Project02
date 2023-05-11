package com.seze.meditopiaapp;

import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import com.seze.meditopiaapp.DB.AppDataBase;
@Entity(tableName = AppDataBase.SUBSCRIPTION_TABLE,
        foreignKeys = @ForeignKey(entity = User.class,
                parentColumns = "mUserId",
                childColumns = "mUserId",
                onDelete = ForeignKey.CASCADE))
public class Subscription {
    @PrimaryKey(autoGenerate = true)
    private int mSubscriptionId;

    private boolean isBreathingExercisePremium;
    private boolean isSoothingMusicPremium;
    private boolean isRelaxationExercisePremium;

    private int mUserId; // foreign key to User table

    public Subscription(boolean breathingExercisePremium, boolean soothingMusicPremium, boolean relaxationExercisePremium, int userId) {
        this.isBreathingExercisePremium = breathingExercisePremium;
        this.isSoothingMusicPremium = soothingMusicPremium;
        this.isRelaxationExercisePremium = relaxationExercisePremium;
        this.mUserId = userId;
    }

    public int getSubscriptionId() {
        return mSubscriptionId;
    }

    public void setSubscriptionId(int subscriptionId) {
        mSubscriptionId = subscriptionId;
    }

    public boolean isBreathingExercisePremium() {
        return isBreathingExercisePremium;
    }

    public void setBreathingExercisePremium(boolean breathingExercisePremium) {
        isBreathingExercisePremium = breathingExercisePremium;
    }

    public boolean isSoothingMusicPremium() {
        return isSoothingMusicPremium;
    }

    public void setSoothingMusicPremium(boolean soothingMusicPremium) {
        isSoothingMusicPremium = soothingMusicPremium;
    }

    public boolean isRelaxationExercisePremium() {
        return isRelaxationExercisePremium;
    }

    public void setRelaxationExercisePremium(boolean relaxationExercisePremium) {
        isRelaxationExercisePremium = relaxationExercisePremium;
    }

    public int getUserId() {
        return mUserId;
    }

    public void setUserId(int userId) {
        this.mUserId = userId;
    }
}