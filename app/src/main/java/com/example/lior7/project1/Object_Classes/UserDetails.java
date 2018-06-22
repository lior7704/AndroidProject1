package com.example.lior7.project1.Object_Classes;

public class UserDetails {

    private  int id;
    private String name;
    private int age;
    private int score;
    private String difficulty;
    private double latitude, longitude;

    public  UserDetails(int id, String name, int age, int score, String difficulty, double latitude, double longitude){
        this.id = id;
        this.name = name;
        this.age = age;
        this.latitude = latitude;
        this.longitude =  longitude;
        this.score = score;
        this.difficulty = difficulty;
    }

    public UserDetails(){

    }

    public int getId() { return id; }

    public void setId(int id) { this.id = id; }

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }

    public int getAge() { return age; }

    public void setAge(int age) { this.age = age; }

    public int getScore() { return score; }

    public void setScore(int score) { this.score = score; }

    public String getDifficulty() { return difficulty; }

    public void setDifficulty(String difficulty) { this.difficulty = difficulty; }

    public double getLatitude() { return latitude; }

    public void setLatitude(double latitude) { this.latitude = latitude; }

    public double getLongitude() { return longitude; }

    public void setLongitude(double longitude) { this.longitude = longitude; }

}
