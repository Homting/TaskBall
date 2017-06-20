package io.homtingli.github.taskball;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.widget.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MySecondActivity extends Activity {
    /**
     * Called when the activity is first created.
     */
    private TextView tv = null;
    private ListView lv = null;
    private LinearLayout l1;
    private Button reset;
    private List<String> name = new ArrayList<String>();
    private List<Boolean> check = new ArrayList<Boolean>();

    private ArrayList<String> listStr = null;
    private List<HashMap<String, Object>> list = null;
    private MyAdapter adapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//no titlebar
        setContentView(R.layout.second);

        SharedPreferences sh = getSharedPreferences(Const.SHARED,MODE_PRIVATE);

        reset = (Button) findViewById(R.id.reset);
        reset.setVisibility(View.INVISIBLE);

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Const.finishnumber=0;
                Const.totalnumber = 0;
                getSharedPreferences(Const.SHARED,MODE_PRIVATE).edit().clear().apply();
                //sh.edit().clear().apply();
                finish();
            }
        });

        Map<String,String> m = (Map<String, String>) sh.getAll();
        for(Map.Entry<String,String> mm:m.entrySet()) {
            Log.e(mm.getKey(),mm.getValue());
            name.add(mm.getValue());
            check.add(false);
        }

        tv = (TextView) this.findViewById(R.id.tv);
        lv = (ListView) this.findViewById(R.id.lv);
        l1 = (LinearLayout) findViewById(R.id.l1);

        l1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                moveTaskToBack(false);
            }
        });
        showCheckBoxListView();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode== KeyEvent.KEYCODE_BACK){
            moveTaskToBack(false);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //listview with checkbox
    private void showCheckBoxListView() {
        list = new ArrayList<HashMap<String, Object>>();
        int i;
        for(i = 0; i < name.size(); i++) {

            HashMap<String, Object> map = new HashMap<String, Object>();
            map.put("item_tv", name.get(i));
            map.put("item_cb", check.get(i));
            list.add(map);

            adapter = new MyAdapter(this, list, R.layout.listviewitem,
                    new String[] { "item_tv", "item_cb" }, new int[] {
                    R.id.item_tv, R.id.item_cb });
            lv.setAdapter(adapter);
            listStr = new ArrayList<String>();
            final int finalI = i;
            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
                    ViewHolder holder = (ViewHolder) view.getTag();

                    holder.cb.toggle();// toggle status when click
                    check.set(finalI,holder.cb.isChecked());
                    MyAdapter.isSelected.put(position, holder.cb.isChecked()); // change map's value
                    if (holder.cb.isChecked()) {
                        listStr.add(name.get(position));// name[position]);
                        holder.tv.setPaintFlags(tv.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                        Const.finishnumber++;
                    } else {
                        listStr.remove(name.get(position));
                        holder.tv.setPaintFlags(tv.getPaintFlags() & (~Paint.STRIKE_THRU_TEXT_FLAG));
                        Const.finishnumber--;
                    }
                    if(Const.finishnumber == Const.totalnumber) {
                        reset.setVisibility(View.VISIBLE);
                    }
                    tv.setText("You finished "+listStr.size()+" items");
                }
            });
        }
    }
}
