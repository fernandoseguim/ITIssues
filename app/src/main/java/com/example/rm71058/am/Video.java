package com.example.rm71058.am;

/**
 * Created by rm71058 on 27/10/2016.
 */
public class Video {
    int code;
    int time;
    String description;

    public Video(int code, int time, String description) {
        this.code = code;
        this.time = time;
        this.description = description;
    }

    @Override
    public String toString(){
        return "Código: " + code + " Duração:" + time+ " Descrição: " + description;
    }
}
