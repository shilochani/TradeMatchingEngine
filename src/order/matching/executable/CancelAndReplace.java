package order.matching.executable;

import order.matching.util.OrderBook;

public class CancelAndReplace implements Executable {
    private String orderId;
    private int newQuantity;
    private int newPrice;

    /**
     * Constructor.
     *
     * @param orderId
     * @param newQuantity
     * @param newPrice
     */
    public CancelAndReplace(String orderId, int newQuantity, int newPrice) {
        this.orderId = orderId;
        this.newQuantity = newQuantity;
        this.newPrice = newPrice;
    }

    @Override
    public void execute(OrderBook orderBook) {
        orderBook.replaceOrder(orderId, newQuantity, newPrice);
    }
}

