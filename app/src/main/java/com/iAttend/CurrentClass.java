package com.iAttend;

import android.content.Context;
import android.content.SharedPreferences;

import java.sql.Time;
import java.util.HashMap;

public class CurrentClass {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "iAttendPref_class";


    // User name (make variable public to access from outside)
    public static final String CLASS_CODE = "classCode";

    // Email address (make variable public to access from outside)
    public static final String END_TIME = "endTime";

    public static final String HAVING_CLASS = "having_CLASS";


    // Constructor
    public CurrentClass(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createCurrentClass(String classCode, String endTime){
        // Storing login value as TRUE


        // Storing name in pref
        editor.putString(CLASS_CODE, classCode);

        // Storing email in pref
        editor.putString(END_TIME, endTime);



        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getClassDetails(){
        HashMap<String, String> user = new HashMap<String, String>();


        user.put(CLASS_CODE, pref.getString(CLASS_CODE, null));

        // user email id
        user.put(END_TIME, pref.getString(END_TIME, null));


        // return user
        return user;
    }

    public void START_CLASS(){
        editor.putBoolean(HAVING_CLASS, true);
    }
    public void END_CLASS(){
        editor.putBoolean(HAVING_CLASS,false);
    }
    public boolean HAVE_CLASS(){
       return pref.getBoolean(HAVING_CLASS,false);
    }

}
