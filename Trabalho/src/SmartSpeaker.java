import java.util.Objects;

public class SmartSpeaker extends SmartDevice{

    private static final int MAX = 100;

    private int volume;
    private String radioStation;
    private String brand;
    private double baseComsumption;

    public SmartSpeaker(){
        super();
        this.volume=0;
        this.radioStation="n/a";
        this.brand="n/a";
        this.baseComsumption=0;
    }

    public SmartSpeaker(boolean on, double instalationCost, int volume, String radioStation, String brand, double baseComsumption){
        super(on, instalationCost);
        this.volume=volume;
        this.radioStation= radioStation;
        this.brand=brand;
        this.baseComsumption=baseComsumption;
    }

    public SmartSpeaker(int id, boolean on, double instalationCost, int volume, String radioStation, String brand, double baseComsumption){
        super(on, id, instalationCost);
        this.volume=volume;
        this.radioStation= radioStation;
        this.brand=brand;
        this.baseComsumption=baseComsumption;
    }

    public SmartSpeaker(SmartSpeaker device){
        super(device);
        this.volume= device.getVolume();
        this.radioStation=device.getRadioStation();
        this.brand=device.getBrand();
        this.baseComsumption= device.getBaseComsumption();
    }
    public int getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        if(volume > 100) volume = SmartSpeaker.MAX;
        else if(volume < 0) volume = 0;
        this.volume = volume;
    }

    public void volumeUp(){
        this.volume += (this.volume < SmartSpeaker.MAX) ? 1 : 0;
    }

    public void volumeDown(){
        this.volume -= (this.volume > 0) ? 1 : 0;
    }

    public String getRadioStation() {
        return radioStation;
    }

    public void setRadioStation(String radioStation) {
        this.radioStation = radioStation;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public double getBaseComsumption() {
        return baseComsumption;
    }

    public void setBaseComsumption(double baseComsumption) {
        this.baseComsumption = baseComsumption;
    }

    public String toString() {
        return "SmartSpeaker{" +
                "volume=" + volume +
                ", radioStation='" + radioStation + '\'' +
                ", brand='" + brand + '\'' +
                ", baseComsumption=" + baseComsumption +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartSpeaker that = (SmartSpeaker) o;
        return super.equals(o) && volume == that.volume && Double.compare(that.baseComsumption, baseComsumption) == 0 && Objects.equals(radioStation, that.radioStation) && Objects.equals(brand, that.brand);
    }

    @Override
    public int hashCode() {
        return Objects.hash(volume, radioStation, brand, baseComsumption);
    }

    public SmartSpeaker clone(){
        return new SmartSpeaker(this);
    }

    public double comsumption(){
        return baseComsumption + this.volume;
    }
}
