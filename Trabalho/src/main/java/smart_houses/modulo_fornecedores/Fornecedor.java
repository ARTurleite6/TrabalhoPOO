package smart_houses.modulo_fornecedores;

import smart_houses.EstadoPrograma;
import smart_houses.smart_devices.SmartDevice;

import java.util.Objects;
import java.util.Set;

public class Fornecedor {

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Fornecedor that = (Fornecedor) o;
        return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }

    public double precoDiaDispositivos(Set<SmartDevice> devices){
        double per = 0.9;
        if(devices.size() < 10) per = 0.7;
        double finalPer = per;
        return devices.stream().mapToDouble(d -> EstadoPrograma.custoEnergia * d.comsumption() * (1 + EstadoPrograma.imposto) * finalPer).sum();
    }

    @Override
    public String toString() {
        return "Fornecedor{" +
                ", name='" + name + '\'' +
                '}';
    }
    public Fornecedor clone(){
        return new Fornecedor(this);
    }

}
