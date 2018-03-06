package snacker.puzzlesolver;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

public class Puzzlelist extends AppCompatActivity {

    DBHelper database;
    @SuppressWarnings("deprecation")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_puzzlelist);

        database = new DBHelper(this, "puzzle.db", null, 1);
        final SQLiteDatabase db = database.getWritableDatabase();



        Cursor cursor = db.rawQuery("SELECT * FROM PUZZLES", null);
        startManagingCursor(cursor);

        final SimpleCursorAdapter Adapter = new SimpleCursorAdapter(this,android.R.layout.simple_list_item_2, cursor, new String[] {"type", "item"},
                new int[] {android.R.id.text1, android.R.id.text2});
        ListView list = (ListView)findViewById(R.id.dblist);
        list.setAdapter(Adapter);
        list.setOnItemClickListener(new ListView.OnItemClickListener(){
            public void onItemClick(AdapterView<?> parentView, View clickedView, int position, long id){
                Cursor cursor = (Cursor) Adapter.getItem(position);
                String type = cursor.getString(1);
                String name = cursor.getString(2);
                int width = cursor.getInt(3);
                int height = cursor.getInt(4);
                String val = cursor.getString(5);
                switch(type) {
                    case "Nonogram":
                        Intent intent = new Intent(getApplicationContext(), NonoActivity.class);
                        intent.putExtra("isOpen", 1);
                        intent.putExtra("name",name);
                        intent.putExtra("width",width);
                        intent.putExtra("height", height);
                        intent.putExtra("val", val);
                        startActivityForResult(intent,103);
                        break;
                    case "Sudoku":
                        Intent intent2 = new Intent(getApplicationContext(), SudokuActivity.class);
                        intent2.putExtra("isOpen", 1);
                        intent2.putExtra("name",name);
                        intent2.putExtra("val", val);
                        startActivityForResult(intent2, 104);
                        break;
                    case "Kakuro":
                        Intent intent3 = new Intent(getApplicationContext(), KakuroActivity.class);
                        intent3.putExtra("isOpen", 1);
                        intent3.putExtra("name",name);
                        intent3.putExtra("width",width);
                        intent3.putExtra("height", height);
                        intent3.putExtra("val", val);
                        startActivityForResult(intent3, 105);
                        break;
                }
            }
        });
        list.setOnItemLongClickListener(new ListView.OnItemLongClickListener(){
            public boolean onItemLongClick(AdapterView<?> parentView, View clickedView, int position, long id){
                Cursor cursor = (Cursor) Adapter.getItem(position);
                ListView list = (ListView)findViewById(R.id.dblist);
                String name = cursor.getString(2);
                int del = db.delete("PUZZLES", "item='"+ name +"'", null);
                Toast.makeText(Puzzlelist.this,"Puzzle successfully deleted", Toast.LENGTH_SHORT).show();
                Adapter.notifyDataSetChanged();
                list.invalidateViews();
                return true;
            }
        });
    }
}
