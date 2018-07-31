package cakart.cakart.in.flashcard_app.flashcard;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

import cakart.cakart.in.flashcard_app.model.FlashCard;
import cakart.cakart.in.flashcard_app.model.FlashDeck;
import cakart.cakart.in.flashcard_app.R;
import cakart.cakart.in.flashcard_app.db.HttpHandler;
import cakart.cakart.in.flashcard_app.db.MyDatabaseHelper;
import cakart.cakart.in.flashcard_app.register.MainActivity;


public class DeckFragment extends Fragment {
    private ProgressDialog pDialog;
    private String TAG = MainActivity.class.getSimpleName();
    ArrayList<FlashDeck> fds;
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_deck, container, false);
        listView = (ListView) view.findViewById(R.id.deck_list);

        SharedPreferences sharedPref = getActivity().getApplicationContext().getSharedPreferences(
                "app_prefs", Context.MODE_PRIVATE);
        if (sharedPref.getBoolean("isDownloaded", false)) {
            showList();
        } else {
            new getFlashCards().execute();
        }
        setUpListItemClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        showList();
    }


    public class getFlashCards extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Loading Cards.. Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            MyDatabaseHelper db = new MyDatabaseHelper(getActivity());

            for (int i = 65; i <= 90; i++) {
                char letter = (char) i;
                HttpHandler sh = new HttpHandler();
                String jsonStr = sh.makeServiceCall("http://glossary.cakart.in/get_keyword.json?starting_letter=" + letter);
                if (jsonStr != null) {
                    try {
                        JSONObject jsonObj = new JSONObject(jsonStr);
                        final JSONArray results = jsonObj.getJSONArray("results");
                        long deck_id = 0;
                        if (results.length() > 0) {
                            FlashDeck fd = new FlashDeck();
                            fd.setName("DECK - " + letter);
                            fd.setNumber_of_decks(results.length());
                            deck_id = db.insertDeck(fd);
                        } else {
                            continue;
                        }

                        for (int j = 0; j < results.length(); j++) {
                            JSONObject json = (JSONObject) results.get(j);
                            FlashCard fc = new FlashCard();
                            fc.setTitle(json.getString("glossary_kayword"));
                            fc.setDescription(json.getString("description"));
                            fc.setDeck_id((int) deck_id);
                            db.insertCard(fc);
                        }

                    } catch (final JSONException e) {
                        Log.e(TAG, "Json parsing error: " + e.getMessage());


                    }
                } else {
                    Log.e(TAG, "Couldn't get json from server.");


                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            showList();
        }
    }

    private void setUpListItemClickListener() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), ShowCardActivity.class);
                intent.putExtra("deck_id", fds.get(position).getDeck_id());
                Log.d("Akhil", "" + fds.get(position).getDeck_id());
                startActivity(intent);
            }
        });
    }

    public void showList() {

        MyDatabaseHelper db = new MyDatabaseHelper(getActivity());
        fds = new ArrayList<FlashDeck>(db.getAllDecks());

        if (fds.size() > 0) {
            SharedPreferences sharedPref = getActivity().getSharedPreferences(
                    "app_prefs", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("isDownloaded", true);
            editor.putString("last_downloaded", new Date().toString());
            editor.commit();
        }

        FlashDeckAdapter itemArrayAdapter = new FlashDeckAdapter(getActivity(), R.layout.deck_item, fds);
        listView.setAdapter(itemArrayAdapter);
    }

}
