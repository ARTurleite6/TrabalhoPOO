package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.exceptions.ExisteCasaException;

import java.util.Scanner;

public class MenuEstadoPrograma {

    protected static void run(EstadoPrograma e){
        int choice = 0;
        Scanner scan = new Scanner(System.in);
        while(choice != 4){
            System.out.println("""
                    1 : Casa que mais gastou no periodo passado
                    2 : Comercializador com maior volume de faturação
                    3 : Faturas de um comercializador
                    4 : Sair
                    """);
            choice = scan.nextInt();
            if(choice == 1){
                System.out.println(e.getCasaMaisGastadora());
            }
            else if(choice == 2){
                System.out.println(e.getFornecedorMaiorFaturacao());
            }
            else if(choice == 3){
                System.out.println("Insere o nome do fornecedor ao qual queres as faturas");
                String nome = scan.next();
                System.out.println(e.getFaturasFornecedor(nome));
            }
        }
    }
}
