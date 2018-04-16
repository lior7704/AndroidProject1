package com.example.lior7.project1;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DifficultyActivity extends AppCompatActivity implements View.OnClickListener {
    public static final String NUM_OF_CUBES = "NUM_OF_CUBES";
    public static final String TIME = "TIME";
    public static final int NUM_CARDS_EASY = 2, EASY_TIME = 30;
    public static final int NUM_CARDS_MEDIUM = 4, MEDIUM_TIME = 45;
    public static final int NUM_CARDS_HARD = 6, HARD_TIME = 60;
    public static final String RESULT = "RESULT";
    public static final int REQUEST_CODE = 1;
    private String name, age, headerText;
    private Button btnEasy, btnMedium, btnHard;
    private TextView tvHeader;
    int numCards, time;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);

        Bundle data = getIntent().getExtras();
        name = data.getString(MainActivity.NAME);
        age = data.getString(MainActivity.AGE);
        boundUI();
    }

    private void boundUI()
    {
        btnEasy = findViewById(R.id.buttonEasy);
        btnEasy.setOnClickListener(this);
        btnMedium = findViewById(R.id.buttonMedium);
        btnMedium.setOnClickListener(this);
        btnHard = findViewById(R.id.buttonHard);
        btnHard.setOnClickListener(this);
        tvHeader = findViewById(R.id.textViewHeader);

        // set textView text with user input
        Resources res = getResources();
        headerText = String.format(res.getString(R.string.diff_header), name, age);
        tvHeader.setText(headerText);
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        switch (id){
            case R.id.buttonEasy:
                numCards = NUM_CARDS_EASY;
                time = EASY_TIME;
                break;
            case R.id.buttonMedium:
                numCards = NUM_CARDS_MEDIUM;
                time = MEDIUM_TIME;
                break;
            case R.id.buttonHard:
                numCards = NUM_CARDS_HARD;
                time = HARD_TIME;
                break;
        }

        Intent intent = new Intent(this, GameActivity.class);
        intent.putExtra(MainActivity.NAME, name);
        intent.putExtra(NUM_OF_CUBES, numCards);
        intent.putExtra(TIME, time);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode != RESULT_CANCELED){
            String result = data.getStringExtra(DifficultyActivity.RESULT);
            if(!result.equals("")){
                tvHeader.setText(getString(R.string.game_win_message));
            }
            else
                tvHeader.setText(headerText);
        }
    }

}
