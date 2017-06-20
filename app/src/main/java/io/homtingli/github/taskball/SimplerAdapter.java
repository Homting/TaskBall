package io.homtingli.github.taskball;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Project Name: TaskBall
 * Created By: Jin LI
 * Email:jin.li@vub.be  homtingli@gmail.com
 * By IntelliJ IDEA 2016.3
 * Date: 23/February/2017 Time: 21:13
 */
public class SimplerAdapter extends RecyclerView.Adapter<MyViewHolder>{

    private LayoutInflater layoutInflater;
    private Context context;
    private List<String> mDatas;

    public interface OnItemClickListner{
        void onItemClick(View view, int pos);
        void onItemLongClick(View view, int pos);
    }

    private OnItemClickListner onItemClickListner;

    public void setOnItemClickListner(OnItemClickListner listner){
        this.onItemClickListner = listner;
    }

    public SimplerAdapter(Context context){
        this.context = context;
        this.mDatas = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.item_simple_textview,parent,false);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int pos) {
        holder.tv.setText(mDatas.get(pos));

        if(onItemClickListner!=null) {
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(onItemClickListner!=null) {
                        int position = holder.getLayoutPosition();
                        onItemClickListner.onItemClick(holder.itemView,position);
                        editTextView(context,holder);
                    }
                }
            });

            holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

                @Override
                public boolean onLongClick(View view) {
                    int position = holder.getLayoutPosition();
                    onItemClickListner.onItemLongClick(holder.itemView,position);
                    deleteData(position);
                    return false;
                }
            });
        }
    }

    private void editTextView(Context context, final MyViewHolder holder) {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.prompts,null);

        AlertDialog.Builder a = new AlertDialog.Builder(context);
        a.setView(promptsView);

        final EditText userInput = (EditText) promptsView.findViewById(R.id.editTextDialogUserInput);
        a.setCancelable(false)
                .setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                mDatas.set(holder.getAdapterPosition(),userInput.getText().toString());
                                holder.tv.setText(userInput.getText());
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
        AlertDialog alertDialog = a.create();
        alertDialog.show();
    }


    @Override
    public int getItemCount() {
        return this.mDatas.size();
    }

    public void addData() {
        int pos = mDatas.size();
        if (isAdded()) {
            mDatas.add(Const.HINT);
            notifyItemInserted(pos);
        }
    }

    public boolean isAdded() {
        int pos = mDatas.size();
        return pos == 0 || !mDatas.get(pos - 1).equals(Const.HINT);
    }

    public boolean isRunning() {
        int pos = mDatas.size();
        if (pos!=0 && !mDatas.get(pos-1).equals(Const.HINT)) {
            storeData();
            return true;
        }
        return false;
    }

    public void deleteData(int pos) {
        if (pos>=mDatas.size()) {
            return;
        }
        mDatas.remove(pos);
        notifyItemRemoved(pos);
    }

    public void readData() {
        SharedPreferences sh = context.getSharedPreferences(Const.SHARED,Context.MODE_PRIVATE);
        Map<String,String> map = (Map<String, String>) sh.getAll();
        for(Map.Entry<String,String> mm:map.entrySet()) {
            mDatas.add(mm.getValue());
        }
    }

    public void storeData() {
        SharedPreferences sh = context.getSharedPreferences(Const.SHARED,Context.MODE_PRIVATE);
        sh.edit().clear().apply();
        if (mDatas.size()!=0) {
            SharedPreferences.Editor she  = sh.edit();
            for (int i=0;i<mDatas.size();i++) {
                she.putString(""+i,mDatas.get(i));
            }
            she.apply();
        }
    }
}

class MyViewHolder extends RecyclerView.ViewHolder {

    TextView tv;

    public MyViewHolder(View itemView) {
        super(itemView);
        tv = (TextView) itemView.findViewById(R.id.id_tv);
    }
}
