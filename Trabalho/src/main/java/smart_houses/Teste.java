package smart_houses;

import smart_houses.exceptions.ExisteCasaException;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;

import java.time.LocalDate;

public class Teste {
    public static void main(String[] args) {
        Fatura f = new Fatura("Casa1", "EDP", "256250278", 200, 150, LocalDate.now().minusDays(1), LocalDate.now());
        Fatura f2 = new Fatura("Casa2", "Endesa", "256250270", 150, 100, LocalDate.now().minusDays(1), LocalDate.now());
        Fornecedor fornecedor = new Fornecedor("EDP");
        Fornecedor fornecedor2 = new Fornecedor("Endesa");
        Casa c1 = new Casa("Casa1", "Artur", "256250278", "EDP");
        Casa c2 = new Casa("Casa2", "Joao", "256250270", "Endesa");
        EstadoPrograma e = new EstadoPrograma();
        e.addFornecedor(fornecedor);
        e.addFornecedor(fornecedor2);
        try {
            e.adicionaCasa(c1);
        }
        catch (ExisteCasaException exc){
            System.out.println(exc.getMessage());
        }
        try {
            e.adicionaCasa(c2);
        }
        catch (ExisteCasaException exc){
            System.out.println(exc.getMessage());
        }
            System.out.println(e.getFornecedorMaiorFaturacao());
    }
}
