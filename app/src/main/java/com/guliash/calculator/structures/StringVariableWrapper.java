package com.guliash.calculator.structures;

import android.os.Parcel;
import android.os.Parcelable;

import com.guliash.parser.StringVariable;

public final class StringVariableWrapper extends StringVariable implements Parcelable, Cloneable {

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

    public static StringVariableWrapper defaultVariable() {
        return new StringVariableWrapper("", "0");
    }

    public StringVariableWrapper(Parcel parcel) {
        super();
        setName(parcel.readString());
        setValue(parcel.readString());
    }

    @Override
    public StringVariableWrapper clone() {
        try {
            return (StringVariableWrapper)super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(getName());
        dest.writeString(getValue());
    }
}
