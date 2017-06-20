package io.homtingli.github.taskball;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;

/**
 * Project Name: ListViewWithCheckBox
 * Created By: LiJin
 * Email:594268218@qq.com  homtingli@gmail.com
 * By IntelliJ IDEA 15
 * Date: 06/September/2016 Time: 23:15
 */
public class MyAdapter extends BaseAdapter {
    public static HashMap<Integer, Boolean> isSelected;
    private Context context;
    private LayoutInflater inflater = null;
    private List<HashMap<String, Object>> list = null;
    private String keyString[] = null;
    private String itemString = null; // to record textview's value
    private int idValue[] = null;// id value

    public MyAdapter(Context context, List<HashMap<String, Object>> list,
                     int resource, String[] from, int[] to) {
        this.context = context;
        this.list = list;
        keyString = new String[from.length];
        idValue = new int[to.length];
        System.arraycopy(from, 0, keyString, 0, from.length);
        System.arraycopy(to, 0, idValue, 0, to.length);
        inflater = LayoutInflater.from(context);
        init();
    }

    // init, all checkbox status: unselect
    private void init() {
        isSelected = new HashMap<Integer, Boolean>();
        for (int i = 0; i < list.size(); i++) {
            isSelected.put(i, false);
        }
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int arg0) {
        return list.get(arg0);
    }

    @Override
    public long getItemId(int arg0) {
        return 0;

    }

    @Override
    public View getView(int position, View view, ViewGroup arg2) {
        ViewHolder holder = null;
        if (holder==null) {
            holder = new ViewHolder();
            if (view == null) {
                view = inflater.inflate(R.layout.listviewitem, null);
            }

            holder.tv = (TextView) view.findViewById(R.id.item_tv);
            holder.cb = (CheckBox) view.findViewById(R.id.item_cb);
            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }
        HashMap<String, Object> map = list.get(position);
        if (map != null) {
            itemString = (String) map.get(keyString[0]);
            holder.tv.setText(itemString);
        }
        holder.cb.setChecked(isSelected.get(position));
        return view;
    }
}
