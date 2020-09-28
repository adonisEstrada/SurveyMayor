package com.example.surveymayor;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ListViewTotalActivity extends AppCompatActivity {

    private PersonViewModel personViewModel;
    private HintViewModel hintViewModel;
    private QuestionViewModel questionViewModel;
    private AnswerViewModel answerViewModel;

    private Bundle bundle;

    private ListView listView;
    private ProgressBar progressBar;
    private ImageButton imageButton;

    private List<Person> people;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_view_total);
        listView = (ListView) findViewById(R.id.list_view);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        imageButton = (ImageButton) findViewById(R.id.image_update);

        personViewModel = new ViewModelProvider(this).get(PersonViewModel.class);
        hintViewModel = new ViewModelProvider(this).get(HintViewModel.class);
        questionViewModel = new ViewModelProvider(this).get(QuestionViewModel.class);
        answerViewModel = new ViewModelProvider(this).get(AnswerViewModel.class);

        bundle = getIntent().getExtras();

        if (bundle != null) {
            if (bundle.getString("command").equals("person")) {
                showProgress();
                personViewModel.getAll().observe(this, new Observer<List<Person>>() {
                    @Override
                    public void onChanged(List<Person> people) {
                        personAdapter(people);
                        hideProgress();
                        ListViewTotalActivity.this.people = people;
                    }
                });
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        Intent intent = new Intent(ListViewTotalActivity.this, ListViewTotalActivity.class);
                        intent.putExtra("command", "question");
                        intent.putExtra("person", people.get(i).getPersonId());
                        startActivity(intent);
                    }
                });
            } else if (bundle.getString("command").equals("question")) {
                showProgress();
                hintViewModel.getHintByPerson(bundle.getString("person")).observe(this, new Observer<List<Hint>>() {
                    @Override
                    public void onChanged(final List<Hint> hints) {
                        questionViewModel.getQuestions().observe(ListViewTotalActivity.this, new Observer<List<Question>>() {
                            @Override
                            public void onChanged(final List<Question> questions) {
                                answerViewModel.getAnswer(bundle.getString("person")).observe(ListViewTotalActivity.this, new Observer<List<Answer>>() {
                                    @Override
                                    public void onChanged(List<Answer> answers) {
                                        questionAdapter(questions, hints, answers);
                                        hideProgress();
                                    }
                                });
                            }
                        });
                    }
                });
            }
        }

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(ListViewTotalActivity.this).setTitle("Atención!")
                        .setMessage("La sincronización con el servidor está a punto de comenzar, desea continuar?")
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).setPositiveButton("Si", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        personViewModel.getAllUpdate().observe(ListViewTotalActivity.this, new Observer<List<Person>>() {
                            @Override
                            public void onChanged(List<Person> people) {
                                for (final Person person : people) {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("id", person.getPersonId());

                                    FirebaseFirestore.getInstance().collection("person")
                                            .document(person.getId()).set(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    person.setUpdate(true);
                                                    personViewModel.insert(person);
                                                }
                                            });
                                }
                            }
                        });
                        answerViewModel.getAll().observe(ListViewTotalActivity.this, new Observer<List<Answer>>() {
                            @Override
                            public void onChanged(List<Answer> answers) {
                                for (final Answer answer : answers) {
                                    Map<String, String> map = new HashMap<>();
                                    map.put("hint_id", answer.getHintId());
                                    map.put("person", answer.getPersonId());
                                    FirebaseFirestore.getInstance().collection("answer")
                                            .document(answer.getId()).set(map)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    answer.setUpdate(true);
                                                    answerViewModel.insert(answer);
                                                }
                                            });
                                }
                            }
                        });
                    }
                }).create().show();
            }
        });
    }

    private void questionAdapter(List<Question> questions, List<Hint> hints, List<Answer> answers) {
        List<ListViewItems> adapters = new ArrayList<>();
        for (Question question : questions) {
            ListViewItems listViewItems = new ListViewItems(question.getValue());
            if (hints != null && !hints.isEmpty()) {
                for (Hint hint : hints) {
                    if (question.getId().equals(hint.getQuestionId())) {
                        listViewItems.setTitulo2(hint.getValue());
                        if (answers != null && !answers.isEmpty()) {
                            for (Answer answer : answers) {
                                if (answer.getHintId().equals(hint.getId()) && answer.getUpdate()) {
                                    listViewItems.setTitulo3("Actualizado");
                                }
                            }
                        }
                    }
                }
            }
            adapters.add(listViewItems);
        }
        listView.setAdapter(new GenericAdapter(this, R.id.list_view, adapters));
    }

    private void personAdapter(List<Person> people) {
        List<ListViewItems> adapters = new ArrayList<>();
        for (Person person : people) {
            ListViewItems listViewItems = new ListViewItems(person.getPersonId());
            if (person.getUpdate()) {
                listViewItems.setTitulo2("Actualizado");
            }
            adapters.add(listViewItems);
        }
        listView.setAdapter(new GenericAdapter(this, R.id.list_view, adapters));
    }

    private void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        progressBar.setVisibility(View.INVISIBLE);
    }
}
