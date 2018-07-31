package cakart.cakart.in.flashcard_app.flashcard;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.wajahatkarim3.easyflipview.EasyFlipView;

import cakart.cakart.in.flashcard_app.model.FlashCard;
import cakart.cakart.in.flashcard_app.R;
import cakart.cakart.in.flashcard_app.db.MyDatabaseHelper;


public class CardFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.card_fragment, container, false);

        final EasyFlipView easyFlipView = (EasyFlipView) view.findViewById(R.id.easyFlipView2);

        TextView front_word = (TextView) view.findViewById(R.id.txtWord_cardfront);
        TextView back_word = (TextView) view.findViewById(R.id.txtWord_cardback);
        TextView back_desc = (TextView) view.findViewById(R.id.txtMeaning_cardback);

        TextView qn_type = (TextView) view.findViewById(R.id.txtCategory_cardfront);

        if (getArguments().getInt("known") == 1) {
            qn_type.setText("KNOWN");
            qn_type.setTextColor(Color.GREEN);
        } else {
            qn_type.setText("NEW WORD");
        }

        Button show_ans = (Button) view.findViewById(R.id.btnViewMeaning_cardfront);
        Button iknow = (Button) view.findViewById(R.id.btnYes_cardback);
        Button idknow = (Button) view.findViewById(R.id.btnNo_cardback);

        front_word.setText(getArguments().getString("msg"));
        back_word.setText(getArguments().getString("msg"));
        back_desc.setText(getArguments().getString("desc"));

        show_ans.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                easyFlipView.flipTheView();
            }
        });

        iknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper db = new MyDatabaseHelper(getContext());
                FlashCard fc = new FlashCard();
                fc.setId(getArguments().getInt("card_id"));
                fc.setKnown(1);
                db.updateCard(fc);
                ShowCardActivity sc = (ShowCardActivity) getActivity();
                sc.showNext();
            }
        });

        idknow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper db = new MyDatabaseHelper(getContext());
                FlashCard fc = new FlashCard();
                fc.setId(getArguments().getInt("card_id"));
                fc.setKnown(0);
                db.updateCard(fc);
                ShowCardActivity sc = (ShowCardActivity) getActivity();
                sc.showNext();
            }
        });

        return view;
    }

    public static CardFragment newInstance(String keyword, String desc, int card_id, int known) {

        CardFragment f = new CardFragment();
        Bundle b = new Bundle();
        b.putString("msg", keyword);
        b.putString("desc", desc);
        b.putInt("card_id", card_id);
        b.putInt("known", known);
        f.setArguments(b);
        return f;
    }
}
