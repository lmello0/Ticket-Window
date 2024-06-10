package br.com.lmello.common.customer;

public enum Operation {
    WITHDRAW,
    DEPOSIT,
    PAYMENT;

    public int getOperationTime() {
        final int MILLISECONDS_PER_SECOND = 1000;

        return switch (this) {
            case WITHDRAW -> 60 * MILLISECONDS_PER_SECOND;
            case DEPOSIT -> 90 * MILLISECONDS_PER_SECOND;
            case PAYMENT -> 120 * MILLISECONDS_PER_SECOND;
        };
    }
}
