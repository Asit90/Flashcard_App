package cakart.cakart.in.flashcard_app.flashcard;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;


import com.wajahatkarim3.easyflipview.EasyFlipView;

import java.util.List;

import cakart.cakart.in.flashcard_app.model.FlashCard;
import cakart.cakart.in.flashcard_app.R;
import cakart.cakart.in.flashcard_app.db.MyDatabaseHelper;


public class ShowCardActivity extends AppCompatActivity {
    int deck_id;
    int current_card_index = 0;
    List<FlashCard> cards;
    EasyFlipView easyFlipView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_card);

        loadCards(getIntent().getExtras().getInt("deck_id"));

        showCard(0);
    }





    public void showCard(int index) {
        FlashCard fc = cards.get(index);
        CardFragment fragment = new CardFragment();
        Bundle b = new Bundle();
        b.putString("msg", fc.getTitle());
        b.putString("desc", fc.getDescription());
        b.putInt("card_id", fc.getId());
        System.out.println("KNWO - " + fc.getKnown());
        b.putInt("known", fc.getKnown());
        fragment.setArguments(b);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
        ft.replace(R.id.content_frame, fragment);
        ft.commit();
    }

    public void showNext() {
        if (current_card_index == cards.size() - 1) {
            current_card_index = 0;
        } else {
            current_card_index = current_card_index + 1;
            showCard(current_card_index);
        }

    }

    public void loadCards(int deck_id) {
        Log.d("Akhil", deck_id + "");
        MyDatabaseHelper db = new MyDatabaseHelper(getApplicationContext());
        cards = db.getAllCards(deck_id);
    }
}
