package com.example.lenovo.calculator.bean;

import org.litepal.crud.DataSupport;

/**
 * Coder : chenshuaiyu
 * Time : 2018/2/23 11:30
 */

public class Record extends DataSupport {

    private String time;
    private String expression;
    private String answer;
    private String isChecked;

    public Record() {
    }

    public Record(String time, String expression, String answer) {
        this.time = time;
        this.expression = expression;
        this.answer = answer;
    }

    public String getIsChecked() {
        return isChecked;
    }

    public void setIsChecked(String isChecked) {
        this.isChecked = isChecked;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getExpression() {
        return expression;
    }

    public void setExpression(String expression) {
        this.expression = expression;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
