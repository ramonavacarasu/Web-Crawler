import java.util.*;
import java.util.concurrent.*;


class FutureUtils {

    public static int howManyIsDone(List<Future> items) {
        int counter = 0;

        try {
            for (Future item : items) {
                if (item.isDone()) {
                    counter++;
                }
            }
        } catch (Exception e) {
            System.out.println("oops");
        }

        return counter;
    }


}