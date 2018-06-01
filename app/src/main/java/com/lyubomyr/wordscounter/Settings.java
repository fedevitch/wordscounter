package com.lyubomyr.wordscounter;

public class Settings {

    public Boolean saveResults;
    public int numOfRecords;

    public Boolean displayChart;
    public Boolean displayTable;

    public String ignoredSymbols;

    public enum Names {
        save_results, num_of_records, ignored_symbols,
        display_chart, display_table
    };

    public enum Types {
        _switch, num_picker, text_modal, checkbox
    }


}
