package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.modulo_casas.Casa;

import java.time.LocalDate;
import java.util.Optional;
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
                System.out.println(e.getFornecedorMaiorFaturacao());
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
            else if(choice == 5){
                System.out.println("Insercao da primeira data");
                LocalDate dataI = MenuPrincipal.inputData();
                System.out.println("Insercao da segunda data");
                LocalDate dataF = MenuPrincipal.inputData();
                Optional<Casa> casa = e.maiorConsumidorPeriodo(dataI, dataF);
                casa.ifPresentOrElse(
                        System.out::println,
                        () -> System.out.println("Nao existe output possivel para maior consumidor")
                );
            }
        }
    }
}
