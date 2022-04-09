package com.example.android.ui.tracker;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PeriodTrackerViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public PeriodTrackerViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is period tracker fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
