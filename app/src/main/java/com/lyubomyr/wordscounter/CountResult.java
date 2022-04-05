package com.lyubomyr.wordscounter;

import android.util.Log;

import com.lyubomyr.wordscounter.Storage.SavedResultEntity;
import com.lyubomyr.wordscounter.Storage.SavedResultJoined;
import com.lyubomyr.wordscounter.Storage.WordCharEntity;
import com.lyubomyr.wordscounter.Storage.WordEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class CountResult {

    public String text;
    public Date created_at;

    private final String resultId;

    private final List<Word> wordsVocabulary;
    private final List<WordChar> charsVocabulary;
    private int wordsCount;
    private int charsCount;

    private final String LOG_TAG = "COUNT_RESULT";

    CountResult() {
        this.wordsVocabulary = new ArrayList<Word>();
        this.charsVocabulary = new ArrayList<WordChar>();
        this.wordsCount = 0;
        this.charsCount = 0;

        this.resultId = UUID.randomUUID().toString();
    }

    CountResult(SavedResultJoined dbEntity){
        this.wordsVocabulary = new ArrayList<Word>();
        this.charsVocabulary = new ArrayList<WordChar>();
        this.wordsCount = dbEntity.words_count;
        this.charsCount = dbEntity.chars_count;
        this.text = dbEntity.text;
        this.created_at = dbEntity.created_at;
        this.resultId = dbEntity.id;

        for (WordEntity wordEntity : dbEntity.words) {
            this.wordsVocabulary.add(new Word(wordEntity));
        }

        for (WordCharEntity wordCharEntity : dbEntity.characters) {
            this.charsVocabulary.add(new WordChar(wordCharEntity));
        }


    }

    public List<Word> getWordsVocabulary() {
        return wordsVocabulary;
    }

    public List<WordChar> getCharsVocabulary() {
        return charsVocabulary;
    }

    public int getWordsCount() {
        return wordsCount;
    }

    public int getCharsCount() {
        return charsCount;
    }

    public boolean isPresent(Word word){
        if(this.wordsVocabulary.isEmpty()){
            return false;
        }
        for (Word value : this.wordsVocabulary) {
            if (value.getWord().equals(word.getWord())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPresent(String word){
        if(this.wordsVocabulary.isEmpty()){
            return false;
        }
        for (Word value : this.wordsVocabulary) {
            if (value.getWord().equals(word)) {
                return true;
            }
        }
        return false;
    }

    public boolean isPresent(WordChar character){
        if(this.charsVocabulary.isEmpty()){
            return false;
        }
        for (WordChar wordChar : this.charsVocabulary) {
            if (wordChar.getWordCharacter().equals(character.getWordCharacter())) {
                return true;
            }
        }
        return false;
    }

    public boolean isPresent(Character character){
        if(this.charsVocabulary.isEmpty()){
            return false;
        }
        for (WordChar wordChar : this.charsVocabulary) {
            if (wordChar.getWordCharacter().equals(character)) {
                return true;
            }
        }
        return false;
    }

    public void addNewWord(Word word){
        this.wordsCount++;
        this.wordsVocabulary.add(word);
    }

    public void addNewWord(String word){
        this.wordsCount++;
        this.wordsVocabulary.add(new Word(word));
    }

    public void addNewChar(WordChar character){
        this.charsCount++;
        this.charsVocabulary.add(character);
    }

    public void addNewChar(char character){
        this.charsCount++;
        this.charsVocabulary.add(new WordChar(character));
    }

    public void incrementWordAppearsCount(Word word){
        if(this.wordsVocabulary.isEmpty()){
            Log.e(LOG_TAG, "Increment appears on empty vocabulary detected");
            Log.e(LOG_TAG, "caller signature incrementWordAppearsCount(Word word)");
            return;
        }

        for (Word vocWord : this.wordsVocabulary) {
            if (vocWord.getWord().equals(word.getWord())) {
                vocWord.incrementAppearsCount();
                return;
            }
        }
    }

    public void incrementWordAppearsCount(String word){
        if(this.wordsVocabulary.isEmpty()){
            Log.e(LOG_TAG, "Increment appears on empty vocabulary detected");
            Log.e(LOG_TAG, "caller signature incrementWordAppearsCount(String word)");
            return;
        }

        for (Word vocWord : this.wordsVocabulary) {
            if (vocWord.getWord().equals(word)) {
                vocWord.incrementAppearsCount();
                return;
            }
        }
    }

    public void incrementCharAppearsCount(WordChar character){
        if(this.charsVocabulary.isEmpty()){
            Log.e(LOG_TAG, "Increment appears on empty vocabulary detected");
            Log.e(LOG_TAG, "caller signature incrementCharAppearsCount(WordChar character)");
            return;
        }

        for (WordChar vocChar : this.charsVocabulary) {
            if (vocChar.getWordCharacter().equals(character.getWordCharacter())) {
                vocChar.incrementAppearsCount();
                return;
            }
        }
    }

    public void incrementCharAppearsCount(Character character){
        if(this.charsVocabulary.isEmpty()){
            Log.e(LOG_TAG, "Increment appears on empty vocabulary detected");
            Log.e(LOG_TAG, "caller signature incrementCharAppearsCount(Character character)");
            return;
        }

        for (WordChar vocChar : this.charsVocabulary) {
            if (vocChar.getWordCharacter().equals(character)) {
                vocChar.incrementAppearsCount();
                return;
            }
        }
    }

    public SavedResultJoined getDbEntity(){
        SavedResultJoined dbEntity = new SavedResultJoined();

        dbEntity.id = this.resultId;
        Log.d(LOG_TAG, "dbEntity id " + String.valueOf(dbEntity.id));

        dbEntity.chars_count = this.charsCount;
        dbEntity.words_count = this.wordsCount;
        dbEntity.text = this.text;
        dbEntity.created_at = new Date();

        dbEntity.words = new ArrayList<>();
        for (Word w : this.wordsVocabulary) {
            dbEntity.words.add(w.getDbEntity(dbEntity.id));
        }

        dbEntity.characters = new ArrayList<>();
        for (WordChar c : this.charsVocabulary) {
            dbEntity.characters.add(c.getDbEntity(dbEntity.id));
        }

        return dbEntity;
    }

    public String getResultId() {
        return resultId;
    }
}
