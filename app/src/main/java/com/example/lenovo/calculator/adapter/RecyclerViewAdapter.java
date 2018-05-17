package com.example.lenovo.calculator.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lenovo.calculator.R;
import com.example.lenovo.calculator.activity.HistoryRecordActivity;
import com.example.lenovo.calculator.bean.Record;
import com.example.lenovo.calculator.fragment.HistoryRecordFragment;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

/**
 * Coder : chenshuaiyu
 * Time : 2018/2/23 11:22
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    public static int checkedNum=0;

    public boolean isChecked=false;

    private List<Record> mRecords=new ArrayList<>();

    ViewHolder mViewHolder;

    private HistoryRecordActivity mContext;

    public RecyclerViewAdapter(HistoryRecordActivity context, List<Record> records) {
        mRecords = records;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.item_record,parent,false);
        mViewHolder=new ViewHolder(view);
        return mViewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bindViewHolder(holder,mRecords.get(position));
    }

    @Override
    public int getItemCount() {
        return mRecords.size();
    }

    public void refreshHistoryRecord(){
        mRecords.clear();
        notifyDataSetChanged();
    }

    public void setCheckBoxVisibility(boolean b){
        isChecked=b;
        for (int i = 0; i < mRecords.size(); i++) {
            onBindViewHolder(mViewHolder,i);
        }
        notifyDataSetChanged();
    }

    public void deleteChecked(){
        DataSupport.deleteAll(Record.class,"isChecked=?","true");
        mRecords=DataSupport.findAll(Record.class);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView expressionTextView;
        TextView timeTextView;
        TextView answerTextView;
        CheckBox mCheckBox;

        public ViewHolder(View itemView) {
            super(itemView);
            expressionTextView=itemView.findViewById(R.id.expression_text_view);
            timeTextView=itemView.findViewById(R.id.time_text_view);
            answerTextView=itemView.findViewById(R.id.answer_text_view);
            mCheckBox= itemView.findViewById(R.id.checkbox);
        }
        public void bindViewHolder(final ViewHolder holder, final Record record){
            expressionTextView.setText(record.getExpression());
            timeTextView.setText(record.getTime());
            answerTextView.setText(record.getAnswer());
            if(isChecked){
                mCheckBox.setVisibility(View.VISIBLE);
                mCheckBox.setChecked(Boolean.parseBoolean(record.getIsChecked()));
                setCheckedItemView(holder);
            }else{
                mCheckBox.setVisibility(View.GONE);
                setNotCheckedItemView(holder);
            }
            setCheckBoxCheckedChange(holder, record);
        }

        private void setCheckBoxCheckedChange(ViewHolder holder, final Record record) {
            holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    Record record1=new Record();
                    if(isChecked){
                        record1.setIsChecked("true");
                        record1.updateAll("time=?",record.getTime());
                        checkedNum++;
//                        Log.d("time", isChecked+" true");
                    }else{
                        record1.setIsChecked("false");
                        record1.updateAll("time=?",record.getTime());
                        if(checkedNum!=0)
                            checkedNum--;
//                        Log.d("time", isChecked+" false");
                    }
                    if(mContext==null)
                        mContext= (HistoryRecordActivity) buttonView.getContext();
                    mContext.setCheckedSubTitle();
                }
            });
        }

        private void setNotCheckedItemView(final ViewHolder holder) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("time", "no");
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    if(mContext==null)
                        mContext= (HistoryRecordActivity) v.getContext();
                    mContext.batchOperate();

                    holder.mCheckBox.setChecked(true);

                    return true;
                }
            });
        }

        private void setCheckedItemView(final ViewHolder holder) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(holder.mCheckBox.isChecked()){
//                        Log.d("time", holder.mCheckBox.isChecked()+"");
                        holder.mCheckBox.setChecked(false);
                    }else{
//                        Log.d("time", holder.mCheckBox.isChecked()+"");
                        holder.mCheckBox.setChecked(true);
                    }
                }
            });
            itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    return false;
                }
            });
        }
    }

}
