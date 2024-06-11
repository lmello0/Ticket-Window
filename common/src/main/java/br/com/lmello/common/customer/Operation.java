package br.com.lmello.common.customer;

import br.com.lmello.common.Constants;

public enum Operation {
    WITHDRAW,
    DEPOSIT,
    PAYMENT;

    public int getOperationTime() {
        return switch (this) {
            case WITHDRAW -> 60 * Constants.SECOND_IN_MS;
            case DEPOSIT -> 90 * Constants.SECOND_IN_MS;
            case PAYMENT -> 120 * Constants.SECOND_IN_MS;
        };
    }
}
