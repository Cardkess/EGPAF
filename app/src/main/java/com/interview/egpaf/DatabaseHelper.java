package com.interview.egpaf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "egpaf.db";

    private static final String TABLE_USERS = "users";
    private static final String USERS_COL_1 = "id";
    private static final String USERS_COL_2 = "first_name";
    private static final String USERS_COL_3 = "surname";
    private static final String USERS_COL_4 = "email";
    private static final String USERS_COL_5 = "password";
    private static final String USERS_COL_6 = "created_at";
    private static final String USERS_COL_7 = "updated_at";

    private static final String TABLE_PATIENTS = "patients";
    private static final String PATIENTS_COL_1 = "id";
    private static final String PATIENTS_COL_2 = "first_name";
    private static final String PATIENTS_COL_3 = "surname";
    private static final String PATIENTS_COL_4 = "gender";
    private static final String PATIENTS_COL_5 = "dob";
    private static final String PATIENTS_COL_6 = "estimated_age";
    private static final String PATIENTS_COL_7 = "address";
    private static final String PATIENTS_COL_8 = "occupation";
    private static final String PATIENTS_COL_9 = "created_at";
    private static final String PATIENTS_COL_10 = "updated_at";

    private static final String TABLE_RECORDS = "records";
    private static final String RECORDS_COL_1 = "id";
    private static final String RECORDS_COL_2 = "weight";
    private static final String RECORDS_COL_3 = "height";
    private static final String RECORDS_COL_4 = "temp_reading";
    private static final String RECORDS_COL_5 = "diagnosis";
    private static final String RECORDS_COL_6 = "patient_id";
    private static final String RECORDS_COL_7 = "date";
    private static final String RECORDS_COL_8 = "created_at";
    private static final String RECORDS_COL_9 = "updated_at";


    public DatabaseHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);

        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE " + TABLE_USERS + " ("+ USERS_COL_1 +"	INTEGER PRIMARY KEY AUTOINCREMENT, "+ USERS_COL_2 +" TEXT, "+ USERS_COL_3 +" TEXT, "+ USERS_COL_4 +" TEXT UNIQUE, "+ USERS_COL_5 +" TEXT, "+ USERS_COL_6 +" TEXT, "+ USERS_COL_7 +" TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_PATIENTS + " ("+ PATIENTS_COL_1 +"	INTEGER PRIMARY KEY AUTOINCREMENT, "+ PATIENTS_COL_2 +" TEXT, "+ PATIENTS_COL_3 +" TEXT, "+ PATIENTS_COL_4 +" TEXT, "+ PATIENTS_COL_5 +" TEXT, "+ PATIENTS_COL_6 +" INTEGER, "+ PATIENTS_COL_7 +" TEXT, "+ PATIENTS_COL_8 +" TEXT, "+ PATIENTS_COL_9 +" TEXT, "+ PATIENTS_COL_10 +" TEXT)");

        db.execSQL("CREATE TABLE " + TABLE_RECORDS + " ("+ RECORDS_COL_1 +"	INTEGER PRIMARY KEY AUTOINCREMENT, "+ RECORDS_COL_2 +" TEXT, "+ RECORDS_COL_3 +" TEXT, "+ RECORDS_COL_4 +" TEXT, "+ RECORDS_COL_5 +" INTEGER, "+ RECORDS_COL_6 +" TEXT, "+ RECORDS_COL_7 +" TEXT, "+ RECORDS_COL_8 +" TEXT, "+ RECORDS_COL_9 +" TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE if EXISTS " + TABLE_USERS);
        db.execSQL("DROP TABLE if EXISTS " + TABLE_PATIENTS);
        db.execSQL("DROP TABLE if EXISTS " + TABLE_RECORDS);
        onCreate(db);
    }

    public List<Patient> searchPatients(String string){

        List<Patient> patientList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor res;

        //Search by Address
        res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE address LIKE '"+ string +"%' ORDER BY first_name ASC",null);


        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

            patientList.add(patient);
        }

        //Search by Occupation
        res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE occupation LIKE '"+ string +"%' ORDER BY first_name ASC",null);


        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

            patientList.add(patient);
        }

        //Search by id
        if(isInt(string)) {

            res = db.rawQuery("SELECT * FROM " + TABLE_PATIENTS + " WHERE id = " + Integer.valueOf(string) + " ORDER BY first_name ASC", null);


            for (int i = 0; i < res.getCount(); i++) {

                res.moveToNext();

                Patient patient = new Patient(res.getInt(res.getColumnIndex("id")), res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")), res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")), res.getString(res.getColumnIndex("dob")), res.getString(res.getColumnIndex("address")), res.getString(res.getColumnIndex("occupation")), res.getString(res.getColumnIndex("created_at")), res.getString(res.getColumnIndex("updated_at")));

                patientList.add(patient);
            }
        }
        //Search by estimated_age
        if (string.equals("yes") || string.equals("Yes") || string.equals("YES")) {
            res = db.rawQuery("SELECT * FROM " + TABLE_PATIENTS + " WHERE estimated_age = 1 ORDER BY first_name ASC", null);

            for (int i = 0; i < res.getCount() ; i++) {

                res.moveToNext();

                Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

                patientList.add(patient);
            }
        }

        if (string.equals("no") || string.equals("No") || string.equals("NO")) {
            res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE estimated_age = 0 ORDER BY first_name ASC",null);

            for (int i = 0; i < res.getCount() ; i++) {

                res.moveToNext();

                Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

                patientList.add(patient);
            }
        }


        //Search by gender
        res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE gender LIKE '"+ string +"%' ORDER BY first_name ASC",null);


        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

            patientList.add(patient);
        }

        //Search by date
        res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE dob LIKE '"+ string +"%' ORDER BY first_name ASC",null);


        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

            patientList.add(patient);
        }

        //Search by Firstname
        res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE first_name LIKE '"+ string +"%' ORDER BY first_name ASC",null);


        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

            patientList.add(patient);
        }

        res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE surname LIKE '"+ string +"%' ORDER BY surname ASC",null);


        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

            patientList.add(patient);
        }

        ArrayList<Integer> ids = new ArrayList<Integer>();
        // Create a new ArrayList
        ArrayList<Patient> newList = new ArrayList<Patient>();

        // Traverse through the first list
        for (Patient element : patientList) {

            ids.add(element.getId());
        }

        List<Integer> cleanIDs = new ArrayList<>(new HashSet<>(ids));

        for (int id: cleanIDs) {

            res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE id = " + id,null);

            res.moveToNext();

            Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));
            newList.add(patient);
        }

        return newList;

    }

    public Cursor searchUser(String email){

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM "+ TABLE_USERS +" WHERE email = '"+ email +"'",null);
        return res;

    }

    public Patient getPatient(int id){

        Patient patient = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" WHERE id = " + id,null);

        if (res.getCount() == 1){
            res.moveToNext();
            patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));
        }
        return patient;
    }

    public List<Patient> getAllPatients(){

        List<Patient> patientList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM "+ TABLE_PATIENTS +" ORDER BY id DESC",null);

        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            Patient patient = new Patient(res.getInt(res.getColumnIndex("id")),res.getInt(res.getColumnIndex("estimated_age")), res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")), res.getString(res.getColumnIndex("gender")),res.getString(res.getColumnIndex("dob")),res.getString(res.getColumnIndex("address")),res.getString(res.getColumnIndex("occupation")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));
          
            patientList.add(patient);
        }

        return patientList;

    }

    public List<User> getAllUsers(){

        List<User> usersList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM "+ TABLE_USERS,null);

        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            User user = new User(res.getString(res.getColumnIndex("first_name")),res.getString(res.getColumnIndex("surname")),res.getString(res.getColumnIndex("email")),"",res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

            usersList.add(user);
        }

        return usersList;

    }

    public List<Record> getRecords(int id){

        List<Record> recordsList = new ArrayList<>();

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM "+ TABLE_RECORDS +" WHERE patient_id ="+ id +" ORDER BY id DESC",null);

        for (int i = 0; i < res.getCount() ; i++) {

            res.moveToNext();

            //Record record = new Record(res.getInt(res.getColumnIndex("id")),res.getString(res.getColumnIndex("weight")), res.getString(res.getColumnIndex("height")),res.getString(res.getColumnIndex("temp_reading")), res.getString(res.getColumnIndex("diagnosis")), res.getInt(res.getColumnIndex("patient_id")),res.getString(res.getColumnIndex("date")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));
            Record record = new Record(res.getInt(res.getColumnIndex("id")),res.getString(res.getColumnIndex("weight")),res.getString(res.getColumnIndex("height")),res.getString(res.getColumnIndex("temp_reading")),res.getString(res.getColumnIndex("diagnosis")),res.getInt(res.getColumnIndex("patient_id")),res.getString(res.getColumnIndex("date")),res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));

            recordsList.add(record);
        }

        return recordsList;

    }

    public User loginUser(String email, String password){
        User user = null;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery( "SELECT * FROM "+ TABLE_USERS +" WHERE email = '"+ email +"'" +" AND password = '"+ password +"'",null);

        if (res.getCount() == 1){
            res.moveToNext();
            user = new User(res.getString(1),res.getString(2),res.getString(3),"",res.getString(res.getColumnIndex("created_at")),res.getString(res.getColumnIndex("updated_at")));
        }
        return user;
    }

    public boolean registerUser(User user){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(USERS_COL_2, user.getFirstname());
        contentValues.put(USERS_COL_3, user.getSurname());
        contentValues.put(USERS_COL_4, user.getEmail());
        contentValues.put(USERS_COL_5, user.getPassword());

        long insert = db.insert(TABLE_USERS, null, contentValues);

        if (insert == -1){

            return false;

        }else {

            return true;

        }
    }

    public boolean registerPatient(Patient patient){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(PATIENTS_COL_2, patient.getFirstname());
        contentValues.put(PATIENTS_COL_3, patient.getSurname());
        contentValues.put(PATIENTS_COL_4, patient.getGender());
        contentValues.put(PATIENTS_COL_5, patient.getDob());
        contentValues.put(PATIENTS_COL_6, patient.getEstimated_dob());
        contentValues.put(PATIENTS_COL_7, patient.getAddress());
        contentValues.put(PATIENTS_COL_8, patient.getOccupation());
        contentValues.put(PATIENTS_COL_9, patient.getCreated_at());
        contentValues.put(PATIENTS_COL_10, patient.getUpdated_at());

        long insert = db.insert(TABLE_PATIENTS, null, contentValues);

        if (insert == -1){

            return false;

        }else {

            return true;

        }
    }

    public boolean addRecord(Record record){

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(RECORDS_COL_2, record.getWeight());
        contentValues.put(RECORDS_COL_3, record.getHeight());
        contentValues.put(RECORDS_COL_4, record.getTemp_reading());
        contentValues.put(RECORDS_COL_5, record.getDiagnosis());
        contentValues.put(RECORDS_COL_6, record.getPatient_id());
        contentValues.put(RECORDS_COL_7, record.getDate());
        contentValues.put(RECORDS_COL_8, record.getCreated_at());
        contentValues.put(RECORDS_COL_9, record.getUpdated_at());

        long insert = db.insert(TABLE_RECORDS, null, contentValues);

        if (insert == -1){

            return false;

        }else {

            return true;

        }
    }

    //check if a string is a number

    private static boolean isInt(String str) {

        try {
            @SuppressWarnings("unused")
            int x = Integer.parseInt(str);
            return true; //String is an Integer
        } catch (NumberFormatException e) {
            return false; //String is not an Integer
        }

    }
}
