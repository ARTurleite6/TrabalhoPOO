package smart_houses.Testes;

import org.junit.jupiter.api.Test;
import smart_houses.EstadoPrograma;
import smart_houses.Fatura;
import smart_houses.exceptions.*;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;
import smart_houses.smart_devices.SmartSpeaker;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EstadoProgramaTest {

    @Test
    void getFaturasFornecedor() {

        EstadoPrograma estado = new EstadoPrograma();
        Fornecedor f = new Fornecedor("EDP") ;
        Fornecedor f2 = new Fornecedor("Eneba") ;
        Casa c = new Casa("Artur", "256250278", "EDP");
        Casa c2 = new Casa("Joao", "2562508", "Eneba");
        Fatura fatura1 = null;
        try {
            fatura1 = f.criaFatura(c, LocalDate.now().minusDays(10), LocalDate.now());
        } catch (FornecedorErradoException e) {
            e.printStackTrace();
        }
        Fatura fatura2 = null;
        try {
            fatura2 = f2.criaFatura(c2, LocalDate.now().minusDays(20), LocalDate.now().minusDays(10));
        } catch (FornecedorErradoException e) {
            e.printStackTrace();
        }
        assert fatura1 != null;
        c.adicionaFatura(fatura1);
        assert fatura2 != null;
        c2.adicionaFatura(fatura2);
        System.out.println(c);
        System.out.println(c2);
        try {
            estado.addFornecedor(f);
        } catch (ExisteFornecedorException e) {
            e.printStackTrace();
        }
        try {
            estado.addFornecedor(f2);
        } catch (ExisteFornecedorException e) {
            e.printStackTrace();
        }
        try {
            estado.adicionaCasa(c);
        } catch (ExisteCasaException | FornecedorInexistenteException e) {
            e.printStackTrace();
        }
        try {
            estado.adicionaCasa(c2);
        } catch (ExisteCasaException | FornecedorInexistenteException e) {
            e.printStackTrace();
        }

        List<Fatura> faturas = List.of(fatura2);

        assertEquals(faturas, estado.getFaturasFornecedor("Eneba"));
    }

    @Test
    void geraFaturas() throws ExisteFornecedorException, FornecedorInexistenteException, ExisteCasaException, DataInvalidaException, FornecedorErradoException, CasaInexistenteException {
        EstadoPrograma e = new EstadoPrograma();
        Fornecedor f = new Fornecedor("EDP");
        Casa c = new Casa("Artur", "250", "EDP");
        e.addFornecedor(f);
        e.adicionaCasa(c);
        e.avancaData(LocalDate.now().plusDays(1));
        assertEquals(1, e.getCasa("250").getFaturas().size());
    }

    @Test
    void getCasaMaisGastadora() throws ExisteFornecedorException, FornecedorInexistenteException, ExisteCasaException, FornecedorErradoException {
        EstadoPrograma e = new EstadoPrograma();
        Fornecedor f = new Fornecedor("EDP");
        Casa c1 = new Casa("Artur", "256250278", "EDP");
        Casa c2 = new Casa("Artur", "256250", "EDP");
        try {
            c1.addDevice(new SmartBulb(true, 10, SmartBulb.Tones.NEUTRAL, 10));
        } catch (AlreadyExistDeviceException ex) {
            ex.printStackTrace();
        }
        try {
            c2.addDevice(new SmartBulb(true, 10, SmartBulb.Tones.NEUTRAL, 5));
        } catch (AlreadyExistDeviceException ex) {
            ex.printStackTrace();
        }
        Fatura fatura = f.criaFatura(c1, LocalDate.now().minusDays(10), LocalDate.now());
        Fatura fatura2 = f.criaFatura(c2, LocalDate.now().minusDays(10), LocalDate.now());
        c1.adicionaFatura(fatura);
        c2.adicionaFatura(fatura2);
        e.addFornecedor(f);
        e.adicionaCasa(c1);
        e.adicionaCasa(c2);

        assertEquals(Optional.of(c1), e.getCasaMaisGastadora());
    }

    @Test
    void maiorConsumidorPeriodo() {
    }

    @org.junit.jupiter.api.Test
    void faturacaoFornecedor() {
    }

    @org.junit.jupiter.api.Test
    void getFornecedorMaiorFaturacao() {
    }

    @org.junit.jupiter.api.Test
    void podiumDispositivos(){
        EstadoPrograma e = new EstadoPrograma();
        Fornecedor f = new Fornecedor("EDP");
        Casa c = new Casa("Artur", "256", "EDP");
        Casa c2 = new Casa("Artur", "200", "EDP");
        SmartDevice sm1 = new SmartBulb();
        SmartDevice sm2 = new SmartBulb();
        SmartDevice sm3 = new SmartBulb();
        SmartDevice sm4 = new SmartSpeaker();
        SmartDevice sm5 = new SmartSpeaker();
        SmartDevice sm6 = new SmartCamera();
        try {
            c.addDevice(sm1);
        } catch (AlreadyExistDeviceException ex) {
            ex.printStackTrace();
        }
        try {
            c.addDevice(sm2);
        } catch (AlreadyExistDeviceException ex) {
            ex.printStackTrace();
        }
        try {
            c2.addDevice(sm3);
        } catch (AlreadyExistDeviceException ex) {
            ex.printStackTrace();
        }
        try {
            c2.addDevice(sm4);
        } catch (AlreadyExistDeviceException ex) {
            ex.printStackTrace();
        }
        try {
            c.addDevice(sm5);
        } catch (AlreadyExistDeviceException ex) {
            ex.printStackTrace();
        }
        try {
            c.addDevice(sm6);
        } catch (AlreadyExistDeviceException ex) {
            ex.printStackTrace();
        }
        try {
            e.addFornecedor(f);
        } catch (ExisteFornecedorException ex) {
            ex.printStackTrace();
        }
        try {
            e.adicionaCasa(c);
        } catch (ExisteCasaException | FornecedorInexistenteException ex) {
            ex.printStackTrace();
        }
        try {
            e.adicionaCasa(c2);
        } catch (ExisteCasaException | FornecedorInexistenteException ex) {
            ex.printStackTrace();
        }
        List<String> podium = List.of("SmartBulb", "SmartSpeaker", "SmartCamera");
        assertEquals(podium, e.podiumDeviceMaisUsado());
    }
}