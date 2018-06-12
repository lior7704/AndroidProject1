package com.example.lior7.project1.Activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.lior7.project1.R;

public class MainActivity extends AppCompatActivity{
    public static final String NAME = "NAME";
    public static final String AGE = "AGE";
    private boolean isValidInputs;
    private EditText editTextName, editTextAge;
    private Button StartButton;
    private Button highScoresButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();

        highScoresButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HighScoresActivity.class));
            }
        });

        StartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isContinue = checkInputs();
                if(isContinue)
                    goToLevelsActivity();
            }
        });
    }

    private void bindUI()
    {
        editTextName = findViewById(R.id.editTextName);
        editTextName.requestFocus();
        editTextAge = findViewById(R.id.editTextAge);
        StartButton = findViewById(R.id.buttonStart);
        highScoresButton = findViewById(R.id.buttonHighScores);
    }

    private boolean checkInputs()
    {
        isValidInputs = true;
        if(editTextName.getText().length() == 0) {
            editTextName.setError("Please enter name");
            isValidInputs = false;
        }
        if(editTextAge.getText().length() == 0) {
            editTextAge.setError("Please enter age");
            isValidInputs = false;
        }
        return isValidInputs;
    }

    private void goToLevelsActivity()
    {
        String name = editTextName.getText().toString();
        String age = editTextAge.getText().toString();

        Intent intent = new Intent(this, DifficultyActivity.class);
        intent.putExtra(NAME, name);
        intent.putExtra(AGE, age);
        startActivity(intent);
    }
}
