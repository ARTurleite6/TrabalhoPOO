package smart_houses;

import smart_houses.modulo_casas.CollectionCasas;
import smart_houses.modulo_fornecedores.CollectionFornecedores;

import java.io.*;
import java.time.LocalDate;

public class EstadoPrograma implements Serializable{
    private final CollectionCasas casas;
    private final CollectionFornecedores fornecedores;
    private LocalDate data;

    public static final double custoEnergia = 4.8;
    public static final double imposto = 0.06;


    public EstadoPrograma(){
        this.casas = new CollectionCasas();
        this.fornecedores = new CollectionFornecedores();
        this.data = LocalDate.now();
    }

    public EstadoPrograma(EstadoPrograma c){
        this.casas = c.getCasas();
        this.fornecedores = c.getFornecedores();
        this.data = c.getData();
    }

    public LocalDate getData() {
        return data;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    private void geraFaturas(int days){
        this.casas.getHouses().values().forEach(casa -> this.fornecedores.geraFaturaFornecedor(casa.getNif(), casa.getSetDevices(), casa.getFornecedor(), this.data, this.data.plusDays(days)));
    }

    private void geraFaturas(LocalDate fim){
        this.casas.getHouses().values().forEach(casa -> this.fornecedores.geraFaturaFornecedor(casa.getNif(), casa.getSetDevices(), casa.getFornecedor(), this.data, fim));
    }

    public void avancaData(){
        this.geraFaturas(1);
        this.data = this.data.plusDays(1);
    }

    public void avancaData(int days){
        this.geraFaturas(days);
        this.data = this.data.plusDays(days);
    }

    public void avancaData(LocalDate date){
        this.geraFaturas(date);
        this.data = date;
    }

    public EstadoPrograma carregaDados(){
        try {
            ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/main/resources/teste.txt"));
            return (EstadoPrograma) ois.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    public CollectionCasas getCasas() {
        return casas;
    }

    public CollectionFornecedores getFornecedores() {
        return fornecedores;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstadoPrograma that = (EstadoPrograma) o;

        if (getCasas() != null ? !getCasas().equals(that.getCasas()) : that.getCasas() != null) return false;
        if (getFornecedores() != null ? !getFornecedores().equals(that.getFornecedores()) : that.getFornecedores() != null)
            return false;
        return getData() != null ? getData().equals(that.getData()) : that.getData() == null;
    }

    @Override
    public int hashCode() {
        int result = getCasas() != null ? getCasas().hashCode() : 0;
        result = 31 * result + (getFornecedores() != null ? getFornecedores().hashCode() : 0);
        result = 31 * result + (getData() != null ? getData().hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "EstadoPrograma{" +
                "casas=" + casas +
                ", fornecedores=" + fornecedores +
                ", data=" + data +
                '}';
    }

    public EstadoPrograma clone(){
        return new EstadoPrograma(this);
    }

    public void guardaDados(){
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
}
