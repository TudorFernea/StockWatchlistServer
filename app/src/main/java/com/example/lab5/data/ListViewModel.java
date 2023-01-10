package com.example.lab5.data;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.lab5.model.Stock;

import java.util.List;

public class ListViewModel extends ViewModel {

    MutableLiveData<List<Stock>> stocksLiveData;

    public ListViewModel() {
        stocksLiveData = new MutableLiveData<>();
    }

}
