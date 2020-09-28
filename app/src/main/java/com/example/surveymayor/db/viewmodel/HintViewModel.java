package com.example.surveymayor.db.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.surveymayor.db.dto.Hint;
import com.example.surveymayor.db.repository.HintRepository;

import java.util.List;

public class HintViewModel extends AndroidViewModel {

    private HintRepository hintRepository;

    public HintViewModel(@NonNull Application application) {
        super(application);
        hintRepository = new HintRepository(application);
    }

    public LiveData<List<Hint>> getAll(String question) {
        return hintRepository.getHints(question);
    }

    public void insert(Hint hint) {
        hintRepository.insert(hint);
    }

    public LiveData<Hint> getHint(String hintId) {
        return hintRepository.getHint(hintId);
    }

    public LiveData<List<Hint>> getHintByPerson(String person){
        return hintRepository.getHintsByPerson(person);
    }
}
