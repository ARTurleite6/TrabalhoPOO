package smart_houses.modulo_fornecedores;

import smart_houses.EstadoPrograma;
import smart_houses.Fatura;
import smart_houses.exceptions.FornecedorErradoException;
import smart_houses.modulo_casas.Casa;

import java.io.Serializable;
import java.time.LocalDate;

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

    public Fatura criaFatura(Casa casa, LocalDate inicio, LocalDate fim) throws FornecedorErradoException {
        if(!casa.getFornecedor().equals(this.name)) throw new FornecedorErradoException("Este nao é o fornecedor desta casa, casa = " + casa);
        long days = DAYS.between(inicio, fim);
        double consumo = casa.consumoDispositivos() * days;
        double preco = this.precoDia(consumo) * days;
        return new Fatura(this.name, casa.getNif(), preco, consumo, inicio, fim);
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

        return this.getName().equals(that.getName());
    }

    @Override
    public int hashCode() {
        return getName().hashCode();
    }

    public double precoDia(double consumo){
        return EstadoPrograma.custoEnergia * consumo * (1 + EstadoPrograma.imposto) * 0.9;
    }

    public Fornecedor clone(){
        return new Fornecedor(this);
    }

}
