package ir.rahbod.habibi.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DbHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Service";
    private SQLiteDatabase db;
    private static final String TABLE_SERVICES = "services";
    private static final String KEY_ID = "id";
    private static final String KEY_DEVICE_ID = "deviceID";
    private static final String KEY_DEVICE_TITLE = "serviceTitle";


    public DbHelper(Context con) {
        super(con, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_REMINDERS = "CREATE TABLE IF NOT EXISTS " + TABLE_SERVICES + " ( "
                + KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT , " + KEY_DEVICE_TITLE + " TEXT , "
                + KEY_DEVICE_ID + " INTEGER)";

        db.execSQL(CREATE_TABLE_REMINDERS);
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

    public void addDevices(String title, int ID) {
        db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_DEVICE_TITLE, title);
        values.put(KEY_DEVICE_ID, ID);
        db.insert(TABLE_SERVICES, null, values);
    }

    public String getDevicesTitle(int ID) {
        db = this.getReadableDatabase();
        String query = "SELECT * FROM "
                + TABLE_SERVICES +
                " WHERE "
                + KEY_DEVICE_ID +
                " = "
                + ID;
        String title = "";
        Cursor cursor = db.rawQuery(query, null);
        while (cursor.moveToNext()) {
            title = cursor.getString(cursor.getColumnIndex(KEY_DEVICE_TITLE));
        }
        return title;
    }
}