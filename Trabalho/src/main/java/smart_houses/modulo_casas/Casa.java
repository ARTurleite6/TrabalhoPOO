package smart_houses.modulo_casas;

import smart_houses.Fatura;
import smart_houses.exceptions.*;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;
import smart_houses.smart_devices.SmartSpeaker;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class Casa implements Serializable {

    private String nome;
    private String nif;
    private Map<Integer, SmartDevice> devices;
    private Map<String, Set<Integer>> rooms;
    private List<Fatura> faturas;
    private String fornecedor;

    public Casa(){
        this.nome = "";
        this.nif = "";
        this.devices = new HashMap<>();
        this.rooms = new HashMap<>();
        this.fornecedor = "";
        this.faturas = new ArrayList<>();
    }

    public Casa(String nome, String nif, String fornecedor){
        this.nome = nome;
        this.nif = nif;
        this.devices = new HashMap<>();
        this.rooms = new HashMap<>();
        this.fornecedor = fornecedor;
        this.faturas = new ArrayList<>();
    }

    public Casa(String nome, String nif, Set<String> rooms, String fornecedor){
        this.nome = nome;
        this.nif = nif;
        this.devices = new HashMap<>();
        this.setRooms(rooms);
        this.fornecedor = fornecedor;
        this.faturas = new ArrayList<>();
    }

    public Casa(String nome, String nif, Map<String, Set<Integer>> rooms, String fornecedor){
        this.nome = nome;
        this.nif = nif;
        this.devices = new HashMap<>();
        this.setRooms(rooms);
        this.fornecedor = fornecedor;
        this.faturas = new ArrayList<>();
    }

    public Casa(String nome, String nif, Map<Integer, SmartDevice> devices, Map<String, Set<Integer>> rooms, String fornecedor){
        this.nome = nome;
        this.nif = nif;
        this.setDevices(devices);
        this.setRooms(rooms);
        this.fornecedor = fornecedor;
        this.faturas = new ArrayList<>();
    }

    public Casa(Casa casa){
        this.nome = casa.getNome();
        this.nif = casa.getNif();
        this.devices = casa.getMapDevices();
        this.rooms = casa.getRooms();
        this.fornecedor = casa.getFornecedor();
        this.faturas = casa.getFaturas();
    }

    public void setFaturas(List<Fatura> faturas){
        this.faturas = faturas.stream().map(Fatura::clone).collect(Collectors.toList());
    }

    public void adicionaFatura(Fatura fatura){
        this.faturas.add(fatura.clone());
    }

    public List<Fatura> getFaturas() {
        return this.faturas.stream().map(Fatura::clone).collect(Collectors.toList());
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

    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Casa casa = (Casa) o;

        if (!getNome().equals(casa.getNome())) return false;
        if (!getNif().equals(casa.getNif())) return false;
        if (!devices.equals(casa.devices)) return false;
        if (!getRooms().equals(casa.getRooms())) return false;
        if (!getFaturas().equals(casa.getFaturas())) return false;
        return getFornecedor().equals(casa.getFornecedor());
    }

    @Override
    public int hashCode() {
        int result = 7;
        result = 31 * result + getNome().hashCode();
        result = 31 * result + getNif().hashCode();
        result = 31 * result + devices.hashCode();
        result = 31 * result + getRooms().hashCode();
        result = 31 * result + getFaturas().hashCode();
        result = 31 * result + getFornecedor().hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Casa{" +
                "nome='" + nome + '\'' +
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

    public void setAllDevicesStateRoom(String room, boolean on) throws RoomInexistenteException {
      Set<Integer> devices = this.rooms.get(room);
      if(devices == null) throw new RoomInexistenteException("Nao existe a divisao : " + room + " nesta casa");
        this.rooms.get(room).forEach(device -> this.devices.get(device).setOn(on));
    }

    public Casa clone(){
        return new Casa(this);
    }

    public void setAllDevicesState(boolean state){
        this.devices.values().forEach(device -> device.setOn(state));
    }

    public void setDeviceState(int id, boolean state) throws DeviceInexistenteException {
        SmartDevice device = this.devices.get(id);
        if(device == null) throw new DeviceInexistenteException("Não existe o device de id " + id + "nesta casa");
        this.devices.get(id).setOn(state);
    }

    public boolean existDevice(int id){
        return this.devices.containsKey(id);
    }

    public void addDevice(SmartDevice device) throws AlreadyExistDeviceException {
        if(this.devices.containsKey(device.getId())) throw new AlreadyExistDeviceException("Já existe um device com o código " + device.getId());
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

    public boolean existRoom(String room){
        return this.rooms.containsKey(room);
    }

    public void addRoom(String room) throws RoomAlreadyExistsException{
        if(this.rooms.containsKey(room)) throw new RoomAlreadyExistsException("A divisao: " + room + " já existe nesta casa");
        this.rooms.put(room, new TreeSet<>());
    }

    public void removeRoom(String room){
        this.rooms.remove(room);
    }

    public void addDeviceOnRoom(String room, int device) throws RoomInexistenteException {
      Set<Integer> devices = this.rooms.get(room);
      if(devices == null) throw new RoomInexistenteException("Esta room nao existe na casa");
        this.rooms.get(room).add(device);
    }

    public boolean existDeviceOnRoom(String room, int device){
        return this.rooms.containsKey(room) && this.devices.containsKey(device) && this.rooms.get(room).contains(device);
    }

    public void removeDeviceOnRoom(int device) throws DeviceInexistenteException {
        if(!this.devices.containsKey(device)) throw new DeviceInexistenteException("Não existe dispositivo com código " + device);
        String room = divisaoDeDispositivo(device);
        if(room != null){
            this.rooms.get(room).remove(device);
        }
    }

    public double consumoDispositivos(){
        return this.devices.values().stream().mapToDouble(SmartDevice::comsumption).sum();
    }

    public double consumoPeriodo(LocalDate inicio, LocalDate fim){
        return this.faturas.stream().filter(f -> (f.getInicioPeriodo().isEqual(inicio) || f.getInicioPeriodo().isAfter(inicio)) && (f.getFimPeriodo().isEqual(fim) || f.getFimPeriodo().isBefore(fim))).mapToDouble(Fatura::getConsumo).sum();
    }

    public List<Fatura> faturasFornecedor(String fornecedor){
        return this.faturas.stream().filter(f -> f.getFornecedor().equals(fornecedor)).toList();
    }

    public int quantos(){
        return this.devices.size();
    }

    private String divisaoDeDispositivo (int device){
        String room = null;
        Iterator<Map.Entry<String, Set<Integer>>> iter = this.rooms.entrySet().iterator();

        while(iter.hasNext() && room == null){
            Map.Entry<String, Set<Integer>> div = iter.next();
            Iterator<Integer> iter1 = div.getValue().iterator();
            while(iter1.hasNext() && room == null){
                int dev = iter1.next();
                if(dev == device) room = div.getKey();
            }
        }

        return room;
    }

    public void mudaDeviceDeRoom(String room, int device) throws DeviceInexistenteException, RoomInexistenteException {
        if(!this.devices.containsKey(device)) throw new DeviceInexistenteException("Não existe o device de código : " + device);
        if(!this.rooms.containsKey(room)) throw new RoomInexistenteException("Não existe a divisao " + room + " nesta casa");

        String divisao = this.divisaoDeDispositivo(device);
        if(room != null){
            this.rooms.get(divisao).remove(device);
        }
        this.rooms.get(room).add(device);
    }

    public void juntaRooms(String room1, String room2, String nova) throws RoomAlreadyExistsException {
        if(this.rooms.containsKey(nova)) throw new RoomAlreadyExistsException("Esta room ja existe na casa");

        Set<Integer> devices1 = this.rooms.get(room1);
        Set<Integer> devices2 = this.rooms.get(room2);

        this.rooms.put(nova, new TreeSet<>());
        if(devices1 != null) devices1.forEach(d -> this.rooms.get(nova).add(d));
        if(devices2 != null) devices2.forEach(d -> this.rooms.get(nova).add(d));
        this.rooms.remove(room1);
        this.rooms.remove(room2);

    }

    public void alteraInfoBulb(int id, Consumer<SmartBulb> mapperBulb) throws DeviceInexistenteException, TipoDeviceErradoException {
      SmartDevice device = this.devices.get(id);
      if(device == null) throw new DeviceInexistenteException("Não existe um device com id de " + id + " na casa de nif " + this.nif);

      if(!(device instanceof SmartBulb)) throw new TipoDeviceErradoException("O tipo de device procurado não é do tipo SmartBulb");

      mapperBulb.accept((SmartBulb) device);

    }

    public void alteraInfoCamera(int id, Consumer<SmartCamera> mapperCamera) throws DeviceInexistenteException, TipoDeviceErradoException {
      SmartDevice device = this.devices.get(id);
      if(device == null) throw new DeviceInexistenteException("Não existe um device com id de " + id + " na casa de nif " + this.nif);

      if(!(device instanceof SmartCamera)) throw new TipoDeviceErradoException("O tipo de device procurado não é do tipo SmartCamera");

      mapperCamera.accept((SmartCamera) device);

    }

    public void alteraInfoSpeaker(int id, Consumer<SmartSpeaker> mapperSpeaker) throws DeviceInexistenteException, TipoDeviceErradoException {
      SmartDevice device = this.devices.get(id);
      if(device == null) throw new DeviceInexistenteException("Não existe um device com id de " + id + " na casa de nif " + this.nif);

      if(!(device instanceof SmartSpeaker)) throw new TipoDeviceErradoException("O tipo de device procurado não é do tipo SmartSpeaker");

      mapperSpeaker.accept((SmartSpeaker) device);

    }

    public SmartDevice getDevice(int id) throws DeviceInexistenteException {
      if(!this.devices.containsKey(id)) throw new DeviceInexistenteException("Não existe o device de id " + id + " na casa de nif " + this.nif);
      return this.devices.get(id);
    }

}
