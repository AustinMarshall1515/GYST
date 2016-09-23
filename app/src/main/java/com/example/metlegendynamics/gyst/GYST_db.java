package com.example.metlegendynamics.gyst;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Josh on 2016-09-22.
 */
public class GYST_db {

    /**
     * @author Josh
     * Class Outline:
     *
     * NOTE: any section in this class can be reached by highlighting
     *      the line with a number on it and searching for it.
     *
     *      -USE 'CONTINUE FROM HERE' IN SEARCH TO FIND WHERE YOU LEFT OFF
     *
     * This class contains the database definitions as well as the table definitions
     * All database work is done in this class, and is split up in the following way
     *
     * DEFINITIONS (variables used for creation)
     * 1: DATABASE NAME DEFINITION
     * 2: SEMESTERS TABLE DEFINITION
     * 3: COURSES TABLE DEFINITION
     * 4: ASSIGNMENTS TABLE DEFINITION
     *
     * INITIALIZATIONS
     * 1: DATABASE HELPERS AND CLASS HELPERS
     * 2: SEMESTERS TABLE FUNCTIONS
     * 3: COURSES TABLE FUNCTIONS
     * 4: ASSIGNMENTS TABLE FUNCTIONS
     *
     * OBJECT CLASSES - will be used to help make data easier to transfer and use
     * 1: Course class
     * 2: Assignment Class
     */

    //1: DATABASE NAME DEFINITION
    private static final String DATABASE_NAME = "GYST_db";//name of the database
    private static final int DATABASE_VERSION = 1;//database version


    //2: SEMESTER TABLE DEFINITION
    /**
     * @author Josh
     * SEMESTER TABLE OUTLINE
     *
     * columns:
     * -id             =   Primary key
     * -semesterName   =   identifier for semester
     */
    private static final String SEM_TABLE_NAME      =   "semesters";
    private static final String SEM_TABLE_KEY_ROWID =   "id";//pkey
    private static final String SEM_TABLE_KEY_NAME  =   "semesterName";

    //3: COURSES TABLE DEFINITION
    /**
     * COURSES TABLE OUTLINE
     *
     * columns:
     * -id          =   Primary key
     * -name        =   Name of the course
     * -grade       =   Grade Received in course (Can be null)
     * -semester    =   Semester associated with course
     * -startTime   =   Time the course starts
     * -EndTime     =   Time the course ends
     *
     * instead of including assignments here, simply select the assignments based on the course
     *
     */
    private static final String COURSES_TABLE_NAME           =   "courses";
    private static final String COURSES_TABLE_KEY_ROWID      =   "id";//PKEY
    private static final String COURSES_TABLE_KEY_NAME       =   "courseName";
    private static final String COURSES_TABLE_KEY_GRADE      =   "grade";
    private static final String COURSES_TABLE_KEY_SEMESTER   =   "semester";
    private static final String COURSES_TABLE_KEY_STARTTIME  =   "startTime";
    private static final String COURSES_TABLE_KEY_ENDTIME    =   "endTime";

    //4: ASSIGNMENTS TABLE DEFINITION
    /**
     * ASSIGNMENTS TABLE OUTLINE
     *
     * columns:
     * -id          =   Primary Key
     * -name        =   The name of the assignment
     * -course      =   Course the assignment is associated with
     * -due         =   When the assignment is due
     * -reminder    =   reminders for the assignment (may be useless)
     */
    private static final String ASSIGNMENTS_TABLE_NAME          = "assignments";
    private static final String ASSIGNMENTS_TABLE_KEY_ROWID     = "id";
    private static final String ASSIGNMENTS_TABLE_KEY_NAME      = "name";
    private static final String ASSIGNMENTS_TABLE_KEY_COURSE    = "course";
    private static final String ASSIGNMENTS_TABLE_KEY_DUE       = "due";

    //1: DATABASE HELPERS AND CLASS HELPERS
    //database helpers and adapters
    private DbHelper help;
    private final Context context;
    private SQLiteDatabase dbase;

    //inner class DBHelper, used to create and update the database
    private static class DbHelper extends SQLiteOpenHelper{

        private DbHelper(Context context){
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }//end constructor

        /**
         * @author Josh
         * override method used for database creation
         * this method runs only once when the database is
         * created, after that the update method is called instead
         */
        @Override
        public void onCreate(SQLiteDatabase db) {
            //Creates the Semesters table
            db.execSQL(
                    "CREATE TABLE " + SEM_TABLE_NAME + " ("+
                    SEM_TABLE_KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    SEM_TABLE_KEY_NAME + " TEXT NOT NULL);"
            );

            //Creates the Courses Table
            db.execSQL(
                "CREATE TABLE " + COURSES_TABLE_NAME + " ("+
                COURSES_TABLE_KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                COURSES_TABLE_KEY_NAME + " TEXT NOT NULL, "+
                COURSES_TABLE_KEY_GRADE + " REAL, "+
                COURSES_TABLE_KEY_SEMESTER + " TEXT NOT NULL, "+
                COURSES_TABLE_KEY_STARTTIME + " NUMERIC NOT NULL, "+
                COURSES_TABLE_KEY_ENDTIME + " NUMERIC NOT NULL);"
            );

            //CREATES THE ASSIGNMENTS TABLE
            db.execSQL(
                    "CREATE TABLE " + ASSIGNMENTS_TABLE_NAME + " ("+
                    ASSIGNMENTS_TABLE_KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT, "+
                    ASSIGNMENTS_TABLE_KEY_NAME + " TEXT NOT NULL, "+
                    ASSIGNMENTS_TABLE_KEY_COURSE + " TEXT NOT NULL, "+
                    ASSIGNMENTS_TABLE_KEY_DUE + " NUMERIC);"
            );
        }//end onCreate

        @Override
        public void onUpgrade(SQLiteDatabase db, int i, int i1) {
            //drop all tables if exist, recreate db
            db.execSQL("DROP TABLE IF EXISTS " + SEM_TABLE_NAME);   //SEMESTER
            db.execSQL("DROP TABLE IF EXISTS " + COURSES_TABLE_NAME);//courses
            db.execSQL("DROP TABLE IF EXISTS " + ASSIGNMENTS_TABLE_NAME);//assignments

            //recreate db after all tables dropped
            onCreate(db);
        }//end onUpdate
    }//end inner class

    //Constructor
    public GYST_db(Context c){
        context = c;
    }//end constructor

    //opens database for reading and writing
    public GYST_db open(){
        help = new DbHelper(context);
        dbase = help.getWritableDatabase();
        return this;
    }//end open

    //closes database... derp
    public GYST_db close(){
        help.close();
        return this;
    }//end close


    //2: SEMESTERS TABLE FUNCTIONS

    //CREATE
    //simply uses contentValues to insert a new entry to the database
    //returns the new id if success, -1 if fail
    public long createEntry(String semName){
        ContentValues cv = new ContentValues();
        cv.put(SEM_TABLE_KEY_NAME, semName);

        return  dbase.insert(SEM_TABLE_NAME, null, cv);
    }//end semName

    //READ
    /**
     * ::AUSTIN::
     * -if you need the returned datatype to be something other
     * than an arrayList let me know, i'll update this function to use
     * something more convenient if possible
     *
     * used this thinking that you might use a ListView.
     *
     * :::::::::::::::::::::::::::::::::::::::::::::::::::::
     *
     * @author Josh
     * collects all semesters from the database and returns them via
     * ArrayList
     * @return ArrayList<String> res the results of the select operation
     */
    public ArrayList<String> getSemesters(){
        ArrayList<String> res = new ArrayList<>();
        String[] columns = new String[] {SEM_TABLE_KEY_ROWID, SEM_TABLE_KEY_NAME};
        Cursor c = dbase.query(SEM_TABLE_NAME, columns, null, null, null, null, null);

        int iName = c.getColumnIndex(SEM_TABLE_KEY_NAME);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            res.add(c.getString(iName));
        }//end for

        return res;
    }//end getSemesters

    //UPDATE
    public int updateSemester(String semName){
        int ret;
        ContentValues cv = new ContentValues();
        cv.put(SEM_TABLE_KEY_NAME, semName);

        ret = dbase.update(SEM_TABLE_NAME, cv, SEM_TABLE_KEY_NAME + " = ?", null);
        return ret;
    }//end updateSemester


    //DELETE
    public int deleteSemester(String semName){
        int ret;
        ret = dbase.delete(SEM_TABLE_NAME, SEM_TABLE_KEY_NAME + " = " + semName , null);
        return ret;
    }//end deleteSemester

    // 3: COURSES TABLE FUNCTIONS
    public ArrayList<Course> getCourses(String semester){
        ArrayList<Course> res = new ArrayList<>();
        String[] columns = new String[] {
                COURSES_TABLE_KEY_ROWID, COURSES_TABLE_KEY_NAME, COURSES_TABLE_KEY_GRADE,
                COURSES_TABLE_KEY_STARTTIME, COURSES_TABLE_KEY_ENDTIME
        };//END ARRAY

        //select all courses where semester column matches selected semester
        Cursor c = dbase.query(COURSES_TABLE_NAME, columns, COURSES_TABLE_KEY_SEMESTER + " = " + semester, null, null, null, null);

        int iName = c.getColumnIndex(COURSES_TABLE_KEY_NAME);
        int iGrade = c.getColumnIndex(COURSES_TABLE_KEY_GRADE);
        int iStart = c.getColumnIndex(COURSES_TABLE_KEY_STARTTIME);
        int iEnd = c.getColumnIndex(COURSES_TABLE_KEY_ENDTIME);

        for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
            Course co = new Course(
                c.getString(iName),
                c.getString(iStart),
                c.getString(iEnd),
                c.getInt(iGrade)
            );

            res.add(co);
        }//end for

        return res;
    }//end getCourses

    /**
     Class Name: Course
     Purpose: This class outlines what a course looks like, and the data it holds
     this class is used to help keep multiple courses added to the arrayList
     in line and organized.
     Author: Josh Parsons
     Date: Sept. 23rd 2016
     */
    private class Course{
        private String name; //name of course
        private int grade;  //grade received
        private String semester;//semester course is in
        private String start;//startTime
        private String end;//endTime

        public Course(String n, String st, String e, int g){
            this.name = n;
            this.grade = g;
            this.start = st;
            this.end = e;
        }//end constructor

        public String Name(){
            return this.name;
        }//end getter

        public int Grade(){
            return this.grade;
        }//end getter

        public String Semester(){
            return this.semester;
        }//end getter

        public String StartTime(){
            return this.start;
        }//end getter

        public String EndTime(){
            return this.end;
        }//end getter
    }//end inner course class
}//end class