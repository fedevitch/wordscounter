package com.lyubomyr.wordscounter;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

public class CountResult {

    private List<Word> wordsVocabulary;
    private List<WordChar> charsVocabulary;

    private String LOG_TAG = "COUNT_RESULT";

    CountResult() {
        this.wordsVocabulary = new ArrayList<Word>();
        this.charsVocabulary = new ArrayList<WordChar>();
    }

    CountResult(List<Word> words, List<WordChar> chars){
        this.wordsVocabulary = words;
        this.charsVocabulary = chars;
    }

    public List<Word> getWordsVocabulary() {
        return wordsVocabulary;
    }

    public List<WordChar> getCharsVocabulary() {
        return charsVocabulary;
    }

    public boolean isPresent(Word word){
        if(this.wordsVocabulary.isEmpty()){
            return false;
        }
        ListIterator<Word> wordListIterator = this.wordsVocabulary.listIterator();
        while(wordListIterator.hasNext()){
            if(wordListIterator.next().getWord().equals(word.getWord())){
                return true;
            }
        }
        return false;
    }

    public boolean isPresent(String word){
        if(this.wordsVocabulary.isEmpty()){
            return false;
        }
        ListIterator<Word> wordListIterator = this.wordsVocabulary.listIterator();
        while(wordListIterator.hasNext()){
            if(wordListIterator.next().getWord().equals(word)){
                return true;
            }
        }
        return false;
    }

    public boolean isPresent(WordChar character){
        if(this.charsVocabulary.isEmpty()){
            return false;
        }
        ListIterator<WordChar> charListIterator = this.charsVocabulary.listIterator();
        while(charListIterator.hasNext()){
            if(charListIterator.next().getWordCharacter().equals(character.getWordCharacter())){
                return true;
            }
        }
        return false;
    }

    public boolean isPresent(Character character){
        if(this.charsVocabulary.isEmpty()){
            return false;
        }
        ListIterator<WordChar> charListIterator = this.charsVocabulary.listIterator();
        while(charListIterator.hasNext()){
            if(charListIterator.next().getWordCharacter().equals(character)){
                return true;
            }
        }
        return false;
    }

    public void addNewWord(Word word){
        this.wordsVocabulary.add(word);
    }

    public void addNewWord(String word){
        this.wordsVocabulary.add(new Word(word));
    }

    public void addNewChar(WordChar character){
        this.charsVocabulary.add(character);
    }

    public void addNewChar(char character){
        this.charsVocabulary.add(new WordChar(character));
    }

    public void incrementWordAppearsCount(Word word){
        if(this.wordsVocabulary.isEmpty()){
            Log.e(LOG_TAG, "Increment appears on empty vocabulary detected");
            Log.e(LOG_TAG, "caller signature incrementWordAppearsCount(Word word)");
            return;
        }

        ListIterator<Word> it = this.wordsVocabulary.listIterator();
        while (it.hasNext()){
            Word vocWord = it.next();
            if(vocWord.getWord().equals(word.getWord())){
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

        ListIterator<Word> it = this.wordsVocabulary.listIterator();
        while (it.hasNext()){
            Word vocWord = it.next();
            if(vocWord.getWord().equals(word)){
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

        ListIterator<WordChar> it = this.charsVocabulary.listIterator();
        while (it.hasNext()){
            WordChar vocChar = it.next();
            if(vocChar.getWordCharacter().equals(character.getWordCharacter())){
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

        ListIterator<WordChar> it = this.charsVocabulary.listIterator();
        while (it.hasNext()){
            WordChar vocChar = it.next();
            if(vocChar.getWordCharacter().equals(character)){
                vocChar.incrementAppearsCount();
                return;
            }
        }
    }

}
