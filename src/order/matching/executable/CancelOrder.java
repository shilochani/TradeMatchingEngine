package order.matching.executable;

import order.matching.util.OrderBook;

public class CancelOrder implements Executable {
    private String orderId;

    public CancelOrder(String orderId) {
        this.orderId = orderId;
    }

    @Override
    public void execute(OrderBook orderBook) {
        orderBook.removeOrder(orderId);
    }
}

