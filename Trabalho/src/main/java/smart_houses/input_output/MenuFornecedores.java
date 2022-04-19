package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.modulo_fornecedores.Fornecedor;

import java.util.Scanner;

public class MenuFornecedores {

    private static Fornecedor criarFornecedor(){

        Scanner scan = new Scanner(System.in);
        System.out.println("Insira o Nome da empresa");
        String name = scan.next();

        return new Fornecedor(name);
    }

    protected static void run(EstadoPrograma c){
        int choice = 0;
        while(choice != 2){
            choice = MenuFornecedores.gestaoFornecedores();
            if (choice == 1) {
                Fornecedor f = MenuFornecedores.criarFornecedor();
                c.addFornecedor(f);
            }
        }
    }

    private static int gestaoFornecedores(){
        System.out.println("""
                Menu Gestao Fornecedores
                1: Criar Fornecedores
                2: Sair""");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
