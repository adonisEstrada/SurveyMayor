package com.example.surveymayor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.surveymayor.db.dto.Answer;
import com.example.surveymayor.db.dto.Hint;
import com.example.surveymayor.db.dto.Person;
import com.example.surveymayor.db.dto.Question;
import com.example.surveymayor.db.viewmodel.AnswerViewModel;
import com.example.surveymayor.db.viewmodel.HintViewModel;
import com.example.surveymayor.db.viewmodel.PersonViewModel;
import com.example.surveymayor.db.viewmodel.QuestionViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ListViewActivity extends AppCompatActivity {

    private QuestionViewModel questionViewModel;
    private HintViewModel hintViewModel;
    private PersonViewModel personViewModel;
    private AnswerViewModel answerViewModel;

    private ListView listView;
    private ProgressBar progressBar;
    private ImageButton imageUpdate;

    private List<Question> questions;
    private List<Hint> hints;

    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view);
        listView = (ListView) findViewById(R.id.list_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageUpdate = (ImageButton) findViewById(R.id.image_update);

        questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        hintViewModel = new ViewModelProvider(this).get(HintViewModel.class);
        personViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        answerViewModel = new ViewModelProvider(this).get(AnswerViewModel.class);

        bundle = getIntent().getExtras();

        if (bundle != null && bundle.getString("command").equals("questions")) {
            loadQuestions();
        }
        if (bundle != null && bundle.getString("command").equals("hints")) {
            loadHints(bundle.getString("question"));
        }
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, final int index, long l) {
                if (bundle.getString("command").equals("questions")) {
                    Intent intent = new Intent(ListViewActivity.this, ListViewActivity.class);
                    intent.putExtra("command", "hints");
                    intent.putExtra("question", questions.get(index).getId());
                    intent.putExtra("person", bundle.getString("person"));
                    startActivity(intent);
                } else {
                    hintViewModel.getHint(hints.get(index).getId()).observe(ListViewActivity.this, new Observer<Hint>() {
                        @Override
                        public void onChanged(Hint hint) {
                            if (hint != null) {
                                new AlertDialog.Builder(ListViewActivity.this).setTitle("Atención!")
                                        .setMessage("Ya se seleccionó una respuesta anteriormente, desea corregir la respuesta?")
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                finish();
                                                onBackPressed();
                                            }
                                        }).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        save(index);
                                    }
                                }).create().show();
                            } else {
                                new AlertDialog.Builder(ListViewActivity.this).setTitle("Atención!").setMessage("Se guardará la respuesta")
                                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                dialogInterface.dismiss();
                                                finish();
                                                onBackPressed();
                                            }
                                        }).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        save(index);
                                    }
                                }).create().show();
                            }
                        }
                    });
                }
            }
        });
        imageUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getQuestionFromFirebase();
            }
        });
    }

    private void save(final int index) {
        personViewModel.getPerson(bundle.getString("person")).observe(ListViewActivity.this, new Observer<Person>() {
            @Override
            public void onChanged(Person person) {
                if (person == null) {
                    person = new Person();
                    person.setId(UUID.randomUUID().toString());
                    person.setPersonId(bundle.get("person").toString());
                    person.setUpdate(false);
                    personViewModel.insert(person);
                }
                final Person finalPerson = person;
                answerViewModel.getAnswerByHint(hints.get(index).getId()).observe(ListViewActivity.this, new Observer<Answer>() {
                    @Override
                    public void onChanged(Answer answer) {
                        if (answer != null) {
                            answer.setHintId(hints.get(index).getId());
                            answer.setPersonId(finalPerson.getId());
                            answer.setUpdate(false);
                            answerViewModel.insert(answer);
                        } else {
                            answer = new Answer();
                            answer.setId(UUID.randomUUID().toString());
                            answer.setHintId(hints.get(index).getId());
                            answer.setPersonId(finalPerson.getId());
                            answer.setUpdate(false);
                            answerViewModel.insert(answer);
                        }
                        finish();
                        onBackPressed();
                    }
                });
            }
        });
    }

    private void loadHints(String question) {
        showProgress();
        hintViewModel.getAll(question).observe(this, new Observer<List<Hint>>() {
            @Override
            public void onChanged(List<Hint> hints) {
                if (!hints.isEmpty()) {
                    hideProgress();
                    ListViewActivity.this.hints = hints;
                    loadHintAdapter();
                }
            }
        });
    }

    public void getQuestionFromFirebase() {
        new AlertDialog.Builder(this).setTitle("Atención!")
                .setMessage("Se actualizará el listado de preguntas, verifique tener conexión a internet.")
                .setNeutralButton("Aceptar", null)
                .setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialogInterface) {
                        showProgress();
                        FirebaseFirestore.getInstance().collection("questions").get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(Task<QuerySnapshot> task) {
                                for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                    Question question = new Question();
                                    question.setId(document.getDocument().getId());
                                    question.setValue(document.getDocument().getData().get("value").toString());
                                    questionViewModel.insert(question);
                                }
                                hideProgress();
                                new AlertDialog.Builder(ListViewActivity.this).setTitle("Listo!")
                                        .setNeutralButton("Aceptar", null)
                                        .setMessage("Listado de preguntas actualizada.")
                                        .create().show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hideProgress();
                                new AlertDialog.Builder(ListViewActivity.this).setTitle("Error!")
                                        .setNeutralButton("Aceptar", null)
                                        .setMessage("Verifique su conexión a internet.")
                                        .create().show();
                            }
                        });
                        FirebaseFirestore.getInstance().collection("hints")
                                .get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(Task<QuerySnapshot> task) {
                                for (DocumentChange document : task.getResult().getDocumentChanges()) {
                                    Hint hint = new Hint();
                                    hint.setId(document.getDocument().getId());
                                    hint.setQuestionId(document.getDocument().getData().get("question_id").toString());
                                    hint.setValue(document.getDocument().getData().get("value").toString());
                                    hintViewModel.insert(hint);
                                }
                                hideProgress();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                hideProgress();
                                new AlertDialog.Builder(ListViewActivity.this).setTitle("Error!")
                                        .setNeutralButton("Aceptar", null)
                                        .setMessage("Verifique su conexión a internet.")
                                        .create().show();
                            }
                        });
                    }
                }).create().show();
    }

    private void firebaseCallback() {
        FirebaseFirestore.getInstance().collection("questions").get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (DocumentChange document : task.getResult().getDocumentChanges()) {
                    Question question = new Question();
                    question.setId(document.getDocument().getId());
                    question.setValue(document.getDocument().getData().get("value").toString());
                    questionViewModel.insert(question);
                }
                hideProgress();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
                new AlertDialog.Builder(ListViewActivity.this).setTitle("Error!")
                        .setNeutralButton("Aceptar", null)
                        .setMessage("Verifique su conexión a internet.")
                        .create().show();
            }
        });
        FirebaseFirestore.getInstance().collection("hints")
                .get(Source.SERVER).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                for (DocumentChange document : task.getResult().getDocumentChanges()) {
                    Hint hint = new Hint();
                    hint.setId(document.getDocument().getId());
                    hint.setQuestionId(document.getDocument().getData().get("question_id").toString());
                    hint.setValue(document.getDocument().getData().get("value").toString());
                    hintViewModel.insert(hint);
                }
                hideProgress();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                hideProgress();
                new AlertDialog.Builder(ListViewActivity.this).setTitle("Error!")
                        .setNeutralButton("Aceptar", null)
                        .setMessage("Verifique su conexión a internet.")
                        .create().show();
            }
        });
    }

    private void loadQuestions() {
        showProgress();
        questionViewModel.getQuestions().observe(this, new Observer<List<Question>>() {
            @Override
            public void onChanged(List<Question> questions) {
                if (!questions.isEmpty()) {
                    hideProgress();
                    ListViewActivity.this.questions = questions;
                    loadQuestionAdapter(null);
                } else {
                    firebaseCallback();
                }
            }
        });

        if (bundle.get("person") != null) {
            hintViewModel.getHintByPerson(bundle.getString("person")).observe(this, new Observer<List<Hint>>() {
                @Override
                public void onChanged(List<Hint> hints) {
                    loadQuestionAdapter(hints);
                }
            });
        }
    }

    private void loadQuestionAdapter(List<Hint> hints) {
        List<ListViewItems> adapters = new ArrayList<>();
        if (questions != null && !questions.isEmpty()) {
            for (Question question : questions) {
                ListViewItems listViewItems = new ListViewItems(question.getValue());
                if (hints != null && !hints.isEmpty()) {
                    for (Hint hint : hints) {
                        if (hint.getQuestionId().equals(question.getId())) {
                            listViewItems.setTitulo2(hint.getValue());
                        }
                    }
                }
                adapters.add(listViewItems);
            }
        }
        GenericAdapter genericAdapter = new GenericAdapter(this, R.id.list_view, adapters);
        listView.setAdapter(genericAdapter);
    }


    private void loadHintAdapter() {
        List<ListViewItems> adapters = new ArrayList<>();
        for (Hint hint : hints) {
            ListViewItems listViewItems = new ListViewItems(hint.getValue());
            adapters.add(listViewItems);
        }
        GenericAdapter genericAdapter = new GenericAdapter(this, R.id.list_view, adapters);
        listView.setAdapter(genericAdapter);
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
