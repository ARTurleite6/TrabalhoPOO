package smart_houses;

import smart_houses.exceptions.ExisteCasaException;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

public class Teste {
    public static void main(String[] args) {
        Fatura f = new Fatura("Casa1", "EDP", "256250278", 200, 150, LocalDate.now().minusDays(1), LocalDate.now());
        Fatura f3 = new Fatura("Casa1", "EDP", "256250278", 200, 200, LocalDate.now().minusDays(1), LocalDate.now());
        Fatura f2 = new Fatura("Casa2", "Endesa", "256250270", 150, 100, LocalDate.now().minusDays(1), LocalDate.now());
    }
}
