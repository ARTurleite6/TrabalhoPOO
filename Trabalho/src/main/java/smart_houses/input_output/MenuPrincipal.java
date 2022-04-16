package smart_houses.input_output;

import smart_houses.EstadoPrograma;

import java.util.Scanner;

public class MenuPrincipal {

    protected static void run(EstadoPrograma c){
        EstadoPrograma collections = new EstadoPrograma();
        int choice = 0;

        while(choice != 5){
            choice = MenuPrincipal.initialMenu();
            switch (choice){
                case 1 : {
                    MenuCasas.run(collections);
                    break;
                }
                case 2 : {
                    MenuDispositivos.run(collections);
                    break;
                }
                case 3 : {
                    MenuFornecedores.run(collections);
                    break;
                }
                case 4 : {
                    System.out.println(collections.getCasas().toString());
                    break;
                }
            }
        }

        System.out.println("Saindo...");
    }

    private static int initialMenu(){
        System.out.println("""
                Menu Principal:
                1: Gerir Casas
                2: Gerir Dispositivos
                3: Gerir Fornecedores
                4: Listar casas
                5: Sair do Programa""");
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }






}
