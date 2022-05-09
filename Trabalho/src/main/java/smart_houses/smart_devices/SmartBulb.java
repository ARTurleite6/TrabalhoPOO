package smart_houses.smart_devices;

import java.io.Serializable;

public class SmartBulb extends SmartDevice implements Serializable {
    public static enum Tones{
        NEUTRAL(2), WARM(1), COLD(3);

        private double consume;

        Tones(double consume){
            this.consume = consume;
        }

        public double getConsume(){
            return this.consume;
        }
    }

    private Tones tone;
    private int dimension;

    public SmartBulb(){
        super();
        this.tone = Tones.NEUTRAL;
        this.dimension = 20;
    }

    public SmartBulb(boolean on, double consume, Tones tone, int dimension){
        super(on, consume);
        this.tone = tone;
        this.dimension = dimension;
    }

    public SmartBulb(SmartBulb device){
        super(device);
        this.tone = device.getTone();
        this.dimension = device.getDimension();
    }

    public Tones getTone() {
        return tone;
    }

    public void setTone(Tones tone) {
        this.tone = tone;
    }

    public int getDimension() {
        return dimension;
    }

    public void setDimension(int dimension) {
        this.dimension = dimension;
    }

    public double comsumption(){
        return (this.isOn() ? 1 : 0) * (this.getConsume() + tone.getConsume());
    }

    public SmartBulb clone(){
        return new SmartBulb(this);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("SmartBulb{").append("id=").append(this.getId()).append(", on=").append(this.isOn()).append(", tone=").append(tone).append(", dimension=").append(dimension).append(", consume=").append(this.getConsume()).append('}').toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SmartBulb smartBulb = (SmartBulb) o;

        if (getDimension() != smartBulb.getDimension()) return false;
        return getTone() == smartBulb.getTone();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getTone().hashCode();
        result = 31 * result + getDimension();
        return result;
    }

}
