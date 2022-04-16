package smart_houses.modulo_fornecedores;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class CollectionFornecedores {
    private Map<String, Fornecedor> fornecedores;

    public CollectionFornecedores(){
        this.fornecedores = new HashMap<>();
    }

    public CollectionFornecedores(Map<String, Fornecedor> fornecedores) {
        this.setFornecedores(fornecedores);
    }

    public CollectionFornecedores(CollectionFornecedores fornecedores){
        this.fornecedores = fornecedores.getFornecedores();
    }

    public Map<String, Fornecedor> getFornecedores() {
        return this.fornecedores.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void setFornecedores(Map<String, Fornecedor> fornecedores) {
        this.fornecedores = fornecedores.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    public void addFornecedor(Fornecedor f){
        this.fornecedores.put(f.getName(), f.clone());
    }

    public boolean existeFornecedor(String nome){
        return this.fornecedores.containsKey(nome);
    }

    @Override
    public String toString() {
        return "Fornecedores{" +
                "fornecedores=" + fornecedores +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        CollectionFornecedores that = (CollectionFornecedores) o;

        return getFornecedores().equals(that.getFornecedores());
    }

    @Override
    public int hashCode() {
        return getFornecedores().hashCode();
    }

    public CollectionFornecedores clone(){
        return new CollectionFornecedores(this);
    }
}
