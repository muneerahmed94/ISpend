package com.rhcloud.httpispend_jntuhceh.ispend;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Muneer on 09-03-2016.
 */
public class UserLocalStore
{
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    public UserLocalStore(Context context)
    {
        userLocalDatabase = context.getSharedPreferences(SP_NAME, 0);
    }

    public void storeUserData(User user)
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putString("email", user.email);
        userLocalDatabaseEditor.putString("mobile", user.mobile);
        userLocalDatabaseEditor.putString("name", user.name);
        userLocalDatabaseEditor.putString("password", user.password);
        userLocalDatabaseEditor.commit();
    }

    public void setUserLoggedIn(boolean loggedIn)
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.putBoolean("loggedIn", loggedIn);
        userLocalDatabaseEditor.commit();
    }

    public void clearUserData()
    {
        SharedPreferences.Editor userLocalDatabaseEditor = userLocalDatabase.edit();
        userLocalDatabaseEditor.clear();
        userLocalDatabaseEditor.commit();
    }

    public User getLoggedInUser()
    {
        if (userLocalDatabase.getBoolean("loggedIn", false) == false)
        {
            return null;
        }

        String email = userLocalDatabase.getString("email", "");
        String mobile = userLocalDatabase.getString("mobile", "");
        String name = userLocalDatabase.getString("name", "");
        String password = userLocalDatabase.getString("password", "");

        User user = new User(email, mobile, name, password);
        return user;
    }
}

