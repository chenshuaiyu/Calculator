package com.example.lenovo.calculator.utils;

import android.util.Log;
import android.widget.Toast;

import com.example.lenovo.calculator.R;

import java.util.HashMap;
import java.util.Map;

/**
 * Coder : chenshuaiyu
 * Time : 2018/2/15 18:29
 */

public class Utils {

    static char[] stack=new char[100];
    static int top=0;
    static String[] numStack=new String[1000];
    static int numTop=0;

    static Map<Integer,String> textMap=new HashMap<>();
    static Map<String,Integer> priorityMap=new HashMap<>();


    private static void initTextMap(){
        textMap.put(R.id.button_0,"0");
        textMap.put(R.id.button_1,"1");
        textMap.put(R.id.button_2,"2");
        textMap.put(R.id.button_3,"3");
        textMap.put(R.id.button_4,"4");
        textMap.put(R.id.button_5,"5");
        textMap.put(R.id.button_6,"6");
        textMap.put(R.id.button_7,"7");
        textMap.put(R.id.button_8,"8");
        textMap.put(R.id.button_9,"9");
        textMap.put(R.id.button_jia,"+");
        textMap.put(R.id.button_jian,"-");
        textMap.put(R.id.button_cheng,"*");
        textMap.put(R.id.button_chu,"/");
        textMap.put(R.id.button_point,".");
        textMap.put(R.id.button_π,"π");
        textMap.put(R.id.button_left_bracket,"(");
        textMap.put(R.id.button_right_bracket,")");
        textMap.put(R.id.button_jiecheng,"!");
        textMap.put(R.id.button_sin,"sin");
        textMap.put(R.id.button_cos,"cos");
        textMap.put(R.id.button_tan,"tan");
        textMap.put(R.id.button_ln,"ln");
        textMap.put(R.id.button_pingfang,"²");
        textMap.put(R.id.button_genhao,"√");
        textMap.put(R.id.button_ci,"^");
    }

    private static void initPriorityMap() {
        priorityMap.put("+",1);
        priorityMap.put("-",1);
        priorityMap.put("*",2);
        priorityMap.put("/",2);
        priorityMap.put("(",0);
        priorityMap.put("!",3);
        priorityMap.put("s",3);
        priorityMap.put("c",3);
        priorityMap.put("t",3);
        priorityMap.put("l",3);
        priorityMap.put("²",3);
        priorityMap.put("√",3);
        priorityMap.put("^",3);
    }

    public static String seekText(int id){
        initTextMap();
        return (String)textMap.get(id);
    }

    public static int seekPriority(String text){
        initPriorityMap();
        return priorityMap.get(text).intValue();
    }

    public static double jia(double a,double b){
        return a+b;
    }
    public static double jian(double a,double b){
        return a-b;
    }
    public static double cheng(double a,double b){
        return a*b;
    }
    public static double chu(double a,double b) throws Exception {
        if(b==0)
            throw new Exception();
        return a/b;
    }
    public static long jiecheng(int a) throws Exception {
        if(a<0)
            throw new Exception();
        if(a==1 || a==0)
            return 1;
        return a*jiecheng(a-1);
    }
    public static double pingfang(double a){
        return a*a;
    }
    public static double genhao(double a){
        return Math.sqrt(a);
    }
    public static double sin(double a){
        return Math.sin(a);
    }
    public static double cos(double a){
        return Math.cos(a);
    }
    public static double tan(double a){
        return Math.tan(a);
    }
    public static double ln(double a){
        return Math.log(a);
    }
    public static double pow(double a,double b){
        return Math.pow(a,b);
    }

    public static boolean isOperator(char operator){
        switch (operator){
            case '(':
            case ')':
            case '+':
            case '-':
            case '*':
            case '/':
            case 's':
            case 'c':
            case 't':
            case '²':
            case '!':
            case 'l':
            case '√':
            case '^':
                return true;
            default:
                return false;
        }
    }

    public static String transformPostfixRxpression(String expression){
        String postfixExpression="";
        char[] array = expression.toCharArray();
        for(int i=0;i<array.length;i++) {
            if (array[i] >= '0' && array[i] <= '9' || array[i]=='π') {
                if (i-1>=0 && isOperator(array[i-1]) && postfixExpression.toCharArray().length-1>=0 && postfixExpression.toCharArray()[postfixExpression.toCharArray().length-1]!=' ')
                    postfixExpression += " ";
                postfixExpression += array[i];
            }else if (array[i] == '.')
                postfixExpression += array[i];
            else if (array[i] == '('){
                if(i-1>=0 && ((array[i-1]>='0' && array[i-1]<='9') || array[i-1]=='.'))
                    stack[top++] = '*';
                stack[top++] = array[i];
            }else if (array[i] == ')') {
                while (top-1>=0 && stack[top-1] != '(') {
                    postfixExpression+=" ";
                    postfixExpression += stack[top-1];
                    top--;
                }
                top--;
            }else if (isOperator(array[i])){
                if(postfixExpression.toCharArray().length-1>=0 && postfixExpression.charAt(postfixExpression.toCharArray().length-1)!=' ')
                    postfixExpression+=" ";
                if (top != 0) {
                    if(array[i]=='^'){
                        if(!(stack[top-1]=='^')){
                            if (seekPriority(array[i] + "") <= seekPriority(stack[top - 1]+ "")) {
                                postfixExpression += stack[top-1];
                                top--;
                                while (top-1>=0 && seekPriority(array[i] + "") <= seekPriority(stack[top - 1]+ "")) {
                                    postfixExpression += " "+stack[top-1];
                                    top--;
                                }
                            }
                        }
                    }else if(array[i]=='√'){
                        if(!(stack[top-1]=='√')){
                            if (seekPriority(array[i] + "") <= seekPriority(stack[top - 1]+ "")) {
                                postfixExpression += stack[top-1];
                                top--;
                                while (top-1>=0 && seekPriority(array[i] + "") <= seekPriority(stack[top - 1]+ "")) {
                                    postfixExpression += " "+stack[top-1];
                                    top--;
                                }
                            }
                        }
                    }else if (seekPriority(array[i] + "") <= seekPriority(stack[top - 1]+ "")) {
                        postfixExpression += stack[top-1];
                        top--;
                        while (top-1>=0 && seekPriority(array[i] + "") <= seekPriority(stack[top - 1]+ "")) {
                            postfixExpression += " "+stack[top-1];
                            top--;
                        }
                    }
                }
                if(i-1>=0 && ((array[i-1]>='0' && array[i-1]<='9') || array[i-1]=='.') && (array[i]=='s' || array[i]=='c' || array[i]=='t' || array[i]=='l' || array[i]=='√')){
                    stack[top++]='*';
                }
                stack[top++] = array[i];
                if(array[i]=='s' || array[i]=='c' || array[i]=='t')
                    i+=2;
                else if(array[i]=='l')
                    i+=1;
            }
        }
        if(top-1>=0 && postfixExpression.charAt(postfixExpression.length()-1)==' '){
            postfixExpression+=stack[top-1];
            top--;
        }
        while(top-1>=0){
            postfixExpression+=" "+stack[top-1];
            top--;
        }
        return postfixExpression;
    }

    public static double calculate(String postfixExpression)throws Exception{
        numTop=0;
        if(postfixExpression.contains("π")){
            postfixExpression.replace("π",Math.PI+"");
        }
        String[] strings = postfixExpression.split(" ");
        for (int i = 0; i < strings.length; i++) {
            if(strings[i].equals("π"))
                strings[i]=""+Math.PI;
        }
        for (int i = 0; i < strings.length; i++) {
            switch (strings[i]){
                case " ":break;
                case "+":
                case "-":
                case "*":
                case "/":
                case "s":
                case "c":
                case "t":
                case "!":
                case "l":
                case "√":
                case "^":
                case "²":
                    singleCalculate(strings[i]);
                    break;
                default:
                    numStack[numTop++]=strings[i];
                    break;
            }
        }
        return Double.parseDouble(numStack[numTop-1]);
    }

    private static void singleCalculate(String s) throws Exception {
        switch (s){
            case "+":
                if(numTop-2>=0){
                    numStack[numTop-2]=""+jia(Double.parseDouble(numStack[numTop-1]),Double.parseDouble(numStack[numTop-2]));
                    numTop--;
                }
                break;
            case "-":
                if(numTop-2>=0){
                    numStack[numTop-2]=""+jian(Double.parseDouble(numStack[numTop-2]),Double.parseDouble(numStack[numTop-1]));
                    numTop--;
                }else{
                    numStack[numTop-1]="-"+numStack[numTop-1];
                }
                break;
            case "*":
                if(numTop-2>=0){
                    numStack[numTop-2]=""+cheng(Double.parseDouble(numStack[numTop-1]),Double.parseDouble(numStack[numTop-2]));
                    numTop--;
                }else{
                    numStack[numTop-1]="0";
                }
                break;
            case "/":
                if(numTop-2>=0){
                    numStack[numTop-2]=""+chu(Double.parseDouble(numStack[numTop-2]),Double.parseDouble(numStack[numTop-1]));
                    numTop--;
                }else{
                    numStack[numTop-1]="0";
                }
                break;
            case "s":
                numStack[numTop-1]=""+sin(Double.parseDouble(numStack[numTop-1])*Math.PI/180);
                break;
            case "c":
                numStack[numTop-1]=""+cos(Double.parseDouble(numStack[numTop-1])*Math.PI/180);
                break;
            case "t":
                numStack[numTop-1]=""+tan(Double.parseDouble(numStack[numTop-1])*Math.PI/180);
                break;
            case "l":
                numStack[numTop-1]=""+ln(Double.parseDouble(numStack[numTop-1]));
                break;
            case "√":
                numStack[numTop-1]=""+genhao(Double.parseDouble(numStack[numTop-1]));
                break;
            case "^":
                numStack[numTop-2]=""+pow(Double.parseDouble(numStack[numTop-2]),Double.parseDouble(numStack[numTop-1]));
                numTop--;
                break;
            case "!":
                numStack[numTop-1]=""+jiecheng(Integer.parseInt(numStack[numTop-1]));
                break;
            case "²":
                numStack[numTop-1]=""+pingfang(Double.parseDouble(numStack[numTop-1]));
                break;
        }
    }

}
