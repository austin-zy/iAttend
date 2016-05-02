package com.iAttend;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;


public class LocationPreference {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "iAttendPref_location";


    // User name (make variable public to access from outside)
    public static final String KEY_UUID = "uuid";

    // Email address (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    public static final String IS_THERE = "is_there";


    // Constructor
    public LocationPreference(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createLocationPreference(String uuid, String name){
        // Storing login value as TRUE


        // Storing name in pref
        editor.putString(KEY_NAME, name);

        // Storing email in pref
        editor.putString(KEY_UUID, uuid);



        // commit changes
        editor.commit();
    }

    public HashMap<String, String> getlocationDetails(){
        HashMap<String, String> user = new HashMap<String, String>();


        user.put(KEY_NAME, pref.getString(KEY_NAME, null));

        // user email id
        user.put(KEY_UUID, pref.getString(KEY_UUID, null));


        // return user
        return user;
    }

    public void IS_THERE(){
        editor.putBoolean(IS_THERE, true);
    }
}
