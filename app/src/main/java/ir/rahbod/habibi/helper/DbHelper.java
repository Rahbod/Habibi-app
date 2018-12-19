package ir.rahbod.habibi.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Service";
    private SQLiteDatabase db;
    private static final String TABLE_SERVICES = "services";
    private static final String TABLE_ADDRESS = "address";
    private static final String KEY_ID = "id";
    private static final String KEY_DEVICE_ID = "deviceID";
    private static final String KEY_ADDRESS_ID = "addressID";
    private static final String KEY_TITLE = "title";


    public DbHelper(Context con) {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_DEVICES = "CREATE TABLE IF NOT EXISTS " + TABLE_SERVICES + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_TITLE + " TEXT , "
                + KEY_DEVICE_ID + " INTEGER)";

        String CREATE_TABLE_ADDRESS = "CREATE TABLE IF NOT EXISTS " + TABLE_ADDRESS + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_TITLE + " TEXT , "
                + KEY_ADDRESS_ID + " INTEGER)";

        db.execSQL(CREATE_TABLE_DEVICES);
        db.execSQL(CREATE_TABLE_ADDRESS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onCreate(db);
    }

    public int getMaxID(String table) {
        String query = "SELECT MAX(id) FROM " + table;
        db = this.getReadableDatabase();
        int MaxID = 0;
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            MaxID = cursor.getInt(0);
        }
        return MaxID;
    }

    public void delete(int id) {
        db.execSQL("DELETE FROM " + TABLE_SERVICES + " WHERE id=" + id);
    }

//    public void addDevices(String title, int ID) {
//        db = this.getWritableDatabase();
//        ContentValues values = new ContentValues();
//        values.put(KEY_TITLE, title);
//        values.put(KEY_DEVICE_ID, ID);
//        db.insert(TABLE_SERVICES, null, values);
//    }
//
//    public String getDevicesTitle(int ID) {
//        db = this.getReadableDatabase();
//        String query = "SELECT * FROM "
//                + TABLE_SERVICES +
//                " WHERE "
//                + KEY_DEVICE_ID +
//                " = "
//                + ID;
//        String title = "";
//        Cursor cursor = db.rawQuery(query, null);
//        while (cursor.moveToNext()) {
//            title = cursor.getString(cursor.getColumnIndex(KEY_TITLE));
//        }
//        return title;
//    }

//    public void deleteTableService() {
//        db = getWritableDatabase();
//        db.execSQL("DELETE FROM " + TABLE_SERVICES);
//    }
}