package com.example.surveymayor.db;


import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.surveymayor.db.dao.AnswerDao;
import com.example.surveymayor.db.dao.HintDao;
import com.example.surveymayor.db.dao.PersonDao;
import com.example.surveymayor.db.dao.QuestionDao;
import com.example.surveymayor.db.dto.Answer;
import com.example.surveymayor.db.dto.Hint;
import com.example.surveymayor.db.dto.Person;
import com.example.surveymayor.db.dto.Question;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Question.class, Answer.class, Hint.class, Person.class}, version = 1, exportSchema = false)
public abstract class RoomDB extends RoomDatabase {
    public abstract QuestionDao questionDao();
    public abstract HintDao hintDao();
    public abstract PersonDao personDao();
    public abstract AnswerDao answerDao();

    private static volatile RoomDB INSTANCE;
    private static final int NUMBER_OF_THREADS = 4;
    public static final ExecutorService databaseWriteExecutor =
            Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static RoomDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (RoomDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            RoomDB.class, "survey_database").addCallback(roomDBCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomDBCallback = new RoomDatabase.Callback() {
        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
            new PopulateRoomDBAsync(INSTANCE).execute();
        }
    };

    public static class PopulateRoomDBAsync extends AsyncTask<Void, Void, Void> {

        private QuestionDao questionDao;

        public PopulateRoomDBAsync(RoomDB roomDB) {
            this.questionDao = roomDB.questionDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {

//            Question question = new Question();
//            question.setId(UUID.randomUUID().toString());
//            question.setValue("¿Es usted un gay?");
//            questionDao.insert(question);
//            FirebaseFirestore.getInstance().collection("questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                @Override
//                public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                    for (QueryDocumentSnapshot document : task.getResult()) {
//                        Question question = new Question();
//                        question.setId(document.getId());
//                        question.setValue(document.getData().get("value").toString());
//                        questionDao.insert(question);
//                    }
//                }
//            });

            return null;
        }
    }
}
