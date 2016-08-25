package com.parsifal.log01.utils;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

/**
 * Created by YangMing on 2016/8/19 13:28.
 */
public class MathUtil {

    private static final String ERROR_MSG1 = "inputData is Null";

    private static final String ERROR_MSG2 = "inputData count is 0";

    public static double getMax(double[] inputData) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int len = inputData.length;
        double max = inputData[0];
        for (int i = 0; i < len; i++) {
            if (max < inputData[i])
                max = inputData[i];
        }
        return max;
    }

    public static double getMin(double[] inputData) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int len = inputData.length;
        double min = inputData[0];
        for (int i = 0; i < len; i++) {
            if (min > inputData[i])
                min = inputData[i];
        }
        return min;
    }

    public static double getSum(double[] inputData) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int len = inputData.length;
        double sum = 0;
        for (int i = 0; i < len; i++) {
            sum = sum + inputData[i];
        }
        return sum;
    }

    public static int getCount(double[] inputData) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        return inputData.length;
    }

    public static double getAverage(double[] inputData) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int len = inputData.length;
        double result;
        result = getSum(inputData) / len;
        return result;
    }

    public static double getSquareSum(double[] inputData) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int len = inputData.length;
        double squareSum = 0.0d;
        for (int i = 0; i < len; i++) {
            squareSum = squareSum + inputData[i] * inputData[i];
        }
        return squareSum;
    }

    public static double getVariance(double[] inputData) {
        int count = getCount(inputData);
        double squareSum = getSquareSum(inputData);
        double average = getAverage(inputData);
        double result;
        result = (squareSum - count * average * average) / count;
        return result;
    }

    public static double getStandardDeviation(double[] inputData) {
        double result;
        result = Math.sqrt(Math.abs(getVariance(inputData)));
        return result;
    }

    public static double getZ(double x, double ц, double σ) {
        return (x - ц) / σ;
    }

    public static double[] removeDuplicatesAndSort(double[] inputData) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        List<Double> numList = new ArrayList<Double>();
        for (double i : inputData) {
            numList.add(i);
        }
        Set<Double> numSet = new TreeSet<>();
        numSet.addAll(numList);

        double[] result = new double[numSet.size()];
        Iterator iterator = numSet.iterator();
        int i = 0;
        while (iterator.hasNext()) {
            result[i] = (double) iterator.next();
            i++;
        }
        return result;
    }

    public static int[] occurrences(double[] inputData, double[] srcData) {
        if (null == inputData || null == srcData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length || 0 == srcData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int inputLength = inputData.length;
        int srcLength = srcData.length;
        int[] result = new int[inputLength];
        int count = 0;
        for (int i = 0; i < inputLength; i++) {
            for (int j = 0; j < srcLength; j++) {
                if (srcData[j] == inputData[i]) {
                    count++;
                }
            }
            result[i] = count;
            count = 0;
        }
        return result;
    }

    public static int[] cumulativeCount(int[] inputData) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int inputLength = inputData.length;
        int[] result = new int[inputLength];
        int temp = 0;
        for (int i = 0; i < inputLength; i++) {
            temp += inputData[i];
            result[i] = temp;
        }
        return result;
    }

    public static double[] cumulativeFrequency(int[] inputData, int total) {
        if (null == inputData) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == inputData.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int inputLength = inputData.length;
        double[] result = new double[inputLength];
        for (int i = 0; i < inputLength; i++) {
            result[i] = inputData[i] / Integer.valueOf(total).doubleValue();
        }
        return result;
    }

    public static double[] absoluteDifference(double[] array1, double[] array2) {
        if (null == array1 || null == array1) {
            throw new NullPointerException(ERROR_MSG1);
        }
        if (0 == array2.length || 0 == array2.length) {
            throw new ArrayIndexOutOfBoundsException(ERROR_MSG2);
        }
        int inputLength = array1.length;
        double[] result = new double[inputLength];
        for (int i = 0; i < inputLength; i++) {
            result[i] = Math.abs(array1[i] - array2[i]);
        }
        return result;
    }
}
