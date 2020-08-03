package com.example.cattlecare;

import android.content.Context;
import android.content.SharedPreferences;

public class launcherManager {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    private static String PREF_NAME = "launcherManager";
    private static String IS_FIRST_TIME = "isFirst";

    public launcherManager(Context context){
        sharedPreferences=context.getSharedPreferences(PREF_NAME,0);
        editor=sharedPreferences.edit();
    }

    public void setFirstLaunch( boolean isFirst){
        editor.putBoolean(IS_FIRST_TIME,isFirst);
        editor.commit();
    }

    public boolean isFirstTime(){
        return sharedPreferences.getBoolean(IS_FIRST_TIME,true);
    }

}
