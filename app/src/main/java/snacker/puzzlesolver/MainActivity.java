package snacker.puzzlesolver;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        RadioButton rb = (RadioButton) findViewById(R.id.SelectSudoku);
        rb.setChecked(true);

        final RadioGroup rg = (RadioGroup)findViewById(R.id.SelectPuzzle);
        Button b = (Button)findViewById(R.id.button);
        Button c = (Button)findViewById(R.id.button2);

        b.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                int id = rg.getCheckedRadioButtonId();
                RadioButton rb = (RadioButton) findViewById(id);
                switch (rb.getId()){
                    case R.id.SelectSudoku:
                            Intent intent = new Intent(getApplicationContext(), SudokuActivity.class);
                            startActivityForResult(intent, 101);
                            break;
                    case R.id.SelectNono:
                        EditText width = (EditText)findViewById(R.id.WidthText);
                        EditText height = (EditText)findViewById(R.id.HeightText);
                        Intent intent2 = new Intent(getApplicationContext(), NonoActivity.class);
                        intent2.putExtra("isOpen",0);
                        intent2.putExtra("width",Integer.parseInt(width.getText().toString()));
                        intent2.putExtra("height",Integer.parseInt(height.getText().toString()));
                        startActivityForResult(intent2, 102);
                        break;
                    case R.id.SelectKakuro:
                        EditText width2 = (EditText)findViewById(R.id.WidthText);
                        EditText height2 = (EditText)findViewById(R.id.HeightText);
                        Intent intent3 = new Intent(getApplicationContext(), KakuroActivity.class);
                        intent3.putExtra("isOpen",0);
                        intent3.putExtra("width",Integer.parseInt(width2.getText().toString()));
                        intent3.putExtra("height",Integer.parseInt(height2.getText().toString()));
                        startActivityForResult(intent3, 103);
                        break;


                }
            }
        });

        c.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(getApplicationContext(), Puzzlelist.class);
                startActivity(intent);
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_create) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_manage) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void onbutton1clicked(View v){
        Intent intent = new Intent(getApplicationContext(), SudokuActivity.class);

        startActivityForResult(intent, 101);
    }


}
