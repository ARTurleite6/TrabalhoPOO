package smart_houses.smart_devices;

import java.io.Serializable;

public abstract class SmartDevice implements Serializable {

    private static int next_id = 1;

    private boolean on;
    private int id;
    private double consume;

    public SmartDevice(){
        this.on = false;
        this.id = SmartDevice.next_id++;
        this.consume = 0;
    }

    public SmartDevice(boolean on, double consume){
        this.id = SmartDevice.next_id++;
        this.on = on;
        this.consume = consume;
    }

    public SmartDevice(SmartDevice device){
        this.on = device.isOn();
        this.id = device.getId();
        this.consume = device.getConsume();
    }

    public boolean isOn() {
        return on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public double getConsume() {
        return this.consume;
    }

    public void setConsume(double consume) {
        this.consume = consume;
    }

    public abstract String toString();

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmartDevice that = (SmartDevice) o;

        if (isOn() != that.isOn()) return false;
        if (Double.compare(that.getConsume(), getConsume()) != 0) return false;
        return getId() == that.getId();
    }

    public int hashCode() {
        int result;
        long temp;
        result = (isOn() ? 1 : 0);
        result = 31 * result + getId();
        temp = Double.doubleToLongBits(getConsume());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public abstract SmartDevice clone();
    public abstract double comsumption();


}
