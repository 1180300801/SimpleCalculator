package com.example.simpleCalculator;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private answerViewModel model;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //可保留少量数据
        super.onCreate(savedInstanceState);

        //根据当前屏幕方向选择要展示的Activity
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR);
        setContentView(R.layout.activity_main);

        //获取展示计算结果的View
        TextView ans = findViewById(R.id.textView);

        //使用ViewModel+LiveData保存计算结果，以便配置更改后将计算结果传递给新的Activity
        model = new ViewModelProvider(this).get(answerViewModel.class);
        final Observer<String> ansObserver = new Observer<String>() {
            @Override
            public void onChanged(@Nullable final String newName) {
                // Update the UI, in this case, a TextView.
                ans.setText(newName);
            }
        };
        model.getCurrentAnswer().observe(this, ansObserver);
    }

    /**
     * 用户点击按钮后处理对应的事务
     * @param v 点击的按钮
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void onClick(View v){
        ////获取供用户输入表达式的View
        EditText input = findViewById(R.id.inputText);

        //获取展示计算结果的View
        TextView ans = findViewById(R.id.textView);

        //获取当前点击的按钮
        Button click = findViewById(v.getId());

        //点击按钮"="，调用Calculator的calculate方法计算结果，并更改LiveData的值为计算得到的结果
        if(click.getText().equals("=")){
            Calculator calculator = new Calculator();
            String answer = calculator.calculate(input.getText().toString());
            model.getCurrentAnswer().setValue(answer);
        }

        //点击按钮"DEL"，删除输入末尾的一个符号，暂不支持长按清屏操作
        else if(click.getText().equals("DEL")){
            if(input.getText().length()>0)
                input.getText().delete(input.getText().length()-1,input.getText().length());
        }

        //点击的是其它按钮，将按钮对应的text连接到输入末尾
        else {
            input.append(click.getText());
        }
    }
}