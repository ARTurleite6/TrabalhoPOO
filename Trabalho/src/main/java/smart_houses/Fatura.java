package smart_houses;

import java.io.Serializable;
import java.time.LocalDate;

public class Fatura implements Comparable<Fatura>, Serializable {

    public static int next_codigoFatura = 1;

    private int codigoFatura;
    private String fornecedor;
    private String nifCliente;
    private double custo;
    private double consumo;
    private LocalDate inicioPeriodo;
    private LocalDate fimPeriodo;


    public Fatura(){
        this.codigoFatura = Fatura.next_codigoFatura++;
        this.fornecedor = "";
        this.nifCliente = "";
        this.custo = 0;
        this.consumo = 0;
        this.inicioPeriodo = LocalDate.now();
        this.fimPeriodo = LocalDate.now();
    }

    public Fatura(String fornecedor, String nifCliente, double custo, double consumo, LocalDate inicioPeriodo, LocalDate fimPeriodo) {
        this.codigoFatura = Fatura.next_codigoFatura++;
        this.fornecedor = fornecedor;
        this.nifCliente = nifCliente;
        this.custo = custo;
        this.consumo = consumo;
        this.inicioPeriodo = inicioPeriodo;
        this.fimPeriodo = fimPeriodo;
    }

    public Fatura(Fatura f){
        this.codigoFatura = f.getCodigoFatura();
        this.fornecedor = f.getFornecedor();
        this.nifCliente = f.getNifCliente();
        this.custo = f.getCusto();
        this.consumo = f.getConsumo();
        this.inicioPeriodo = f.getInicioPeriodo();
        this.fimPeriodo = f.getFimPeriodo();
    }

    public int getCodigoFatura() {
        return codigoFatura;
    }

    public String getFornecedor() {
        return fornecedor;
    }

    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    public String getNifCliente() {
        return nifCliente;
    }

    public void setNifCliente(String nifCliente) {
        this.nifCliente = nifCliente;
    }

    public double getCusto() {
        return custo;
    }

    public void setCusto(double custo) {
        this.custo = custo;
    }

    public double getConsumo() {
        return consumo;
    }

    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    public LocalDate getInicioPeriodo() {
        return inicioPeriodo;
    }

    public void setInicioPeriodo(LocalDate inicioPeriodo) {
        this.inicioPeriodo = inicioPeriodo;
    }

    public LocalDate getFimPeriodo() {
        return fimPeriodo;
    }

    public void setFimPeriodo(LocalDate fimPeriodo) {
        this.fimPeriodo = fimPeriodo;
    }

    @Override
    public String toString() {
        return "Fatura{" +
                "codigoFatura=" + codigoFatura +
                ", fornecedor='" + fornecedor + '\'' +
                ", nifCliente='" + nifCliente + '\'' +
                ", custo=" + custo +
                ", consumo=" + consumo +
                ", inicioPeriodo=" + inicioPeriodo +
                ", fimPeriodo=" + fimPeriodo +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Fatura fatura = (Fatura) o;

        if (getCodigoFatura() != fatura.getCodigoFatura()) return false;
        if (Double.compare(fatura.getCusto(), getCusto()) != 0) return false;
        if (Double.compare(fatura.getConsumo(), getConsumo()) != 0) return false;
        if (!getFornecedor().equals(fatura.getFornecedor())) return false;
        if (!getNifCliente().equals(fatura.getNifCliente())) return false;
        if (!getInicioPeriodo().equals(fatura.getInicioPeriodo())) return false;
        return getFimPeriodo().equals(fatura.getFimPeriodo());
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getCodigoFatura();
        result = 31 * result + getFornecedor().hashCode();
        result = 31 * result + getNifCliente().hashCode();
        temp = Double.doubleToLongBits(getCusto());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getConsumo());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + getInicioPeriodo().hashCode();
        result = 31 * result + getFimPeriodo().hashCode();
        return result;
    }

    public int compareTo(Fatura f){
        return this.inicioPeriodo.compareTo(f.getInicioPeriodo());
    }

    public Fatura clone(){
        return new Fatura(this);
    }
}
