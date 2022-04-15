public class ColecoesPrograma {
    private CollectionCasas casas;
    private Fornecedores fornecedores;

    public ColecoesPrograma(){
        this.casas = new CollectionCasas();
        this.fornecedores = new Fornecedores();
    }

    public ColecoesPrograma(CollectionCasas casas, Fornecedores fornecedores){
        this.casas = casas.clone();
        this.fornecedores = fornecedores.clone();
    }

    public ColecoesPrograma(ColecoesPrograma c){
        this.casas = c.getCasas();
        this.fornecedores = c.getFornecedores();
    }

    public CollectionCasas getCasas() {
        return casas;
    }

    public Fornecedores getFornecedores() {
        return fornecedores;
    }

    public void setCasas(CollectionCasas casas) {
        this.casas = casas.clone();
    }

    public void setFornecedores(Fornecedores fornecedores) {
        this.fornecedores = fornecedores.clone();
    }

    @Override
    public String toString() {
        return "ColecoesPrograma{" +
                "casas=" + casas +
                ", fornecedores=" + fornecedores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ColecoesPrograma that = (ColecoesPrograma) o;

        if (!getCasas().equals(that.getCasas())) return false;
        return getFornecedores().equals(that.getFornecedores());
    }

    @Override
    public int hashCode() {
        int result = getCasas().hashCode();
        result = 31 * result + getFornecedores().hashCode();
        return result;
    }

    public ColecoesPrograma clone(){
        return new ColecoesPrograma(this);
    }
}
