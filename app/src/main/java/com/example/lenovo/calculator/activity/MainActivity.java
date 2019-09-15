package com.example.lenovo.calculator.activity;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lenovo.calculator.R;
import com.example.lenovo.calculator.bean.Record;
import com.example.lenovo.calculator.fragment.NumFragment;
import com.example.lenovo.calculator.fragment.OperatorFragment;
import com.example.lenovo.calculator.utils.Utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author : chenshuaiyu
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText screen_text_view;
    private boolean nextCalculation = false;
    private boolean isMore = false;
    private ImageButton button_more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        button_more = findViewById(R.id.button_more);
        screen_text_view = findViewById(R.id.screen_text_view);


        if (button_more != null) {
            addFragment(new NumFragment());
        }

        if (savedInstanceState == null) {
            screen_text_view.setText("");
        } else {
            screen_text_view.setText(savedInstanceState.getString("data_text"));
            screen_text_view.setSelection(Integer.parseInt(savedInstanceState.getString("data_selection", "0")));
        }
        changeTextSize(50);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (!TextUtils.isEmpty(screen_text_view.getText())) {
            outState.putString("data_text", screen_text_view.getText().toString());
            outState.putString("data_selection", screen_text_view.getSelectionStart() + "");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.history_record:
                startActivity(new Intent(this, HistoryRecordActivity.class));
                break;
            default:
                break;
        }
        return true;
    }

    private void addFragment(Fragment fragment) {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.frame_layout, fragment)
                .commit();
    }

    private void replaceFragment(Fragment fragment) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        if (fragment instanceof OperatorFragment) {
            fragmentTransaction.setCustomAnimations(R.anim.in_operator, R.anim.out_number);
        } else if (fragment instanceof NumFragment) {
            fragmentTransaction.setCustomAnimations(R.anim.in_number, R.anim.out_operator);
        }
        fragmentTransaction.replace(R.id.frame_layout, fragment)
                .commit();
    }

    @Override
    public void onClick(View v) {
        int offset = 0;
        String text = "";
        int len = 0;
        switch (v.getId()) {
            case R.id.button_0:
            case R.id.button_1:
            case R.id.button_2:
            case R.id.button_3:
            case R.id.button_4:
            case R.id.button_5:
            case R.id.button_6:
            case R.id.button_7:
            case R.id.button_8:
            case R.id.button_9:
                offset = 0;
                adjustTextSize(1);
                text = Utils.seekText(v.getId());
                if (nextCalculation) {
                    screen_text_view.setText(text);
                    nextCalculation = false;
                } else {
                    offset = screen_text_view.getSelectionStart();
                    screen_text_view.setText(new StringBuilder(screen_text_view.getText()).insert(offset, text));
                }
                setSelection(offset + 1);
                break;
            case R.id.button_jia:
            case R.id.button_jian:
            case R.id.button_cheng:
            case R.id.button_chu:
            case R.id.button_jiecheng:
            case R.id.button_pingfang:
            case R.id.button_ci:
                adjustTextSize(1);
                text = Utils.seekText(v.getId());
                offset = screen_text_view.getSelectionStart();
                len = screen_text_view.getText().length();
                if (nextCalculation) {
                    screen_text_view.setText(screen_text_view.getText() + text);
                    setSelection(offset + text.length());
                    nextCalculation = false;
                } else {
                    if (screen_text_view.getText().toString().isEmpty()) {
                        screen_text_view.setText(text);
                        setSelection(offset + text.length());
                    } else {
                        if (offset == 0) {
                            screen_text_view.setText(new StringBuilder(screen_text_view.getText()).insert(offset, text));
                            setSelection(offset + text.length());
                        } else {
                            char c = screen_text_view.getText().charAt(offset - 1);
                            if (Utils.isOperator(c) || c == 's' || c == 'n') {
                                if (Utils.isOperator(c)) {
                                    if (c == 's') {
                                        String left = new String(screen_text_view.getText().toString().toCharArray(), 0, offset - 3);
                                        String right = new String(screen_text_view.getText().toString().toCharArray(), offset, len - offset);
                                        screen_text_view.setText(left + text + right);
                                        setSelection(offset - 2);
                                    } else if (c == '²' || c == '!') {
                                        screen_text_view.setText(new StringBuilder(screen_text_view.getText()).insert(offset, text));
                                        setSelection(offset + text.length());
                                    } else if ((text.equals("-") && c == '(') || (c == ')')) {
                                        screen_text_view.setText(new StringBuilder(screen_text_view.getText()).insert(offset, text));
                                        setSelection(offset + text.length());
                                    } else {
                                        String left = new String(screen_text_view.getText().toString().toCharArray(), 0, offset - 1);
                                        String right = new String(screen_text_view.getText().toString().toCharArray(), offset, len - offset);
                                        screen_text_view.setText(left + text + right);
                                        setSelection(offset);
                                    }
                                } else {
                                    if (screen_text_view.getText().charAt(offset - 2) == 'l') {
                                        String left = new String(screen_text_view.getText().toString().toCharArray(), 0, offset - 2);
                                        String right = new String(screen_text_view.getText().toString().toCharArray(), offset, len - offset);
                                        screen_text_view.setText(left + text + right);
                                        setSelection(offset - 1);
                                    } else {
                                        String left = new String(screen_text_view.getText().toString().toCharArray(), 0, offset - 3);
                                        String right = new String(screen_text_view.getText().toString().toCharArray(), offset, len - offset);
                                        screen_text_view.setText(left + text + right);
                                        setSelection(offset - 2);
                                    }
                                }
                            } else {
                                screen_text_view.setText(new StringBuilder(screen_text_view.getText()).insert(offset, text));
                                setSelection(offset + text.length());
                            }
                        }
                    }
                }
                break;
            case R.id.button_genhao:
            case R.id.button_sin:
            case R.id.button_cos:
            case R.id.button_tan:
                adjustTextSize(1);
                text = Utils.seekText(v.getId());
                offset = screen_text_view.getSelectionStart();
                len = screen_text_view.getText().length();
                if (nextCalculation) {
                    screen_text_view.setText(text);
                    setSelection(text.length());
                    nextCalculation = false;
                } else {
                    screen_text_view.setText(new StringBuilder(screen_text_view.getText()).insert(offset, text));
                    setSelection(offset + text.length());
                }
                break;
            case R.id.button_left_bracket:
            case R.id.button_right_bracket:
            case R.id.button_π:
            case R.id.button_point:
            case R.id.button_ln:
                adjustTextSize(1);
                text = Utils.seekText(v.getId());
                offset = screen_text_view.getSelectionStart();
                if (nextCalculation) {
                    screen_text_view.setText(text);
                    setSelection(text.length());
                    nextCalculation = false;
                } else {
                    if ((!text.equals(".")) || (text.equals(".") && screen_text_view.getText().length() >= 1 && (screen_text_view.getText().charAt(screen_text_view.getText().length() - 1) != '.'))) {
                        screen_text_view.setText(new StringBuilder(screen_text_view.getText()).insert(offset, text));
                        setSelection(offset + text.length());
                    }
                }
                break;
            case R.id.button_zhengfuhao:
                changePlusOrMinus();
                setSelection(screen_text_view.getText().toString().length());
                break;
            case R.id.button_clean:
                screen_text_view.setText("");
                break;
            case R.id.button_delete:
                if (nextCalculation) {
                    screen_text_view.setText("");
                    nextCalculation = false;
                } else {
                    adjustTextSize(-1);
                    String s = screen_text_view.getText().toString();
                    int offset2 = screen_text_view.getSelectionStart();
                    if (!screen_text_view.getText().toString().equals("") && offset2 != 0) {
                        if (s.charAt(offset2 - 1) == 'n') {
                            if (s.charAt(offset2 - 2) == 'i' || s.charAt(offset2 - 2) == 'o' || s.charAt(offset2 - 2) == 'a') {
                                String pre = s.substring(0, offset2 - 3);
                                String next = s.substring(offset2, s.length());
                                screen_text_view.setText(pre + next);
                                setSelection(offset2 - 3);
                            } else if (s.charAt(offset2 - 2) == 'l') {
                                String pre = s.substring(0, offset2 - 2);
                                String next = s.substring(offset2, s.length());
                                screen_text_view.setText(pre + next);
                                setSelection(offset2 - 2);
                            }
                        } else if (s.charAt(offset2 - 1) == 's') {
                            if (s.charAt(offset2 - 2) == 'o') {
                                String pre = s.substring(0, offset2 - 3);
                                String next = s.substring(offset2, s.length());
                                screen_text_view.setText(pre + next);
                                setSelection(offset2 - 3);
                            }
                        } else {
                            String str = "";
                            char[] chars = screen_text_view.getText().toString().toCharArray();
                            for (int i = 0; i < chars.length; i++) {
                                if (i != offset2 - 1) {
                                    str += chars[i];
                                }
                            }
                            screen_text_view.setText(str);
                            setSelection(offset2 - 1);
                        }
                    }
                }
                break;
            case R.id.button_dengyu:
//                screen_text_view.setText(Utils.transformPostfixRxpression(screen_text_view.getText().toString()));
//                Toast.makeText(this,Utils.transformPostfixRxpression(screen_text_view.getText().toString()) , Toast.LENGTH_LONG).show();
                if (!screen_text_view.getText().toString().isEmpty()) {
                    double ans = 0;
                    String expression = "";
                    Record record = new Record();
                    record.setIsChecked("false");
                    try {
                        record.setTime(getSystemTime());
                        expression = screen_text_view.getText().toString();
                        record.setExpression(expression);
                        ans = Utils.calculate(Utils.transformPostfixRxpression(expression));
                        record.setAnswer(judgeAfterPoint(ans) + "");
                        record.save();
                        screen_text_view.setText(judgeAfterPoint(ans));
                    } catch (Exception e) {
                        Toast.makeText(this, "错误", Toast.LENGTH_SHORT).show();
                        record.setAnswer("错误");
                        record.save();
                    }
                } else {
                    screen_text_view.setText("0");
                }
                setSelection(screen_text_view.getText().toString().length());
                screen_text_view.setTextSize(50);
                nextCalculation = true;
                break;
            case R.id.button_more:
                button_more = findViewById(R.id.button_more);
                if (!isMore) {
                    isMore = true;
                    button_more.setImageResource(R.drawable.ic_up);
                    replaceFragment(new OperatorFragment());

                } else {
                    isMore = false;
                    button_more.setImageResource(R.drawable.ic_down);
                    replaceFragment(new NumFragment());
                }
                break;
        }
    }

    private String getSystemTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(d);
    }

    private String judgeAfterPoint(double ans) {
        if (ans - (int) ans == 0.0) {
            ans = (int) ans;
        }
        String s = ans + "";
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", ""); //去掉多余的0
            s = s.replaceAll("[.]$", ""); //如最后一位是.则去掉
        }
        return s;
    }

    private void setSelection(int index) {
        screen_text_view.setSelection(index);
    }

    private void changePlusOrMinus() {
        String text = screen_text_view.getText().toString();
        char[] array = text.toCharArray();
        if (array.length != 0) {
            if (array[0] == '-') {
                screen_text_view.setText(array, 1, screen_text_view.getText().length() - 1);
            } else {
                screen_text_view.setText("-" + screen_text_view.getText().toString());
            }
        } else {
            screen_text_view.setText("-" + screen_text_view.getText().toString());
        }
        changeTextSize(50);
    }

    private void adjustTextSize(int flag) {
        int textLength = screen_text_view.getText().toString().length() + flag;
        if (textLength < 43) {
            changeTextSize(50);
        } else {
            changeTextSize(35);
        }
    }

    private void changeTextSize(float size) {
        screen_text_view.setTextSize(size);
    }
}
