package snacker.puzzlesolver;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SudokuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sudoku);
        Button btn = (Button)findViewById(R.id.btn_clear);
        Button btn2 = (Button)findViewById(R.id.btn_solve);
        Button btn3 = (Button)findViewById(R.id.btn_save);
        TextView t = (TextView)findViewById(R.id.nameView);
        Sudoku sdk = (Sudoku)findViewById(R.id.SudokuGrid);
        sdk.setOnTouchListener(sdk);
        Intent intent = getIntent();
        String name, values;
        if(intent.getIntExtra("isOpen", 0) == 1){
            name = intent.getStringExtra("name");
            t.setText(name);
            values = intent.getStringExtra("val");
            sdk.setData(values.substring(0, values.length() - 1));
        }
        btn.setOnClickListener(new Button.OnClickListener(){
           public void onClick(View v){
               Sudoku sdk = (Sudoku)findViewById(R.id.SudokuGrid);
               sdk.initializeBoard();
           }
        });
        btn2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Sudoku sdk = (Sudoku)findViewById(R.id.SudokuGrid);
                try{
                sdk.solvePuzzle();
                }
                catch(Exception e){}
            }
        });

        btn3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Sudoku nn = (Sudoku) findViewById(R.id.SudokuGrid);
                TextView t = (TextView) findViewById(R.id.nameView);
                DBHelper database = new DBHelper(SudokuActivity.this, "puzzle.db", null, 1);
                SQLiteDatabase db = database.getWritableDatabase();
                ContentValues newdata = new ContentValues();
                newdata.put("type","Sudoku");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formattedDate = df.format(c.getTime());
                if(t.getText().equals("Unsaved puzzle")) {
                    newdata.put("item", formattedDate);
                    t.setText(formattedDate);
                    newdata.put("width", 9);
                    newdata.put("height", 9);
                    String val = nn.getLines();
                    newdata.put("val", val);
                    db.insert("PUZZLES", null, newdata);
                }
                else{
                    newdata.put("width", 9);
                    newdata.put("height", 9);
                    String val = nn.getLines();
                    newdata.put("val", val);
                    db.update("PUZZLES", newdata, "item='" + t.getText() + "'", null );
                }
                Toast.makeText(SudokuActivity.this,"Puzzle successfully saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
;