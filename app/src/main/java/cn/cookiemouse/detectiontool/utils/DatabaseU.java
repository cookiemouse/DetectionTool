package cn.cookiemouse.detectiontool.utils;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import cn.cookiemouse.detectiontool.data.Data;
import cn.cookiemouse.detectiontool.data.DetectionData;
import cn.cookiemouse.detectiontool.data.ParameterData;

public class DatabaseU {
    private static final String TAG = "DatabaseU";

    private SQLiteDatabase mSqLiteDatabase;

    public DatabaseU(Context context) {
        DatabaseHelper databaseHelper = new DatabaseHelper(context, Data.DATA_DB_NAME);
        mSqLiteDatabase = databaseHelper.getWritableDatabase();
    }

    //  ============================Detection===================================

    //  查询Detection
    public List<DetectionData> getDetection() {
        List<DetectionData> detectionDataList = new ArrayList<>();
        try {
            mSqLiteDatabase.beginTransaction();
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_DETECTION
                    , new String[]{"rowid", "name", "address"}
                    , null
                    , null
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();
            if (cursor.moveToFirst()) {
                do {
                    detectionDataList.add(new DetectionData(cursor.getInt(0)
                            , cursor.getString(1)
                            , cursor.getString(2))
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getDetection: -->", e);
        } finally {
            mSqLiteDatabase.endTransaction();
        }
        return detectionDataList;
    }

    private boolean detectionExist(DetectionData detectionData) {
        List<DetectionData> detectionDataList = this.getDetection();
        if (null == detectionDataList) {
            return false;
        }
        for (DetectionData data : detectionDataList) {
            if (data.getRowid() == detectionData.getRowid()) {
                return true;
            }
        }
        return false;
    }

    //  添加Detection
    public void addDetection(DetectionData data) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", data.getName());
        contentValues.put("address", data.getAddress());
        if (detectionExist(data)) {
            try {
                mSqLiteDatabase.beginTransaction();
                mSqLiteDatabase.update(Data.DATA_TAB_DETECTION
                        , contentValues
                        , "rowid=?"
                        , new String[]{("" + data.getRowid())});
                mSqLiteDatabase.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e(TAG, "addDetection: -->", e);
            } finally {
                mSqLiteDatabase.endTransaction();
            }
        } else {
            try {
                mSqLiteDatabase.beginTransaction();
                mSqLiteDatabase.insert(Data.DATA_TAB_DETECTION, null, contentValues);
                mSqLiteDatabase.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e(TAG, "addDetection: -->", e);
            } finally {
                mSqLiteDatabase.endTransaction();
            }
        }
    }

    //  update Detection
    public void modifyDetection() {
    }

    //  delete Detection
    public void deleteDetection() {
    }

    //  ============================Parameter===================================

    //  查询Parameter
    public void getParameter() {
    }

    //  添加Parameter
    public void addParameter() {
    }

    //  update Parameter
    public void modifyParameter() {
    }

    //  delete Parameter
    public void deleteParameter() {
    }

    //  ============================DatabaseU===================================
}
