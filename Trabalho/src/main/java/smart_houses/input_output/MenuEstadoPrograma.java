package smart_houses.input_output;

import smart_houses.EstadoPrograma;

import java.time.LocalDate;
import java.util.Scanner;

public class MenuEstadoPrograma {

    protected static void run(EstadoPrograma e){
        int choice = 0;
        Scanner scan = new Scanner(System.in);
        while(choice != 5){
            System.out.println("""
                    1 : Casa que mais gastou no periodo passado
                    2 : Comercializador com maior volume de faturação
                    3 : Faturas de um fornecedor
                    4 : Maior consumidor de um periodo
                    5 : Sair
                    """);
            choice = scan.nextInt();
            if(choice == 1){
                e.getCasaMaisGastadora().ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Nao existe nenhuma casa para calcular o maximo")
                );
            }
            else if(choice == 2){
                e.getFornecedorMaiorFaturacao().ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Nao existe nenhum fornecedor para calcular o maximo")
                );
            }
            else if(choice == 3){
                System.out.println("Insere o nome do fornecedor ao qual queres as faturas");
                String nome = scan.next();
                if(e.existeFornecedor(nome)) {
                    System.out.println(e.getFaturasFornecedor(nome));
                }
                else{
                    System.out.println("Este fornecedor não existe");
                }
            }
            else if(choice == 4){
                System.out.println("Insercao da primeira data");
                LocalDate dataI = MenuPrincipal.inputData();
                System.out.println("Insercao da segunda data");
                LocalDate dataF = MenuPrincipal.inputData();
                System.out.println("Insira quantos elementos quer no ranking");
                int N = scan.nextInt();
                System.out.println(e.maiorConsumidorPeriodo(dataI, dataF, N));
            }
        }
    }
}
