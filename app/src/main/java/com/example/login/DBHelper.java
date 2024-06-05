package com.example.login;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DBNAME = "Auth.db";
    private static final String TAG = "DBHelper";

    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase MyDB) {
        MyDB.execSQL("CREATE TABLE user(username TEXT PRIMARY KEY, password TEXT, firstname TEXT, lastname TEXT, email TEXT, dob TEXT, phone TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase MyDB, int oldVersion, int newVersion) {
        MyDB.execSQL("DROP TABLE IF EXISTS user");
        onCreate(MyDB);
    }

    public Boolean insertData(String firstname, String lastname, String username, String email, String dob, String phone, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("firstname", firstname);
        contentValues.put("lastname", lastname);
        contentValues.put("username", username);
        contentValues.put("email", email);
        contentValues.put("dob", dob);
        contentValues.put("phone", phone);
        contentValues.put("password", password);

        long result = MyDB.insert("user", null, contentValues);

        if (result == -1) {
            Log.e(TAG, "Failed to insert data");
            return false;
        } else {
            Log.i(TAG, "Data inserted successfully");
            return true;
        }
    }

    public Boolean checkusername(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM user WHERE username = ?", new String[]{username});
        return cursor.getCount() > 0;
    }

    public Boolean checkusernamepassword(String username, String password) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor = MyDB.rawQuery("SELECT * FROM user WHERE username = ? AND password = ?", new String[]{username, password});
        return cursor.getCount() > 0;
    }
}
