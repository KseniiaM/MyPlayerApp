package com.elzette.myplayerapp.Helpers;

import android.text.format.DateUtils;

public class SeekBarConverterUtil {

    public static String createTimeString(long timeInMillis) {
        long timeInSec = timeInMillis / DateUtils.SECOND_IN_MILLIS;
        return DateUtils.formatElapsedTime(timeInSec);
    }

    public static int progressToTime(int progress, long totalDuration) {
        return (int) ((((double) progress) / 100) * totalDuration);
    }

    public static int getProgressPercentage(long currentDuration, long totalDuration) {
        return (int) (((float) currentDuration / (float) totalDuration) * 100);
    }
}
