package com.example.surveymayor.db.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.surveymayor.db.dto.Question;
import com.example.surveymayor.db.repository.QuestionRepository;

import java.util.List;

public class QuestionViewModel extends AndroidViewModel {

    private QuestionRepository questionRepository;

    public QuestionViewModel(@NonNull Application application) {
        super(application);
        questionRepository = new QuestionRepository(application);
    }

    public LiveData<List<Question>> getQuestions(){
        return questionRepository.getQuestions();
    }

    public void insert(Question question){
        questionRepository.insert(question);
    }

//    public void getQuestionFromFirebase(){
//        questionRepository.getQuestionFromFirebase();
//    }
}
