package smart_houses;

import smart_houses.exceptions.AlreadyExistDeviceException;

public class DataGen {
    public static void main(String[] args) {
        Parser p = new Parser();
        EstadoPrograma e = null;
        try {
            e = p.parse();
        } catch (AlreadyExistDeviceException ex) {
            System.out.println(ex.getMessage());
        }
        e.guardaDados();

        System.out.println(e.getCasas().get("365597405"));
    }
}
