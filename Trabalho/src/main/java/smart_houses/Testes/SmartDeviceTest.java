package smart_houses.Testes;

import org.junit.jupiter.api.Test;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;
import smart_houses.smart_devices.SmartSpeaker;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SmartDeviceTest {

    @Test
    void comsumption() {
        SmartDevice sb1 = new SmartBulb(true, 0.20, SmartBulb.Tones.NEUTRAL, 20);
        SmartDevice sb2 = new SmartBulb(true, 0.20, SmartBulb.Tones.WARM, 20);
        SmartDevice sb3 = new SmartBulb(true, 0.20, SmartBulb.Tones.COLD, 20);

        assertEquals(0.23, sb1.comsumption());
        assertEquals(0.25, sb2.comsumption());
        assertEquals(0.22, sb3.comsumption());

        SmartDevice sc = new SmartCamera(true, 0.20, 1920, 1080, 50);
        assertEquals(2.3236, sc.comsumption());

        SmartDevice ss = new SmartSpeaker(true, 0.20, 70, "RFM", "Sony");

        assertEquals(0.27, ss.comsumption());
    }
}