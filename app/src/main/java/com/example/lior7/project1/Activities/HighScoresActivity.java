package com.example.lior7.project1.Activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.Button;
import com.example.lior7.project1.Fragments.MapFragment;
import com.example.lior7.project1.Fragments.TableFragment;
import com.example.lior7.project1.Object_Classes.UserDetails;
import com.example.lior7.project1.Persistent_Data_Storage.DBController;
import com.example.lior7.project1.R;

import java.util.List;

public class HighScoresActivity extends FragmentActivity {

    private Button mapButton;
    private Button tableButton;
    Fragment tableFragment;
    Fragment mapFragment;

    public static DBController userDetailsDB;
    public static boolean givePermission;

    public static List<UserDetails> userDetailsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_scores);

        bindUI();

        // Click this button to display table fragment.
        tableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tableFragment = new TableFragment();
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
    }

    private void bindUI()
    {
        tableButton = findViewById(R.id.dynamic_fragment_table_button);
        mapButton = findViewById(R.id.dynamic_fragment_map_button);
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
