package com.example.surveymayor.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.surveymayor.db.RoomDB;
import com.example.surveymayor.db.dao.HintDao;
import com.example.surveymayor.db.dao.QuestionDao;
import com.example.surveymayor.db.dto.Answer;
import com.example.surveymayor.db.dto.Hint;
import com.example.surveymayor.db.dto.Question;

import java.util.List;

public class HintRepository {

    private HintDao hintDao;

    public HintRepository(Application application) {
        RoomDB roomDB = RoomDB.getDatabase(application);
        hintDao = roomDB.hintDao();
    }

    public LiveData<List<Hint>> getHints(String question) {
        return hintDao.getHintByQuestion(question);
    }

    public void insert(Hint hint) {
        new InsertHintAsyncTask(hintDao).execute(hint);
    }

    public LiveData<Hint> getHint(String hintId){
        return hintDao.getHint(hintId);
    }

    /**
     * obtiene los hints que ha seleccionado esa persona
     * @param person en base a la cédula
     * @return lista de hints
     */
    public LiveData<List<Hint>> getHintsByPerson(String person){
        return hintDao.getHintsByPerson(person);
    }

    private static class InsertHintAsyncTask extends AsyncTask<Hint, Void, Void> {

        private HintDao hintDao;

        public InsertHintAsyncTask(HintDao hintDao) {
            this.hintDao = hintDao;
        }

        @Override
        protected Void doInBackground(Hint... hints) {
            hintDao.insert(hints[0]);
            return null;
        }
    }
}
