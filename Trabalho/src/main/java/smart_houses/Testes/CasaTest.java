package smart_houses.Testes;

import org.junit.jupiter.api.Test;
import smart_houses.Fatura;
import smart_houses.exceptions.AlreadyExistDeviceException;
import smart_houses.exceptions.DeviceInexistenteException;
import smart_houses.exceptions.RoomAlreadyExistsException;
import smart_houses.exceptions.RoomInexistenteException;
import smart_houses.modulo_casas.Casa;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CasaTest {

    @Test
    void adicionaFatura() {
        Casa c = new Casa("Artur", "256250278", "EDP");
        Fatura f1 = new Fatura();
        Fatura f2 = new Fatura();
        Fatura f3 = new Fatura();

        c.adicionaFatura(f1);
        c.adicionaFatura(f2);
        c.adicionaFatura(f3);

        assertEquals(List.of(f1, f2, f3), c.getFaturas());
    }

    @Test
    void testEquals() {
        Casa c = new Casa("Artur", "23", "EDP");
        Casa c2 = new Casa("Artur", "23", "EDP");

        assertTrue(c.equals(c2));
    }

    @Test
    void setAllDevicesStateRoom() throws RoomAlreadyExistsException, AlreadyExistDeviceException, DeviceInexistenteException, RoomInexistenteException {

        Casa c = new Casa("Artur", "23", "EDP");

        SmartDevice d = new SmartCamera();
        SmartDevice d2 = new SmartCamera();
        SmartDevice d3 = new SmartCamera();
        SmartDevice d4 = new SmartCamera();
        c.addRoom("Quarto");
        c.addDevice(d);
        c.addDevice(d2);
        c.addDevice(d3);
        c.addDevice(d4);
        c.addDeviceOnRoom("Quarto", d.getId());
        c.addDeviceOnRoom("Quarto", d2.getId());
        c.addDeviceOnRoom("Quarto", d3.getId());
        c.addDeviceOnRoom("Quarto", d4.getId());
        assertEquals(List.of(d, d2, d3, d4), c.getListDevices());
        c.setAllDevicesStateRoom("Quarto", true);
        assertTrue(c.getListDevices().stream().allMatch(SmartDevice::isOn));
    }

    @Test
    void setAllDevicesState() throws AlreadyExistDeviceException, RoomInexistenteException {
        Casa c = new Casa("Artur", "23", "EDP");

        SmartDevice d = new SmartCamera();
        SmartDevice d2 = new SmartCamera();
        SmartDevice d3 = new SmartCamera();
        SmartDevice d4 = new SmartCamera();
        c.addDevice(d);
        c.addDevice(d2);
        c.addDevice(d3);
        c.addDevice(d4);
        assertEquals(List.of(d, d2, d3, d4), c.getListDevices());
        c.setAllDevicesState(true);
        assertTrue(c.getListDevices().stream().allMatch(SmartDevice::isOn));
    }

    @Test
    void setDeviceState() throws AlreadyExistDeviceException, DeviceInexistenteException {
        Casa c = new Casa("Artur", "23", "EDP");

        SmartDevice d = new SmartCamera();
        SmartDevice d2 = new SmartCamera();
        SmartDevice d3 = new SmartCamera();
        SmartDevice d4 = new SmartCamera();
        c.addDevice(d);
        c.addDevice(d2);
        c.addDevice(d3);
        c.addDevice(d4);
        assertEquals(List.of(d, d2, d3, d4), c.getListDevices());
        c.setDeviceState(d.getId(), true);
        assertTrue(c.getDevice(d.getId()).isOn());
    }

    @Test
    void consumoDispositivos() throws AlreadyExistDeviceException {
        Casa c = new Casa("Artur", "23", "EDP");
        SmartDevice d = new SmartBulb(true, 0.20, SmartBulb.Tones.NEUTRAL, 20);
        SmartDevice d2 = new SmartBulb(true, 0.20, SmartBulb.Tones.NEUTRAL, 20);
        c.addDevice(d);
        c.addDevice(d2);
        assertEquals(d.comsumption() + d2.comsumption(), c.consumoDispositivos());
    }

    @Test
    void consumoPeriodo() {
        Casa c = new Casa("Artur", "23", "EDP");
        Fatura f = new Fatura("EDP", "23", 20, 10, LocalDate.of(2010, 1, 1), LocalDate.of(2012, 01, 01));
        Fatura f2 = new Fatura("EDP", "23", 20, 40, LocalDate.of(2012, 1, 1), LocalDate.of(2014, 01, 01));
        c.adicionaFatura(f);
        c.adicionaFatura(f2);
        assertEquals(40, c.consumoPeriodo());
    }

    @Test
    void testConsumoPeriodo() {
        Casa c = new Casa("Artur", "23", "EDP");
        Fatura f = new Fatura("EDP", "23", 20, 10, LocalDate.of(2010, 1, 1), LocalDate.of(2012, 1, 1));
        Fatura f2 = new Fatura("EDP", "23", 20, 40, LocalDate.of(2012, 1, 1), LocalDate.of(2014, 01, 01));
        Fatura f3 = new Fatura("EDP", "23", 20, 40, LocalDate.of(2008, 1, 1), LocalDate.of(2010, 01, 01));
        c.adicionaFatura(f);
        c.adicionaFatura(f2);
        c.adicionaFatura(f3);
        assertEquals(50, c.consumoPeriodo(LocalDate.of(2010, 1, 1),LocalDate.of(2020, 1, 1)));
    }

    @Test
    void mudaDeviceDeRoom() {
    }

    @Test
    void juntaRooms() {
    }

    @Test
    void alteraInfoBulb() {
    }

    @Test
    void alteraInfoCamera() {
    }

    @Test
    void alteraInfoSpeaker() {
    }
}