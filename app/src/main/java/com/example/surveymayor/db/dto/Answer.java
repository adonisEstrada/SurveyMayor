package com.example.surveymayor.db.dto;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Embedded;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity(tableName = "answer")
public class Answer {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "person_id")
    private String personId;
    //    @Embedded
//    private Person person;
    //
    @ColumnInfo(name = "hint_id")
    private String hintId;
//    @Embedded
//    private Hint hint;

    private Boolean update;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPersonId() {
        return personId;
    }

    public void setPersonId(String personId) {
        this.personId = personId;
    }

    public String getHintId() {
        return hintId;
    }

    public void setHintId(String hintId) {
        this.hintId = hintId;
    }

    public Boolean getUpdate() {
        return update;
    }

    public void setUpdate(Boolean update) {
        this.update = update;
    }

//    public Person getPerson() {
//        return person;
//    }
//
//    public void setPerson(Person person) {
//        this.person = person;
//    }
//
//    public Hint getHint() {
//        return hint;
//    }
//
//    public void setHint(Hint hint) {
//        this.hint = hint;
//    }
}
