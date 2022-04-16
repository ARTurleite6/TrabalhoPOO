package smart_houses;

import smart_houses.modulo_casas.CollectionCasas;
import smart_houses.modulo_fornecedores.CollectionFornecedores;

public class EstadoPrograma {
    private final CollectionCasas casas;
    private final CollectionFornecedores fornecedores;
    public static final double custoEnergia = 10;
    public static final double imposto = 0.23;

    public EstadoPrograma(){
        this.casas = new CollectionCasas();
        this.fornecedores = new CollectionFornecedores();
    }

    public EstadoPrograma(EstadoPrograma c){
        this.casas = c.getCasas();
        this.fornecedores = c.getFornecedores();
    }

    public CollectionCasas getCasas() {
        return casas;
    }

    public CollectionFornecedores getFornecedores() {
        return fornecedores;
    }

    @Override
    public String toString() {
        return "EstadoPrograma{" +
                "casas=" + casas +
                ", fornecedores=" + fornecedores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstadoPrograma that = (EstadoPrograma) o;

        if (!getCasas().equals(that.getCasas())) return false;
        return getFornecedores().equals(that.getFornecedores());
    }

    @Override
    public int hashCode() {
        int result = getCasas().hashCode();
        result = 31 * result + getFornecedores().hashCode();
        return result;
    }

    public EstadoPrograma clone(){
        return new EstadoPrograma(this);
    }
}
