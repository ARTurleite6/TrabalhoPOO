package smart_houses.input_output;

import smart_houses.EstadoPrograma;

public class Programa {



    public static void main(String[] args) {
        EstadoPrograma c = new EstadoPrograma();
        c = c.carregaDados();
        MenuPrincipal.run(c);
        c.guardaDados();
    }

}
