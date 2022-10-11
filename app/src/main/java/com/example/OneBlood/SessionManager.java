package com.example.OneBlood;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.HashMap;

public class SessionManager {
    SharedPreferences userSession;
    SharedPreferences.Editor mEditor;
    Context mContext;

    public static final String IS_LOGIN = "isLoggedIn";
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_USER_NAME = "userName";
    public static final String KEY_USER_EMAIL = "userEmail";
    public static final String KEY_USER_PASSWORD = "userPassword";

    public SessionManager(Context context){
        mContext = context;
        userSession = mContext.getSharedPreferences("userLoginSession",Context.MODE_PRIVATE);
        mEditor = userSession.edit();
    }

    public void createLogInSession(String userName, String userEmail, String userId, String userPassword){
        mEditor.putBoolean(IS_LOGIN, true);
        mEditor.putString(KEY_USER_NAME, userName);
        mEditor.putString(KEY_USER_EMAIL, userEmail);
        mEditor.putString(KEY_USER_ID, userId);
        mEditor.putString(KEY_USER_PASSWORD, userPassword);

        mEditor.commit();
    }

    public HashMap<String, String> getUserDetail(){
        HashMap<String, String> userData = new HashMap<String, String>();
        userData.put(KEY_USER_NAME, userSession.getString(KEY_USER_NAME, null));
        userData.put(KEY_USER_EMAIL, userSession.getString(KEY_USER_EMAIL, null));
        userData.put(KEY_USER_PASSWORD, userSession.getString(KEY_USER_PASSWORD, null));
        userData.put(KEY_USER_ID, userSession.getString(KEY_USER_ID, null));

        return userData;
    }

    //Check if the user is logged in
    public boolean checkLogin(){
        if(userSession.getBoolean(IS_LOGIN, false)){
            return true;
        }else{
            return false;
        }
    }

    //Log user Out
    public void logOut(){
        mEditor.clear();
        mEditor.commit();
    }
}

