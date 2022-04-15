import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectionCasas {
    private Map<String, Casa> houses;

    public CollectionCasas(){
        this.houses = new HashMap<>();
    }

    public CollectionCasas(Map<String, Casa> houses){
        this.setHouses(houses);
    }

    public CollectionCasas(CollectionCasas houses){
        this.houses = houses.getHouses();
    }

    public Map<String, Casa> getHouses (){
        return this.houses.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setHouses(Map<String, Casa> houses){
        this.houses = houses.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public Casa getCasa(String nif){
        return this.houses.get(nif).clone();
    }

    public void addCasa(Casa casa){
        this.houses.put(casa.getNif(), casa.clone());
    }

    public void removeCasa(String nif){
        this.houses.remove(nif);
    }

    public boolean existCasa(String nif){
        return this.houses.containsKey(nif);
    }

    public void addDeviceToCasa(String nif, SmartDevice device){
        this.houses.get(nif).addDevice(device.clone());
    }

    public void addDeviceToCasaOnRoom(String nif, String room, int id){
        this.houses.get(nif).addDeviceOnRoom(room, id);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectionCasas that = (CollectionCasas) o;

        return getHouses().equals(that.getHouses());
    }

    @Override
    public int hashCode() {
        return getHouses().hashCode();
    }

    @Override
    public String toString() {
        return "CollectionCasas{" +
                "houses=" + houses +
                '}';
    }

    public CollectionCasas clone(){
        return new CollectionCasas(this);
    }
}
