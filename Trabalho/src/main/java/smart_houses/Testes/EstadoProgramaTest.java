package smart_houses.Testes;

import org.junit.jupiter.api.Test;
import smart_houses.EstadoPrograma;
import smart_houses.Fatura;
import smart_houses.exceptions.AlreadyExistDeviceException;
import smart_houses.exceptions.ExisteCasaException;
import smart_houses.exceptions.ExisteFornecedorException;
import smart_houses.exceptions.FornecedorInexistenteException;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;
import smart_houses.smart_devices.SmartSpeaker;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class EstadoProgramaTest {

    @Test
    void getCasaMaisGastadora() throws FornecedorInexistenteException, ExisteCasaException, ExisteFornecedorException {
        Fornecedor f1 = new Fornecedor("EDP");
        Casa c = new Casa("Artur", "250", "EDP");
        Casa c2 = new Casa("Afonso", "251", "EDP");
        Casa c3 = new Casa("Goncalo", "252", "EDP");
        Fatura f = new Fatura("EDP", "250", 120, 100, LocalDate.now(), LocalDate.now().plusDays(10));
        Fatura f2 = new Fatura("EDP", "250", 180, 100, LocalDate.now(), LocalDate.now().plusDays(10));
        Fatura f3 = new Fatura("EDP", "250", 190, 100, LocalDate.now(), LocalDate.now().plusDays(10));
        Fatura f4 = new Fatura("EDP", "250", 80, 100, LocalDate.now(), LocalDate.now().plusDays(10));
        Fatura f5 = new Fatura("EDP", "250", 150, 100, LocalDate.now(), LocalDate.now().plusDays(10));
        c.adicionaFatura(f);
        c.adicionaFatura(f2);
        c2.adicionaFatura(f3);
        c2.adicionaFatura(f5);
        c3.adicionaFatura(f4);
        EstadoPrograma e = new EstadoPrograma();
        e.addFornecedor(f1);
        e.adicionaCasa(c);
        e.adicionaCasa(c2);
        e.adicionaCasa(c3);
        assertEquals(c2, e.getCasaMaisGastadora().get());
    }

    @Test
    void maiorConsumidorPeriodo() throws FornecedorInexistenteException, ExisteCasaException, ExisteFornecedorException {
        Fornecedor f1 = new Fornecedor("EDP");
        Casa c = new Casa("Artur", "250", "EDP");
        Casa c2 = new Casa("Afonso", "251", "EDP");
        Casa c3 = new Casa("Goncalo", "252", "EDP");
        Fatura f = new Fatura("EDP", "250", 120, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1));
        Fatura f2 = new Fatura("EDP", "250", 180, 100, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 3, 1));
        Fatura f3 = new Fatura("EDP", "250", 190, 100, LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 1));
        Fatura f4 = new Fatura("EDP", "250", 80, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1));
        Fatura f5 = new Fatura("EDP", "250", 150, 100, LocalDate.of(2018, 1, 1), LocalDate.of(2020, 1, 1));
        c.adicionaFatura(f);
        c.adicionaFatura(f2);
        c2.adicionaFatura(f3);
        c2.adicionaFatura(f5);
        c3.adicionaFatura(f4);
        EstadoPrograma e = new EstadoPrograma();
        e.addFornecedor(f1);
        e.adicionaCasa(c);
        e.adicionaCasa(c2);
        e.adicionaCasa(c3);
        assertEquals(List.of(c, c2), e.maiorConsumidorPeriodo(LocalDate.of(2020, 1, 1), LocalDate.of(2021, 1, 1), 2));
    }

    @Test
    void getFornecedorMaiorFaturacao() throws FornecedorInexistenteException, ExisteCasaException, ExisteFornecedorException {
        Fornecedor fornecedor2 = new Fornecedor("EDP");
        Fatura f = new Fatura("EDP", "250", 120, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1));
        Fatura f2 = new Fatura("EDP", "250", 180, 100, LocalDate.of(2020, 2, 1), LocalDate.of(2020, 3, 1));
        Fatura f3 = new Fatura("Endesa", "250", 190, 100, LocalDate.of(2020, 3, 1), LocalDate.of(2020, 4, 1));
        Fatura f4 = new Fatura("Endesa", "250", 80, 100, LocalDate.of(2020, 1, 1), LocalDate.of(2020, 2, 1));
        Fatura f5 = new Fatura("Endesa", "250", 150, 100, LocalDate.of(2018, 1, 1), LocalDate.of(2020, 1, 1));
        Fornecedor fornecedor1 = new Fornecedor("Endesa");
        fornecedor2.adicionaFatura(f);
        fornecedor2.adicionaFatura(f2);
        fornecedor1.adicionaFatura(f3);
        fornecedor1.adicionaFatura(f4);
        fornecedor1.adicionaFatura(f5);
        EstadoPrograma e = new EstadoPrograma();
        e.addFornecedor(fornecedor1);
        e.addFornecedor(fornecedor2);
        assertEquals(fornecedor1, e.getFornecedorMaiorFaturacao());

    }

    @Test
    void podiumDeviceMaisUsado() throws AlreadyExistDeviceException, FornecedorInexistenteException, ExisteCasaException, ExisteFornecedorException {

        SmartDevice sb1 = new SmartBulb();
        SmartDevice sb2 = new SmartBulb();
        SmartDevice sb3 = new SmartBulb();
        SmartDevice sc1 = new SmartCamera();
        SmartDevice sc2 = new SmartCamera();
        SmartDevice ss = new SmartSpeaker();
        Casa c1 = new Casa("Artur", "256", "EDP");
        Casa c2 = new Casa("Afonso", "257", "EDP");
        Casa c3 = new Casa("Goncalo", "258", "EDP");
        c1.addDevice(sb1);
        c1.addDevice(sc2);
        c2.addDevice(sc1);
        c3.addDevice(sb2);
        c2.addDevice(sb3);
        c3.addDevice(ss);
        EstadoPrograma e = new EstadoPrograma();
        e.addFornecedor(new Fornecedor("EDP"));
        e.adicionaCasa(c1);
        e.adicionaCasa(c2);
        e.adicionaCasa(c3);
        assertEquals(List.of("SmartBulb", "SmartCamera", "SmartSpeaker"), e.podiumDeviceMaisUsado());

    }
}