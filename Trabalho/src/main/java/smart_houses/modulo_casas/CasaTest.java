package smart_houses.modulo_casas;

import org.junit.jupiter.api.Test;
import smart_houses.Fatura;
import smart_houses.exceptions.FornecedorErradoException;
import smart_houses.modulo_fornecedores.Fornecedor;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class CasaTest {

    @Test
    void consumoDispositivos() {
    }

    @Test
    void consumo() {
    }

    @Test
    void testConsumo() {
    }

    @Test
    void faturasFornecedor() {
        Fornecedor f1 = new Fornecedor("EDP");
        Casa c = new Casa("Artur", "256", "EDP");
        Fatura f = null;
        try {
            f = f1.criaFatura(c, LocalDate.now().minusDays(10), LocalDate.now());
        } catch (FornecedorErradoException e) {
            e.printStackTrace();
        }
        Fatura f2 = null;
        try {
            f2 = f1.criaFatura(c, LocalDate.now().minusDays(20), LocalDate.now().minusDays(10));
        } catch (FornecedorErradoException e) {
            e.printStackTrace();
        }
        c.adicionaFatura(f);
        c.adicionaFatura(f2);

        List<Fatura> l = List.of(f, f2);

        assertEquals(l, c.faturasFornecedor("EDP"));

    }
}