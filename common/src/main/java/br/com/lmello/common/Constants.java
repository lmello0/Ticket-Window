package br.com.lmello.common;

public class Constants {
    public final static int START_WORK_AT_HOUR = Integer.parseInt(System.getenv("START_WORK_HOUR"));
    public final static int START_WORK_AT_MINUTE = Integer.parseInt(System.getenv("START_WORK_MINUTE"));

    public final static int FINISH_WORK_AT_HOUR = Integer.parseInt(System.getenv("FINISH_WORK_HOUR"));
    public final static int FINISH_WORK_AT_MINUTE = Integer.parseInt(System.getenv("FINISH_WORK_MINUTE"));

    public final static int SECOND_IN_MS = Integer.parseInt(System.getenv("SECOND_IN_MS"));
}
