package com.guliash.calculator.structures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class CalculatorDataset implements Parcelable {
    public String expression;
    public String datasetName;
    public long timestamp;
    public ArrayList<StringVariableWrapper> variables;

    public CalculatorDataset(String expression, String datasetName, ArrayList<StringVariableWrapper> variables,
                             long timestamp) {
        this.expression = expression;
        this.datasetName = datasetName;
        this.variables = variables;
        this.timestamp = timestamp;
    }

    public CalculatorDataset(Parcel parcel) {
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
        if(o == null || !(o instanceof CalculatorDataset)) {
            return false;
        }
        CalculatorDataset obj = (CalculatorDataset)o;
        return obj.datasetName.equals(datasetName);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<CalculatorDataset> CREATOR = new Parcelable.Creator<CalculatorDataset>() {
        public CalculatorDataset createFromParcel(Parcel in) {
            return new CalculatorDataset(in);
        }

        public CalculatorDataset[] newArray(int size) {
            return new CalculatorDataset[size];
        }
    };
}
