package io.homtingli.github.taskball;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private SimplerAdapter adapter;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //new adapter to read data from view Holder
        adapter = new SimplerAdapter(this);
        adapter.readData();

        recyclerView = (RecyclerView) findViewById(R.id.recycleview);
        recyclerView.setAdapter(adapter);

        //set Recycleview layout
        LinearLayoutManager llm = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(llm);

        //set Dividing line
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST);
        recyclerView.addItemDecoration(dividerItemDecoration);

        recyclerView.setItemAnimator(new DefaultItemAnimator());

        //add onclick listener
        adapter.setOnItemClickListner(new SimplerAdapter.OnItemClickListner() {
            @Override
            public void onItemClick(View view, int pos) {
                if (Const.DEBUG) {
                    Toast.makeText(MainActivity.this, "click:" + pos, Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onItemLongClick(View view, int pos) {
                if (Const.DEBUG) {
                    Toast.makeText(MainActivity.this, "long click:" + pos, Toast.LENGTH_SHORT).show();
                }
            }
        });

        //this is for right bottom add button
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!adapter.isAdded()) {
                    Snackbar.make(view, Const.HINT, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                } else {
                    adapter.addData();
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; add items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_start:
                if (!adapter.isRunning()) {
                    Snackbar.make(getCurrentFocus(), Const.HINT, Snackbar.LENGTH_SHORT)
                            .setAction("Action", null).show();
                }
                else {
                    startService();
                }
                break;
            case R.id.action_author:
                Dialogue("Author","I know",R.string.author);
                break;
            case R.id.action_tips:
                Dialogue("Tips","Ok",R.string.string_tips);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void Dialogue(String title,String btn, int message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setCancelable(false);
        builder.setMessage(message);
        builder.setTitle(title);
        builder.setPositiveButton(btn, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create().show();
    }

    @Override
    protected void onPause() {
        super.onPause();
        adapter.storeData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        adapter.storeData();
    }

    //start the service
    //hide the main view and start the ball
    private void startService() {
        intent = new Intent(MainActivity.this, MyBallService.class);
        SharedPreferences pref = getSharedPreferences(Const.SHARED,MODE_PRIVATE);
        Const.totalnumber = pref.getAll().size();
        Log.e("total number",Const.totalnumber+"");
        startService(intent);
        finish();
    }
}