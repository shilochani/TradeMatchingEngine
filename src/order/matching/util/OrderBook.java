package order.matching.util;



import order.matching.order.LimitOrder;
import order.matching.order.OrderSide;

import java.time.Instant;
import java.util.HashMap;
import java.util.TreeSet;

/**
 * Stores prioritized orders and processes order matching.
 */
public class OrderBook {

    private final TreeSet<LimitOrder> buyOrderList = new TreeSet<>();
    private final TreeSet<LimitOrder> sellOrderList = new TreeSet<>();
    private final HashMap<String, LimitOrder> orders = new HashMap<>();


    public boolean hasBuyOrder() {
        return !buyOrderList.isEmpty();
    }

    public boolean hasSellOrder() {
        return !sellOrderList.isEmpty();
    }

    /**
     * Adds order in order book and priorities it.
     *
     * @param order The order to be added.
     */
    public void addOrder(LimitOrder order) {
        orders.put(order.getId(), order);

        if (order.getSide().equals(OrderSide.B)) {
            buyOrderList.add(order);
        } else {
            sellOrderList.add(order);
        }
    }

    /**
     * Looks up the order by order ID and then removes the order from order book.
     *
     * @param orderId ID of the order to be removed.
     */
    public void removeOrder(String orderId) {
        if (!orders.containsKey(orderId)) {
            return;
        }

        LimitOrder currOrder = orders.get(orderId);
        orders.remove(orderId);

        if (currOrder.getSide().equals(OrderSide.B)) {
            buyOrderList.remove(currOrder);
        } else {
            sellOrderList.remove(currOrder);
        }
    }

    public void removeBuyHead() {
        LimitOrder currOrder = buyOrderList.pollFirst();
        orders.remove(currOrder.getId());
    }

    public void removeSellHead() {
        LimitOrder currOrder = sellOrderList.pollFirst();
        orders.remove(currOrder.getId());
    }

    public LimitOrder peekBuyList() {
        assert buyOrderList.size() != 0;

        return buyOrderList.first();
    }

    public LimitOrder peekSellList() {
        assert sellOrderList.size() != 0;

        return sellOrderList.first();
    }

    /**
     * Cancels and replaces a Limit Order in the OB with a new Price and Quantity,
     * keeping the order ID the same. Effectively serves as an update to the Quantity and/or Price of a Limit Order within the OB.
     * The updated order will be treated as a newly-inserted order,
     * which means it will be given lower priority compared to other existing orders of the same Price.
     * The only exception to this rule is when the Price remains the same and the Quantity decreases (or also remains the same),
     * in which case, the order's priority will remain the same.
     *
     * @param orderId
     * @param newQuantity
     * @param newPrice
     */
    public void replaceOrder(String orderId, int newQuantity, int newPrice) {
        if (!orders.containsKey(orderId)) {
            return;
        }

        LimitOrder currOrder = orders.get(orderId);

        if (newPrice == currOrder.getPrice() && newQuantity < currOrder.getQuantity()) {
            currOrder.decreaseQuantity(currOrder.getQuantity() - newQuantity);
        } else {
            removeOrder(orderId);

            OrderSide currSide = currOrder.getSide();
            long currTime = Instant.now().getNano();

            LimitOrder newOrder = new LimitOrder(currSide, orderId, newQuantity, currTime, newPrice);

            orders.put(orderId, newOrder);
            addOrder(newOrder);
        }
    }

    public boolean isSellOrderFullyExecutable(LimitOrder currOrder) {
        int executableQuantity = 0;

        for (LimitOrder buyOrder : buyOrderList) {
            if (buyOrder.getPrice() < currOrder.getPrice()) {
                break;
            }

            executableQuantity += buyOrder.getQuantity();

            if (executableQuantity >= currOrder.getQuantity()) {
                return true;
            }
        }

        return false;
    }

    public boolean isBuyOrderFullyExecutable(LimitOrder currOrder) {
        int executableQuantity = 0;

        for (LimitOrder sellOrder : sellOrderList) {
            if (currOrder.getPrice() < sellOrder.getPrice()) {
                break;
            }

            executableQuantity += sellOrder.getQuantity();

            if (executableQuantity >= currOrder.getQuantity()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("B:");
        for (LimitOrder order : buyOrderList) {
            sb.append(" " + order.toString());
        }

        sb.append("\n" + "S:");
        for (LimitOrder order : sellOrderList) {
            sb.append(" " + order.toString());
        }

        return sb.toString();
    }
}

