package chainofresp.payments.context;

import chainofresp.payments.processor.PaymentProcessor;
import dto.request.OrderPaymentRqDto;
import model.Order;
import model.Receipt;

public class PaymentContext {
    private OrderPaymentRqDto rq;
    private Order order;
    private PaymentProcessor processor;
    private Receipt receipt;

    public PaymentContext(OrderPaymentRqDto orderPaymentRqDto) {
        this.rq = orderPaymentRqDto;

    }

    public OrderPaymentRqDto getRq() {
        return rq;
    }

    public void setRq(OrderPaymentRqDto rq) {
        this.rq = rq;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public PaymentProcessor getProcessor() {
        return processor;
    }

    public void setProcessor(PaymentProcessor processor) {
        this.processor = processor;
    }

    public Receipt getReceipt() {
        return receipt;
    }

    public void setReceipt(Receipt receipt) {
        this.receipt = receipt;
    }
}
