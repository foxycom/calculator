package com.guliash.calculator.structures;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public final class CalculatorDataSet implements Parcelable, Cloneable {

    private String expression;
    private String name;
    private long timestamp;
    private ArrayList<StringVariableWrapper> variables;

    public CalculatorDataSet(String expression, String name, long timestamp,
                             ArrayList<StringVariableWrapper> variables) {

        this.expression = expression;
        this.name = name;
        this.timestamp = timestamp;
        this.variables = variables;
    }

    public CalculatorDataSet(Parcel parcel) {
        expression = parcel.readString();
        name = parcel.readString();
        timestamp = parcel.readLong();
        variables = parcel.readArrayList(StringVariableWrapper.class.getClassLoader());
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public ArrayList<StringVariableWrapper> getVariables() {
        return variables;
    }

    public void setVariables(ArrayList<StringVariableWrapper> variables) {
        this.variables = variables;
    }

    @Override
    public CalculatorDataSet clone() {
        try {
            CalculatorDataSet dataSet = (CalculatorDataSet)super.clone();
            dataSet.variables = (ArrayList< StringVariableWrapper>)getVariables().clone();
            for(int i = 0, size = dataSet.variables.size(); i < size; i++) {
                dataSet.variables.set(i, dataSet.variables.get(i).clone());
            }
            return dataSet;
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof CalculatorDataSet)) {
            return false;
        }
        CalculatorDataSet obj = (CalculatorDataSet)o;
        return obj.name.equals(name);
    }

    @Override
    public String toString() {
        return String.format("{%s %s}", name, expression);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(expression);
        dest.writeString(name);
        dest.writeLong(timestamp);
        dest.writeList(variables);
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
