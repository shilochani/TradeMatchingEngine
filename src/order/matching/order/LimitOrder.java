package order.matching.order;

// import java.awt.*;
import java.util.Comparator;

public class LimitOrder extends Order implements Comparable<LimitOrder> {

    private int price;

    /**
     * Constructor.
     *
     * @param side
     * @param orderId
     * @param quantity
     * @param price
     */
    public LimitOrder(OrderSide side, String orderId, int quantity, long timeStamp, int price) {
        super(side, orderId, quantity, timeStamp);
        this.price = price;
    }

    @Override
    public int compareTo(LimitOrder other) {
        assert getSide().equals(other.getSide());

        if (getSide().equals(OrderSide.B)) {
            return compareToBuyOrder(other);
        } else { // S side
            return compareToSellOrder(other);
        }
    }

    private int compareToBuyOrder(LimitOrder other) {
        // Higher price has higher priority.
        if (price < other.price) {
            return 1;
        } else if (price > other.price) {
            return -1;
        } else {
            return Long.compare(getTimeStamp(), other.getTimeStamp());
        }
    }

    private int compareToSellOrder(LimitOrder other) {
        // Lower price has higher priority.
        if (price > other.price) {
            return 1;
        } else if (price < other.price) {
            return -1;
        } else {
            return Long.compare(getTimeStamp(), other.getTimeStamp());
        }
    }

    @Override
    public boolean isPriceless() {
        return true;
    }

    public int getPrice() {
        return price;
    }

    @Override
    public String toString() {
        return getQuantity() + "@" + price + "#" + getId();
    }
}
