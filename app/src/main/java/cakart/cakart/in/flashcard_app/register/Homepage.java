package cakart.cakart.in.flashcard_app.register;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import cakart.cakart.in.flashcard_app.flashcard.DeckListActivity;
import cakart.cakart.in.flashcard_app.R;

public class Homepage extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        imageView=(ImageView)findViewById(R.id.flashcard);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Homepage.this,DeckListActivity.class);
                intent.putExtra("show_type", "flashcards");
                startActivity(intent);
            }
        });
    }
    public void onBackPressed(){
        Log.d("backbutton","working......");
        final AlertDialog.Builder builder=new AlertDialog.Builder(Homepage.this);
        builder.setMessage("Are you want to close this App?");
        builder.setCancelable(true);
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }
}
