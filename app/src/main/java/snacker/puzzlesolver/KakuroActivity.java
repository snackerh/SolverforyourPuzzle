package snacker.puzzlesolver;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class KakuroActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ;

        setContentView(R.layout.activity_kakuro);
        Intent intent = getIntent();
        Kakuro sdk = (Kakuro)findViewById(R.id.KakuroView);
        Button btn = (Button)findViewById(R.id.btn_clear);

        EditText e = (EditText)findViewById(R.id.line_input);
        Button btn2 = (Button)findViewById(R.id.btn_solve);
        Button btn3 = (Button)findViewById(R.id.btn_save);
        RadioButton rb1 = (RadioButton)findViewById(R.id.HorButton);
        RadioButton rb2 = (RadioButton)findViewById(R.id.VerButton);
        RadioButton rb3 = (RadioButton)findViewById(R.id.ValButton);
        TextView t = (TextView) findViewById(R.id.nameView);

        String name, values;
        final int width = intent.getIntExtra("width", 8);
        final int height = intent.getIntExtra("height", 8);

        sdk.initializeBoard(height, width);
        sdk.initializeSolve(height, width);
        sdk.requestLayout();
        if(intent.getIntExtra("isOpen", 0) == 1){
            name = intent.getStringExtra("name");
            t.setText(name);
            values = intent.getStringExtra("val");
            int m = 0;
            sdk.setData(values.substring(0, values.length() - 1));
        }
        //sdk.invalidate();
        rb1.setChecked(true);
        sdk.setOnTouchListener(sdk);
        sdk.setEditText(e);
        sdk.setHorRadioButton(rb1);
        sdk.setVerRadioButton(rb2);
        sdk.setValRadioButton(rb3);




        btn.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Kakuro sdk = (Kakuro)findViewById(R.id.KakuroView);
                sdk.initializeBoard(sdk.mHeight,sdk.mWidth);
            }
        });
        btn2.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Kakuro sdk = (Kakuro)findViewById(R.id.KakuroView);
                try{
                    sdk.solvePuzzle();
                }
                catch(Exception e){}
            }
        });
        btn3.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                Kakuro sdk = (Kakuro)findViewById(R.id.KakuroView);
                TextView t = (TextView) findViewById(R.id.nameView);
                DBHelper database = new DBHelper(KakuroActivity.this, "puzzle.db", null, 1);
                SQLiteDatabase db = database.getWritableDatabase();
                ContentValues newdata = new ContentValues();
                newdata.put("type","Kakuro");
                Calendar c = Calendar.getInstance();
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
                String formattedDate = df.format(c.getTime());
                if(t.getText().equals("Unsaved puzzle")){
                    t.setText(formattedDate);
                    newdata.put("item",formattedDate);
                    newdata.put("width", width);
                    newdata.put("height", height);
                    String val = sdk.getLines();
                    newdata.put("val", val);
                    db.insert("PUZZLES", null, newdata);
                }
                else{
                    newdata.put("width", width);
                    newdata.put("height", height);
                    String val = sdk.getLines();
                    newdata.put("val", val);
                    db.update("PUZZLES", newdata, "item='" + t.getText() + "'", null );
                }
                Toast.makeText(KakuroActivity.this,"Puzzle successfully saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

}
;