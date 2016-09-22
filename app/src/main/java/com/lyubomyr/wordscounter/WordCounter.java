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

    private int wordsCount;
    private int charsCount;
    private List<String> wordsVocabulary;
    private List<Integer> word_used;
    private List<String> wordStatistics;

    private List<Character> charactersVocabulary;
    private List<Integer> characters_used;
    private List<String> charStatistics;

    private String charset;

    public Boolean resultsReady;

    WordCounter(){
        Log.d("DEBUG","called constructor");

        this.resultsReady = false;
        this.charset = "UTF-8";

        this.wordsCount =  0;
        this.wordsVocabulary = new ArrayList<String>();
        this.word_used = new ArrayList<Integer>();
        this.wordStatistics = new ArrayList<String>();

        this.charsCount = 0;
        this.charactersVocabulary = new ArrayList<Character>();
        this.characters_used = new ArrayList<Integer>();
        this.charStatistics = new ArrayList<String>();

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

    private int compareChar(char s)// функція виявлення нових букв
    {                           //і збільшення індексу, якщо така буква вже зустрічалось

        if (this.charactersVocabulary.isEmpty())
        {
            this.write_to_voc_char(s);
            return 1;
        }
        else
        {
            for (int i = 0; i < this.charactersVocabulary.size(); i++)
            {
                if (this.charactersVocabulary.get(i).equals(s))
                {
                    //слово вже є в словнику, додати до лічильника 1 і вийти
                    int index = this.characters_used.get(i)+1;
                    this.characters_used.set(i, index);
                    return 0;
                }
                else
                {
                    if(i == this.charactersVocabulary.size() - 1)//список пройдено, збігів не знайдено
                    {
                        //додаємо слово до списку
                        this.write_to_voc_char(s);
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    private int compareString(String s)// функція виявлення нових слів
    {                           //і збільшення індексу, якщо таке слово вже зустрічалось
        //StringTokenizer str = new StringTokenizer(s);
        this.charsCount += s.length();
        for(int i = 0; i < s.length(); i++)
        {
            this.compareChar(s.charAt(i));
        }
        if (this.wordsVocabulary.isEmpty())
        {
            write_to_voc(s);
            return 1;
        }
        else
        {
            for (int i = 0; i < this.wordsVocabulary.size(); i++)
            {
                if (this.wordsVocabulary.get(i).equals(s))
                {
                    //слово вже є в словнику, додати до лічильника 1 і вийти
                    int index = this.word_used.get(i)+1;
                    this.word_used.set(i, index);
                    this.wordsCount++;
                    return 0;
                }
                else
                {
                    if(i == this.wordsVocabulary.size() - 1)//список пройдено, збігів не знайдено
                    {
                        //додаємо слово до списку
                        this.write_to_voc(s);
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    private void find(String text, String ignored_chars)//пошук слів і операторів
    {
        if (text.isEmpty()) return;

        Log.d("DEBUG", "find() called ");

        //Log.d("DEBUG", "trying: " + text);
        Log.d("DEBUG", "ignored chars: " + ignored_chars);
        StringTokenizer str = new StringTokenizer(text);//пошук

        while(str.hasMoreTokens()){
            try{
                String s = str.nextToken(ignored_chars);

                //виклик функції порівняння

                this.compareString(s);

            }catch (Exception e){
                Log.e("Error","Помилка при відсіюванні групи: "+ignored_chars);
            }
        }
        this.resultsReady = true;
        return;
    }

    public void startCounting(String text){
        String chars = " abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ"
                + "абвгґдеєжзиіїйклмнопрстуфхцчшщьюяАБВГҐДЕЄЖЗИІЇКЛМНОПРСТУФХЦЧШЩЬЮЯ"
                + "1234567890~_Ⓡ    \n\t";
        String charsToIgnore = " .,;:!?+=*-~`  {(//<[]>)}\"\'|&\n\t  \\    ";//chars to ignore



        if (text.isEmpty()) {
            System.out.println("empty field found");
            return;
        }

        this.find(text,charsToIgnore);

    }

    public String getResult(){
        String info = "Слова: \n";
        System.out.println("Preparing result. Unique words found: " + this.wordsVocabulary.size());
        for (int i = 0; i < this.wordsVocabulary.size(); i++){
            info += " " + this.wordsVocabulary.get(i) + "  " + this.word_used.get(i) + " \n";
            double percent = (i+1)/(this.wordsVocabulary.size()/100.0);
            String statusBar = Double.toString(percent);
            System.out.println(statusBar+"% of results ready");
        }

        info += "Літери: \n";
        for (int i = 0; i < this.charactersVocabulary.size(); i++){
            info += " " + this.charactersVocabulary.get(i) + "  " + this.characters_used.get(i) + " \n";

        }

        String message = "Всього слів у тексті: " + this.wordsCount + ". Всього символів: " + this.charsCount + ".\n "
                        + "Унікальних слів: "+ this.wordsVocabulary.size();

        return message + "\n" + info;
    }

}
