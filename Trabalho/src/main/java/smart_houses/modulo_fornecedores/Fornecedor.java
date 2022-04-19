package smart_houses.modulo_fornecedores;

import smart_houses.EstadoPrograma;
import smart_houses.Fatura;
import smart_houses.smart_devices.SmartDevice;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

import static java.time.temporal.ChronoUnit.DAYS;

public class Fornecedor implements Serializable {

    private String name;


    public Fornecedor() {
        this.name = "n/a";
    }

    public Fornecedor(String name){
        this.name = name;
    }

    public Fornecedor(Fornecedor fornecedor){
        this.name=fornecedor.getName();
    }

    public Fatura criaFatura(String nif, Set<SmartDevice> devices, LocalDate inicio, LocalDate fim){
        long days = DAYS.between(inicio, fim);
        double consumo = devices.stream().mapToDouble(SmartDevice::comsumption).sum() * days;
        double preco = this.precoDiaDispositivos(devices) * days;
        return new Fatura(this.name, nif, preco, consumo, inicio, fim);
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
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fornecedor that = (Fornecedor) o;

        return getName() != null ? getName().equals(that.getName()) : that.getName() == null;
    }

    @Override
    public int hashCode() {
        return getName() != null ? getName().hashCode() : 0;
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
