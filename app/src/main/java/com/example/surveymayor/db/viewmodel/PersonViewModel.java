package com.example.surveymayor.db.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.surveymayor.db.dto.Person;
import com.example.surveymayor.db.repository.PersonRepository;

import java.util.List;

public class PersonViewModel extends AndroidViewModel {

    private PersonRepository personRepository;

    public PersonViewModel(@NonNull Application application) {
        super(application);
        personRepository = new PersonRepository(application);
    }

    public void insert(Person person){
        personRepository.insert(person);
    }

    public LiveData<List<Person>> getAll(){
        return personRepository.getAll();
    }

    public LiveData<Person> getPerson(String person){
        return personRepository.getPerson(person);
    }

    public LiveData<List<Person>> getAllUpdate(){
        return personRepository.getAllUpdate();
    }

}
