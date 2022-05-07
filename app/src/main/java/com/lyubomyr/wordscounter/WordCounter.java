package com.lyubomyr.wordscounter; /**
 * Created by Lyubomyr on 22.09.2016.
 */

import android.util.Log;

import java.lang.reflect.Method;
import java.util.StringTokenizer;
import java.util.*;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.function.Function;

public class WordCounter {

    public CountResult countResult;

    public List<String> wordStatistics;
    public List<String> charStatistics;

    public String charset;

    public Boolean resultsReady;

    private final List<Word> words;
    private final List<WordChar> chars;

    private int wordsCount;
    private int charsCount;
    private final List<String> wordsVocabulary;
    private final List<Integer> word_used;


    private final List<Character> charactersVocabulary;
    private final List<Integer> characters_used;

    private final Settings appSettings;

    private final String LOG_TAG = "Word_counter";




    WordCounter(){
        Log.d(LOG_TAG,"called constructor");

        this.resultsReady = false;
        this.charset = "UTF-8";

        this.countResult = new CountResult();

        this.wordsCount =  0;
        this.wordsVocabulary = new ArrayList<String>();
        this.word_used = new ArrayList<Integer>();
        this.wordStatistics = new ArrayList<String>();

        this.charsCount = 0;
        this.charactersVocabulary = new ArrayList<Character>();
        this.characters_used = new ArrayList<Integer>();
        this.charStatistics = new ArrayList<String>();

        this.words = new ArrayList<Word>();
        this.chars = new ArrayList<WordChar>();

        this.appSettings = Store.getInstance().getSettings();

    }

    private void write_to_voc(String s)//записує слово до словника
    {
        this.wordsVocabulary.add(s);
        this.word_used.add(1);
        this.wordsCount++;
    }

    private void write_to_voc_char(char s)//записує слово до словника
    {
        this.charactersVocabulary.add(s);
        this.characters_used.add(1);

    }

    private void compareChar(char s)// функція виявлення нових букв
    {                           //і збільшення індексу, якщо така буква вже зустрічалось

        if(this.countResult.isPresent(s)){
            this.countResult.incrementCharAppearsCount(s);
        } else {
            this.countResult.addNewChar(s);
        }

        //legacy part

        if (this.charactersVocabulary.isEmpty())
        {
            this.chars.add(new WordChar(s));
            this.write_to_voc_char(s);
            return;
        }
        else
        {
            for (int i = 0; i < this.charactersVocabulary.size(); i++)
            {
                if (this.charactersVocabulary.get(i).equals(s))
                {
                    //слово вже є в словнику, додати до лічильника 1 і вийти
                    this.chars.get(i).incrementAppearsCount();

                    int index = this.characters_used.get(i)+1;
                    this.characters_used.set(i, index);
                    return;
                }
                else
                {
                    if(i == this.charactersVocabulary.size() - 1)//список пройдено, збігів не знайдено
                    {
                        //додаємо слово до списку
                        this.chars.add(new WordChar(s));

                        this.write_to_voc_char(s);
                        return;
                    }
                }
            }
        }
    }

    private void compareString(String s)// функція виявлення нових слів
    {                           //і збільшення індексу, якщо таке слово вже зустрічалось
        //StringTokenizer str = new StringTokenizer(s);
        this.charsCount += s.length();
        for(int i = 0; i < s.length(); i++)
        {
            this.compareChar(s.charAt(i));
        }
        Log.d(LOG_TAG,"chars processed");

        if(this.countResult.isPresent(s)){
            this.countResult.incrementWordAppearsCount(s);
        } else {
            this.countResult.addNewWord(s);
        }

        //legacy part

        if (this.wordsVocabulary.isEmpty())
        {
            write_to_voc(s);
            this.words.add(new Word(s));
            return;
        }
        else
        {
            for (int i = 0; i <= this.wordsVocabulary.size(); i++) {
                if (this.wordsVocabulary.get(i).equals(s)) {
                    //слово вже є в словнику, додати до лічильника 1 і вийти
                    this.words.get(i).incrementAppearsCount();
                    //legacy
                    int index = this.word_used.get(i) + 1;
                    this.word_used.set(i, index);
                    this.wordsCount++;
                    return;
                } else {
                    if (i == this.wordsVocabulary.size() - 1){ //список пройдено, збігів не знайдено

                        //додаємо слово до списку
                        this.words.add(new Word(s));

                        //legacy
                        this.write_to_voc(s);
                        return;
                    }
                }
            }
        }
    }



    private void find(String text, String ignored_chars)//пошук слів і операторів
    {
        if (text.isEmpty()) return;

        Log.d(LOG_TAG, "find() called ");

        //Log.d(LOG_TAG, "trying: " + text);
        Log.d(LOG_TAG, "ignored chars: " + ignored_chars);
        StringTokenizer str = new StringTokenizer(text);//пошук

        while(str.hasMoreTokens()){
            try{
                String s = str.nextToken(ignored_chars);

                //виклик функції порівняння

                this.compareString(s);

            }catch (Exception e){
                Log.e("Error","Помилка при відсіюванні групи: "+ignored_chars);
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
        }
        this.resultsReady = true;
    }

    public void startCounting(String text){
//        String chars = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
//                + "абвгґдеєжзиіїйклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИІЇКЛМНОПРСТУФХЦЧШЩЬЮЯ"
//                + "1234567890~_Ⓡ    \n\t";
        String charsToIgnore = this.appSettings.getIgnoredSymbols(); //" .,;:!?+=*-~`  {(//<[]>)}\"\'|&\n\t  \\    ";



        if (text.isEmpty()) {
            Log.d(LOG_TAG,"empty field found");
            return;
        }

        this.countResult.text = text;

        this.find(text,charsToIgnore);


    }

    //legacy
    public String getResult(){
        String info = "Слова: \n";
        Log.d(LOG_TAG, "Preparing result. Unique words found: " + this.wordsVocabulary.size());
        for (int i = 0; i < this.wordsVocabulary.size(); i++){
            info += " " + this.wordsVocabulary.get(i) + "  " + this.word_used.get(i) + " \n";
            double percent = (i+1)/(this.wordsVocabulary.size()/100.0);
            String statusBar = Double.toString(percent);
            Log.d(LOG_TAG,statusBar+"% of results ready");
        }

        info += "Літери: \n";
        for (int i = 0; i < this.charactersVocabulary.size(); i++){
            info += " " + this.charactersVocabulary.get(i) + "  " + this.characters_used.get(i) + " \n";

        }

        String message = "Всього слів у тексті: " + this.wordsCount + ". Всього символів: " + this.charsCount + ".\n "
                        + "Унікальних слів: "+ this.wordsVocabulary.size();

        return message + "\n" + info;
    }

    public CountResult getCountResult() {
        return countResult;
    }
}
