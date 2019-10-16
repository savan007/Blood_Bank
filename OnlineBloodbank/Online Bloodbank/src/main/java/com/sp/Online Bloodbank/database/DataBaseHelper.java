package com.sp.online bloodbank.views;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;

/**
 * Created by Sandhiya.lal on 12/4/2015.
 */
public class DataBaseHelper extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "HRMS";
    private static String DB_PATH = "/data/data/YOUR_PACKAGE/databases/";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";

    // Table Names
    public static final String TABLE_LOGIN = "UserInfo";

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    //Common Column names
    public static final String KEY_USER_ID = "userId";
    public static final String KEY_IS_LOGGED_IN = "isLoggedIn";
    public static final String KEY_USER_NAME = "userName";

    private SQLiteDatabase mDataBase;

    private static final String CREATE_TABLE_LOGIN = CREATE_TABLE
            +TABLE_LOGIN + "("
            +KEY_USER_ID + " INTEGER PRIMARY KEY,"
            +KEY_USER_NAME + " TEXT,"
            +KEY_IS_LOGGED_IN + " INTEGER"+ ")";


    public DataBaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        DB_PATH = "/data/data/" + context.getPackageName() + "/databases/";
    }

    public void createDataBase() throws IOException {
        boolean mDataBaseExist = checkDataBase();
        if (!mDataBaseExist) {
            this.getReadableDatabase();
        }
    }

    private boolean checkDataBase() {
        SQLiteDatabase mCheckDataBase = null;
        try {
            String mPath = DB_PATH + DATABASE_NAME;
            File pathFile = new File(mPath);
            if(pathFile.exists()) {
                mCheckDataBase = SQLiteDatabase.openDatabase(mPath, null, SQLiteDatabase.NO_LOCALIZED_COLLATORS);
            }
        } catch (SQLiteException mSQLiteException) {
            BookMarketApplication.printLogMessage(5, "HRMS : DatabaseNotFound " + mSQLiteException.toString());
        }

        if (mCheckDataBase != null) {
            mCheckDataBase.close();
        }
        return mCheckDataBase != null;
    }

    public boolean openDataBase() throws SQLException {
        String mPath = DB_PATH + DATABASE_NAME;
        mDataBase = SQLiteDatabase.openDatabase(mPath, null,
                SQLiteDatabase.NO_LOCALIZED_COLLATORS);
        return mDataBase != null;
    }

    @Override
    public synchronized void close() {
        if(mDataBase != null) {
            mDataBase.close();
        }
        super.close();
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        BookMarketApplication.printLogMessage(1, CREATE_TABLE_LOGIN);
        db.execSQL(CREATE_TABLE_LOGIN);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       /* db.execSQL("DROP TABLE IF EXISTS " + TABLE_LOGIN);
        // create new tables
        onCreate(db);*/
    }

}
