package smart_houses;

public class DataGen {
    public static void main(String[] args) {
        Parser p = new Parser();
        EstadoPrograma e = p.Parse();
        e.guardaDados();
    }
}
