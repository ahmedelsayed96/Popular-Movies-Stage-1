package com.example.mido.moviesapp.Movies.Cast;

/**
 * Created by Ahmed
 */

public class CastData {
    private String name, character, profile_path;

    public CastData() {
    }

    public CastData(String name, String character, String image) {
        this.name = name;
        this.character = character;
        this.profile_path = image;
    }

    public String getName() {
        return name;
    }

    public String getProfile_path() {
        return profile_path;
    }

    public String getCharacter() {
        return character;
    }
}
