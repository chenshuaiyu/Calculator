package com.example.lenovo.calculator.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lenovo.calculator.R;
import com.example.lenovo.calculator.activity.HistoryRecordActivity;

/**
 * Coder : chenshuaiyu
 * Time : 2018/2/27 13:37
 */

public class RecordMenuFragment extends Fragment {

    private HistoryRecordFragment historyRecordFragment;

    public interface Callbacks{
        void cancel();
        void delete();
        void setSubTitle();
        void setCheckedSubTitle();
    }

    private Callbacks mCallbacks;

    ImageView deleteImageView;
    TextView deleteTextView;
    ImageView cancelImageView;
    TextView cancelTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.fragment_record_menu,container,false);
        mCallbacks= (Callbacks) getActivity();
        historyRecordFragment= (HistoryRecordFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.history_record);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        deleteImageView=view.findViewById(R.id.delete_image_view);
        cancelImageView=view.findViewById(R.id.cancel_image_view);
        deleteTextView=view.findViewById(R.id.delete_text_view);
        cancelTextView=view.findViewById(R.id.cancel_text_view);
        deleteImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.delete();
                mCallbacks.setSubTitle();
            }
        });
        deleteTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.delete();
                mCallbacks.setSubTitle();
            }
        });
        cancelImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.cancel();
            }
        });
        cancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCallbacks.cancel();
            }
        });

    }
}
