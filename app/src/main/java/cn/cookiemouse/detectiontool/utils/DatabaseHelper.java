package cn.cookiemouse.detectiontool.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import cn.cookiemouse.detectiontool.data.Data;

/**
 * Created by cookiemouse on 2017/7/26.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";

    private static final int VERSION = 1;

    private static final String TABLE_DETECTION = "create table " + Data.DATA_TAB_DETECTION
            + "(name TEXT,address TEXT)";
    private static final String TABLE_PARAMETER = "create table " + Data.DATA_TAB_PARAMETER
            + "(aid INTEGER,tKey TEXT,tValue TEXT)";


    public DatabaseHelper(Context context, String name) {
        this(context, name, null, VERSION);
    }

    private DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.i(TAG, "onCreate: -->" + 1);
        sqLiteDatabase.execSQL(TABLE_DETECTION);
        sqLiteDatabase.execSQL(TABLE_PARAMETER);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
    }
}
