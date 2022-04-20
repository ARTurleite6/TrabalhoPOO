package smart_houses.smart_devices;

public class SmartBulb extends SmartDevice{
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
    private double baseConsume;

    public SmartBulb(){
        super();
        this.tone = Tones.NEUTRAL;
        this.dimension = 20;
        this.baseConsume = 10;
    }

    public SmartBulb(boolean on, double instalationCost, Tones tone, int dimension, double baseConsume){
        super(on, instalationCost);
        this.tone = tone;
        this.dimension = dimension;
        this.baseConsume = baseConsume;
    }

    public SmartBulb(SmartBulb device){
        super(device);
        this.tone = device.getTone();
        this.dimension = device.getDimension();
        this.baseConsume = device.getBaseConsume();
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

    public double getBaseConsume() {
        return baseConsume;
    }

    public void setBaseConsume(double baseConsume) {
        this.baseConsume = baseConsume;
    }

    public double comsumption(){
        return this.baseConsume + tone.getConsume();
    }

    public SmartBulb clone(){
        return new SmartBulb(this);
    }

    @Override
    public String toString() {
        return new StringBuilder().append("SmartBulb{").append("id=").append(this.getId()).append(", on=").append(this.isOn()).append(", instalationCost=").append(this.getInstalationCost()).append(", tone=").append(tone).append(", dimension=").append(dimension).append(", baseConsume=").append(baseConsume).append('}').toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SmartBulb smartBulb = (SmartBulb) o;

        if (getDimension() != smartBulb.getDimension()) return false;
        if (Double.compare(smartBulb.getBaseConsume(), getBaseConsume()) != 0) return false;
        return getTone() == smartBulb.getTone();
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        long temp;
        result = 31 * result + getTone().hashCode();
        result = 31 * result + getDimension();
        temp = Double.doubleToLongBits(getBaseConsume());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        return result;
    }

}
