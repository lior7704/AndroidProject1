package com.example.lior7.project1.Fragments;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.widget.ListView;

import com.example.lior7.project1.Adapters.ListAdapter;
import com.example.lior7.project1.Object_Classes.UserDetails;

import java.util.ArrayList;
import java.util.List;

public class TableFragment extends ListFragment {
    private List<UserDetails> userDetailsList = new ArrayList<>();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setListAdapter(new ListAdapter(getActivity(), userDetailsList));
    }
}
