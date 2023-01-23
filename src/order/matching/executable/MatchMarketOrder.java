package order.matching.executable;

import order.matching.order.LimitOrder;
import order.matching.order.MarketOrder;
import order.matching.order.OrderSide;
import order.matching.util.OrderBook;

public class MatchMarketOrder implements Executable {
    private MarketOrder order;

    public MatchMarketOrder(MarketOrder order) {
        this.order = order;
    }

    @Override
    public void execute(OrderBook orderBook) {
        if (order.getSide().equals(OrderSide.B)) {
            matchBuyOrder(orderBook);
        } else {
            matchSellOrder(orderBook);
        }
    }

    private void matchBuyOrder(OrderBook orderBook) {
        int executedAmount = 0;
        MarketOrder currOrder = order;

        while (currOrder.getQuantity() > 0 && orderBook.hasSellOrder()) {
            LimitOrder other = orderBook.peekSellList();
            int executionPrice = other.getPrice();
            int executionQuantity = Math.min(currOrder.getQuantity(), other.getQuantity());

            executedAmount += executionPrice * executionQuantity;

            if (executionQuantity == other.getQuantity()) {
                orderBook.removeSellHead();
            } else {
                other.decreaseQuantity(executionQuantity);
            }
            currOrder.decreaseQuantity(executionQuantity);
        }

        System.out.println(executedAmount);
    }

    private void matchSellOrder(OrderBook orderBook) {
        int executedAmount = 0;
        MarketOrder currOrder = order;

        while (currOrder.getQuantity() > 0 && orderBook.hasBuyOrder()) {
            LimitOrder other = orderBook.peekBuyList();
            int executionPrice = other.getPrice();
            int executionQuantity = Math.min(currOrder.getQuantity(), other.getQuantity());

            executedAmount += executionPrice * executionQuantity;

            if (executionQuantity == other.getQuantity()) {
                orderBook.removeBuyHead();
            } else {
                other.decreaseQuantity(executionQuantity);
            }
            currOrder.decreaseQuantity(executionQuantity);
        }

        System.out.println(executedAmount);
    }
}
