package smart_houses;

import smart_houses.modulo_casas.Casa;

public class Teste {
    public static void main(String[] args) {
        Fatura f = new Fatura();
        Casa c = new Casa();
        c.adicionaFatura(f);
        System.out.println(f.clone());
    }
}
