package snacker.puzzlesolver;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Snacker on 2017-05-27.
 */

public class DBHelper extends SQLiteOpenHelper{
    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE PUZZLES (_id INTEGER PRIMARY KEY AUTOINCREMENT, type TEXT, item TEXT, width INTEGER, height INTEGER, val TEXT)");
        db.execSQL("INSERT INTO PUZZLES VALUES (null, 'Sudoku', 'a', 9, 9, '1 2 3 4 5')");
        db.execSQL("INSERT INTO PUZZLES VALUES (null, 'Nono', 'b', 9, 9, '1 2 3 4 5')");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void insert(String type, String item, int w, int h, String v){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO PUZZLES VALUES(null, " + type + ", '"+ item + "', " + w+ ", "+ h +", " + v + "');");
        db.close();
    }

    public void update( String item, String v){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE PUZZLE SET val='" + v + "' WHERE item='" + item + "';");
        db.close();
    }
}
