package smart_houses.modulo_fornecedores;

import smart_houses.EstadoPrograma;
import smart_houses.Fatura;
import smart_houses.smart_devices.SmartDevice;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import static java.time.temporal.ChronoUnit.DAYS;

public class Fornecedor implements Serializable {

    private String name;

    private Set<Integer> faturas;


    public Fornecedor() {
        this.name = "n/a";
        this.faturas = new TreeSet<>();
    }

    public Fornecedor(String name){
        this.name = name;
        this.faturas = new TreeSet<>();
    }

    public Fornecedor(Fornecedor fornecedor){
        this.name=fornecedor.getName();
        this.faturas = fornecedor.getFaturas();
    }

    public Fatura criaFatura(String nif, List<SmartDevice> devices, LocalDate inicio, LocalDate fim){
        long days = DAYS.between(inicio, fim);
        double consumo = devices.stream().mapToDouble(SmartDevice::comsumption).sum() * days;
        double preco = this.precoDiaDispositivos(devices) * days;
        return new Fatura(this.name, nif, preco, consumo, inicio, fim);
    }

    public void adicionaFatura(int codigo){
        this.faturas.add(codigo);
    }

    public String getName() {
        return name;
    }

    public Set<Integer> getFaturas(){
        return new TreeSet<>(this.faturas);
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

        if (!getName().equals(that.getName())) return false;
        return getFaturas().equals(that.getFaturas());
    }

    @Override
    public int hashCode() {
        int result = getName().hashCode();
        result = 31 * result + getFaturas().hashCode();
        return result;
    }

    public double precoDiaDispositivos(List<SmartDevice> devices){
        double per = 0.9;
        if(devices.size() < 10) per = 0.7;
        double finalPer = per;
        return devices.stream().mapToDouble(d -> EstadoPrograma.custoEnergia * d.comsumption() * (1 + EstadoPrograma.imposto) * finalPer).sum();
    }

    public Fornecedor clone(){
        return new Fornecedor(this);
    }

}
