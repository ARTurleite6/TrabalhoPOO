import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Casa {
    private String nome;
    private String nif;
    private Map<String, SmartDevice> devices;
    private Map<String, Set<String>> rooms;

    public Casa(){
        this.nome = "n/a";
        this.nif = "n/a";
        this.devices = new HashMap<>();
        this.rooms = new HashMap<>();
    }

    public Casa(String nome, String nif){
        this.nome = nome;
        this.nif = nif;
        this.devices = new HashMap<>();
        this.rooms = new HashMap<>();
    }

    public Casa(String nome, String nif, Map<String, SmartDevice> devices, Map<String, Set<String>> rooms){
        this.nome = nome;
        this.nif = nif;
        this.setDevices(devices);
        this.setRooms(rooms);
    }

    public Casa(Casa casa){
        this.nome = casa.getNome();
        this.nif = casa.getNif();
        this.devices = casa.getDevices();
        this.rooms = casa.getRooms();
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNif() {
        return nif;
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Map<String, SmartDevice> getDevices() {
        return this.devices.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setDevices(Map<String, SmartDevice> devices) {
        this.devices.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public Map<String, Set<String>> getRooms() {
        return rooms;
    }

    public void setRooms(Map<String, Set<String>> rooms) {
        this.rooms = rooms;
    }

    @Override
    public String toString() {
        return "Casa{" +
                "nome='" + nome + '\'' +
                ", nif='" + nif + '\'' +
                ", devices=" + devices +
                ", rooms=" + rooms +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Casa casa = (Casa) o;

        if (getNome() != null ? !getNome().equals(casa.getNome()) : casa.getNome() != null) return false;
        if (getNif() != null ? !getNif().equals(casa.getNif()) : casa.getNif() != null) return false;
        if (getDevices() != null ? !getDevices().equals(casa.getDevices()) : casa.getDevices() != null) return false;
        return getRooms() != null ? getRooms().equals(casa.getRooms()) : casa.getRooms() == null;
    }

    @Override
    public int hashCode() {
        int result = getNome() != null ? getNome().hashCode() : 0;
        result = 31 * result + (getNif() != null ? getNif().hashCode() : 0);
        result = 31 * result + (getDevices() != null ? getDevices().hashCode() : 0);
        result = 31 * result + (getRooms() != null ? getRooms().hashCode() : 0);
        return result;
    }

    public Casa clone(){
        return new Casa(this);
    }
}
