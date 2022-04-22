package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.exceptions.ExisteCasaException;
import smart_houses.modulo_casas.Casa;

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

    private static void criacaoCasa(EstadoPrograma e) {
        Scanner scan = new Scanner(System.in);

        String codigo = null;
        do{
            System.out.println("Insira o codigo da casa");
            codigo = scan.next();
            if(e.existeCasa(codigo)){
                System.out.println("O codigo desta casa ja existe, insira outro");
                codigo = null;
            }
        }while(codigo == null);

        System.out.println("Insira o nome do proprietario:");
        String nome = scan.next();
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
            System.out.println("Pretende continuar a adicionar? S(1)/N(0)");
            keep = scan.nextInt();
        }
        String empresa = null;
        do{
            System.out.println("Insira o nome do Fornecedor da casa");
            empresa = scan.next();
            if(!e.existeFornecedor(empresa)){
                System.out.println("Este fornecedor não existe, tente novamente");
                empresa = null;
            }
        }while(empresa == null);
        Casa casa = new Casa(codigo, nome, nif, rooms, empresa);
        try {
            e.adicionaCasa(casa);
        }
        catch(ExisteCasaException exc){
            System.out.println(exc.getMessage());
        }
    }

    protected static void run(EstadoPrograma e) {
        int choice = 0;
        while (choice != 2) {
            choice = MenuCasas.gestaoCasas();
            if (choice == 1) {
                MenuCasas.criacaoCasa(e);
            }
        }
    }
}
