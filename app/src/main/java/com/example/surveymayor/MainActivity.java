package com.example.surveymayor;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

public class MainActivity extends AppCompatActivity {

    private Button buttonStart;
    private EditText editText;
    private ImageButton imageButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = (Button) findViewById(R.id.button_start);
        editText = (EditText) findViewById(R.id.person_id);
        imageButton = (ImageButton) findViewById(R.id.image_update);


        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editText.getText()!=null && !editText.getText().toString().isEmpty()){
                    startListActivity();
                }else{
                    new AlertDialog.Builder(MainActivity.this).setTitle("Error!")
                            .setNeutralButton("Aceptar", null)
                            .setMessage("El campo Cédula, no debe estar vacío.").create().show();
                }
            }
        });

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListViewTotalActivity.class);
                intent.putExtra("command", "person");
                startActivity(intent);
            }
        });
     }

     private void startListActivity(){
         Intent intent = new Intent(this, ListViewActivity.class);
         intent.putExtra("command", "questions");
         intent.putExtra("person", editText.getText().toString());
         startActivity(intent);
     }
}
