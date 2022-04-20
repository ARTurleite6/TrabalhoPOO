package smart_houses.modulo_casas;

import com.sun.source.tree.Tree;
import smart_houses.Fatura;
import smart_houses.smart_devices.SmartDevice;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

public class Casa implements Serializable {

    private String code;
    private String nome;
    private String nif;
    private Map<Integer, SmartDevice> devices;
    private Map<String, Set<Integer>> rooms;
    private Map<LocalDate, Fatura> faturas;
    private String fornecedor;

    public Casa(){
        this.code = "";
        this.nome = "";
        this.nif = "";
        this.devices = new HashMap<>();
        this.rooms = new HashMap<>();
        this.faturas = new TreeMap<>();
        this.fornecedor = "";
    }

    public Casa(String code, String nome, String nif, String fornecedor){
        this.code = code;
        this.nome = nome;
        this.nif = nif;
        this.devices = new HashMap<>();
        this.rooms = new HashMap<>();
        this.fornecedor = fornecedor;
        this.faturas = new TreeMap<>();
    }

    public Casa(String code, String nome, String nif, Set<String> rooms, String fornecedor){
        this.code = code;
        this.nome = nome;
        this.nif = nif;
        this.devices = new HashMap<>();
        this.setRooms(rooms);
        this.fornecedor = fornecedor;
        this.faturas = new TreeMap<>();
    }

    public Casa(String code, String nome, String nif, Map<String, Set<Integer>> rooms, String fornecedor){
        this.code = code;
        this.nome = nome;
        this.nif = nif;
        this.devices = new HashMap<>();
        this.setRooms(rooms);
        this.fornecedor = fornecedor;
        this.faturas = new TreeMap<>();
    }

    public Casa(String code, String nome, String nif, Map<Integer, SmartDevice> devices, Map<String, Set<Integer>> rooms, String fornecedor){
        this.code =code;
        this.nome = nome;
        this.nif = nif;
        this.setDevices(devices);
        this.setRooms(rooms);
        this.fornecedor = fornecedor;
        this.faturas = new TreeMap<>();
    }

    public Casa(Casa casa){
        this.code = casa.getCode();
        this.nome = casa.getNome();
        this.nif = casa.getNif();
        this.devices = casa.getMapDevices();
        this.rooms = casa.getRooms();
        this.fornecedor = casa.getFornecedor();
        this.faturas = casa.getFaturas();
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public Map<LocalDate, Fatura> getFaturas(){
        return this.faturas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setFaturas(Map<LocalDate, Fatura> faturas){
        this.faturas = faturas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Casa casa = (Casa) o;

        if (getCode() != null ? !getCode().equals(casa.getCode()) : casa.getCode() != null) return false;
        if (getNome() != null ? !getNome().equals(casa.getNome()) : casa.getNome() != null) return false;
        if (getNif() != null ? !getNif().equals(casa.getNif()) : casa.getNif() != null) return false;
        if (!Objects.equals(devices, casa.devices)) return false;
        if (getRooms() != null ? !getRooms().equals(casa.getRooms()) : casa.getRooms() != null) return false;
        if (getFaturas() != null ? !getFaturas().equals(casa.getFaturas()) : casa.getFaturas() != null) return false;
        return getFornecedor() != null ? getFornecedor().equals(casa.getFornecedor()) : casa.getFornecedor() == null;
    }

    @Override
    public int hashCode() {
        int result = getCode() != null ? getCode().hashCode() : 0;
        result = 31 * result + (getNome() != null ? getNome().hashCode() : 0);
        result = 31 * result + (getNif() != null ? getNif().hashCode() : 0);
        result = 31 * result + (devices != null ? devices.hashCode() : 0);
        result = 31 * result + (getRooms() != null ? getRooms().hashCode() : 0);
        result = 31 * result + (getFaturas() != null ? getFaturas().hashCode() : 0);
        result = 31 * result + (getFornecedor() != null ? getFornecedor().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "Casa{" +
                "code='" + code + '\'' +
                ", nome='" + nome + '\'' +
                ", nif='" + nif + '\'' +
                ", devices=" + devices +
                ", rooms=" + rooms +
                ", faturas=" + faturas +
                ", fornecedor='" + fornecedor + '\'' +
                '}';
    }

    public void setNif(String nif) {
        this.nif = nif;
    }

    public Map<Integer, SmartDevice> getMapDevices() {
        return this.devices.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public List<SmartDevice> getListDevices(){
        return this.devices.values().stream().map(SmartDevice::clone).collect(Collectors.toList());
    }

    public List<String> getListRooms(){
        return new ArrayList<>(this.rooms.keySet());
    }

    public void setDevices(Map<Integer, SmartDevice> devices) {
        this.devices = devices.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public Map<String, Set<Integer>> getRooms() {
        return this.rooms.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> new TreeSet<>(e.getValue())));
    }

    public void setRooms(Map<String, Set<Integer>> rooms) {
        this.rooms = rooms;
    }

    public void setRooms(Set<String> rooms) {
        this.rooms = rooms.stream().collect(Collectors.toMap(r -> r, r -> new TreeSet<>()));
    }

    public void adicionaFatura(Fatura fatura){
        this.faturas.put(fatura.getInicioPeriodo(), fatura.clone());
    }

    public Casa clone(){
        return new Casa(this);
    }

    public void setAllDevicesState(boolean state){
        this.devices.values().forEach(device -> device.setOn(state));
    }

    public void setDeviceState(int id, boolean state){
        this.devices.get(id).setOn(state);
    }

    public boolean existDevice(int id){
        return this.devices.containsKey(id);
    }

    public void addDevice(SmartDevice device){
        this.devices.put(device.getId(), device.clone());
    }

    public void removeDevice(int id){
        this.devices.remove(id);
        Iterator<Set<Integer>> d = this.rooms.values().iterator();
        boolean found = false;
        while(d.hasNext() && !found){
            Set<Integer> ds = d.next();
            found = ds.remove(id);
        }
    }

    public Fatura getLastFatura(){
        TreeMap<LocalDate, Fatura> f = new TreeMap<>(this.faturas);
        return f.lastEntry().getValue().clone();
    }

    public boolean existRoom(String room){
        return this.rooms.containsKey(room);
    }

    public void addRoom(String room){
        this.rooms.put(room, new TreeSet<>());
    }

    public void removeRoom(String room){
        this.rooms.remove(room);
    }

    public void addDeviceOnRoom(String room, int device) {
        this.rooms.get(room).add(device);
    }

    public boolean existDeviceOnRoom(String room, int device){
        return this.rooms.containsKey(room) && this.devices.containsKey(device) && this.rooms.get(room).contains(device);
    }

    public void removeDeviceOnRoom(String room, int device){
        this.rooms.get(room).remove(device);
    }

}
