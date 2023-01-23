package order.matching;

import order.matching.util.OrderBook;
import order.matching.util.Parser;
import order.matching.executable.Executable;


import java.util.Scanner;

/**
 * Drives the matching engine logic.
 */
public class MatchingEngine {
    private OrderBook orderBook = new OrderBook();

    public void run() {
        Scanner sc = new Scanner(System.in);

        while (sc.hasNextLine()) {
            String input = sc.nextLine();
            if (input.equals("END")) {
                System.out.println(orderBook.toString());
                break;
            }

            Executable command = Parser.parseInput(input);
            command.execute(orderBook);
        }
    }
}

