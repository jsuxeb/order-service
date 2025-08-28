package dto.request;

import model.PaymentMethod;


public class OrderPaymentRqDto {

    private Long orderId;
    private PaymentMethod method;
    private String paymentType;
    private CardPaymentRqDto cardPaymentRqDto;
    private CashPaymentRqDto cashPaymentRqDto;

    public Long getOrderId() {
        return orderId;
    }

    public void setOrderId(Long orderId) {
        this.orderId = orderId;
    }

    public PaymentMethod getMethod() {
        return method;
    }

    public void setMethod(PaymentMethod method) {
        this.method = method;
    }

    public CardPaymentRqDto getCardPaymentRqDto() {
        return cardPaymentRqDto;
    }

    public void setCardPaymentRqDto(CardPaymentRqDto cardPaymentRqDto) {
        this.cardPaymentRqDto = cardPaymentRqDto;
    }

    public CashPaymentRqDto getCashPaymentRqDto() {
        return cashPaymentRqDto;
    }

    public void setCashPaymentRqDto(CashPaymentRqDto cashPaymentRqDto) {
        this.cashPaymentRqDto = cashPaymentRqDto;
    }

    public String getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(String paymentType) {
        this.paymentType = paymentType;
    }
}
