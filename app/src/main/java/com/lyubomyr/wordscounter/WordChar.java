package com.lyubomyr.wordscounter;

public class WordChar {
    private Character wordCharacter;
    private int appearsCount;

    WordChar(Character c){
        this.wordCharacter = c;
        this.appearsCount = 1;
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
}
