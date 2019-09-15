package com.example.lenovo.calculator.fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.lenovo.calculator.R;
import com.example.lenovo.calculator.activity.HistoryRecordActivity;
import com.example.lenovo.calculator.activity.MainActivity;
import com.example.lenovo.calculator.bean.Record;

import org.litepal.crud.DataSupport;

/**
 * Coder : chenshuaiyu
 * Time : 2018/2/27 13:47
 */

public class ClearRecordFragment extends Fragment {

    Button clearRecordButton;

    HistoryRecordFragment mRecordFragment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clear_record, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        clearRecordButton = view.findViewById(R.id.clear_record);
        clearRecordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final HistoryRecordActivity context = (HistoryRecordActivity) v.getContext();
                AlertDialog alertDialog = new AlertDialog.Builder(getActivity())
                        .setTitle("确认删除所有记录?")
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        })
                        .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                DataSupport.deleteAll(Record.class);
                                if (mRecordFragment == null) {
                                    mRecordFragment = (HistoryRecordFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.history_record);
                                }
                                mRecordFragment.refreshAdapter();
                                context.setZero();
                                clearRecordButton.setEnabled(false);
                            }
                        })
                        .show();
            }
        });
        if (mRecordFragment == null) {
            mRecordFragment = (HistoryRecordFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.history_record);
        }
        if (mRecordFragment.itemCount() == 0) {
            clearRecordButton.setEnabled(false);
        } else {
            clearRecordButton.setEnabled(true);
        }
    }
}
