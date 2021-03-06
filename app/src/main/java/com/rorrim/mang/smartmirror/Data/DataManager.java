package com.rorrim.mang.smartmirror.Data;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rorrim.mang.smartmirror.Auth.AuthManager;

import static android.content.Context.MODE_PRIVATE;

public class DataManager {
    private static DataManager instance;
    private DatabaseReference databaseReference;

    public DataManager() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        /*imageReference= databaseReference.child("image").child(AuthManager.
                getInstance().getUser().getUid());
        addImageReferenceListener();*/
    }

    public static DataManager getInstance() {
        if (instance == null) {
            instance = new DataManager();
        }
        return instance;
    }

    public void uploadCalendar(String date, String time, String contents) {
        //String mirrorUid = DataManager.getInstance().getMirrorUid(context);
        String uid = AuthManager.getInstance().getUser().getUid();
        databaseReference.child("user").child(uid).child("calendar").child(date).child(time)
                .setValue(contents);
    }

    public void uploadAudio(String artist, String title, String fileName) {
        String uid = AuthManager.getInstance().getUser().getUid();
        databaseReference.child("user").child(uid).child("audio").child(artist).child(title).setValue(fileName);
    }

    public void saveStatus(Context context, String activityName, boolean status){
        SharedPreferences prefs = context.getSharedPreferences(activityName, MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(activityName, status);
        editor.commit();
    }
    public void saveMirrorUid(Context context, String mirrorUid)    {
        SharedPreferences prefs = context.getSharedPreferences("mirrorUid", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("mirror", mirrorUid);
        editor.commit();
    }

    public Boolean getState(Context context, String activityName) {
        boolean temp;
        SharedPreferences prefs = context.getSharedPreferences(activityName, MODE_PRIVATE);
        temp = prefs.getBoolean(activityName, false);
        return temp;
    }
    public String getMirrorUid(Context context) {
        String temp;
        SharedPreferences prefs = context.getSharedPreferences("mirrorUid", MODE_PRIVATE);
        temp = prefs.getString("mirror", "null");
        return temp;
    }
    public void saveCategory(Context context, String category)   {
        SharedPreferences prefs = context.getSharedPreferences("Category", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("myCategory", category);
        editor.commit();
    }
    public String getCategory(Context context)  {
        String temp;
        SharedPreferences prefs = context.getSharedPreferences("Category", MODE_PRIVATE);
        temp = prefs.getString("myCategory", "null");
        return temp;
    }

}
