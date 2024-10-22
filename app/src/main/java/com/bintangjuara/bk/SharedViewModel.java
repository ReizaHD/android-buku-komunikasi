package com.bintangjuara.bk;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<String> sharedData = new MutableLiveData<>();

    public void setData(String data) {
        sharedData.setValue(data);
    }

    public LiveData<String> getData() {
        return sharedData;
    }
}
