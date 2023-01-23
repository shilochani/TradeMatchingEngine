package order.matching.order;

public class MarketOrder extends Order {

    /**
     * Constructor.
     *
     * @param side
     * @param orderId
     * @param quantity
     */
    public MarketOrder(OrderSide side, String orderId, int quantity, long timeStamp) {
        super(side, orderId, quantity, timeStamp);
    }

    @Override
    public boolean isPriceless() {
        return true;
    }
}
