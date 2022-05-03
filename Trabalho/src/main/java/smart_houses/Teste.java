package smart_houses;

import smart_houses.exceptions.CasaInexistenteException;
import smart_houses.exceptions.ExisteCasaException;
import smart_houses.exceptions.ExisteFornecedorException;
import smart_houses.exceptions.FornecedorInexistenteException;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartDevice;

public class Teste {
    public static void main(String[] args) throws CasaInexistenteException {
        EstadoPrograma e = new EstadoPrograma();
        Casa c1 = new Casa("Artur", "100", "EDP");
        Fornecedor f = new Fornecedor("EDP");
        try {
            e.addFornecedor(f);
        } catch (ExisteFornecedorException ex) {
            ex.printStackTrace();
        }
        SmartDevice s = new SmartBulb(true, 10, SmartBulb.Tones.WARM, 10);
        c1.addDevice(s);
        try {
            e.adicionaCasa(c1);
        } catch (ExisteCasaException ex) {
            ex.printStackTrace();
        } catch (FornecedorInexistenteException ex) {
            ex.printStackTrace();
        }
        System.out.println(e.getListDevicesHouse("100"));
    }
}
