package com.guliash.calculator.structures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CalculatorDataSet implements Parcelable {
    public String expression;
    public String datasetName;
    public long timestamp;
    public ArrayList<StringVariableWrapper> variables;

    public CalculatorDataSet(String expression, String datasetName, ArrayList<StringVariableWrapper> variables,
                             long timestamp) {
        this.expression = expression;
        this.datasetName = datasetName;
        this.variables = variables;
        this.timestamp = timestamp;
    }

    public CalculatorDataSet(Parcel parcel) {
        expression = parcel.readString();
        datasetName = parcel.readString();
        timestamp = parcel.readLong();
        variables = parcel.readArrayList(StringVariableWrapper.class.getClassLoader());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expression);
        dest.writeString(datasetName);
        dest.writeLong(timestamp);
        dest.writeList(variables);
    }

    @Override
    public boolean equals(Object o) {
        if(o == null || !(o instanceof CalculatorDataSet)) {
            return false;
        }
        CalculatorDataSet obj = (CalculatorDataSet)o;
        return obj.datasetName.equals(datasetName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CalculatorDataSet> CREATOR = new Parcelable.Creator<CalculatorDataSet>() {
        public CalculatorDataSet createFromParcel(Parcel in) {
            return new CalculatorDataSet(in);
        }

        public CalculatorDataSet[] newArray(int size) {
            return new CalculatorDataSet[size];
        }
    };
}
