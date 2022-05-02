package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.exceptions.DataInvalidaException;
import smart_houses.exceptions.FornecedorErradoException;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class MenuPrincipal {

    public static LocalDate inputData(){
        Scanner scan = new Scanner(System.in);
        LocalDate data = null;
        String dataStr;
        do{
            System.out.println("Insira a data desejada");
            dataStr = scan.next();
            try{
                data = LocalDate.parse(dataStr);
            }
            catch (DateTimeParseException e){
                System.out.println("Data inseria de forma invalida(aaaa/mm/dd");
            }
        }while(dataStr == null);
        return data;
    }

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
        LocalDate next_date = null;
        switch(choice) {
            case 1 -> next_date = e.getDataAtual().plusDays(1);
            case 2 -> {
                System.out.println("Insira o numero de dias a avancar");
                int dias = scan.nextInt();
                next_date = e.getDataAtual().plusDays(dias);
            }
            case 3 -> {
                System.out.println("Insira a data a avancar");
                String dateStr = scan.next();
                try {
                    next_date = LocalDate.parse(dateStr);
                }
                catch(DateTimeParseException exception){
                    System.out.println("Formato Inserido de forma invalida");
                }
            }
        }
        if(next_date != null){
            try {
                e.avancaData(next_date);
            } catch (DataInvalidaException | FornecedorErradoException ex) {
                System.out.println(ex.getMessage());
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
