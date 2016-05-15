package com.guliash.calculator;

import android.os.Parcel;
import android.os.Parcelable;

import com.guliash.parser.Variable;

public class VariableWrapper extends Variable implements Parcelable {

    public static final Parcelable.Creator<VariableWrapper> CREATOR = new Parcelable.Creator<VariableWrapper>() {
        public VariableWrapper createFromParcel(Parcel in) {
            return new VariableWrapper(in);
        }

        public VariableWrapper[] newArray(int size) {
            return new VariableWrapper[size];
        }
    };

    public VariableWrapper(String name, String value) {
        super(name, value);
    }

    public VariableWrapper(Parcel parcel) {
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
