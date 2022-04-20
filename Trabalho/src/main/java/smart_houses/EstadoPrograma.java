package smart_houses;

import smart_houses.exceptions.ExisteCasaException;
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
import java.util.*;
import java.util.stream.Collectors;

public class EstadoPrograma implements Serializable{
    private final Map<String, Casa> casas;
    private final Map<String, Fornecedor> fornecedores;
    private LocalDate data_atual;

    public static final double custoEnergia = 4.8;
    public static final double imposto = 0.06;


    public EstadoPrograma(){
        this.casas = new HashMap<>();
        this.fornecedores = new HashMap<>();
        this.data_atual = LocalDate.now();
    }

    public EstadoPrograma(EstadoPrograma c){
        this.casas = c.getCasas();
        this.fornecedores = c.getFornecedores();
        this.data_atual = c.getDataAtual();
    }

    public LocalDate getDataAtual() {
        return data_atual;
    }

    public void setDataAtual(LocalDate data_atual) {
        this.data_atual = data_atual;
    }

    private void geraFaturas(int days){
        this.casas.values().forEach(casa -> {
            Fatura f = this.fornecedores.get(casa.getFornecedor()).criaFatura(casa.getCode(), casa.getNif(), casa.getListDevices(), this.data_atual, this.data_atual.plusDays(days));
            casa.adicionaFatura(f);
        });
    }

    private void geraFaturas(LocalDate fim){
        this.casas.values().forEach(casa -> {
            Fatura f = this.fornecedores.get(casa.getFornecedor()).criaFatura(casa.getCode(), casa.getNif(), casa.getListDevices(), this.data_atual, fim);
            casa.adicionaFatura(f);
        });
    }

    public List<Fatura> getFaturasFornecedor(String nome){
        return this.casas.values().stream().
                flatMap(c -> c.getFaturas().values().stream()).
                filter(f -> f.getFornecedor().equals(nome)).
                map(Fatura::clone).
                collect(Collectors.toList());
    }

    public void avancaData(){
        this.geraFaturas(1);
        this.data_atual = this.data_atual.plusDays(1);
    }

    public Casa getCasaMaisGastadora() throws ExisteCasaException {
        Casa casa = this.casas.values().stream().max(Comparator.comparingDouble(c -> c.getLastFatura().getConsumo())).orElse(null);
        if(casa == null) throw new ExisteCasaException("Nao existe nenhuma casa");
        return casa.clone();
    }

    public Fornecedor getFornecedorMaiorFaturacao(){
        List<Fatura> faturas = this.casas.values().stream().flatMap(c -> c.getFaturas().values().stream()).toList();
        Fatura maiorFatura = faturas.stream().max((f1, f2) -> Double.compare(f1.getCusto(), f2.getCusto())).orElse(null);
        if(maiorFatura == null) return null;
        return this.fornecedores.get(maiorFatura.getFornecedor());
    }

    public void avancaData(int days){
        this.geraFaturas(days);
        this.data_atual = this.data_atual.plusDays(days);
    }

    public void avancaData(LocalDate date){
        this.geraFaturas(date);
        this.data_atual = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstadoPrograma that = (EstadoPrograma) o;

        if (getCasas() != null ? !getCasas().equals(that.getCasas()) : that.getCasas() != null) return false;
        if (getFornecedores() != null ? !getFornecedores().equals(that.getFornecedores()) : that.getFornecedores() != null)
            return false;
        return Objects.equals(data_atual, that.data_atual);
    }

    @Override
    public int hashCode() {
        int result = getCasas() != null ? getCasas().hashCode() : 0;
        result = 31 * result + (getFornecedores() != null ? getFornecedores().hashCode() : 0);
        result = 31 * result + (data_atual != null ? data_atual.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EstadoPrograma{" +
                "casas=" + casas +
                ", fornecedores=" + fornecedores +
                ", data_atual=" + data_atual +
                '}';
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

    public void adicionaCasa(Casa c) throws ExisteCasaException {
        if(this.existeCasa(c.getCode())) throw new ExisteCasaException("Este codigo ja existe");
        this.casas.put(c.getCode(), c.clone());
    }

    public boolean existeFornecedor(String nome){
        return this.fornecedores.containsKey(nome);
    }

    public boolean existeCasa(String code){
        return this.casas.containsKey(code);
    }

    public void addDeviceToCasa(String code, SmartDevice device){
        this.casas.get(code).addDevice(device);
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

    public void addDeviceToCasaOnRoom(String code, String room, int id) {
        this.casas.get(code).addDeviceOnRoom(room, id);
    }

    public List<String> getRoomsHouse(String code) {
        return this.casas.get(code).getListRooms();
    }

    public void setAllDevicesHouseOn(String code, boolean ligar) {
        this.casas.get(code).setAllDevicesState(ligar);
    }

    public List<SmartDevice> getSetDevicesHouse(String code) {
        return this.casas.get(code).getListDevices();
    }

    public void setDeviceHouseOn(String code, int id, boolean ligar) {
        this.casas.get(code).setDeviceState(id, ligar);
    }

    public void addFornecedor(Fornecedor f) {
        this.fornecedores.put(f.getName(), f.clone());
    }
}
