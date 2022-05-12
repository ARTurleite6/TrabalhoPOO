package smart_houses.modulo_casas;

import org.junit.jupiter.api.Test;
import smart_houses.Fatura;
import smart_houses.exceptions.*;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartDevice;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CasaTest {

    @Test
    void consumoDispositivos() {
    }

    @Test
    void consumo() {
    }

    @Test
    void testConsumo() {
    }

    @Test
    void faturasFornecedor() {
        Fornecedor f1 = new Fornecedor("EDP");
        Casa c = new Casa("Artur", "256", "EDP");
        Fatura f = null;
        try {
            f = f1.criaFatura(c, LocalDate.now().minusDays(10), LocalDate.now());
        } catch (FornecedorErradoException e) {
            e.printStackTrace();
        }
        Fatura f2 = null;
        try {
            f2 = f1.criaFatura(c, LocalDate.now().minusDays(20), LocalDate.now().minusDays(10));
        } catch (FornecedorErradoException e) {
            e.printStackTrace();
        }
        c.adicionaFatura(f);
        c.adicionaFatura(f2);

        List<Fatura> l = List.of(f, f2);

        assertEquals(l, c.faturasFornecedor("EDP"));

    }

    @Test
    void mudaDispositivoRoom(){
        Fornecedor f1 = new Fornecedor("EDP");
        Casa c = new Casa("Artur", "256", "EDP");
        try {
            c.addRoom("Quarto");
        } catch (RoomAlreadyExistsException e) {
            e.printStackTrace();
        }

        try {
            c.addRoom("Quarto 1");
        } catch (RoomAlreadyExistsException e) {
            e.printStackTrace();
        }

        SmartDevice sm = new SmartBulb();
        try {
            c.addDevice(sm);
        } catch (AlreadyExistDeviceException e) {
            e.printStackTrace();
        }
        try {
            c.addDeviceOnRoom("Quarto", sm.getId());
        } catch (RoomInexistenteException e) {
            e.printStackTrace();
        }

        assertTrue(c.existDeviceOnRoom("Quarto", sm.getId()));

        try {
            c.mudaDeviceDeRoom("Quarto 1", 1);
        } catch (DeviceInexistenteException | RoomInexistenteException e) {
            e.printStackTrace();
        }

        assertFalse(c.existDeviceOnRoom("Quarto", 1));
        assertTrue(c.existDeviceOnRoom("Quarto 1", 1));

    }
}