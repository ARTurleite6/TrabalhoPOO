package smart_houses.smart_devices;

public class SmartSpeaker extends SmartDevice{

    private static final int MAX = 100;

    private int volume;
    private String radioStation;
    private String brand;

    public SmartSpeaker(){
        super();
        this.volume=0;
        this.radioStation="n/a";
        this.brand="n/a";
    }

    public SmartSpeaker(boolean on, double consume, int volume, String radioStation, String brand){
        super(on, consume);
        this.volume=volume;
        this.radioStation= radioStation;
        this.brand=brand;
    }

    public SmartSpeaker(SmartSpeaker device){
        super(device);
        this.volume= device.getVolume();
        this.radioStation=device.getRadioStation();
        this.brand=device.getBrand();
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

    public String toString() {
        return "SmartSpeaker{" +
                "volume=" + volume +
                ", radioStation='" + radioStation + '\'' +
                ", brand='" + brand + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        SmartSpeaker that = (SmartSpeaker) o;

        if (getVolume() != that.getVolume()) return false;
        if (!getRadioStation().equals(that.getRadioStation())) return false;
        return getBrand().equals(that.getBrand());
    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + getVolume();
        result = 31 * result + getRadioStation().hashCode();
        result = 31 * result + getBrand().hashCode();
        return result;
    }

    public SmartSpeaker clone(){
        return new SmartSpeaker(this);
    }

    public double comsumption(){
        return (this.isOn() ? 1 : 0) * (this.getConsume() + this.volume);
    }
}
