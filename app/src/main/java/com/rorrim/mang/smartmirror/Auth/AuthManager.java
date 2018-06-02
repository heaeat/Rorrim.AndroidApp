package com.rorrim.mang.smartmirror.Auth;

import android.app.Activity;
import android.app.Application;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rorrim.mang.smartmirror.Data.DataManager;
import com.rorrim.mang.smartmirror.Interface.Connectable;
import com.rorrim.mang.smartmirror.Model.User;
import com.rorrim.mang.smartmirror.Network.RetrofitClient;
import com.rorrim.mang.smartmirror.Network.RetrofitService;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AuthManager extends Application implements Connectable{
    private FirebaseAuth auth;
    private static AuthManager instance;
    private FirebaseAuth.AuthStateListener authListener;
    private SharedPreferences sharedPreferences;
    private User user;

    public AuthManager(){
        auth = FirebaseAuth.getInstance();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                //로그인 또는 로그아웃 이후에 실행된다.
                FirebaseUser firebaseUser = firebaseAuth.getCurrentUser();
                if(firebaseUser != null){
                    setUser();
                }
            }
        };
        auth.addAuthStateListener(authListener);
    }



    public static AuthManager getInstance(){
        if(instance == null){
            instance = new AuthManager();
        }
        return instance;
    }
    /*

    public void emailSignUp(final Activity curActivity, String email, String pw){
        auth.createUserWithEmailAndPassword(email, pw)
                .addOnCompleteListener(curActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(curActivity, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d("Auth Manager", "CreateUserWithEmail:fail");
                        }
                    }
                });
    }

    public void emailSignIn(final Activity curActivity, String email, String pw) {

        auth.signInWithEmailAndPassword(email, pw)
                .addOnCompleteListener(curActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(curActivity, MainActivity.class);
                            startActivity(intent);
                        }else{
                            Log.d("Auth Manager", "signInWithEmail:fail");
                        }
                    }
                });
    }

    */

    public FirebaseAuth getAuth(){
        return auth;
    }

    public void signOut(){
        auth.signOut();
    }

    private void saveUserData(){
        sharedPreferences = getSharedPreferences("UserInfo", Activity.MODE_PRIVATE);
        FirebaseUser user = auth.getCurrentUser();
        if(user != null){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", user.getEmail());
            editor.putString("phone", user.getPhoneNumber());
            editor.putString("uid", user.getUid());
        }
    }

    public void setUser(){
        this.user = new User(auth.getCurrentUser().getUid(), auth.getCurrentUser().getEmail());
        //setProfileUrl();
    }

    private void setProfileUrl() {
        //여기해야함
        RetrofitService retrofitService = RetrofitClient.getInstance().getRetrofit().create(RetrofitService.class);
        Call<String> call = retrofitService.getProfileUrl(user.getUid());

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                // you  will get the reponse in the response parameter
                if (response.isSuccessful()) {
                    //user.setProfileUrl(response.body());
                    //mAdapter.updateAnswers(response.body().getItems());
                } else {
                    int statusCode = response.code();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d("Login Activity", "error loading from login");
            }
        });

    }

    public void setUser(User user){
        this.user = user;
    }
    public User getUser(){
        return user;
    }
}
