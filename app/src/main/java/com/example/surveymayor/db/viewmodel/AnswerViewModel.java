package com.example.surveymayor.db.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.surveymayor.db.dto.Answer;
import com.example.surveymayor.db.repository.AnswerRepository;

import java.util.List;

public class AnswerViewModel extends AndroidViewModel {

    private AnswerRepository answerRepository;

    public AnswerViewModel(@NonNull Application application) {
        super(application);
        answerRepository = new AnswerRepository(application);
    }

    public void insert(Answer answer) {
        answerRepository.insertAnswer(answer);
    }

    public LiveData<List<Answer>> getAnswer(String question, String person) {
        return answerRepository.getAll(question, person);
    }

    public LiveData<List<Answer>> getAnswer(String person) {
        return answerRepository.getAnswer(person);
    }

    public LiveData<Answer> getAnswerByHint(String hintId) {
        return answerRepository.getAnswerByHint(hintId);
    }

    public LiveData<List<Answer>> getAll(){
        return answerRepository.getAll();
    }
}
