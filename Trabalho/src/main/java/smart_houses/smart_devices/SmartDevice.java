package smart_houses.smart_devices;

import java.io.Serializable;

public abstract class SmartDevice implements Serializable {

    private static int next_id = 1;

    private boolean on;
    private int id;
    private double instalationCost;

    public SmartDevice(){
        this.on = false;
        this.id = SmartDevice.next_id++;
        this.instalationCost = 0;
    }

    public SmartDevice(boolean on, double instalationCost){
        this.id = SmartDevice.next_id++;
        this.on = on;
        this.instalationCost = instalationCost;
    }

    public SmartDevice(boolean on, int id, double instalationCost){
        this.on = on;
        this.id = id;
        this.instalationCost = instalationCost;
    }

    public SmartDevice(SmartDevice device){
        this.on = device.isOn();
        this.id = device.getId();
        this.instalationCost = device.getInstalationCost();
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

    public double getInstalationCost() {
        return instalationCost;
    }

    public void setInstalationCost(double instalationCost) {
        this.instalationCost = instalationCost;
    }

    @Override
    public String toString() {
        return "SmartDevice{" +
                "on=" + on +
                ", id='" + id + '\'' +
                ", instalationCost=" + instalationCost +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        SmartDevice that = (SmartDevice) o;

        if (isOn() != that.isOn()) return false;
        if (Double.compare(that.getInstalationCost(), getInstalationCost()) != 0) return false;
        return getId() == that.getId();
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = (isOn() ? 1 : 0);
        result = 31 * result + getId();
        temp = Double.doubleToLongBits(getInstalationCost());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

    public abstract SmartDevice clone();
    public abstract double comsumption();


}
