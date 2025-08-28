package dto.request;

import model.CashMode;

public class CashPaymentRqDto {
    private CashMode mode;

    public CashMode getMode() {
        return mode;
    }

    public void setMode(CashMode mode) {
        this.mode = mode;
    }
}
