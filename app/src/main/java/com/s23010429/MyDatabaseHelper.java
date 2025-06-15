package com.example.s23010429;

import android.content.ContentValues;
import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MyDatabaseHelper {
    // Database Info
    private static final String DATABASE_NAME = "UserDB";
    private static final int DATABASE_VERSION = 1;

    // Table Names
    public static final String TABLE_USERS = "Users";

    // User Table Columns
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_USERNAME = "username";
    public static final String COLUMN_PASSWORD = "password";

    // Database fields
    private final Context context;
    private DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    // Constructor
    public MyDatabaseHelper(Context ctx) {
        this.context = ctx;
        dbHelper = new DatabaseHelper(context);
    }

    // Open database
    public MyDatabaseHelper open() throws SQLException {
        database = dbHelper.getWritableDatabase();
        return this;
    }

    // Close database
    public void close() {
        if (dbHelper != null) {
            dbHelper.close();
        }
    }

    // Insert new user data
    public long insertUserData(String username, String password) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_USERNAME, username);
        values.put(COLUMN_PASSWORD, password);

        try {
            return database.insert(TABLE_USERS, null, values);
        } catch (Exception e) {
            Log.e("MyDatabaseHelper", "Error inserting data: " + e.getMessage());
            return -1;
        }
    }

    // Database helper class
    private static class DatabaseHelper extends SQLiteOpenHelper {
        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            // Create users table
            String CREATE_USERS_TABLE = "CREATE TABLE " + TABLE_USERS + " ("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_USERNAME + " TEXT, "
                    + COLUMN_PASSWORD + " TEXT)";

            db.execSQL(CREATE_USERS_TABLE);
            Log.d("DatabaseHelper", "Database and tables created");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            // Drop older table if existed and recreate
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
            onCreate(db);
        }
    }
}