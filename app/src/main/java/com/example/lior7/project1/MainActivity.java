package com.example.lior7.project1;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity{
    public static final String NAME = "NAME";
    public static final String AGE = "AGE";
    private boolean isValidInputs;
    private EditText editTextName, editTextAge;
    private Button StartButton;
    private Button mapButton;
    private Button tableButton;
    Fragment tableFragment;
    Fragment mapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bindUI();

        // Create and set Android Fragment as default.
        tableFragment = new TableFragment();
        this.setDefaultFragment(tableFragment);

        // Click this button to display table fragment.
        tableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(tableFragment);
            }
        });

        // Click this button to display map fragment.
        mapButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mapFragment = new MapFragment();
                replaceFragment(mapFragment);
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
        tableButton = findViewById(R.id.dynamic_fragment_table_button);
        mapButton = findViewById(R.id.dynamic_fragment_map_button);
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

    // This method is used to set the default fragment that will be shown.
    private void setDefaultFragment(Fragment defaultFragment)
    {
        this.replaceFragment(defaultFragment);
    }

    // Replace current Fragment with the destination Fragment.
    public void replaceFragment(Fragment destFragment)
    {
        // First get FragmentManager object.
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Begin Fragment transaction.
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the layout holder with the required Fragment object.
        fragmentTransaction.replace(R.id.fragment_container, destFragment);

        // Commit the Fragment replace action.
        fragmentTransaction.commit();
    }
}
