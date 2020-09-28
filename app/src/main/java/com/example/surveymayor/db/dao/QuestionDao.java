package com.example.surveymayor.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.surveymayor.db.dto.Question;

import java.util.List;

@Dao
public interface QuestionDao {

    @Query("SELECT * FROM question")
    LiveData<List<Question>> getAll();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Question question);
}
