import java.util.Scanner;

public class Menu {

    public static int initialMenu(){
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

    public static int gestaoFornecedores(){
        System.out.println("""
                Menu Gestao Fornecedores
                1: Criar Fornecedores
                2: Sair""");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static int gestaoDispositivos(){
        System.out.println("""
                Menu Gestao Dispositivos
                1: Criar Dispositivos
                2: Sair""");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public static int gestaoCasas(){
        System.out.println("""
                Menu Gestao Casas
                1: Criar Casa
                2: Sair""");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }



}
