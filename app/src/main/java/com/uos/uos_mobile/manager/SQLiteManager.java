package com.uos.uos_mobile.manager;

import static com.uos.uos_mobile.other.Global.SQLite.SQL_CREATE_TABLE;
import static com.uos.uos_mobile.other.Global.SQLite.SQL_DROP_TABLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uos.uos_mobile.item.BasketItem;
import com.uos.uos_mobile.item.WaitingOrderItem;
import com.uos.uos_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {
    private SQLiteDatabase sqLiteDatabase;

    public SQLiteManager(Context context) {
        super(context, Global.SQLite.DB_ORDER_LIST, null, Global.SQLite.DB_VERSION);
    }

    public void openDatabase() {
        sqLiteDatabase = getWritableDatabase();
    }

    public void closeDatabase() {
        sqLiteDatabase.close();
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL(SQL_DROP_TABLE);
        onCreate(sqLiteDatabase);
    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    // 새로 삽입된 ROW 행 ID반환 (오류 발생 시: -1)
    public long insert(String tableName, ContentValues contentValues) {
        return sqLiteDatabase.insert(tableName, null, contentValues);
    }

    public Cursor read(String tableName, String[] getColumn, String[] conditionColumns, String[] conditionValues, String sortColumn, String sortType) {
        int loop = 0;
        String condition = "";

        if (conditionColumns.length == 1) {
            condition += conditionColumns[loop] + " = ?";
        } else {
            loop = 0;
            while (loop < conditionColumns.length) {
                condition += conditionColumns[loop] + " = ?";
                if (loop < conditionColumns.length - 1) {
                    condition += " AND ";
                }
                loop++;
            }
        }

        if (sortColumn == null) {
            return sqLiteDatabase.query(
                    tableName
                    , getColumn
                    , condition
                    , conditionValues
                    , null
                    , null
                    , null
            );
        }

        return sqLiteDatabase.query(
                tableName
                , getColumn
                , condition
                , conditionValues
                , null
                , null
                , sortColumn + " " + sortType
        );
    }

    // 영향을 받은 ROW수 반환 (0: 삭제 실패, 1: 모든 데이터 삭제 or 1개 삭제)
    public int delete(String tableName, String[] conditionColumns, String[] conditionValues) {
        int loop = 0;
        String condition = "";

        if (conditionColumns == null) {
            return sqLiteDatabase.delete(tableName, null, null);
        }

        if (conditionColumns.length == 1) {
            condition = conditionColumns[loop] + " = ?";
        } else {
            loop = 0;
            while (loop < conditionColumns.length) {
                condition += conditionColumns[loop] + " = ?";
                if (loop < conditionColumns.length - 1) {
                    condition += " AND ";
                }
                loop++;
            }
        }

        return sqLiteDatabase.delete(tableName, condition, conditionValues);
    }

    // 영향을 받은 ROW수 반환 (0: update 실패)
    public int update(String tableName, ContentValues contentValues, String[] conditionColumns, String[] conditionValues) {
        int loop = 0;
        String condition = "";

        if (conditionColumns.length == 1) {
            condition = conditionColumns[loop] + " = ?";
        } else {
            loop = 0;
            while (loop < conditionColumns.length) {
                condition += conditionColumns[loop] + " = ?";
                if (loop < conditionColumns.length - 1) {
                    condition += " AND ";
                }
                loop++;
            }
        }

        return sqLiteDatabase.update(tableName, contentValues, condition, conditionValues);
    }
}
