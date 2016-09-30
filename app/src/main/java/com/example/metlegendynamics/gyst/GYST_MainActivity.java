package com.example.metlegendynamics.gyst;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

public class GYST_MainActivity extends AppCompatActivity {

    FragmentPagerAdapter adapterViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gyst__main);
        ViewPager vpPager = (ViewPager) findViewById(R.id.container);
        adapterViewPager = new MyPagerAdapter(getSupportFragmentManager());
        vpPager.setAdapter(adapterViewPager);
    }

    public void add_semester(View view) {
        Intent i = new Intent(this, AddSemesterActivity.class);
        startActivity(i);
    }

    public static class MyPagerAdapter extends FragmentPagerAdapter {
        private static int NUM_ITEMS = 4;

        public MyPagerAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        // Returns total number of pages
        @Override
        public int getCount() {
            return NUM_ITEMS;
        }

        // Returns the fragment to display for that page
        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: // Semesters fragment
                    return SemesterFragment.newInstance(1, "Semesters Page");
                case 1: // Course fragment
                    return CourseFragment.newInstance(2, "Course Page");
                case 2: // assignment fragment
                    return AssignmentsFragment.newInstance(3, "assignment Page");
                case 3: // notification fragment
                    return NotificationsFragment.newInstance(4, "notification Page");
                default:
                    return null;
            }
        }

        // Returns the page title for the top indicator
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0: // Semesters fragment
                    return "Semesters";
                case 1: // Course fragment
                    return "Courses";
                case 2: // assignment fragment
                    return "Assignments";
                case 3: // notification fragment
                    return "Notifications";
                default:
                    return null;
            }
        }

    }

}
