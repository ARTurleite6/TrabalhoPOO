package smart_houses.input_output;

import smart_houses.EstadoPrograma;

import java.util.Scanner;

public class MenuPrincipal {

    protected static void run(EstadoPrograma c){
        EstadoPrograma collections = new EstadoPrograma();
        int choice = 0;

        while(choice != 6){
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
                    System.out.println(collections.toString());
                    break;
                }
                case 5 : {
                    MenuPrincipal.alteraDia(collections);
                }
            }
        }

        System.out.println("Saindo...");
    }

    private static void alteraDia(EstadoPrograma e){
        System.out.println("""
                Alterar Dia:
                1: Avancar um dia
                2: Avancar X dias 
                3: Avancar para determinada data
                4: Sair
                """);
        Scanner scan = new Scanner(System.in);
        int choice = scan.nextInt();
        switch (choice) {
            case 1 -> {
                e.avancaData();
                System.out.println("Avancando, Gerando Faturas...");
                break;
            }
            case 2 -> {
                System.out.println("Insira o numero de dias a avancar");
                int days = scan.nextInt();
                e.avancaData(days);
                break;
            }
            case 3 -> {
                System.out.println("Insira a data para onde quer avancar");
                
                break;
            }
        }
    }

    private static int initialMenu(){
        System.out.println("""
                Menu Principal:
                1: Gerir Casas
                2: Gerir Dispositivos
                3: Gerir Fornecedores
                4: Estado programa
                5: Alterar dia
                6: Sair do Programa""");
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }






}
