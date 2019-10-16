package com.sp.online bloodbank.views;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.sp.bookmarket.models.LoginResponse;

import java.io.IOException;

/**
 * Created by Sandhiya.lal on 12/10/2015.
 */
public class DBAdapter {

    private final Context mContext;
    private SQLiteDatabase mDb;
    private final DataBaseHelper mDbHelper;

    public DBAdapter(Context context) {
        this.mContext = context;
        mDbHelper = new DataBaseHelper(mContext);
    }

    public DBAdapter createDatabase() throws SQLException {
        try {
            mDbHelper.createDataBase();
        } catch (IOException mIOException) {
            throw new Error("UnableToCreateDatabase");
        }
        return this;
    }

    public DBAdapter open() throws SQLException {
        try {
            mDbHelper.openDataBase();
            mDbHelper.close();
            mDb = mDbHelper.getReadableDatabase();
        } catch (SQLException mSQLException) {
            BookMarketApplication.printLogMessage(5, mSQLException.toString());
            throw mSQLException;
        }
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

   /*
    ******** UserInfo table ********
    */
    public String addNewUser(LoginResponse loginResponse) {
        if (!isUserExist(Integer.parseInt(loginResponse.getUser_id()))) {
            mDb = mDbHelper.getWritableDatabase();

            ContentValues values = new ContentValues();
            values.put(DataBaseHelper.KEY_USER_ID, loginResponse.getUser_id());
            values.put(DataBaseHelper.KEY_USER_NAME, loginResponse.getUserProfile().getUserName());
            values.put(DataBaseHelper.KEY_IS_LOGGED_IN, 1);
            BookMarketApplication.printLogMessage(3, "Database : Add new user in UserInfo table");
            mDb.insert(DataBaseHelper.TABLE_LOGIN, null, values);
            mDb.close();
        } else {
            mDb = mDbHelper.getWritableDatabase();
            ContentValues values = new ContentValues();

            values.put(DataBaseHelper.KEY_USER_NAME, loginResponse.getUserProfile().getUserName());
            values.put(DataBaseHelper.KEY_IS_LOGGED_IN, 1);
            BookMarketApplication.printLogMessage(3, "Database : Update existing user in UserInfo table");
            mDb.update(DataBaseHelper.TABLE_LOGIN, values, DataBaseHelper.KEY_USER_ID + " =? ", new String[]{loginResponse.getUser_id()});
            mDb.close();
        }
        return loginResponse.getUser_id();
    }

    public Boolean isUserExist(int userId) {
        Boolean isUserExist = false;
        mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_LOGIN +
                " WHERE " + DataBaseHelper.KEY_USER_ID + " =? ", new String[]{String.valueOf(userId)});
        if (cursor != null && cursor.getCount() > 0) {
            isUserExist = true;
            cursor.close();
        }

        return isUserExist;
    }

    public Boolean isLoggedIn() {
        boolean is_Logged_In = false;
        mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_LOGIN +
                " WHERE " + DataBaseHelper.KEY_IS_LOGGED_IN + " =? ", new String[]{String.valueOf(1)});

        if (cursor != null && cursor.getCount() > 0) {
            is_Logged_In = true;
            cursor.close();
        }

        return is_Logged_In;
    }

    public String getLoggedInUserDetail() {
        String userId = null;
        mDb = mDbHelper.getReadableDatabase();
        Cursor cursor = mDb.rawQuery("SELECT * FROM " + DataBaseHelper.TABLE_LOGIN +
                " WHERE " + DataBaseHelper.KEY_IS_LOGGED_IN + " =? ", new String[]{String.valueOf(1)});
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {
                    userId = cursor.getString(cursor.getColumnIndex(DataBaseHelper.KEY_USER_ID));
                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        return userId;
    }

    public void logoutUser() {
        String userId = getLoggedInUserDetail();
        mDb = mDbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(DataBaseHelper.KEY_IS_LOGGED_IN, 0);

        mDb.update(DataBaseHelper.TABLE_LOGIN, values, DataBaseHelper.KEY_USER_ID + " =? "
                , new String[]{String.valueOf(userId)});
        mDb.close();
    }
}
