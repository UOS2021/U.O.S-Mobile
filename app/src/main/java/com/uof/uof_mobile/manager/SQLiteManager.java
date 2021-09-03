package com.uof.uof_mobile.manager;

import static com.uof.uof_mobile.other.Global.SQLite.SQL_CREATE_TABLE;
import static com.uof.uof_mobile.other.Global.SQLite.SQL_DROP_TABLE;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.uof.uof_mobile.item.BasketItem;
import com.uof.uof_mobile.item.WaitingOrderItem;
import com.uof.uof_mobile.other.Global;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class SQLiteManager extends SQLiteOpenHelper {
    private Context context;
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

    public boolean saveOrder(int orderNumber, String companyName, JSONObject orderData) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Global.SQLite.CL_ORDER_COMPANY, companyName);
        contentValues.put(Global.SQLite.CL_ORDER_NUMBER, orderNumber);
        contentValues.put(Global.SQLite.CL_ORDER_TIME, new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(new Date(System.currentTimeMillis())));
        contentValues.put(Global.SQLite.CL_ORDER_INFO, orderData.toString());
        contentValues.put(Global.SQLite.CL_ORDER_STATE, "wait");
        contentValues.put(Global.SQLite.CL_ORDER_ID, Global.User.id);

        return (insert(Global.SQLite.TB_ORDER_LIST, contentValues) != -1);
    }

    public ArrayList<WaitingOrderItem> loadOrder() {
        ArrayList<WaitingOrderItem> waitingOrderItemArrayList = new ArrayList<>();

        Cursor cursor = read(Global.SQLite.TB_ORDER_LIST, null, new String[]{Global.SQLite.CL_ORDER_ID}, new String[]{Global.User.id}, Global.SQLite.CL_ORDER_STATE, Global.SQLite.SORT_ASCENDING);

        if (cursor.getCount() > 0) {
            cursor.moveToFirst();
            do {
                try {
                    ArrayList<BasketItem> basketItemArrayList = new ArrayList<>();

                    JSONArray orderList = new JSONObject(cursor.getString(cursor.getColumnIndex(Global.SQLite.CL_ORDER_INFO))).getJSONArray("order");

                    for (int loop = 0; loop < orderList.length(); loop++) {
                        JSONObject orderItem = orderList.getJSONObject(loop);
                        basketItemArrayList.add(new BasketItem(orderItem.getInt("type"), orderItem.getString("menu"), orderItem.getString("submenu"), orderItem.getInt("price"), orderItem.getInt("count")));
                    }

                    waitingOrderItemArrayList.add(new WaitingOrderItem(
                            cursor.getString(cursor.getColumnIndex(Global.SQLite.CL_ORDER_COMPANY))
                            , Integer.valueOf(cursor.getString(cursor.getColumnIndex(Global.SQLite.CL_ORDER_NUMBER)))
                            , cursor.getString(cursor.getColumnIndex(Global.SQLite.CL_ORDER_TIME))
                            , cursor.getString(cursor.getColumnIndex(Global.SQLite.CL_ORDER_STATE))
                            , basketItemArrayList));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } while (cursor.moveToNext());
        }

        return waitingOrderItemArrayList;
    }

    public boolean hasOrderNumber(int orderNumber) {
        return (read(Global.SQLite.TB_ORDER_LIST, null, new String[]{Global.SQLite.CL_ORDER_NUMBER}, new String[]{String.valueOf(orderNumber)}, Global.SQLite.CL_ORDER_NUMBER, Global.SQLite.SORT_ASCENDING).getCount() == 1);
    }

    public boolean removeOrder(int orderNumber) {
        return (delete(Global.SQLite.TB_ORDER_LIST, new String[]{Global.SQLite.CL_ORDER_NUMBER}, new String[]{String.valueOf(orderNumber)}) == 1);
    }

    public boolean setOrderState(int orderNumber, String state) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(Global.SQLite.CL_ORDER_STATE, state);
        return (update(Global.SQLite.TB_ORDER_LIST, contentValues, new String[]{Global.SQLite.CL_ORDER_NUMBER}, new String[]{String.valueOf(orderNumber)}) != 0);
    }

    public int getWaitingOrderCount() {
        Cursor cursor = read(Global.SQLite.TB_ORDER_LIST, null, new String[]{Global.SQLite.CL_ORDER_STATE}, new String[]{"wait"}, null, null);

        if (cursor == null) {
            return 0;
        }

        return cursor.getCount();
    }
}
