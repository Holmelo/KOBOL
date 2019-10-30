package kobolapp.com.kobolapp3;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {
    public DatabaseHelper(@Nullable Context context) {
        super(context, "Accounts.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE users(email TEXT PRIMARY KEY,password TEXT,name TEXT,address TEXT,phone TEXT,medicare TEXT,doctor TEXT,occupation TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS users"); //drops older table if exists
        onCreate(db);
    }

    //inserting in database
    public boolean insert(String email, String password, String name, String address, String phone, String medicare, String doctor, String occupation) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        contentValues.put("name", name);
        contentValues.put("address", address);
        contentValues.put("phone", phone);
        contentValues.put("medicare", medicare);
        contentValues.put("doctor", doctor);
        contentValues.put("occupation", occupation);
        long ins = db.insert("users",null,contentValues);
        if (ins == 1) return false;
        else return true;
    }

    public Boolean checkemail(String email) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=?", new String[] {email});
        if (cursor.getCount()>0) return false;
        else return true;
    }

    //checking credentials
    public boolean emailpassword(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM users WHERE email=? AND password=?", new String[] {email, password});
        if(cursor.getCount()>0) return true;
        else return false;
    }

}
