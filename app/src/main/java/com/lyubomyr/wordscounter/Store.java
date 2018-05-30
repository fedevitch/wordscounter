package com.lyubomyr.wordscounter;

public class Store {

    private CountResult countResult;

    private static final Store ourInstance = new Store();

    public static Store getInstance() {
        return ourInstance;
    }

    private Store() {

    }

    public CountResult getCountResult() {
        return countResult;
    }

    public void setCountResult(CountResult countResult) {
        this.countResult = countResult;
    }
}
