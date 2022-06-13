package com.ryanamiri.planner;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SQLite extends SQLiteOpenHelper {

    public SQLite(Context context) {
        super(context, "planner.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        db.execSQL("create Table tasks(_id INTEGER PRIMARY KEY, name TEXT, time TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("drop Table if exists tasks");
    }

    public void reset(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("drop Table if exists tasks");
        db.execSQL("create Table tasks(_id INTEGER PRIMARY KEY, name TEXT, time TEXT)");
    }

    /*
     * Method insert Add data to the database, can be called anywhere
     *
     * @param name, The name of the task.
     * @param time, The time the task should take place at.
     */
    public boolean insert(String name, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("time", time);
        long result = db.insert("tasks", null, contentValues);
        if(result == -1)
            return false;
        return true;
    }

    /*
     * Method update Updates certain task.
     *
     * @param id, The id of the task in the database.
     * @param name, The new name of the task.
     * @param time, The new time of the task.
     */
    public boolean update(String id, String name, String time){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("time", time);
        long result = db.update("tasks", contentValues, "_id=?", new String[] {id});
        if(result == -1)
            return false;
        return true;
    }

    /*
     * Method delete Deletes certain task.
     *
     * @param id, The id of the task to delete.
     */
    public boolean delete(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete("tasks", "_id=?", new String[] {id});
        if(result == -1)
            return false;
        return true;
    }

    /*
     * Method getData Gets all the data from the database and returns it to be used.
     */
    public Cursor getData(){
        SQLiteDatabase db = this.getWritableDatabase();
        return db.rawQuery("Select * from tasks", null);
    }
}
