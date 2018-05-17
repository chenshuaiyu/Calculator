package com.example.lenovo.calculator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lenovo.calculator.R;
import com.example.lenovo.calculator.activity.HistoryRecordActivity;
import com.example.lenovo.calculator.adapter.RecyclerViewAdapter;
import com.example.lenovo.calculator.bean.Record;

import org.litepal.crud.DataSupport;

import java.util.List;

/**
 * Coder : chenshuaiyu
 * Time : 2018/2/23 11:18
 */

public class HistoryRecordFragment extends Fragment {

    RecyclerView mRecyclerView;
    RecyclerViewAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_history_record,container,false);
        mRecyclerView=view.findViewById(R.id.record_recycler_view);
        if(mAdapter==null)
            mAdapter=new RecyclerViewAdapter((HistoryRecordActivity) getActivity(),init());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        return view;
    }

    public void refreshAdapter() {
        mAdapter.refreshHistoryRecord();
    }

    public void setCheck(boolean b){
        mAdapter.setCheckBoxVisibility(b);
    }

    public void deleteChecked(){
        mAdapter.deleteChecked();
    }

    public static List<Record> init() {
        return DataSupport.findAll(Record.class);
    }

    public int itemCount(){
        if(mAdapter==null)
            mAdapter=new RecyclerViewAdapter((HistoryRecordActivity) getActivity(),init());
        return mAdapter.getItemCount();
    }

}
