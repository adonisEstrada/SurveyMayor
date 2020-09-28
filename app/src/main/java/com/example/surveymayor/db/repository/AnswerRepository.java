package com.example.surveymayor.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.surveymayor.db.RoomDB;
import com.example.surveymayor.db.dao.AnswerDao;
import com.example.surveymayor.db.dto.Answer;

import java.util.List;

public class AnswerRepository {

    private AnswerDao answerDao;

    public AnswerRepository(Application application) {
        RoomDB roomDB = RoomDB.getDatabase(application);
        answerDao = roomDB.answerDao();
    }

    public void insertAnswer(Answer answer){
        new InsertAnswerAsyncTask(answerDao).execute(answer);
    }

    public LiveData<List<Answer>> getAll(String question, String person){
        return answerDao.getAnswersByPersonaAndQuestion(question, person);
    }

    public LiveData<List<Answer>> getAnswer(String person){
        return answerDao.getAnswersByPerson(person);
    }

    public LiveData<Answer> getAnswerByHint(String hintId){
        return answerDao.getAnswerByHint(hintId);
    }

    public LiveData<List<Answer>> getAll(){
        return answerDao.getAll();
    }

    private static class InsertAnswerAsyncTask extends AsyncTask<Answer, Void, Void>{

        private AnswerDao answerDao;

        public InsertAnswerAsyncTask(AnswerDao answerDao) {
            this.answerDao = answerDao;
        }

        @Override
        protected Void doInBackground(Answer... answers) {
            answerDao.insert(answers[0]);
            return null;
        }
    }
}
