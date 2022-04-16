import java.util.Scanner;

public class GestaoFornecedores {

    private static Fornecedor criarFornecedor(){

        Scanner scan = new Scanner(System.in);
        System.out.println("Insira o Nome da empresa");
        String name = scan.next();

        return new Fornecedor(10, name, 0.23);
    }

    public static void run(ColecoesPrograma c){
        int choice = 0;
        while(choice != 2){
            choice = Menu.gestaoFornecedores();
            if (choice == 1) {
                Fornecedor f = GestaoFornecedores.criarFornecedor();
                c.getFornecedores().addFornecedor(f);
            }
        }
    }
}
