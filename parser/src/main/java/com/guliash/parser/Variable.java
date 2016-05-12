package com.guliash.parser;

import android.os.Parcel;
import android.os.Parcelable;

public class Variable implements Parcelable {
    public String name;
    public Double value;
    public Variable(String name, Double value) {
        this.name = name;
        this.value = value;
    }

    public Variable() {}

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        Variable obj = (Variable)o;
        return obj.equals(name);
    }

    @Override
    public String toString() {
        return name + " " + value.toString();
    }

    public Variable(Parcel parcel) {
        name = parcel.readString();
        value = parcel.readDouble();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeDouble(value);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Variable> CREATOR = new Parcelable.Creator<Variable>() {
        public Variable createFromParcel(Parcel in) {
            return new Variable(in);
        }

        public Variable[] newArray(int size) {
            return new Variable[size];
        }
    };
}
