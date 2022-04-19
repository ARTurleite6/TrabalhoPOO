package smart_houses;

import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartDevice;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class EstadoPrograma implements Serializable{
    private final Map<String, Casa> casas;
    private final Map<String, Fornecedor> fornecedores;
    private LocalDate data;

    public static final double custoEnergia = 4.8;
    public static final double imposto = 0.06;


    public EstadoPrograma(){
        this.casas = new HashMap<>();
        this.fornecedores = new HashMap<>();
        this.data = LocalDate.now();
    }

    public EstadoPrograma(EstadoPrograma c){
        this.casas = c.getCasas();
        this.fornecedores = c.getFornecedores();
        this.data = c.getData();
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    private void geraFaturas(int days){
        this.casas.values().forEach(casa -> {
            Fatura f = this.fornecedores.get(casa.getFornecedor()).criaFatura(casa.getNif(), casa.getSetDevices(), this.data, this.data.plusDays(days));
            casa.adicionaFatura(f);
        });
    }

    private void geraFaturas(LocalDate fim){
        this.casas.values().forEach(casa -> {
            Fatura f = this.fornecedores.get(casa.getFornecedor()).criaFatura(casa.getNif(), casa.getSetDevices(), this.data, fim);
            casa.adicionaFatura(f);
        });
    }

    public List<Fatura> getFaturasFornecedor(String nome){
        return this.casas.values().stream().
                flatMap(c -> c.getFaturas().stream()).
                filter(f -> f.getFornecedor().equals(nome)).
                map(Fatura::clone).
                collect(Collectors.toList());
    }

    public void avancaData(){
        this.geraFaturas(1);
        this.data = this.data.plusDays(1);
    }

    public void avancaData(int days){
        this.geraFaturas(days);
        this.data = this.data.plusDays(days);
    }

    public void avancaData(LocalDate date){
        this.geraFaturas(date);
        this.data = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstadoPrograma that = (EstadoPrograma) o;

        if (getCasas() != null ? !getCasas().equals(that.getCasas()) : that.getCasas() != null) return false;
        if (getFornecedores() != null ? !getFornecedores().equals(that.getFornecedores()) : that.getFornecedores() != null)
            return false;
        return getData() != null ? getData().equals(that.getData()) : that.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = getCasas() != null ? getCasas().hashCode() : 0;
        result = 31 * result + (getFornecedores() != null ? getFornecedores().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }

    public EstadoPrograma carregaDados(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/main/resources/teste.txt"));
            return (EstadoPrograma) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public Map<String, Casa> getCasas() {
        return this.casas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public Map<String, Fornecedor> getFornecedores() {
        return this.fornecedores.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void adicionaCasa(Casa c){
        this.casas.put(c.getNif(), c.clone());
    }

    public boolean existeFornecedor(String nome){
        return this.fornecedores.containsKey(nome);
    }

    public boolean existeCasa(String nif){
        return this.casas.containsKey(nif);
    }

    public void addDeviceToCasa(String nif, SmartDevice device){
        this.casas.get(nif).addDevice(device);
    }

    @Override
    public String toString() {
        return "EstadoPrograma{" +
                "casas=" + casas +
                ", fornecedores=" + fornecedores +
                ", data=" + data +
                '}';
    }

    public EstadoPrograma clone(){
        return new EstadoPrograma(this);
    }

    public void guardaDados(){
        try {
            FileOutputStream file = new FileOutputStream("./src/main/resources/teste.txt");
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeObject(this);
            oos.close();
        } catch (FileNotFoundException e) {
            System.out.println("Este ficheiro nao existe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addDeviceToCasaOnRoom(String nif, String room, int id) {
        this.casas.get(nif).addDeviceOnRoom(room, id);
    }

    public List<String> getRoomsHouse(String nif) {
        return this.casas.get(nif).getListRooms();
    }

    public void setAllDevicesHouseOn(String nif, boolean ligar) {
        this.casas.get(nif).setAllDevicesState(ligar);
    }

    public Set<SmartDevice> getSetDevicesHouse(String nif) {
        return this.casas.get(nif).getSetDevices();
    }

    public void setDeviceHouseOn(String nif, int id, boolean ligar) {
        this.casas.get(nif).setDeviceState(id, ligar);
    }

    public void addFornecedor(Fornecedor f) {
        this.fornecedores.put(f.getName(), f.clone());
    }
}
