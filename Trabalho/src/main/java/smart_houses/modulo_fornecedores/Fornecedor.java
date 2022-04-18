package smart_houses.modulo_fornecedores;

import smart_houses.EstadoPrograma;
import smart_houses.Fatura;
import smart_houses.smart_devices.SmartDevice;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class Fornecedor implements Serializable {

    private String name;
    private Map<String, Set<Fatura>> faturas;


    public Fornecedor() {
        this.name = "n/a";
        this.faturas = new HashMap<>();
    }

    public Fornecedor(String name){
        this.name = name;
        this.faturas = new HashMap<>();
    }

    public Fornecedor(String name, Map<String, Set<Fatura>> faturas){
        this.name = name;
        this.setFaturas(faturas);
    }

    public Fornecedor(Fornecedor fornecedor){
        this.name=fornecedor.getName();
        this.faturas = fornecedor.getFaturas();
    }

    public Map<String, Set<Fatura>> getFaturas() {
        return faturas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Fatura::clone).collect(Collectors.toCollection(TreeSet::new))));
    }

    public void setFaturas(Map<String, Set<Fatura>> faturas) {
        this.faturas = faturas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().stream().map(Fatura::clone).collect(Collectors.toCollection(TreeSet::new))));
    }

    public void criaFatura(String nif, Set<SmartDevice> devices, LocalDate inicio, LocalDate fim){
        this.faturas.putIfAbsent(nif, new TreeSet<>());
        long days = DAYS.between(inicio, fim);
        double consumo = devices.stream().mapToDouble(SmartDevice::comsumption).sum() * days;
        double preco = this.precoDiaDispositivos(devices) * days;
        this.faturas.get(nif).add(new Fatura(this.name, nif, preco, consumo, inicio, fim));
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                "name='" + name + '\'' +
                ", faturas=" + faturas +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fornecedor that = (Fornecedor) o;

        if (getName() != null ? !getName().equals(that.getName()) : that.getName() != null) return false;
        return getFaturas() != null ? getFaturas().equals(that.getFaturas()) : that.getFaturas() == null;
    }

    @Override
    public int hashCode() {
        int result = getName() != null ? getName().hashCode() : 0;
        result = 31 * result + (getFaturas() != null ? getFaturas().hashCode() : 0);
        return result;
    }

    public double precoDiaDispositivos(Set<SmartDevice> devices){
        double per = 0.9;
        if(devices.size() < 10) per = 0.7;
        double finalPer = per;
        return devices.stream().mapToDouble(d -> EstadoPrograma.custoEnergia * d.comsumption() * (1 + EstadoPrograma.imposto) * finalPer).sum();
    }

    public Fornecedor clone(){
        return new Fornecedor(this);
    }

}
