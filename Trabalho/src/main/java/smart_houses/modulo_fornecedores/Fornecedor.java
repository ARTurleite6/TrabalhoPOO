package smart_houses.modulo_fornecedores;

import smart_houses.EstadoPrograma;
import smart_houses.Fatura;
import smart_houses.exceptions.FornecedorErradoException;
import smart_houses.modulo_casas.Casa;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.time.temporal.ChronoUnit.DAYS;

public class Fornecedor implements Serializable {

    private String name;
    private double desconto;
    private List<Fatura> faturas;
    private Function<Casa, Double> formula;

    /**
     *
     */
    public Fornecedor() {
        this.desconto = 0.1;
        this.name = "n/a";
        this.faturas = new ArrayList<>();
    }

    /**
     * @param name
     */
    public Fornecedor(String name){
        this.name = name;
        this.desconto = 0.1;
        this.faturas = new ArrayList<>();
    }

    /**
     * @param name
     * @param desconto
     */
    public Fornecedor(String name, double desconto){
        this.name = name;
        this.desconto = desconto;
        this.faturas = new ArrayList<>();
    }

    /**
     * @param fornecedor
     */
    public Fornecedor(Fornecedor fornecedor){
        this.desconto = fornecedor.getDesconto();
        this.name=fornecedor.getName();
        this.faturas = fornecedor.getFaturas();
    }

    /**
     * @return
     */
    public List<Fatura> getFaturas() {
        return this.faturas.stream().map(Fatura::clone).collect(Collectors.toList());
    }

    /**
     * @param faturas
     */
    public void setFaturas(List<Fatura> faturas) {
        this.faturas = faturas.stream().map(Fatura::clone).collect(Collectors.toList());
    }

    /**
     * @param casa
     * @param inicio
     * @param fim
     * @return
     * @throws FornecedorErradoException
     */
    public Fatura criaFatura(Casa casa, LocalDate inicio, LocalDate fim) throws FornecedorErradoException {
        if(!casa.getFornecedor().equals(this.name)) throw new FornecedorErradoException("Este nao é o fornecedor desta casa, casa = " + casa);
        long days = DAYS.between(inicio, fim);
        double consumo = casa.consumoDispositivos() * days;
        double preco = this.precoDia(consumo, casa.getMapDevices().size()) * days;
        return new Fatura(this.name, casa.getNif(), preco, consumo, inicio, fim);
    }

    /**
     * @return
     */
    public double getDesconto() {
        return desconto;
    }

    /**
     * @param desconto
     */
    public void setDesconto(double desconto) {
        this.desconto = desconto;
    }

    /**
     * @return
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return
     */
    public String toString() {
        return "Fornecedor{" +
                "name='" + name + '\'' +
                ", desconto=" + desconto + '\'' +
                ", faturas=" + this.faturas +
                '}';
    }

    /**
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fornecedor that = (Fornecedor) o;

        return this.getName().equals(that.getName()) && this.desconto == that.getDesconto() && this.faturas.equals(that.getFaturas());
    }

    /**
     * @return
     */
    public int hashCode() {
        int r = 7;
        r = r * 31 + this.name.hashCode();
        r = r * 31 + Double.hashCode(this.desconto);
        r = r * 31 + this.faturas.hashCode();
        return r;
    }

    /**
     * @param consumo
     * @param n_devices
     * @return
     */
    public double precoDia(double consumo, int n_devices){
        double precoSDesc = EstadoPrograma.custoEnergia * consumo * (1 + EstadoPrograma.imposto) * 0.9;
        double desc = precoSDesc * this.desconto;
        return precoSDesc - desc;
    }

    /**
     * @return
     */
    public Fornecedor clone(){
        return new Fornecedor(this);
    }

    /**
     * @param f
     */
    public void adicionaFatura(Fatura f){
        this.faturas.add(f.clone());
    }

    /**
     * @return
     */
    public double faturacao(){
        return this.faturas.stream().mapToDouble(Fatura::getCusto).sum();
    }

}
