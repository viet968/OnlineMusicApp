package com.example.onlinemusicapp.Model;

import java.util.Objects;

public class Album implements Comparable<Album>,Cloneable{
    private String name;
    private String imageLink;
    public Album(String name, String imageLink) {
        this.name = name;
        this.imageLink = imageLink;

    }

    public Album(){}


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUri() {
        return imageLink;
    }

    public void setImageUri(String image) {
        this.imageLink = image;
    }


    @Override
    public String toString() {
        return name+"\n"+ imageLink;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Album album = (Album) o;
        return Objects.equals(name, album.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    @Override
    public int compareTo(Album album) {
        if(this.getName().equals(album.getName())) return 0;
        return 1;
    }


    @Override
    public Album clone() {
        Album clone = null;
        try{
            clone = (Album) super.clone();

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return clone;
    }
}
