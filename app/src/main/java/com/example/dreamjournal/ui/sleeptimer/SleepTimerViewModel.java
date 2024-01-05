package com.example.dreamjournal.ui.sleeptimer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SleepTimerViewModel extends ViewModel {

    private final MutableLiveData<String> mText;

    public SleepTimerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is notifications fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}