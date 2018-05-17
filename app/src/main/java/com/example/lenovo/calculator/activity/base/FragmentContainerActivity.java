package com.example.lenovo.calculator.activity.base;

import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Coder : chenshuaiyu
 * Time : 2018/1/21 13:54
 */

public abstract class FragmentContainerActivity extends AppCompatActivity {

    @LayoutRes
    protected abstract int getLayoutResId();

    @IdRes
    protected abstract int getUpFragmentContainerId();

    @IdRes
    protected abstract int getDownFragmentContainerId();

    protected abstract android.support.v4.app.Fragment createUpFragment();

    protected abstract android.support.v4.app.Fragment createDownFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());

        android.support.v4.app.FragmentManager fm=getSupportFragmentManager();

        android.support.v4.app.Fragment recordFragment=fm.findFragmentById(getUpFragmentContainerId());
        if(null==recordFragment) {
            recordFragment=createUpFragment();
            fm.beginTransaction()
                    .add(getUpFragmentContainerId(),recordFragment)
                    .commit();
        }

        android.support.v4.app.Fragment menuFragment=fm.findFragmentById(getDownFragmentContainerId());
        if(null==menuFragment) {
            menuFragment=createDownFragment();
            fm.beginTransaction()
                    .add(getDownFragmentContainerId(),menuFragment)
                    .commit();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

    }
}
