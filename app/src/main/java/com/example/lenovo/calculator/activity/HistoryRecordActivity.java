package com.example.lenovo.calculator.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.lenovo.calculator.R;
import com.example.lenovo.calculator.activity.base.FragmentContainerActivity;
import com.example.lenovo.calculator.adapter.RecyclerViewAdapter;
import com.example.lenovo.calculator.bean.Record;
import com.example.lenovo.calculator.fragment.ClearRecordFragment;
import com.example.lenovo.calculator.fragment.HistoryRecordFragment;
import com.example.lenovo.calculator.fragment.RecordMenuFragment;

import org.litepal.crud.DataSupport;

public class HistoryRecordActivity extends FragmentContainerActivity implements RecordMenuFragment.Callbacks {

    private boolean checkBoxVisibility=false;

    private HistoryRecordFragment mRecordFragment;

    private ActionBar actionBar;

    private Menu mMenu;

    private boolean batchOperateVisibility=false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        actionBar=getSupportActionBar();
        actionBar.setHomeButtonEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        if(mRecordFragment==null)
            mRecordFragment= (HistoryRecordFragment) getSupportFragmentManager().findFragmentById(getUpFragmentContainerId());
        if(mRecordFragment.itemCount()!=0)
            batchOperateVisibility=true;
        setSubTitle();
    }

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_history_record;
    }

    @Override
    protected int getUpFragmentContainerId() {
        return R.id.history_record;
    }

    @Override
    protected int getDownFragmentContainerId() {
        return R.id.menu;
    }

    @Override
    protected Fragment createUpFragment() {
        mRecordFragment=new HistoryRecordFragment();
        return mRecordFragment;
    }

    @Override
    protected Fragment createDownFragment() {
        return new ClearRecordFragment();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.history_record,menu);
        mMenu=menu;
        if(!batchOperateVisibility)
            mMenu.getItem(0).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.isChecked:
                batchOperate();
                break;
            case android.R.id.home:
                if(checkBoxVisibility)
                    cancel();
                else
                    finish();
                break;

            default:break;
        }
        return true;
    }

    public void batchOperate() {
        if(mRecordFragment==null)
            mRecordFragment= (HistoryRecordFragment) getSupportFragmentManager().findFragmentById(getUpFragmentContainerId());
        mRecordFragment.setCheck(true);
        checkBoxVisibility=true;

        RecyclerViewAdapter.checkedNum=0;
        replaceDownFragment(new RecordMenuFragment());

        setCheckedSubTitle();

        mMenu.getItem(0).setVisible(false);
    }

    @Override
    public void onBackPressed() {
        if(checkBoxVisibility) {
            cancel();
        }else
            finish();
    }

    private void replaceDownFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(R.anim.in_menu,0)
                .replace(getDownFragmentContainerId(),fragment)
                .commit();
    }

    @Override
    public void cancel() {
        checkBoxVisibility=false;
        mRecordFragment.setCheck(false);
        replaceDownFragment(new ClearRecordFragment());
        Record record=new Record();
        record.setIsChecked("false");
        record.updateAll();
        setSubTitle();

        mMenu.getItem(0).setVisible(true);
    }

    @Override
    public void delete() {
        mRecordFragment.deleteChecked();
        cancel();
        if(mRecordFragment.itemCount()==0){
            batchOperateVisibility=false;
            mMenu.getItem(0).setVisible(false);
        }

    }

    @Override
    public void setSubTitle() {
        actionBar.setSubtitle("共有 "+mRecordFragment.itemCount()+" 条记录");
    }

    @Override
    public void setCheckedSubTitle() {
        actionBar.setSubtitle("已选中 "+ RecyclerViewAdapter.checkedNum+" 个");
    }

    public void setZero(){
        RecyclerViewAdapter.checkedNum=0;
        actionBar.setSubtitle("共有 "+0+" 条记录");
        batchOperateVisibility=false;
        mMenu.getItem(0).setVisible(false);
    }
}
