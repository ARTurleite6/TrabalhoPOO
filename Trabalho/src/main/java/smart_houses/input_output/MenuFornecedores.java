package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.exceptions.ExisteFornecedorException;
import smart_houses.modulo_fornecedores.Fornecedor;

import java.util.Scanner;

public class MenuFornecedores {

    private static void criarFornecedor(EstadoPrograma e){

        Scanner scan = new Scanner(System.in);
        String name = null;
        do{
            name = scan.next();
            if(e.existeFornecedor(name)){
                System.out.println("Este Fornecedor ja existe, tente novamente");
                name = null;
            }
        }while(name == null);

        try {
            e.addFornecedor(new Fornecedor(name));
        } catch (ExisteFornecedorException ex) {
            System.out.println(ex.getMessage());
        }
    }

    protected static void run(EstadoPrograma e){
        int choice = 0;
        while(choice != 2){
            choice = MenuFornecedores.gestaoFornecedores();
            if (choice == 1) {
                MenuFornecedores.criarFornecedor(e);
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
