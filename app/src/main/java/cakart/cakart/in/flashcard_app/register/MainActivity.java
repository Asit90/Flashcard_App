package cakart.cakart.in.flashcard_app.register;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import cakart.cakart.in.flashcard_app.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                SharedPreferences pref=getApplicationContext().getSharedPreferences("user", Context.MODE_PRIVATE);
                if(pref.getString("email",null)==null) {
                    Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                }
                    else {
                    Intent intent=new Intent(MainActivity.this,Homepage.class);
                    startActivity(intent);
                    finish(); }
                }
        },2500);
    }
}
