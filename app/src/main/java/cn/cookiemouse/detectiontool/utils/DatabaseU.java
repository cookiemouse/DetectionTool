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
    private DatabaseHelper mDatabaseHelper;

    public DatabaseU(Context context) {
        mDatabaseHelper = new DatabaseHelper(context, Data.DATA_DB_NAME);
    }

    //  ============================Detection===================================

    //  查询Detection
    public List<DetectionData> getDetection() {
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
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
                    detectionDataList.add(new DetectionData(cursor.getLong(0)
                            , cursor.getString(1)
                            , cursor.getString(2))
                    );
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e(TAG, "getDetection: -->", e);
        } finally {
            mSqLiteDatabase.endTransaction();
            mSqLiteDatabase.close();
        }
        return detectionDataList;
    }

    //  获取特定Detection
    public DetectionData getDetection(long detectionRowId) {
        DetectionData detectionData = null;
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        try {
            mSqLiteDatabase.beginTransaction();
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_DETECTION
                    , new String[]{"rowid", "name", "address"}
                    , "rowid=?"
                    , new String[]{"" + detectionRowId}
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();
            if (cursor.moveToFirst()) {
                detectionData = new DetectionData(cursor.getLong(0)
                        , cursor.getString(1)
                        , cursor.getString(2)
                );
            }
        } catch (Exception e) {
            Log.e(TAG, "getDetection: -->", e);
        } finally {
            mSqLiteDatabase.endTransaction();
            mSqLiteDatabase.close();
        }
        return detectionData;
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
    public long addDetection(DetectionData data) {
        long rowid = Data.DATA_INIT_ROWID;
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", data.getName());
        contentValues.put("address", data.getAddress());
        if (detectionExist(data)) {
            try {
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                rowid = data.getRowid();
                mSqLiteDatabase.beginTransaction();
                mSqLiteDatabase.update(Data.DATA_TAB_DETECTION
                        , contentValues
                        , "rowid=?"
                        , new String[]{("" + rowid)});
                mSqLiteDatabase.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e(TAG, "addDetection: -->", e);
            } finally {
                mSqLiteDatabase.endTransaction();
            }
        } else {
            try {
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                mSqLiteDatabase.beginTransaction();
                rowid = mSqLiteDatabase.insert(Data.DATA_TAB_DETECTION, null, contentValues);
                mSqLiteDatabase.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e(TAG, "addDetection: -->", e);
            } finally {
                mSqLiteDatabase.endTransaction();
                mSqLiteDatabase.close();
            }
        }
        return rowid;
    }

    //  delete Detection
    public void deleteDetection(DetectionData data) {
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        try {
            mSqLiteDatabase.beginTransaction();
            mSqLiteDatabase.delete(Data.DATA_TAB_DETECTION
                    , "rowid=?"
                    , new String[]{("" + data.getRowid())}
            );
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "deleteDetection: ", e);
        } finally {
            mSqLiteDatabase.endTransaction();
            mSqLiteDatabase.close();
        }
    }

    //  ============================Parameter===================================

    //  查询Parameter
    public List<ParameterData> getParameter(long detectionRowId) {
        Log.i(TAG, "getParameter: detectionRowId-->" + detectionRowId);
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        List<ParameterData> parameterDataList = new ArrayList<>();
        try {
            mSqLiteDatabase.beginTransaction();
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_PARAMETER
                    , new String[]{"detectionId", "rowid", "tKey", "tValue"}
                    , "detectionId=?"
                    , new String[]{"" + detectionRowId}
                    , null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();
            Log.i(TAG, "getParameter: size-->" + cursor.getCount());
            Log.i(TAG, "getParameter: -1-");
            if (cursor.moveToFirst()) {
                Log.i(TAG, "getParameter: -2-");
                do {
                    Log.i(TAG, "getParameter: -3-");
                    parameterDataList.add(new ParameterData(cursor.getLong(0)
                            , cursor.getLong(1)
                            , cursor.getString(2)
                            , cursor.getString(3))
                    );
                } while (cursor.moveToNext());
                Log.i(TAG, "getParameter: -4-");
            }
        } catch (Exception e) {
            Log.e(TAG, "getParameter: ", e);
        } finally {
            mSqLiteDatabase.endTransaction();
            mSqLiteDatabase.close();
        }
        return parameterDataList;
    }

    //  添加Parameter
    public long addParameter(ParameterData parameterData) {
        long rowIdParameter = Data.DATA_INIT_ROWID;
        ContentValues contentValues = new ContentValues();
        contentValues.put("detectionId", parameterData.getKey());
        contentValues.put("tKey", parameterData.getKey());
        contentValues.put("tValue", parameterData.getValue());
        if (parameterExist(parameterData)) {
            try {
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                rowIdParameter = parameterData.getRowidParameter();
                mSqLiteDatabase.beginTransaction();
                mSqLiteDatabase.update(Data.DATA_TAB_PARAMETER
                        , contentValues
                        , "rowid=?"
                        , new String[]{("" + rowIdParameter)});
                mSqLiteDatabase.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e(TAG, "addParameter: -->", e);
            } finally {
                mSqLiteDatabase.endTransaction();
            }
        } else {
            try {
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                mSqLiteDatabase.beginTransaction();
                rowIdParameter = mSqLiteDatabase.insert(Data.DATA_TAB_PARAMETER, null, contentValues);
                mSqLiteDatabase.setTransactionSuccessful();

            } catch (Exception e) {
                Log.e(TAG, "addParameter: -->", e);
            } finally {
                mSqLiteDatabase.endTransaction();
                mSqLiteDatabase.close();
            }
        }
        return rowIdParameter;
    }

    //  添加Parameter By list
    public void addParameter(List<ParameterData> parameterDataList) {
        for (ParameterData parameterData : parameterDataList) {
            ContentValues contentValues = new ContentValues();
            contentValues.put("detectionId", parameterData.getRowidDetection());
            contentValues.put("tKey", parameterData.getKey());
            contentValues.put("tValue", parameterData.getValue());
            try {
                mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
                mSqLiteDatabase.beginTransaction();
                mSqLiteDatabase.insert(Data.DATA_TAB_PARAMETER, null, contentValues);
                mSqLiteDatabase.setTransactionSuccessful();

                Log.i(TAG, "addParameter: insert.id-->" + parameterData.getRowidDetection());

            } catch (Exception e) {
                Log.e(TAG, "addParameter: -->", e);
            } finally {
                mSqLiteDatabase.endTransaction();
                mSqLiteDatabase.close();
            }
        }
    }

    //  delete Parameter
    public void deleteParameter(ParameterData parameterData) {
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        try {
            mSqLiteDatabase.beginTransaction();
            mSqLiteDatabase.delete(Data.DATA_TAB_PARAMETER
                    , "rowid=?"
                    , new String[]{("" + parameterData.getRowidParameter())}
            );
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "deleteParameter: ", e);
        } finally {
            mSqLiteDatabase.endTransaction();
            mSqLiteDatabase.close();
        }
    }

    //  delete Parameter
    public void deleteParameter(long detectionRowId) {
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        try {
            mSqLiteDatabase.beginTransaction();
            mSqLiteDatabase.delete(Data.DATA_TAB_PARAMETER
                    , "detectionId=?"
                    , new String[]{("" + detectionRowId)}
            );
            mSqLiteDatabase.setTransactionSuccessful();
        } catch (Exception e) {
            Log.e(TAG, "deleteParameter: ", e);
        } finally {
            mSqLiteDatabase.endTransaction();
            mSqLiteDatabase.close();
        }
    }

    //  Parameter是否存在
    private boolean parameterExist(ParameterData parameterData) {
        boolean exist = false;
        mSqLiteDatabase = mDatabaseHelper.getWritableDatabase();
        try {
            mSqLiteDatabase.beginTransaction();
            Cursor cursor = mSqLiteDatabase.query(Data.DATA_TAB_PARAMETER
                    , null
                    , "detectionId=?&rowid=?"
                    , new String[]{("" + parameterData.getRowidDetection())
                            , ("" + parameterData.getRowidParameter())}
                    , null, null, null, null);
            mSqLiteDatabase.setTransactionSuccessful();
            exist = (cursor.getCount() > 0);
        } catch (Exception e) {
            Log.e(TAG, "deleteParameter: ", e);
        } finally {
            mSqLiteDatabase.endTransaction();
            mSqLiteDatabase.close();
        }
        Log.i(TAG, "parameterExist: -->" + exist);
        return exist;
    }

    //  ============================DatabaseU===================================
}
