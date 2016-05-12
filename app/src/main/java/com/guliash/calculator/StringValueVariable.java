package com.guliash.calculator;

import android.os.Parcel;
import android.os.Parcelable;

public class StringValueVariable implements Parcelable {
    public String strVal, name;
    public StringValueVariable(String name, String strVal) {
        this.name = name;
        this.strVal = strVal;
    }
    public StringValueVariable(Parcel parcel) {
        super();
        name = parcel.readString();
        strVal = parcel.readString();
    }

    public static final Parcelable.Creator<StringValueVariable> CREATOR = new Parcelable.Creator<StringValueVariable>() {
        public StringValueVariable createFromParcel(Parcel in) {
            return new StringValueVariable(in);
        }

        public StringValueVariable[] newArray(int size) {
            return new StringValueVariable[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(strVal);
    }
}
