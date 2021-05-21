package com.example.simpleCalculator;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class answerViewModel extends ViewModel {
    //用于保存计算结果，横竖屏切换时保持数据
    private MutableLiveData<String> currentAnswer;

    public MutableLiveData<String> getCurrentAnswer() {
        if (currentAnswer == null) {
            currentAnswer = new MutableLiveData<>();
        }
        return currentAnswer;
    }
}
