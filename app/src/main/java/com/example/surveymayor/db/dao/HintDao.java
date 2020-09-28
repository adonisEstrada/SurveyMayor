package com.example.surveymayor.db.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.surveymayor.db.dto.Hint;
import com.example.surveymayor.db.dto.Question;

import java.util.List;

@Dao
public interface HintDao {

    @Query("SELECT * FROM hint where question_id=:questionId")
    LiveData<List<Hint>> getHintByQuestion(String questionId);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Hint hint);

    @Query("SELECT * FROM hint " +
            "INNER JOIN answer ON answer.hint_id=hint.id " +
            "where hint.id = :hintId")
    LiveData<Hint> getHint(String hintId);

    @Query("SELECT hint.* FROM hint " +
            "INNER JOIN answer ON answer.hint_id=hint.id " +
            "INNER JOIN person ON person.id=answer.person_id " +
            "WHERE person.person_id=:person")
    LiveData<List<Hint>> getHintsByPerson(String person);

}
