package com.lyubomyr.wordscounter;

import com.lyubomyr.wordscounter.Storage.WordCharEntity;

public class WordChar {
    private final Character wordCharacter;
    private int appearsCount;

    WordChar(char c){
        this.wordCharacter = c;
        this.appearsCount = 1;
    }

    WordChar(WordCharEntity wordCharEntity){
        this.wordCharacter = wordCharEntity.character;
        this.appearsCount = wordCharEntity.appears;
    }

    public void incrementAppearsCount(){
        this.appearsCount++;
    }

    public Character getWordCharacter() {
        return wordCharacter;
    }

    public int getAppearsCount() {
        return appearsCount;
    }

    public WordCharEntity getDbEntity(String resultId){
        WordCharEntity dbEntity = new WordCharEntity();

        dbEntity.result_id = resultId;
        dbEntity.character = this.wordCharacter;
        dbEntity.appears = this.appearsCount;

        return dbEntity;
    }
}
