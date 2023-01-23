package order.matching.util;

import order.matching.executable.*;
import order.matching.order.*;

import java.time.Instant;

/**
 * Parses input strings.
 */
public class Parser {
    public static Executable parseInput(String input) {
        String[] splitInput = input.strip().split(" ");
        if (splitInput.length < 2) {
            throw new IllegalArgumentException("The number of arguments is insufficient.");
        }

        String command = splitInput[0];
        switch(command) {
        case "SUB":
            return parseOrder(splitInput);
        case "CXL":
            return parseCancel(splitInput);
        case "CRP":
            return parseCancelAndReplace(splitInput);
        default:
            throw new IllegalArgumentException("The command " + command + " is not valid.");
        }
    }

    private static Executable parseOrder(String[] splitInput) {
        String orderType = splitInput[1];

        switch(orderType) {
        case "LO":
            return parseLimitOrder(splitInput);
        case "MO":
            return parseMarketOrder(splitInput);
        /* case "IOC":
            return parseIocOrder(splitInput);
        case "FOK":
            return parseFokOrder(splitInput);
        */
        default:
            throw new IllegalArgumentException("The order type " + orderType + " is invalid.");
        }
    }

    private static MatchLimitOrder parseLimitOrder(String[] splitInput) {
        if (splitInput.length < 6) {
            throw new IllegalArgumentException("The number of arguments for LimitOrder is insufficient.");
        }

        OrderSide side = parseSide(splitInput[2]);
        String orderId = splitInput[3];
        int quantity = Integer.parseInt(splitInput[4]);
        int price = Integer.parseInt(splitInput[5]);
        long currTime = Instant.now().getNano();

        LimitOrder order = new LimitOrder(side, orderId, quantity, currTime, price);

        return new MatchLimitOrder(order);
    }

    private static MatchMarketOrder parseMarketOrder(String[] splitInput) {
        if (splitInput.length < 5) {
            throw new IllegalArgumentException("The number of arguments for MarketOrder is insufficient.");
        }

        OrderSide side = parseSide(splitInput[2]);
        String orderId = splitInput[3];
        int quantity = Integer.parseInt(splitInput[4]);
        long currTime = Instant.now().getNano();

        MarketOrder order = new MarketOrder(side, orderId, quantity, currTime);

        return new MatchMarketOrder(order);
    }

    private static CancelOrder parseCancel(String[] splitInput) {
        String orderId = splitInput[1];
        return new CancelOrder(orderId);
    }

    private static OrderSide parseSide(String sideStr) {
        switch(sideStr) {
        case "B":
            return OrderSide.B;
        case "S":
            return OrderSide.S;
        default:
            throw new IllegalArgumentException("The side parameter is invalid.");
        }
    }

   /* private static MatchIocOrder parseIocOrder(String[] splitInput) {
        if (splitInput.length < 6) {
            throw new IllegalArgumentException("The number of arguments for IocOrder is insufficient.");
        }

        OrderSide side = parseSide(splitInput[2]);
        String orderId = splitInput[3];
        int quantity = Integer.parseInt(splitInput[4]);
        int price = Integer.parseInt(splitInput[5]);
        long currTime = Instant.now().getNano();

        IocOrder order = new IocOrder(side, orderId, quantity, currTime, price);

        return new MatchIocOrder(order);
    }
    */

   /* private static MatchFokOrder parseFokOrder(String[] splitInput) {
        if (splitInput.length < 6) {
            throw new IllegalArgumentException("The number of arguments for FokOrder is insufficient.");
        }

        OrderSide side = parseSide(splitInput[2]);
        String orderId = splitInput[3];
        int quantity = Integer.parseInt(splitInput[4]);
        int price = Integer.parseInt(splitInput[5]);
        long currTime = Instant.now().getNano();

       FokOrder order = new FokOrder(side, orderId, quantity, currTime, price);

        return new MatchFokOrder(order);
    }
    */

    private static CancelAndReplace parseCancelAndReplace(String[] splitInput) {
        if(splitInput.length < 4) {
            throw new IllegalArgumentException("The number of arguments for CRP is insufficient.");
        }

        String orderId = splitInput[1];
        int newQuantity = Integer.parseInt(splitInput[2]);
        int newPrice = Integer.parseInt(splitInput[3]);

        return new CancelAndReplace(orderId, newQuantity, newPrice);
    }
}
