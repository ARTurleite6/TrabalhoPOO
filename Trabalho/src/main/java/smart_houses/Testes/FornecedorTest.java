package smart_houses.Testes;

import org.junit.jupiter.api.Test;
import smart_houses.Fatura;
import smart_houses.exceptions.AlreadyExistDeviceException;
import smart_houses.exceptions.FornecedorErradoException;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartDevice;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FornecedorTest {

    @Test
    void criaFatura() throws AlreadyExistDeviceException, FornecedorErradoException {
        Casa c = new Casa("Artur", "256", "EDP");
        Fornecedor f = new Fornecedor("EDP");
        SmartDevice sm = new SmartBulb(true, 0.20, SmartBulb.Tones.WARM, 10);
        SmartDevice sm2 = new SmartBulb(true, 0.20, SmartBulb.Tones.WARM, 10);
        c.addDevice(sm);
        c.addDevice(sm2);
        Fatura fatura = f.criaFatura(c, LocalDate.now(), LocalDate.now().plusDays(10));
        assertNotNull(fatura);
        assertEquals(fatura.getInicioPeriodo(), LocalDate.now());
        assertEquals(fatura.getFimPeriodo(), LocalDate.now().plusDays(10));
        assertEquals("EDP", fatura.getFornecedor());
        assertEquals("256", fatura.getNifCliente());
        assertEquals(10 * (sm.comsumption() + sm2.comsumption()), fatura.getConsumo());
        assertEquals(0.64395, fatura.getCusto());
    }

    @Test
    void faturacao() throws FornecedorErradoException, AlreadyExistDeviceException {
        Casa c = new Casa("Artur", "256", "EDP");
        Fornecedor f = new Fornecedor("EDP");
        SmartDevice sm = new SmartBulb(true, 0.20, SmartBulb.Tones.WARM, 10);
        SmartDevice sm2 = new SmartBulb(true, 0.20, SmartBulb.Tones.WARM, 10);
        c.addDevice(sm);
        c.addDevice(sm2);
        Fatura fatura = f.criaFatura(c, LocalDate.now(), LocalDate.now().plusDays(10));
        Fatura fatura2 = f.criaFatura(c, LocalDate.now(), LocalDate.now().plusDays(10));
        f.adicionaFatura(fatura2);
        f.adicionaFatura(fatura);
        assertNotNull(fatura);
        assertEquals(fatura.getInicioPeriodo(), LocalDate.now());
        assertEquals(fatura.getFimPeriodo(), LocalDate.now().plusDays(10));
        assertEquals(0.64395 * 2, f.faturacao());
    }
}