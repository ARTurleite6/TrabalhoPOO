package smart_houses;

import smart_houses.exceptions.*;
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
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EstadoPrograma implements Serializable {
    private final Map<String, Casa> casas;
    private final Map<String, Fornecedor> fornecedores;
    private final Map<Integer, Fatura> faturas;

    private Queue<Consumer<EstadoPrograma>> pedidos;

    private LocalDate data_atual;

    public static final double custoEnergia = 4.8;
    public static final double imposto = 0.06;


    public EstadoPrograma() {
        this.casas = new HashMap<>();
        this.fornecedores = new HashMap<>();
        this.data_atual = LocalDate.now();
        this.faturas = new HashMap<>();
        this.pedidos = new LinkedList<>();
    }

    public EstadoPrograma(EstadoPrograma c) {
        this.casas = c.getCasas();
        this.fornecedores = c.getFornecedores();
        this.data_atual = c.getDataAtual();
        this.faturas = c.getFaturas();
        this.pedidos = c.getPedidos();
    }

    public Queue<Consumer<EstadoPrograma>> getPedidos() {
        return new LinkedList<>(this.pedidos);
    }


    public void setPedidos(Queue<Consumer<EstadoPrograma>> pedidos) {
        this.pedidos = new LinkedList<>(pedidos);
    }

    public void addPedido(Consumer<EstadoPrograma> pedido){
        this.pedidos.add(pedido);
    }

    private void runAllRequests(){
        this.pedidos.forEach(p -> p.accept(this));
    }

    public Map<Integer, Fatura> getFaturas() {
        return faturas;
    }

    public LocalDate getDataAtual() {
        return data_atual;
    }

    public void setDataAtual(LocalDate data_atual) {
        this.data_atual = data_atual;
    }

    private void geraFaturas(int days) {
        this.casas.values().forEach(casa -> {
            Fatura f = this.fornecedores.get(casa.getFornecedor()).criaFatura(casa.getCode(), casa.getNif(), casa.getListDevices(), this.data_atual, this.data_atual.plusDays(days));
            this.guardaFatura(f);
        });
    }

    private void geraFaturas(LocalDate fim) {
        this.casas.values().forEach(casa -> {
            Fatura f = this.fornecedores.get(casa.getFornecedor()).criaFatura(casa.getCode(), casa.getNif(), casa.getListDevices(), this.data_atual, fim);
            this.guardaFatura(f);
        });
    }

    private void guardaFatura(Fatura f) {
        this.faturas.put(f.getCodigoFatura(), f.clone());
        this.casas.get(f.getCodCasa()).adicionaFatura(f.getCodigoFatura());
        this.fornecedores.get(f.getFornecedor()).adicionaFatura(f.getCodigoFatura());
    }


    public List<Fatura> getFaturasFornecedor(String nome) {
        return this.fornecedores.get(nome)
                .getFaturas()
                .stream()
                .map(codigo -> this.faturas.get(codigo).clone()).
                toList();
    }

    public Optional<Casa> getCasaMaisGastadora() {

        Map<String, Double> consumoHouse = this.faturas.values()
                .stream()
                .collect(Collectors.groupingBy(Fatura::getCodCasa, Collectors.summingDouble(Fatura::getConsumo)));

        return consumoHouse
                .entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(e -> this.casas.get(e.getKey()));
    }

    public List<Casa> maiorConsumidorPeriodo(LocalDate inicio, LocalDate fim, int N) {
        Map<String, Double> consumoHouse = this.faturas.values()
                .stream()
                .filter(f -> (f.getInicioPeriodo().isAfter(inicio) || f.getInicioPeriodo().equals(inicio)) && (f.getFimPeriodo().isBefore(fim) || f.getFimPeriodo().isEqual(fim)))
                .collect(Collectors.groupingBy(Fatura::getCodCasa, Collectors.summingDouble(Fatura::getConsumo)));

        return consumoHouse.entrySet()
                .stream()
                .sorted((e1, e2) -> Double.compare(e2.getValue(), e1.getValue()))
                .limit(N)
                .map(e -> this.casas.get(e.getKey())).toList();

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstadoPrograma that = (EstadoPrograma) o;

        if (!getCasas().equals(that.getCasas())) return false;
        if (!getFornecedores().equals(that.getFornecedores())) return false;
        if (!getFaturas().equals(that.getFaturas())) return false;
        if (!getPedidos().equals(that.getPedidos())) return false;
        return data_atual.equals(that.data_atual);
    }

    @Override
    public int hashCode() {
        int result = getCasas().hashCode();
        result = 31 * result + getFornecedores().hashCode();
        result = 31 * result + getFaturas().hashCode();
        result = 31 * result + getPedidos().hashCode();
        result = 31 * result + data_atual.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EstadoPrograma{" +
                "casas=" + casas +
                ", fornecedores=" + fornecedores +
                ", faturas=" + faturas +
                ", pedidos=" + pedidos +
                ", data_atual=" + data_atual +
                '}';
    }

    public Optional<Fornecedor> getFornecedorMaiorFaturacao() {

        Map<String ,Double> faturasFornecedor = this.faturas.values()
                .stream()
                .collect(Collectors.groupingBy(Fatura::getFornecedor, Collectors.summingDouble(Fatura::getCusto)));

        return faturasFornecedor.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue))
                .map(e -> this.fornecedores.get(e.getKey()));
    }

    public void avancaData(LocalDate date) throws DataInvalidaException{
        if(date.isBefore(this.data_atual)) throw new DataInvalidaException("Esta data é anterior à atual");
        this.geraFaturas(date);
        this.data_atual = date;
        this.runAllRequests();
    }

    public EstadoPrograma carregaDados() {
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/main/resources/teste.txt"));
            EstadoPrograma programa = (EstadoPrograma) ois.readObject();
            ois.close();
            return programa;
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
        if (this.casas.containsKey(c.getCode())) throw new ExisteCasaException("Esta casa tem um codigo que ja existe");
        this.casas.put(c.getCode(), c.clone());
    }

    public boolean existeFornecedor(String nome) {
        return this.fornecedores.containsKey(nome);
    }

    public boolean existeCasa(String code) {
        return this.casas.containsKey(code);
    }

    public void addDeviceToCasa(String code, SmartDevice device) {
        this.casas.get(code).addDevice(device);
    }

    public EstadoPrograma clone() {
        return new EstadoPrograma(this);
    }

    public void guardaDados() {
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

    public void addFornecedor(Fornecedor f) throws ExisteFornecedorException {
        if (this.fornecedores.containsKey(f.getName()))
            throw new ExisteFornecedorException("Este fornecedor já existe");
        this.fornecedores.put(f.getName(), f.clone());
    }

    public void setAllDevicesHouseOnRoom(String code, String room, boolean on) {
        this.casas.get(code).setAllDevicesStateRoom(room, on);
    }

    public List<String> getCodigosCasa(){
        return new ArrayList<>(this.casas.keySet());
    }

    public List<String> getNomeFornecedores(){
        return new ArrayList<>(this.fornecedores.keySet());
    }

    public void mudaFornecedorCasa(String casa, String fornecedor) throws CasaInexistenteException, FornecedorInexistenteException{
        Casa c = this.casas.get(casa);
        if(c == null) throw new CasaInexistenteException("Nao existe esta casa");
        if(!this.fornecedores.containsKey(fornecedor)) throw new FornecedorInexistenteException("Nao existe este Fornecedor");
        c.setFornecedor(fornecedor);
    }
}
