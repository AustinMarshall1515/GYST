package com.example.metlegendynamics.gyst;

import android.content.Intent;
import android.database.sqlite.SQLiteException;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class AddSemesterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_semester);
    }

    public void addSemester(View view) {
        Intent i = new Intent(this, GYST_MainActivity.class);
        EditText semesterText = (EditText) findViewById(R.id.editTextSemester);
        String sem = semesterText.getText().toString();
        boolean success = true;
        try{
            GYST_db help = new GYST_db(this);
            help.open();
            help.createEntry(sem);
            help.close();
        }catch(SQLiteException sex){
            success = false;
        }//end try catch

        if(success){
            //add successful, pass back
            i.putExtra("result", "Semester Added");
        }else
            i.putExtra("result", "Failed");

        //send back to main
        startActivity(i);

    }//end addSemester
}
