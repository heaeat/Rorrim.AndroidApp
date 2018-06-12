package com.rorrim.mang.smartmirror.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.databinding.DataBindingUtil;
import android.databinding.ObservableArrayList;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Switch;
import android.widget.Toast;

import com.rorrim.mang.smartmirror.Adapter.MusicAdapter;
import com.rorrim.mang.smartmirror.Auth.AuthManager;
import com.rorrim.mang.smartmirror.Data.DataManager;
import com.rorrim.mang.smartmirror.File.FileManager;
import com.rorrim.mang.smartmirror.Listener.RecyclerViewClickListener;
import com.rorrim.mang.smartmirror.Model.Music;
import com.rorrim.mang.smartmirror.Network.RetrofitClient;
import com.rorrim.mang.smartmirror.Network.RetrofitService;
import com.rorrim.mang.smartmirror.R;
import com.rorrim.mang.smartmirror.databinding.ActivityMusicBinding;

import java.util.HashMap;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MusicActivity extends Activity {

    private static final int REQUEST_EXTERNAL_STORAGE = 2;
    private ActivityMusicBinding binding;
    private ObservableArrayList<Music> musicList;
    private MusicAdapter mAdapter;
    private HashMap<String, String> playList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_music);
        binding.musicMenuLayout.setSwitch();
        musicList = new ObservableArrayList<>();
        binding.setActivity(this);
        mAdapter = new MusicAdapter(this, musicList, new RecyclerViewClickListener() {
            @Override
            public void onItemClick(Music music) {
                deleteListMusic(music);
            }
        });
        binding.musicMusicRv.setAdapter(mAdapter);
        binding.setMusicList(musicList);
        requestMusicList();
        getPlayList();
    }

    public void deleteListMusic(final Music music){
        /* 재생목록에서 음악을 지우자!
        * 이름으로 서버에서도 지우자!!! */
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_NEGATIVE: {
                        //Yes 버튼을 클릭했을때 처리
                        try {
                            Log.e("끼야양", "didid");
  //                          FileManager.getInstance().uploadMusic(getContext(), music);
                        }
                        catch (Exception e) {
                            Log.e("SimplePlayer", e.getMessage());
                        }
                        break;
                    }
                    case DialogInterface.BUTTON_POSITIVE:
                        //No 버튼을 클릭했을때 처리
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("지울거야?").setPositiveButton("아니?", dialogClickListener)
                .setNegativeButton("그래!", dialogClickListener).show();

    }

    public void sendMusic(String albumId){
        Toast.makeText(MusicActivity.this, albumId,
                Toast.LENGTH_SHORT).show();
    }


    private void requestMusicList(){
        int permissionReadStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteStorage = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if(permissionReadStorage == PackageManager.PERMISSION_DENIED || permissionWriteStorage == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE}, REQUEST_EXTERNAL_STORAGE);
        } else {
            getMusicList();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_EXTERNAL_STORAGE:
                for (int i = 0; i < permissions.length; i++) {
                    String permission = permissions[i];
                    int grantResult = grantResults[i];
                    if (permission.equals(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResult == PackageManager.PERMISSION_GRANTED) {
                            getMusicList();
                        } else {
                            Toast.makeText(MusicActivity.this, "Permission Denined.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }
                break;
        }
    }

    private void getMusicList(){

        /* 서버에서 가져오자!!! */
        // 노래를 지우고싶을때
        // 노래를 선택하면 노래제목, 아티스트, full 네임, uid, mirrorUid를
        // retrofit으로 보내주면됨 method 이름은 popMusic
        // 인자는 위에 적어둔 5개
        // 성공시 String형 true 반환, 실패시 String형 false 반환
        /*
        //가져오고 싶은 컬럼 명을 나열합니다. 음악의 아이디, 앰블럼 아이디, 제목, 아스티스트 정보를 가져옵니다.
        String[] projection = {MediaStore.Audio.Media._ID,
                MediaStore.Audio.Media.ALBUM_ID,
                MediaStore.Audio.Media.TITLE,
                MediaStore.Audio.Media.ARTIST
        };

        Cursor cursor = getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                projection, null, null, null);

        while(cursor.moveToNext()){
            Music music = new Music();
            music.setId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media._ID)));
            music.setAlbumId(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ALBUM_ID)));
            music.setTitle(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)));
            music.setArtist(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST)));
            musicList.add(music);
        }
        cursor.close();
        */

        Music music = new Music();
        music.setId("d");
        music.setAlbumId(null);
        music.setTitle("d");
        music.setArtist("d");
        musicList.add(music);
    }

    //버튼
    public void gotoMusicPopup() {
         //String activityName = getActivityName();
         Intent intent = new Intent(getContext(), MusicPopupActivity.class);
         startActivity(intent);

    }

    public Boolean getState()   {
        boolean temp;
        SharedPreferences prefs = getSharedPreferences("Activity.MusicActivity", MODE_PRIVATE);
        temp = prefs.getBoolean("myState", false);
        return temp;
    }
    public void getPlayList()  {
        RetrofitService retrofitService = RetrofitClient.getInstance().getRetrofit().create(RetrofitService.class);
        String uid = AuthManager.getInstance().getUser().getUid();
        String mirrorUid = AuthManager.getInstance().getUser().getMirrorUid();
        Call<HashMap<String, String>> call = retrofitService.getPlayList(uid, mirrorUid);
        call.enqueue(new Callback<HashMap<String, String>>() {
            @Override
            public void onResponse(Call<HashMap<String, String>> call, Response<HashMap<String, String>> response) {
                if(response.isSuccessful()) {
                    playList(response.body());
                }
            }

            @Override
            public void onFailure(Call<HashMap<String, String>> call, Throwable t) {
                Log.d("PlayList", "Get PlayList Failed");
            }
        });
    }

    public void playList(HashMap<String, String> map)   {
        playList = map;
        for(String song : playList.keySet())    {
            Log.d("노래 : ", playList.get(song));
        }
    }

    public Context getContext(){
        return this;
    }

}
