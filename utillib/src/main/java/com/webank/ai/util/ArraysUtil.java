package com.webank.ai.util;

import android.util.Log;

import java.util.Arrays;
import java.util.Random;

import static com.webank.ai.util.RandomNumUtil.getNum;


/**
 * creaed by  zhijunchen on 2019/2/21
 */
public class ArraysUtil {
    private static final String TAG = "ArraysUtil";

    public void sort(String[] data) {
        Random random = new Random();
        for (int i = 0; i < data.length; i++) {
            int p = random.nextInt(i + 1);
            Log.i(TAG, "i===" + i + "p===" + p);
            String tmp = data[i];
            data[i] = data[p];
            data[p] = tmp;
        }
        Log.i(TAG, "data=" + Arrays.toString(data));
    }

    /**
     * 生成一个相邻数字不重复的数组
     * 每个元素为个位数
     *
     * @param nums
     * @return
     */
    public static void generateRandomNums(int[] nums) {
        nums[0] = getNum(0, 10);
        Log.i(TAG, "num[0]" + "=" + nums[0]);
        for (int i = 1; i < nums.length; i++) {
            int newNum = getNum(0, 10);
            while (newNum == nums[i - 1]) {
                newNum = getNum(0, 10);
            }
            nums[i] = newNum;
            Log.i(TAG, "num[" + i + "]" + "=" + newNum);
        }

    }

    public static <T> void mergeArrays(T[] result, T[]... data) {
        int index = 0;
        for (T[] data1 : data) {
            System.arraycopy(data1, 0, result, index, data1.length);
            index += data1.length;
        }
    }

    public static <T> int getLenght(T[]... data) {
        int length = 0;
        for (T[] data1 : data) {
            length += data1.length;
        }
        return length;
    }


}
