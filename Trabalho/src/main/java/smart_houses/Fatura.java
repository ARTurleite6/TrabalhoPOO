package smart_houses;

import java.time.LocalDate;

public class Fatura implements Comparable<Fatura>{
    private String fornecedor;
    private String nifCliente;
    private double custo;
    private double consumo;
    private LocalDate inicioPeriodo;
    private LocalDate fimPeriodo;


    public Fatura(){
        this.fornecedor = "";
        this.nifCliente = "";
        this.custo = 0;
        this.consumo = 0;
        this.inicioPeriodo = LocalDate.now();
        this.fimPeriodo = LocalDate.now();
    }

    public Fatura(String fornecedor, String nifCliente, double custo, double consumo, LocalDate inicioPeriodo, LocalDate fimPeriodo) {
        this.fornecedor = fornecedor;
        this.nifCliente = nifCliente;
        this.custo = custo;
        this.consumo = consumo;
        this.inicioPeriodo = inicioPeriodo;
        this.fimPeriodo = fimPeriodo;
    }

    public Fatura(Fatura f){
        this.fornecedor = f.getFornecedor();
        this.nifCliente = f.getNifCliente();
        this.custo = f.getCusto();
        this.consumo = f.getConsumo();
        this.inicioPeriodo = f.getInicioPeriodo();
        this.fimPeriodo = f.getFimPeriodo();
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
                "fornecedor='" + fornecedor + '\'' +
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

        if (Double.compare(fatura.getCusto(), getCusto()) != 0) return false;
        if (Double.compare(fatura.getConsumo(), getConsumo()) != 0) return false;
        if (getFornecedor() != null ? !getFornecedor().equals(fatura.getFornecedor()) : fatura.getFornecedor() != null)
            return false;
        if (getNifCliente() != null ? !getNifCliente().equals(fatura.getNifCliente()) : fatura.getNifCliente() != null)
            return false;
        if (getInicioPeriodo() != null ? !getInicioPeriodo().equals(fatura.getInicioPeriodo()) : fatura.getInicioPeriodo() != null)
            return false;
        return getFimPeriodo() != null ? getFimPeriodo().equals(fatura.getFimPeriodo()) : fatura.getFimPeriodo() == null;
    }

    @Override
    public int hashCode() {
        int result;
        long temp;
        result = getFornecedor() != null ? getFornecedor().hashCode() : 0;
        result = 31 * result + (getNifCliente() != null ? getNifCliente().hashCode() : 0);
        temp = Double.doubleToLongBits(getCusto());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        temp = Double.doubleToLongBits(getConsumo());
        result = 31 * result + (int) (temp ^ (temp >>> 32));
        result = 31 * result + (getInicioPeriodo() != null ? getInicioPeriodo().hashCode() : 0);
        result = 31 * result + (getFimPeriodo() != null ? getFimPeriodo().hashCode() : 0);
        return result;
    }

    public int compareTo(Fatura f){
        return this.inicioPeriodo.compareTo(f.getInicioPeriodo());
    }

    public Fatura clone(){
        return new Fatura(this);
    }
}
