package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.exceptions.CasaInexistenteException;
import smart_houses.exceptions.ExisteCasaException;
import smart_houses.exceptions.FornecedorInexistenteException;
import smart_houses.modulo_casas.Casa;

import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class MenuCasas {

    private static int gestaoCasas(){
        System.out.println("""
                Menu Gestao Casas
                1: Criar Casa
                2: Mudar Fornecedor da Casa
                3: Sair""");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    private static void mudaFornecedorCasa(EstadoPrograma e){
        System.out.println("Insira o nif do proprietário da casa desejada");
        Scanner scan = new Scanner(System.in);
        String nif = scan.next();
        System.out.println("Lista de fornecedors: " + e.getNomeFornecedores());
        System.out.println("Insira o fornecedor desejado");
        String fornecedor = scan.next();
        e.addPedido(estado -> {
            try {
                estado.mudaFornecedorCasa(nif, fornecedor);
            } catch (CasaInexistenteException | FornecedorInexistenteException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    private static void criacaoCasa(EstadoPrograma e) {
        Scanner scan = new Scanner(System.in);

        System.out.println("Insira o nif do Proprietário");
        String nif = scan.next();
        scan.nextLine(); // clear buffer

        System.out.println("Insira o nome do proprietario:");
        String nome = scan.nextLine();

        System.out.println("Pretende adicionar divisoes à casa? S(1)/N(0)");
        Set<String> rooms = new TreeSet<>();
        int keep = scan.nextInt();
        scan.nextLine();
        while (keep == 1) {
            System.out.println("Insira o nome da divisao(Nao pode ser repetido)");
            String room = scan.nextLine();
            if (rooms.contains(room)) {
                System.out.println("Esta divisao ja existe");
            } else rooms.add(room);
            System.out.println("Pretende continuar a adicionar? S(1)/N(0)");
            keep = scan.nextInt();
            scan.nextLine();
        }
        String empresa;
        do{
            System.out.println("Insira o nome do Fornecedor da casa");
            System.out.println("Lista de fornecedores existentes: " + e.getFornecedores().values());
            empresa = scan.nextLine();
            if(!e.existeFornecedor(empresa)){
                System.out.println("Este fornecedor não existe, tente novamente");
                empresa = null;
            }
        }while(empresa == null);
        Casa casa = new Casa(nome, nif, rooms, empresa);
        try {
            e.adicionaCasa(casa);
        }
        catch(ExisteCasaException | FornecedorInexistenteException exc){
            System.out.println(exc.getMessage());
        }
    }

    protected static void run(EstadoPrograma e) {
        int choice = 0;
        while (choice != 3) {
            choice = MenuCasas.gestaoCasas();
            if (choice == 1) {
                MenuCasas.criacaoCasa(e);
            }
            else if(choice == 2){
                MenuCasas.mudaFornecedorCasa(e);
            }
        }
    }
}
