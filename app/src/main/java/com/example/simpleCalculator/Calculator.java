package com.example.simpleCalculator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class Calculator {
    private final Map<Character, Integer> op_priority = new HashMap<>(); //存放运算符优先级
    private final List<String> error = new ArrayList<>(); //中缀转后缀时，若遇到错误输入返回此对象标识错误输入

    /**
     * 构造函数，初始化运算符优先级和错误列表
     */
    public Calculator() {
        op_priority.put('(',0);
        op_priority.put('+',1);
        op_priority.put('-',1);
        op_priority.put('*',2);
        op_priority.put('/',2);
        op_priority.put('%',2);
        error.add("error");
    }

    /**
     * 给定表达式（字符串型），计算结果。进行了简单的错误处理
     * @param input 输入的表达式
     * @return 计算结果
     */
    public String calculate(String input){
        //将中缀表达式转为后缀表达式
        List<String> suffix = infixToSuffix(input);
        //错误处理
        if(suffix.equals(error)||suffix.size()==0)
            return "error input";

        //运算数栈，存放参与运算的数和计算的中间结果（包括最终结果）
        Stack<Double> num_stack = new Stack<>();

        //遍历后缀表达式，结合栈操作计算结果
        for (String s : suffix) {
            //为数字时压入栈
            if (Character.isDigit(s.charAt(0))) {
                num_stack.push(Double.parseDouble(s));
            }
            //为运算符时，从栈顶弹出两个数字，使用该运算符进行计算
            else {
                //栈中存储的数字不少于两个则正常计算，否则输入不合法，报错
                if(num_stack.size()>=2)
                    num_stack.push(compute(num_stack.pop(), num_stack.pop(), s.charAt(0)));
                else
                    return "error input";
            }
        }
        String ret = num_stack.pop().toString();
        if(ret.charAt(ret.length()-1)=='0' && ret.charAt(ret.length()-2)=='.')
            return ret.substring(0,ret.length()-2);
        return ret;//返回最终结果
    }

    /**
     * 将中缀表达式转为后缀表达式
     * @param input 输入的中缀表达式
     * @return 转化得到的后缀表达式
     */
    private List<String> infixToSuffix(String input)
    {
        char[] array_input = input.toCharArray();
        Stack<Character> op_stack = new Stack<>(); //运算符栈
        List<String> suffix = new ArrayList<>();
        int LB_num = 0;//栈中左括号数量
        int point_num = 0;//小数点数量
        boolean negative = false;//标志是否为负数
        for(int i = 0;i<array_input.length;){
            //处理运算符或括号
            if(!Character.isDigit(array_input[i])&&array_input[i] != '.'){
                //遇到左括号，入栈
                if(array_input[i] == '(') {
                    op_stack.push(array_input[i]);
                    LB_num++;
                }
                //遇到右括号，依次将符号栈栈顶元素弹出并放入后缀表达式结果中，直到遇到左括号（左括号出栈，但不放入输出）
                else if(array_input[i] == ')'){
                    LB_num--;
                    if(LB_num<0)
                        return error;
                    while(op_stack.peek() != '('){
                        suffix.add(op_stack.pop()+"");
                    }
                    op_stack.pop();
                }
                //遇到运算符且栈为空时，将其压入符号栈
                else if(op_stack.empty()) {
                    if(array_input[i] == '-')
                        if (negative)
                            return error;
                        else
                            negative = true;
                    else
                        op_stack.push(array_input[i]);
                }
                //遇到运算符且栈非空，将栈顶所有优先级大于等于它的符号出栈并放入输出中，然后将其入栈
                else{
                    if(array_input[i] == '-' && op_stack.peek()=='(')
                        if (negative)
                            return error;
                        else
                            negative = true;
                    else {
                        while (!op_stack.empty() && comparePriority(op_stack.peek(), array_input[i]) >= 0) {
                            suffix.add(op_stack.pop() + "");
                        }
                        op_stack.push(array_input[i]);
                    }
                }
                i++;
                continue;
            }
            //处理数字
            StringBuilder sb = new StringBuilder();
            if(negative)
                sb.append('-');
            while(i<array_input.length&&(Character.isDigit(array_input[i])||array_input[i] == '.')){
                if(array_input[i]=='.')
                    if(point_num==0)
                        point_num++;
                    else
                        return error;
                sb.append(array_input[i]);
                i++;
            }
            point_num=0;
            suffix.add(sb.toString());
        }
        //将符号栈剩余符号出栈并存入输出
        while(!op_stack.empty()){
            suffix.add(op_stack.pop()+"");
        }
        return suffix;
    }

    /**
     * 计算a operator b的值，operator = {+,-,*,/,%}
     * @param b 第二个参与运算的数
     * @param a 第一个参与运算的数
     * @param operator 运算符
     * @return 计算结果
     */
    public Double compute(Double b, Double a, char operator) {
        double result;
        switch (operator) {
            case '+':
                result = a + b;
                break;
            case '-':
                result = a - b;
                break;
            case '*':
                result = a * b;
                break;
            case '/':
                result = a / b;
                break;
            case '%':
                result = a %b;
                break;
            default:
                result = 0.0;
                break;
        }
        return result;
    }

    /**
     * 判断运算符a和b的优先级
     * @param a 运算符a
     * @param b 运算符b
     * @return 优先级比较结果
     */
    public int comparePriority(char a, char b) {
        return op_priority.get(a)-op_priority.get(b);
    }
}