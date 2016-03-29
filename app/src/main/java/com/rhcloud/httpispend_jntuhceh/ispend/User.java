package com.rhcloud.httpispend_jntuhceh.ispend;

/**
 * Created by Muneer on 09-03-2016.
 */
public class User {
    String email, mobile, name, password;

    public User(String email, String mobile, String name, String password)
    {
        this.email = email;
        this.mobile = mobile;
        this.name = name;
        this.password = password;

    }

    public User(String email,String password)
    {
        this.email= email;
        this.mobile = "";
        this.name = "";
        this.mobile = "";
        this.password = password;
    }
}
