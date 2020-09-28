package com.example.surveymayor.db.dao;

import android.app.ListActivity;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.surveymayor.db.dto.Answer;

import java.util.List;

@Dao
public interface AnswerDao {

    @Query("SELECT * FROM answer answer " +
            "INNER JOIN hint hint on hint.id=answer.hint_id " +
            "INNER JOIN question question on question.id=hint.question_id " +
            "WHERE person_id=:person and question.id=:question")
    LiveData<List<Answer>> getAnswersByPersonaAndQuestion(String question, String person);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Answer answer);

    @Query("SELECT * FROM answer " +
            " INNER JOIN person on person.id=answer.person_id " +
            "where person.person_id=:personId")
    LiveData<List<Answer>> getAnswersByPerson(String personId);

    @Query("SELECT * FROM answer where hint_id=:hintId")
    LiveData<Answer> getAnswerByHint(String hintId);

    @Query("SELECT DISTINCT * FROM answer " +
            "WHERE `update`=0")
    LiveData<List<Answer>> getAll();

}
