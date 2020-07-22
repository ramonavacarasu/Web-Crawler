import java.time.LocalDate;
import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String string = scanner.nextLine();
        String[] strings = string.split(" ");

        LocalDate time1 = LocalDate.parse(strings[0]);
        LocalDate time2 = LocalDate.parse(strings[1]);
        LocalDate time3 = LocalDate.parse(strings[2]);

        System.out.println(time1.isAfter(time2) && time1.isBefore(time3)
                || time1.isBefore(time2) && time1.isAfter(time3) ? "true" : "false");
    }
}