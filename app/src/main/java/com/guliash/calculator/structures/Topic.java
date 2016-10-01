package com.guliash.calculator.structures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Topic implements Parcelable {

    public String name, description;
    public ArrayList<String> examples;

    public Topic(String name, String description, ArrayList<String> examples) {
        this.name = name;
        this.description = description;
        this.examples = examples;
    }

    protected Topic(Parcel in) {
        name = in.readString();
        description = in.readString();
        examples = in.createStringArrayList();
    }

    public static final Creator<Topic> CREATOR = new Creator<Topic>() {
        @Override
        public Topic createFromParcel(Parcel in) {
            return new Topic(in);
        }

        @Override
        public Topic[] newArray(int size) {
            return new Topic[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(description);
        dest.writeStringList(examples);
    }
}