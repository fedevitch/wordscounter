package com.lyubomyr.wordscounter;

import java.util.List;

public class Word {
    private String word;
    private int appearsCount;
    private int charsCount;
    private List<Character> uniqueChars;
    private int uniqueCharsCount;

    Word(String word) {
        this.word = word;
        this.appearsCount = 1;
        this.charsCount = word.length();
        this.calculateUniqueChars();
    }

    private void calculateUniqueChars(){
        for(int i = 0; i < this.charsCount; i++){
            Character c = this.word.charAt(i);
            if(this.uniqueChars.isEmpty()){
                this.uniqueChars.add(c);
                this.uniqueCharsCount++;
            } else {
                int index = this.uniqueChars.indexOf(c);
                if(index == -1){
                    this.uniqueChars.add(c);
                    this.uniqueCharsCount++;
                }
            }
        }
    }

    public void incrementAppearsCount() {
        this.appearsCount++;
    }

    public String getWord(){
        return this.word;
    }

    public int getAppearsCount(){
        return this.appearsCount;
    }

    public List<Character> getUniqueChars() {
        return uniqueChars;
    }

    public int getUniqueCharsCount() {
        return uniqueCharsCount;
    }
}
