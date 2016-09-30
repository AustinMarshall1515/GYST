package com.example.metlegendynamics.gyst;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
//import android.app.Fragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;


// In this case, the fragment displays simple text based on the page
public class SemesterFragment extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    public ListView lv;
    public TextView sts;
    public ArrayList<String> semesters;

    // newInstance constructor for creating fragment with arguments
    public static SemesterFragment newInstance(int page, String title) {
        SemesterFragment fragmentFirst = new SemesterFragment();
        Bundle args = new Bundle();
        args.putInt("someInt", page);
        args.putString("someTitle", title);
        fragmentFirst.setArguments(args);
        return fragmentFirst;
    }

    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        page = getArguments().getInt("someInt", 0);
        title = getArguments().getString("someTitle");

        semesters = new ArrayList<>();
        GYST_db help = new GYST_db(getContext());
        help.open();
        semesters = help.getSemesters();
        help.close();

//        semesters.add("CPA1");
//        semesters.add("CPA2");
//        semesters.add("CPA3");
//        semesters.add("CPA4");
    }//end onCreate

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_semester, container, false);
        lv = (ListView) view.findViewById(R.id.SemesterListView);
        sts = (TextView) view.findViewById(R.id.SemesterTextSemester);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long arg3) {
                view.setSelected(true);
                String selected = lv.getItemAtPosition(position).toString();
                sts.setText("Semester: " + selected);
            }
        });



        ArrayAdapter<String> adapter = new ArrayAdapter(
                getContext(),
                android.R.layout.simple_list_item_1,
                semesters
        );

        lv.setAdapter(adapter);


        return view;
    }
}