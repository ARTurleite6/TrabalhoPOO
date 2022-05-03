package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.Parser;
import smart_houses.modulo_casas.Casa;

import java.io.IOException;
import java.util.Scanner;

public class Programa {

    public static void main(String[] args) {
        EstadoPrograma c = null;
        try {
            c = EstadoPrograma.carregaDados();
        } catch (IOException e) {
            System.out.println("Erro a abrir o ficheiro");
            Parser p = new Parser();
            c = p.parse();
        } catch (ClassNotFoundException e) {
            System.out.println("Erro a importar as classes");
            Parser p = new Parser();
            c = p.parse();
        }

        MenuPrincipal.run(c);
        c.guardaDados();
    }

}
