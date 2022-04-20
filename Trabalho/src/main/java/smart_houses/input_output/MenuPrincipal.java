package smart_houses.input_output;

import smart_houses.EstadoPrograma;

import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.Scanner;

public class MenuPrincipal {

    protected static void run(EstadoPrograma c){
        int choice = 0;

        while(choice != 7){
            choice = MenuPrincipal.initialMenu();
            switch (choice) {
                case 1 -> MenuCasas.run(c);
                case 2 -> MenuDispositivos.run(c);
                case 3 -> MenuFornecedores.run(c);
                case 4 -> System.out.println(c.toString());
                case 5 -> MenuPrincipal.alteraDia(c);
                case 6 -> MenuEstadoPrograma.run(c);
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
            }
            case 2 -> {
                System.out.println("Insira o numero de dias a avancar");
                int days = scan.nextInt();
                if(days > 0) {
                    e.avancaData(days);
                }
                else System.out.println("Numero de dias invalido");
            }
            case 3 -> {
                System.out.println("Insira a data para onde quer avancar(Dia/Mes/Ano)");
                String dataStr = scan.nextLine();
                try {
                    LocalDate data = LocalDate.parse(dataStr);
                    if(data.isAfter(e.getDataAtual())){
                        e.avancaData(data);
                    }
                    else System.out.println("Data invalida");
                }
                catch (DateTimeException exception){
                    System.out.println("Data foi inserida de forma invalida, tente com (Dia/Mes/Ano)");
                }
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
                6: Estatisticas do Programa
                7: Sair do Programa""");
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }






}
