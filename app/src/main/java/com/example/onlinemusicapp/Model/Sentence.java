package com.example.onlinemusicapp.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;

public class Sentence {

    @Json(name ="words")

    private List<Word> words = null;

    /**
     * No args constructor for use in serialization
     *
     */
    public Sentence() {
    }

    /**
     *
     * @param words
     */
    public Sentence(List<Word> words) {
        super();
        this.words = words;
    }

    public List<Word> getWords() {
        return words;
    }

    public void setWords(List<Word> words) {
        this.words = words;
    }

}
