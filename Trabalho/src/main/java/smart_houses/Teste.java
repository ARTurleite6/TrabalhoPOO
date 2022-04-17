package smart_houses;

import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartDevice;

import java.time.LocalDate;

public class Teste {
    public static void main(String[] args) {
        LocalDate d = LocalDate.now();
        LocalDate fim = d.plusDays(1);
        System.out.println(fim.toString());
    }
}
