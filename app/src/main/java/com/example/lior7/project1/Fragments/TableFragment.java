package com.example.lior7.project1.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;

import com.example.lior7.project1.Adapters.ListAdapter;
import com.example.lior7.project1.Object_Classes.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends ListFragment {
    private List exampleListItemList; // at the top of your fragment list

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        exampleListItemList = new ArrayList();
        // TODO: change to load from DB
        exampleListItemList.add(new UserDetails("Lior", 21, 100));

        this.setListAdapter(new ListAdapter(getActivity(), exampleListItemList));
    }
}
