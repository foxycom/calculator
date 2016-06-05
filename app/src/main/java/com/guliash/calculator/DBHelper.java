package com.guliash.calculator;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.guliash.calculator.structures.CalculatorDataset;
import com.guliash.calculator.structures.StringVariableWrapper;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;

    private static final String VARIABLES_TABLE = "variables";
    private static final String EXPRESSIONS_TABLE = "expressions";
    private static final String MAIN_TABLE = "main";

    private static final String CREATE_MAIN_TABLE = "create table %s (id integer primary key " +
            "autoincrement,name text unique,date integer);";
    private static final String CREATE_VARIABLES_TABLE = "create table %s (id integer,name text," +
            "value text, FOREIGN KEY (id) REFERENCES main(id) ON DELETE CASCADE);";
    private static final String CREATE_EXPRESSIONS_TABLE = "create table %s (id integer,exp text," +
            "FOREIGN KEY (id) REFERENCES main(id) ON DELETE CASCADE);";


    public DBHelper(Context context) {
        super(context, "calculator", null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(String.format(CREATE_MAIN_TABLE, MAIN_TABLE));
        db.execSQL(String.format(CREATE_VARIABLES_TABLE, VARIABLES_TABLE));
        db.execSQL(String.format(CREATE_EXPRESSIONS_TABLE, EXPRESSIONS_TABLE));
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        super.onOpen(db);
        db.execSQL("PRAGMA foreign_keys=ON;");
    }

    /**
     * Checks whether there is a row with the given name in main_table
     * @param name
     * @return
     */
    public long getIdOfRowWithName(String name) {
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(MAIN_TABLE, new String[]{"id", "name"}, "name = ?", new String[]{name}, null, null, null);
        long res = -1;
        if(cursor.moveToFirst()) {
            int idColumnIndex = cursor.getColumnIndex("id");
            res = cursor.getInt(idColumnIndex);
        }
        cursor.close();
        db.close();
        return res;
    }

    private long addRowToMainTable(String name, long date) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("name", name);
        cv.put("date", date);
        long id = db.insert(MAIN_TABLE, null, cv);
        db.close();
        return id;
    }

    private void addDataToVariablesAndExpTables(long id, String expression, List<? extends StringVariableWrapper> variables) {
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("id", id);
        cv.put("exp", expression);
        db.insert(EXPRESSIONS_TABLE, null, cv);
        for(StringVariableWrapper variable : variables) {
            cv = new ContentValues();
            cv.put("id", id);
            cv.put("name", variable.name);
            cv.put("value", variable.value);
            db.insert(VARIABLES_TABLE, null, cv);
        }
        db.close();
    }

    public void addDataset(CalculatorDataset dataset) {
        long id = addRowToMainTable(dataset.datasetName, dataset.timestamp);
        addDataToVariablesAndExpTables(id, dataset.expression, dataset.variables);
    }

    public void updateData(CalculatorDataset dataset) {
        long id = getIdOfRowWithName(dataset.datasetName);
        deleteDataFromVariablesAndExpTables(id);
        addDataToVariablesAndExpTables(id, dataset.expression, dataset.variables);
    }

    public void deleteData(String name) {
        long id = getIdOfRowWithName(name);
        deleteRowFromMainTable(id);
    }

    private void deleteDataFromVariablesAndExpTables(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(VARIABLES_TABLE, "id = ?", new String[] { Long.toString(id) });
        db.delete(EXPRESSIONS_TABLE, "id = ?", new String[] { Long.toString(id) });
        db.close();
    }

    private void deleteRowFromMainTable(long id) {
        SQLiteDatabase db = getWritableDatabase();
        db.delete(MAIN_TABLE, "id = ?", new String[] { Long.toString(id) });
        db.close();
    }

    public ArrayList<CalculatorDataset> getDatasets() {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(MAIN_TABLE, null, null, null, null, null, null);
        ArrayList<CalculatorDataset> res = new ArrayList<>();
        if(cursor.moveToFirst()) {
            int idColIndex = cursor.getColumnIndex("id");
            int nameColIndex = cursor.getColumnIndex("name");
            do {
                res.add(getDataset(cursor.getInt(idColIndex),
                        cursor.getString(nameColIndex)));
            } while(cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return res;
    }

    private CalculatorDataset getDataset(long id, String name) {
        return new CalculatorDataset(getExpression(id), name, getVariables(id), getDatasetTime(id));
    }

    private String getExpression(long id) {
        String res = "";
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(EXPRESSIONS_TABLE, new String[]{"exp"}, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);

        if(cursor.moveToFirst()) {
            int expColIndex = cursor.getColumnIndex("exp");
            res = cursor.getString(expColIndex);
        }
        cursor.close();
        return res;
    }

    private long getDatasetTime(long id) {
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(MAIN_TABLE,  new String[]{"date"}, "id = ?",
                new String[]{String.valueOf(id)}, null, null, null);
        long res = 0;
        if(cursor.moveToFirst()) {
            int timeColIndex = cursor.getColumnIndex("date");
            res = cursor.getLong(timeColIndex);
        }
        cursor.close();
        return res;
    }

    private ArrayList<StringVariableWrapper> getVariables(long id) {
        ArrayList<StringVariableWrapper> variables = new ArrayList<>();
        SQLiteDatabase db = getWritableDatabase();
        Cursor cursor = db.query(VARIABLES_TABLE, new String[]{"name", "value"}, "id = ?",
                new String[]{String.valueOf(id)},null, null, null);
        if(cursor.moveToFirst()) {
            int nameColIndex = cursor.getColumnIndex("name");
            int valueColIndex = cursor.getColumnIndex("value");
            do {
                variables.add(new StringVariableWrapper(cursor.getString(nameColIndex),
                        cursor.getString(valueColIndex)));
            } while(cursor.moveToNext());
        }
        cursor.close();
        return variables;
    }
}
