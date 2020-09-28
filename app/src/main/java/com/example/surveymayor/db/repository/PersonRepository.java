package com.example.surveymayor.db.repository;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.surveymayor.db.RoomDB;
import com.example.surveymayor.db.dao.PersonDao;
import com.example.surveymayor.db.dto.Person;

import java.util.List;

public class PersonRepository {

    private PersonDao personDao;

    public PersonRepository(Application application) {
        RoomDB roomDB = RoomDB.getDatabase(application);
        personDao = roomDB.personDao();
    }

    public void insert(Person person){
        new InsertPersonAsyncTask(personDao).execute(person);
    }

    public LiveData<List<Person>> getAll(){
        return personDao.getAll();
    }

    public LiveData<Person> getPerson(String person){
        return personDao.getPerson(person);
    }

    public LiveData<List<Person>> getAllUpdate(){
        return personDao.getAllUpdate();
    }

    private static class InsertPersonAsyncTask extends AsyncTask<Person, Void, Void>{

        private PersonDao personDao;

        public InsertPersonAsyncTask(PersonDao personDao) {
            this.personDao = personDao;
        }

        @Override
        protected Void doInBackground(Person... people) {
            personDao.insert(people[0]);
            return null;
        }
    }

}
