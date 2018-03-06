package snacker.puzzlesolver;

import android.content.ContentValues;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class NonoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ;

        setContentView(R.layout.activity_nono);
        Intent intent = getIntent();
        Nono sdk = (Nono)findViewById(R.id.NonoView);
        Button btn = (Button)findViewById(R.id.btn_clear);

        EditText e = (EditText)findViewById(R.id.line_input);
        Button btn2 = (Button)findViewById(R.id.btn_solve);
        Button btn3 = (Button)findViewById(R.id.btn_save);

        TextView t = (TextView) findViewById(R.id.nameView);

        String name, values;
        final int width = intent.getIntExtra("width", 5);
        final int height = intent.getIntExtra("height", 5);

        sdk.setOnTouchListener(sdk);
        sdk.initializeBoard(width,height);
        sdk.setEditText(e);

        if(intent.getIntExtra("isOpen", 0) == 1){
            name = intent.getStringExtra("name");
            t.setText(name);
            values = intent.getStringExtra("val");
            String[] arr = values.split("/");
            int m = 0;
            for(int i = 0; i < width; i++){
                sdk.setData(arr[m].substring(0,arr[m].length()-1),i,-1);
                m++;
            }
            for(int j = 0; j < height; j++){
                sdk.setData(arr[m].substring(0,arr[m].length()-1),-1,j);
                m++;
            }
        }

        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Nono sdk = (Nono)findViewById(R.id.NonoView);
                sdk.initializeBoard(sdk.mWidth,sdk.mHeight);
            }
        });
        btn2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
               Nono nn = (Nono)findViewById(R.id.NonoView);
                    nn.solvePuzzle();
            }
        });
        btn3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Nono nn = (Nono)findViewById(R.id.NonoView);
                TextView t = (TextView) findViewById(R.id.nameView);
                DBHelper database = new DBHelper(NonoActivity.this, "puzzle.db", null, 1);
                SQLiteDatabase db = database.getWritableDatabase();
                ContentValues newdata = new ContentValues();
                newdata.put("type","Nonogram");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formattedDate = df.format(c.getTime());
                if(t.getText().equals("Unsaved puzzle")){
                    t.setText(formattedDate);
                    newdata.put("item",formattedDate);
                    newdata.put("width", width);
                    newdata.put("height", height);
                    String val = nn.getLines();
                    newdata.put("val", val);
                    db.insert("PUZZLES", null, newdata);
                }
                else{
                    newdata.put("width", width);
                    newdata.put("height", height);
                    String val = nn.getLines();
                    newdata.put("val", val);
                    db.update("PUZZLES", newdata, "item='" + t.getText() + "'", null );
                }
                Toast.makeText(NonoActivity.this,"Puzzle successfully saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }
}
