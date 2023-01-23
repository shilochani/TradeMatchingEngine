package order.matching.executable;

import order.matching.order.LimitOrder;
import order.matching.order.OrderSide;
import order.matching.util.OrderBook;

public class MatchLimitOrder implements Executable {
    protected LimitOrder order;

    public MatchLimitOrder(LimitOrder order) {
        this.order = order;
    }

    @Override
    public void execute(OrderBook orderBook) {
        if(order.getSide().equals(OrderSide.B)) {
            matchBuyOrder(orderBook);
        } else {
            matchSellOrder(orderBook);
        }
    }

    protected void matchBuyOrder(OrderBook orderBook) {
        int executedAmount = 0;
        LimitOrder currOrder = order;

        while (currOrder.getQuantity() > 0 && orderBook.hasSellOrder()
                && orderBook.peekSellList().getPrice() <= currOrder.getPrice()) {

            LimitOrder other = orderBook.peekSellList();
            int executionPrice = other.getPrice();
            int executionQuantity = Math.min(currOrder.getQuantity(), other.getQuantity());

            executedAmount += executionPrice * executionQuantity;

            if(executionQuantity == other.getQuantity()) {
                orderBook.removeSellHead();
            } else {
                other.decreaseQuantity(executionQuantity);
            }
            currOrder.decreaseQuantity(executionQuantity);
        }

        System.out.println(executedAmount);

        if (currOrder.getQuantity() > 0) {
            orderBook.addOrder(currOrder);
        }
    }

    protected void matchSellOrder(OrderBook orderBook) {
        int executedAmount = 0;
        LimitOrder currOrder = order;

        while (currOrder.getQuantity() > 0 && orderBook.hasBuyOrder()
                && orderBook.peekBuyList().getPrice() >= currOrder.getPrice()) {

            LimitOrder other = orderBook.peekBuyList();
            int executionPrice = other.getPrice();
            int executionQuantity = Math.min(currOrder.getQuantity(), other.getQuantity());

            executedAmount += executionPrice * executionQuantity;
            if(executionQuantity == other.getQuantity()) {
                orderBook.removeBuyHead();
            } else {
                other.decreaseQuantity(executionQuantity);
            }
            currOrder.decreaseQuantity(executionQuantity);
        }

        System.out.println(executedAmount);

        if (currOrder.getQuantity() > 0) {
            orderBook.addOrder(currOrder);
        }
    }
}

