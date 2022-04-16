package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.CollectionFornecedores;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MenuCasas {


    private static int gestaoCasas(){
        System.out.println("""
                Menu Gestao Casas
                1: Criar Casa
                2: Sair""");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }


    private static Casa criacaoCasa(CollectionFornecedores f) {
        Scanner scan = new Scanner(System.in);
        System.out.println("Insira o nome do proprietario:");
        String nome = scan.nextLine();
        System.out.println("Insira o nif do proprietario:");
        String nif = scan.next();

        System.out.println("Pretende adicionar divisoes à casa? S(1)/N(0)");
        Set<String> rooms = new TreeSet<>();
        int keep = scan.nextInt();
        while (keep == 1) {
            System.out.println("Insira o nome da divisao(Nao pode ser repetido)");
            String room = scan.next();
            if (rooms.contains(room)) {
                System.out.println("Esta divisao ja existe");
            } else rooms.add(room);
            System.out.println("Pretende continuar a adicionar? S(1)/N(2)");
            keep = scan.nextInt();
        }
        System.out.println("Insira o nome do Fornecedor da casa");
        String empresa = scan.next();
        Casa casa = null;
        if (f.existeFornecedor(empresa)) {
            casa = new Casa(nome, nif, rooms, empresa);
        } else {
            System.out.println("Este Fornecedor nao existe tente novamente");
        }

        return casa;
    }

    protected static void run(EstadoPrograma c) {
        int choice = 0;
        while (choice != 2) {
            choice = MenuCasas.gestaoCasas();
            if (choice == 1) {
                Casa casa = MenuCasas.criacaoCasa(c.getFornecedores());
                if (casa != null) c.getCasas().addCasa(casa);
            }
        }
    }
}