package cakart.cakart.in.flashcard_app.flashcard;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

import cakart.cakart.in.flashcard_app.model.FlashDeck;
import cakart.cakart.in.flashcard_app.R;
import cakart.cakart.in.flashcard_app.db.MyDatabaseHelper;

public class FlashDeckAdapter extends ArrayAdapter<FlashDeck> {

    private int listItemLayout;

    public FlashDeckAdapter(Context context, int layoutId, ArrayList<FlashDeck> itemList) {
        super(context, layoutId, itemList);
        listItemLayout = layoutId;
    }


    private static class ViewHolder {
        TextView name;
        TextView status;
        ProgressBar pb;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        FlashDeck item = getItem(position);

        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(listItemLayout, parent, false);

            viewHolder.name = (TextView) convertView.findViewById(R.id.deck_name);

            viewHolder.status = (TextView) convertView.findViewById(R.id.status);

            viewHolder.pb = (ProgressBar) convertView.findViewById(R.id.progress);

            convertView.setTag(viewHolder); // view lookup cache stored in tag
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        MyDatabaseHelper db = new MyDatabaseHelper(getContext());
        int known_count = db.getKnown(item.getDeck_id());
        // Populate the data into the template view using the data object
        viewHolder.name.setText(item.getName());
        viewHolder.pb.setMax(item.getNumber_of_decks());
        viewHolder.pb.setProgress(known_count);
        viewHolder.status.setText(known_count + " out of " + item.getNumber_of_decks() + " words mastered");

        // Return the completed view to render on screen
        return convertView;
    }


}