package com.doscan.qrcode.standard.qrcode;

import com.doscan.qrcode.util.LogUtil;

import java.util.regex.Pattern;

/**
 * 纯数字模式
 */
public class NumberInputThing extends InputThing {

    @Override
    public Byte[] getModeIndicator() {
        Byte[] indicator =  {0,0,0,1};
        return indicator;
    }

    @Override
    public boolean isMatch(String content) {
        Pattern pattern = Pattern.compile("^\\d+$");
        boolean result =  pattern
                .matcher(content)
                .matches();
        return result;
    }

    @Override
    public Byte[] getBits(String content) {

        // Encode three numeric letters in ten bits.
        int bitNums = ((content.length() % 3) + 1) * 10;
        Byte[] bits = new Byte[bitNums];

        int length = content.length();
        int i = 0;
        // 当前编码在数组中的下标
        int codeIndex = 0;

        int num1 = 0;
        int num2 = 0;
        int num3 = 0;

        while (i < length) {
            // 计算当前词组的下标
            codeIndex = i % 3;

            if(codeIndex == 0){
                num1 = content.charAt(i) - '0';
            }else if(codeIndex == 1){
                num2 = content.charAt(i) - '0';
            }else{
                num3 = content.charAt(i) - '0';
                // 拼凑数值得到结果
                int codeNum = num1 * 100 + num2 * 10 + num3;
                LogUtil.log("codeNum ---- " + codeNum);
            }
            i++;
        }

        if(codeIndex == 0){
            // 0
            int codeNum = num1;
            LogUtil.log("codeNum ---- " + codeNum);
        }else{
            // 1
            int codeNum = num1 * 10 + num2;
            LogUtil.log("codeNum ---- " + codeNum);
        }

        return new Byte[0];
    }

    @Override
    public String getName() {
        return "纯数字模式";
    }
}
