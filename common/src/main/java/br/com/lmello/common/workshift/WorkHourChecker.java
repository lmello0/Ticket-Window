package br.com.lmello.common.workshift;

import br.com.lmello.common.Constants;

import java.time.LocalTime;

public class WorkHourChecker {
    public static boolean isInWorkHour() {
        LocalTime currentTime = LocalTime.now();
        int currentHour = currentTime.getHour();
        int currentMinute = currentTime.getMinute();

        if (currentHour > Constants.FINISH_WORK_AT_HOUR || (currentHour == Constants.FINISH_WORK_AT_HOUR && currentMinute > Constants.FINISH_WORK_AT_MINUTE)) {
            return false;
        }

        return currentHour >= Constants.START_WORK_AT_HOUR;
    }
}
