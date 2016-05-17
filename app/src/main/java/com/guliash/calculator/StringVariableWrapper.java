package com.guliash.calculator;

import android.os.Parcel;
import android.os.Parcelable;

import com.guliash.parser.StringVariable;

public class StringVariableWrapper extends StringVariable implements Parcelable {

    public static final Parcelable.Creator<StringVariableWrapper> CREATOR = new Parcelable.Creator<StringVariableWrapper>() {
        public StringVariableWrapper createFromParcel(Parcel in) {
            return new StringVariableWrapper(in);
        }

        public StringVariableWrapper[] newArray(int size) {
            return new StringVariableWrapper[size];
        }
    };

    public StringVariableWrapper(String name, String value) {
        super(name, value);
    }

    public StringVariableWrapper(Parcel parcel) {
        super();
        name = parcel.readString();
        value = parcel.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(value);
    }
}
