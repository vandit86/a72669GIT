package com.uminho.pti.smartcar.Data;


/**
 * this class will represent the user.
 * an instance of this calls will be created on login and deleted on logout
 */

public class User {

    private String email;
    private String name;
    private String date;
    private String country;
    private String city;
    private String contact;
    private String user_name;
    private int log_state;
    private int access_level;
    private String password;
    private String address;
    private int user_type;

    public User(String email, String name, String date, String country, String city, String contact, String user_name,
                int log_state, int access_level, String password, String address, int user_type) {
        this.email = email;
        this.name = name;
        this.date = date;
        this.country = country;
        this.city = city;
        this.contact = contact;
        this.user_name = user_name;
        this.log_state = log_state;
        this.access_level = access_level;
        this.password = password;
        this.address = address;
        this.user_type = user_type;
    }


    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getCountry() {
        return country;
    }

    public String getCity() {
        return city;
    }

    public String getContact() {
        return contact;
    }

    public String getUser_name() {
        return user_name;
    }

    public int getLog_state() {
        return log_state;
    }

    public int getAccess_level() {
        return access_level;
    }

    public String getPassword() {
        return password;
    }

    public String getAddress() {
        return address;
    }

    public int getUser_type() {
        return user_type;
    }
}
