package cakart.cakart.in.flashcard_app.model;

import java.util.Date;

public class FlashDeck {
    String name;
    int number_of_cards;
    Date downloaded;
    int deck_id;




    public int getDeck_id() {
        return deck_id;
    }

    public void setDeck_id(int deck_id) {
        this.deck_id = deck_id;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumber_of_decks() {
        return number_of_cards;
    }

    public void setNumber_of_decks(int number_of_decks) {
        this.number_of_cards = number_of_decks;
    }

    public Date getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(Date downloaded) {
        this.downloaded = downloaded;
    }
}
