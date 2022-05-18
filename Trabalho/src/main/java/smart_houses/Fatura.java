package smart_houses;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Classe que representa as faturas
 */
public class Fatura implements Comparable<Fatura>, Serializable {

    // Variavel estatica para gerar um codigo unico para cada fatura
    public static int next_codigoFatura = 1;

    // codigo da fatura
    private int codigoFatura;

    // nome do fornecedor que gerou a fatura
    private String fornecedor;

    // nif do cliente da fatura
    private String nifCliente;
  
    // Custo associado à fatura
    private double custo;

    // Consumo associado à fatura
    private double consumo;
    
    // Inicio do periodo da fatura
    private LocalDate inicioPeriodo;

    // Fim do periodo da fatura
    private LocalDate fimPeriodo;

    //Contrutor por omissao da fatura
    public Fatura(){
        this.codigoFatura = Fatura.next_codigoFatura++;
        this.fornecedor = "";
        this.nifCliente = "";
        this.custo = 0;
        this.consumo = 0;
        this.inicioPeriodo = LocalDate.now();
        this.fimPeriodo = LocalDate.now();
    }

    // Construtor parametrizado da fatura
    public Fatura(String fornecedor, String nifCliente, double custo, double consumo, LocalDate inicioPeriodo, LocalDate fimPeriodo) {
        this.codigoFatura = Fatura.next_codigoFatura++;
        this.fornecedor = fornecedor;
        this.nifCliente = nifCliente;
        this.custo = custo;
        this.consumo = consumo;
        this.inicioPeriodo = inicioPeriodo;
        this.fimPeriodo = fimPeriodo;
    }

/**
 * Contrutor de copia da fatura
 * @param f fatura que pretende copiar
 */
    public Fatura(Fatura f){
        this.codigoFatura = f.getCodigoFatura();
        this.fornecedor = f.getFornecedor();
        this.nifCliente = f.getNifCliente();
        this.custo = f.getCusto();
        this.consumo = f.getConsumo();
        this.inicioPeriodo = f.getInicioPeriodo();
        this.fimPeriodo = f.getFimPeriodo();
    }

/**
 * > Metodo que retorna o valor da variavel de instancia codigo
 * 
 * @return O valor da variavel de instancia codigo
 */
    public int getCodigoFatura() {
        return codigoFatura;
    }


    /**
     * Metodo que retorna o valor da variavel de instancia fornecedor do objeto
     * 
     * @return Valor da variavel fornecedor
     */
    public String getFornecedor() {
        return fornecedor;
    }

    /**
     * Metodo que coloca o valor do parametro do fornecedor na variavel de instancia fornecedor
     * 
     * @param fornecedor valor da variavel de instancia fornecedor
     */
    public void setFornecedor(String fornecedor) {
        this.fornecedor = fornecedor;
    }

    /**
     * Metodo que retorna o valor da variavel do nif do cliente 
     * 
     * @return Valor do nif do cliente
     */
    public String getNifCliente() {
        return nifCliente;
    }

    /**
     * This function sets the nifCliente variable to the value of the parameter nifCliente
     * 
     * @param nifCliente The NIF of the client.
     */
    public void setNifCliente(String nifCliente) {
        this.nifCliente = nifCliente;
    }

    /**
     * This function returns the value of the variable custo
     * 
     * @return The cost of the item.
     */
    public double getCusto() {
        return custo;
    }

    /**
     * This function sets the value of the custo variable to the value of the custo parameter.
     * 
     * @param custo The cost of the item.
     */
    public void setCusto(double custo) {
        this.custo = custo;
    }

    /**
     * This function returns the value of the variable consumo
     * 
     * @return The value of the variable consumo.
     */
    public double getConsumo() {
        return consumo;
    }

    /**
     * This function sets the value of the variable consumo to the value of the parameter consumo
     * 
     * @param consumo the amount of energy consumed by the appliance
     */
    public void setConsumo(double consumo) {
        this.consumo = consumo;
    }

    /**
     * This function returns the start date of the period
     * 
     * @return The value of the variable inicioPeriodo.
     */
    public LocalDate getInicioPeriodo() {
        return inicioPeriodo;
    }

    /**
     * This function sets the value of the inicioPeriodo attribute
     * 
     * @param inicioPeriodo The start date of the period.
     */
    public void setInicioPeriodo(LocalDate inicioPeriodo) {
        this.inicioPeriodo = inicioPeriodo;
    }

    /**
     * This function returns the end date of the period
     * 
     * @return The end date of the period.
     */
    public LocalDate getFimPeriodo() {
        return fimPeriodo;
    }

    /**
     * This function sets the end date of the period
     * 
     * @param fimPeriodo The end date of the period.
     */
    public void setFimPeriodo(LocalDate fimPeriodo) {
        this.fimPeriodo = fimPeriodo;
    }

    /**
     * The toString() method returns a string representation of the object
     * 
     * @return The toString method is being returned.
     */
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

    /**
     * If the object is the same object, return true. If the object is null or of a different class,
     * return false. If the object is of the same class, compare the fields and return true if they are
     * all equal, false otherwise
     * 
     * @param o The object to be compared.
     * @return The hashCode of the object.
     */
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

    /**
     * The hashCode() method returns a hash code value for the object
     * 
     * @return The hashCode of the object.
     */
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

    /**
     * The compareTo() method compares two objects of the same class and returns a negative integer,
     * zero, or a positive integer if the object is less than, equal to, or greater than the specified
     * object
     * 
     * @param f the object to be compared.
     * @return The difference between the dates of the invoices.
     */
    public int compareTo(Fatura f){
        return this.inicioPeriodo.compareTo(f.getInicioPeriodo());
    }

    /**
     * This function returns a new object that is a copy of the object that called it.
     * 
     * @return A new Fatura object with the same values as the original.
     */
    public Fatura clone(){
        return new Fatura(this);
    }
}
