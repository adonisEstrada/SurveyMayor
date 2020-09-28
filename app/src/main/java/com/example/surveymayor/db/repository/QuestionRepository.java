package com.example.surveymayor.db.repository;

import android.app.AlertDialog;
import android.app.Application;
import android.content.DialogInterface;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Query;

import com.example.surveymayor.db.RoomDB;
import com.example.surveymayor.db.dao.QuestionDao;
import com.example.surveymayor.db.dto.Question;
import com.google.android.gms.tasks.OnCanceledListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

public class QuestionRepository {

    private QuestionDao questionDao;
    private Application application;

    public QuestionRepository(Application application) {
        this.application = application;
        RoomDB roomDB = RoomDB.getDatabase(application);
        questionDao = roomDB.questionDao();
    }

    public LiveData<List<Question>> getQuestions() {
        return questionDao.getAll();
    }

    public void insert(Question question) {
        new InsertQuestionAsyncTask(questionDao).execute(question);
    }

//    public void getQuestionFromFirebase() {
//        new AlertDialog.Builder(application.getBaseContext()).setTitle("Atención!")
//                .setMessage("Se actualizará el listado de preguntas, verifique tener conexión a internet.")
//                .setOnDismissListener(new DialogInterface.OnDismissListener() {
//                    @Override
//                    public void onDismiss(DialogInterface dialogInterface) {
//                        FirebaseFirestore.getInstance().collection("questions").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                            @Override
//                            public void onComplete(Task<QuerySnapshot> task) {
//                                for (DocumentChange document : task.getResult().getDocumentChanges()) {
//                                    Question question = new Question();
//                                    question.setId(document.getDocument().getId());
//                                    question.setValue(document.getDocument().getData().get("value").toString());
//                                    insert(question);
//                                }
//                                new AlertDialog.Builder(application.getBaseContext()).setTitle("Listo!")
//                                        .setMessage("Listado de preguntas actualizada.")
//                                        .create().show();
//                            }
//                        }).addOnCanceledListener(new OnCanceledListener() {
//                            @Override
//                            public void onCanceled() {
//                                new AlertDialog.Builder(application.getBaseContext()).setTitle("Error!")
//                                        .setMessage("Verifique su conexión a internet.")
//                                        .create().show();
//                            }
//                        });
//                    }
//                }).create().show();
//    }

    private static class InsertQuestionAsyncTask extends AsyncTask<Question, Void, Void> {

        private QuestionDao questionDao;

        public InsertQuestionAsyncTask(QuestionDao questionDao) {
            this.questionDao = questionDao;
        }

        @Override
        protected Void doInBackground(Question... questions) {
            questionDao.insert(questions[0]);
            return null;
        }
    }

}
