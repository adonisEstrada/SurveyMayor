package com.example.surveymayor.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.surveymayor.db.dto.Person;

import java.util.List;

@Dao
public interface PersonDao {

    @Query("SELECT * FROM person ")
    LiveData<List<Person>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Person person);

    @Query("SELECT * FROM person where person_id=:personId")
    LiveData<Person> getPerson(String personId);

    @Query("SELECT * FROM person " +
            "WHERE `update` = 0")
    LiveData<List<Person>> getAllUpdate();
}
