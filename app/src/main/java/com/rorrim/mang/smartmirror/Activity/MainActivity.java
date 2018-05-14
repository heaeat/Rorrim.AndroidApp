package com.rorrim.mang.smartmirror.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.rorrim.mang.smartmirror.Unused.JsonParser;
import com.rorrim.mang.smartmirror.Network.NetworkManager;
import com.rorrim.mang.smartmirror.Unused.HttpConnection;
import com.rorrim.mang.smartmirror.Model.Data;
import com.rorrim.mang.smartmirror.R;

import java.util.LinkedList;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private NetworkManager nc;
    private HttpConnection conn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        nc = new NetworkManager(this);

        if(!nc.isConnected()){
            Toast.makeText(this, "Network is not connected", Toast.LENGTH_SHORT).show();
            return;
        }

        Toast.makeText(this, "Connected on" + nc.getNetworkTypeName(), Toast.LENGTH_SHORT).show();
        //Intent intent = new Intent(this, CalendarActivity.class);
        //startActivity(intent);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Intent getJson(){

        String jsonStr = HttpConnection.getInstance("http://172.16.10.183/get_json").getJson();
        LinkedList<Data> dataList = JsonParser.getInstance().parseJson(jsonStr);
        Intent intent = new Intent(MainActivity.this, ContentActivity.class);
        intent.putExtra("status", dataList.get(0).toString());
        intent.putExtra("result", dataList.get(1).toString());
        return intent;
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()){
            case R.id.json_btn:
                intent = getJson();
                break;
            default:
                break;
        }

        if(intent != null){
            startActivity(intent);
        }

    }
}