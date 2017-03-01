package com.uminho.pti.smartcar.Data;




public class Route {

    private int id;
    private double distance;
    private double vel_med;
    private double vel_max;
    private String user_email;
    private long start_stamp;
    private long end_stamp;

    public Route(String user_email) {
        this.id=0;
        this.distance = 0;
        this.vel_med = 0;
        this.vel_max = 0;
        this.user_email = user_email;
        this.start_stamp = 0;
        this.end_stamp = 0;
    }

    public Route(int id,double dist,double vel_med,double vel_max,String user_email,long start_stamp,long end_stamp) {
        this.id=id;
        this.distance = dist;
        this.vel_med = vel_med;
        this.vel_max = vel_max;
        this.user_email = user_email;
        this.start_stamp = start_stamp;
        this.end_stamp = end_stamp;
    }

    public int getRouteId(){
        return id;
    }

    public double getDistance() {
        return distance;
    }

    public double getVel_med() {
        return vel_med;
    }

    public double getVel_max() {
        return vel_max;
    }

    public String getUser_email() {
        return user_email;
    }

    public long getStart_stamp() {
        return start_stamp;
    }

    public long getEnd_stamp() {
        return end_stamp;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public void setVel_med(double vel_med) {
        this.vel_med = vel_med;
    }

    public void setVel_max(double vel_max) {
        this.vel_max = vel_max;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public void setStart_stamp(long start_stamp) {
        this.start_stamp = start_stamp;
    }

    public void setEnd_stamp(long end_stamp) {
        this.end_stamp = end_stamp;
    }

    public void setId(int id) {
        this.id = id;
    }
}
