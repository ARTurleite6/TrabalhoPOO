import java.util.Objects;

public class SmartSpeaker {
    private int Volume;
    private String estacaoRadio;
    private String marca;
    private double consumoDiario;

    public SmartSpeaker(){
        this.Volume=0;
        this.estacaoRadio="n/a";
        this.marca="n/a";
        this.consumoDiario=0;
    }

    public SmartSpeaker(int Volume, String estacaoRadio, String marca, double consumoDiario){
        this.Volume=Volume;
        this.estacaoRadio= estacaoRadio;
        this.marca=marca;
        this.consumoDiario=consumoDiario;
    }

    public SmartSpeaker(SmartSpeaker device){
        this.Volume= device.getVolume();
        this.estacaoRadio=device.getEstacaoRadio();
        this.marca=device.getMarca();
        this.consumoDiario= device.getConsumoDiario();
    }
    public int getVolume() {
        return Volume;
    }

    public void setVolume(int volume) {
        Volume = volume;
    }

    public String getEstacaoRadio() {
        return estacaoRadio;
    }

    public void setEstacaoRadio(String estacaoRadio) {
        this.estacaoRadio = estacaoRadio;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public double getConsumoDiario() {
        return consumoDiario;
    }

    public void setConsumoDiario(double consumoDiario) {
        this.consumoDiario = consumoDiario;
    }

    public String toString() {
        return "SmartSpeaker{" +
                "Volume=" + Volume +
                ", estacaoRadio='" + estacaoRadio + '\'' +
                ", marca='" + marca + '\'' +
                ", consumoDiario=" + consumoDiario +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SmartSpeaker that = (SmartSpeaker) o;
        return Volume == that.Volume && Double.compare(that.consumoDiario, consumoDiario) == 0 && Objects.equals(estacaoRadio, that.estacaoRadio) && Objects.equals(marca, that.marca);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Volume, estacaoRadio, marca, consumoDiario);
    }

    public SmartSpeaker clone(){
        return new SmartSpeaker(this);
    }
}
