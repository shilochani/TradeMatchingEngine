package order.matching.executable;

import order.matching.util.OrderBook;

public interface Executable {
    /**
     * Executes the matching process with the provided OrderBook.
     */
    public void execute(OrderBook orderBook);
}