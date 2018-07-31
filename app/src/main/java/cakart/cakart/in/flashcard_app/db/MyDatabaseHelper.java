package cakart.cakart.in.flashcard_app.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cakart.cakart.in.flashcard_app.model.FlashCard;
import cakart.cakart.in.flashcard_app.model.FlashDeck;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    public static final String CREATE_DECK_TABLE = "create table flash_decs (deck_id integer primary key autoincrement, number_of_cards integer, name TEXT, downloaded_date DATETIME DEFAULT CURRENT_TIMESTAMP)";

    public static final String CREATE_CARDS_TABLE = "create table flash_cards (card_id integer primary key autoincrement, deck_id integer, known integer default 0, title TEXT, description TEXT)";

    public static final  String CREATE_QUIZ_DECK_TABLE = "create table quiz_deck (quiz_deck_id integer primary key , name TEXT ,taken_time integer,score integer)";

    public static final String CREATE_QUESTION_TABLE = "create table questions(quiz_id integer , question_id integer primary key, question TEXT, question_type VARCHAR(255),user_ans integer )";

    public static final String CREATE_QUESTION_ANSWER = "create table answers( id integer primary key ,question_id integer ,option TEXT , is_correct integer)";

    // Database Version
    private static final int DATABASE_VERSION = 3;

    // Database Name
    public static final String DATABASE_NAME = "flashcard";

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // create notes table
        db.execSQL(CREATE_DECK_TABLE);
        db.execSQL(CREATE_CARDS_TABLE);
        db.execSQL(CREATE_QUIZ_DECK_TABLE);
        db.execSQL(CREATE_QUESTION_TABLE);
        db.execSQL(CREATE_QUESTION_ANSWER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         // Drop older table if existed

        db.execSQL("DROP TABLE IF EXISTS flash_decs");
        db.execSQL("DROP TABLE IF EXISTS flash_cards");
        db.execSQL("DROP TABLE IF EXISTS quiz_deck");
        db.execSQL("DROP TABLE IF EXISTS questions");
        db.execSQL("DROP TABLE IF EXISTS answers");

        // Create tables again
        onCreate(db);
    }

    public int getKnown(int deck_id) {
        String selectQuery = "SELECT * FROM flash_cards where deck_id=" + deck_id + " and known=1";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        int count = cursor.getCount();
        cursor.close();
        return count;
    }

    public long insertDeck(FlashDeck fd) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put("name", fd.getName());
        values.put("number_of_cards", fd.getNumber_of_decks());

        System.out.println("AKHILL---" + fd.getNumber_of_decks());


        // insert row
        long id = db.insert("flash_decs", null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }

    public List<FlashDeck> getAllDecks() {
        List<FlashDeck> notes = new ArrayList<FlashDeck>();

        // Select All Query

        String selectQuery = "SELECT * FROM flash_decs";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FlashDeck fd = new FlashDeck();
                fd.setDeck_id(cursor.getInt(cursor.getColumnIndex("deck_id")));
                fd.setName(cursor.getString(cursor.getColumnIndex("name")));
                fd.setNumber_of_decks(cursor.getInt(cursor.getColumnIndex("number_of_cards")));
//                fd.setDownloaded(new Date(cursor.getString(cursor.getColumnIndex("downloaded_date"))));
                notes.add(fd);
                System.out.println("AKHIL " + fd.getName());
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();

        // return notes list
        return notes;
    }
    public List<FlashCard> getAllCards(int deck_id) {
        List<FlashCard> notes = new ArrayList<FlashCard>();

        // Select All Query

        String selectQuery = "SELECT * FROM flash_cards where deck_id=" + deck_id;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.d("Akhil2", cursor.getCount() + "");
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FlashCard fd = new FlashCard();
                fd.setId(cursor.getInt(cursor.getColumnIndex("card_id")));
                fd.setDeck_id(cursor.getInt(cursor.getColumnIndex("deck_id")));
                fd.setTitle(cursor.getString(cursor.getColumnIndex("title")));
                fd.setDescription(cursor.getString(cursor.getColumnIndex("description")));
                fd.setKnown(cursor.getInt(cursor.getColumnIndex("known")));
                notes.add(fd);
            } while (cursor.moveToNext());
        }

        // close db connection
        db.close();

        // return notes list
        return notes;
    }
    public long insertCard(FlashCard fc) {
        // get writable database as we want to write data
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        // `id` and `timestamp` will be inserted automatically.
        // no need to add them
        values.put("deck_id", fc.getDeck_id());
        values.put("title", fc.getTitle());
        values.put("description", fc.getDescription());


        // insert row
        long id = db.insert("flash_cards", null, values);

        // close db connection
        db.close();

        // return newly inserted row id
        return id;
    }
    public int updateCard(FlashCard note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("known", note.getKnown());

        // updating row
        return db.update("flash_cards", values, "card_id = ?",
                new String[]{String.valueOf(note.getId())});
    }
}
