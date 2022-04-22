package smart_houses;

import smart_houses.exceptions.ExisteCasaException;
import smart_houses.exceptions.ExisteFornecedorException;
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

public class EstadoPrograma implements Serializable {
    private final Map<String, Casa> casas;
    private final Map<String, Fornecedor> fornecedores;
    private final Map<Integer, Fatura> faturas;
    private LocalDate data_atual;

    public static final double custoEnergia = 4.8;
    public static final double imposto = 0.06;


    public EstadoPrograma() {
        this.casas = new HashMap<>();
        this.fornecedores = new HashMap<>();
        this.data_atual = LocalDate.now();
        this.faturas = new HashMap<>();
    }

    public EstadoPrograma(EstadoPrograma c) {
        this.casas = c.getCasas();
        this.fornecedores = c.getFornecedores();
        this.data_atual = c.getDataAtual();
        this.faturas = c.getFaturas();
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

    public void avancaData() {
        this.geraFaturas(1);
        this.data_atual = this.data_atual.plusDays(1);
    }

    public Optional<Casa> getCasaMaisGastadora() {

        Map<String, Double> consumoHouse = this.faturas.values().stream().collect(Collectors.groupingBy(Fatura::getCodCasa, Collectors.summingDouble(Fatura::getConsumo)));

        return consumoHouse
                .entrySet()
                .stream()
                .max(Comparator
                        .comparingDouble(Map.Entry::getValue))
                .map(e -> this.casas.get(e.getValue()));
    }

    public Optional<Casa> maiorConsumidorPeriodo(LocalDate inicio, LocalDate fim) {
        Map<String, Double> consumoHouse = this.faturas.values()
                .stream()
                .filter(f -> (f.getInicioPeriodo().isAfter(inicio) || f.getInicioPeriodo().equals(inicio)) && (f.getFimPeriodo().isBefore(fim) || f.getFimPeriodo().isEqual(fim)))
                .collect(Collectors.groupingBy(Fatura::getCodCasa, Collectors.summingDouble(Fatura::getConsumo)));

        return consumoHouse.entrySet()
                .stream()
                .max(Comparator.comparingDouble(Map.Entry::getValue)).map(e -> this.casas.get(e.getKey()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstadoPrograma that = (EstadoPrograma) o;

        if (!getCasas().equals(that.getCasas())) return false;
        if (!getFornecedores().equals(that.getFornecedores())) return false;
        if (!getFaturas().equals(that.getFaturas())) return false;
        return data_atual.equals(that.data_atual);
    }

    @Override
    public int hashCode() {
        int result = getCasas().hashCode();
        result = 31 * result + getFornecedores().hashCode();
        result = 31 * result + getFaturas().hashCode();
        result = 31 * result + data_atual.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "EstadoPrograma{" +
                "casas=" + casas +
                ", fornecedores=" + fornecedores +
                ", faturas=" + faturas +
                ", data_atual=" + data_atual +
                '}';
    }

    public Fornecedor getFornecedorMaiorFaturacao() {
        Map<String, Double> faturasFornecedor = this.fornecedores.
                values().
                stream().
                collect(Collectors.
                        toMap(Fornecedor::getName, f -> f.
                                getFaturas().
                                stream().
                                mapToDouble(cod -> this.faturas.get(cod).getConsumo()).sum()));
        Map.Entry<String, Double> maior = faturasFornecedor.entrySet().stream().max(Comparator.comparingDouble(e -> e.getValue())).orElse(null);
        Fornecedor f = null;
        if (maior != null) f = this.fornecedores.get(maior.getKey());
        return f;
    }

    public void avancaData(int days) {
        this.geraFaturas(days);
        this.data_atual = this.data_atual.plusDays(days);
    }

    public void avancaData(LocalDate date) {
        this.geraFaturas(date);
        this.data_atual = date;
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
}
