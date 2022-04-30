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
        System.out.println("Codigos das casas existentes: " + e.getCodigosCasa());
        System.out.println("Insira o nif da casa desejada");
        Scanner scan = new Scanner(System.in);
        String cod = scan.next();
        System.out.println("Lista de fornecedors: " + e.getNomeFornecedores());
        System.out.println("Insira o fornecedor desejado");
        String fornecedor = scan.next();
        e.addPedido(estado -> {
            try {
                estado.mudaFornecedorCasa(cod, fornecedor);
            } catch (CasaInexistenteException | FornecedorInexistenteException ex) {
                System.out.println(ex.getMessage());
            }
        });
    }

    private static void criacaoCasa(EstadoPrograma e) {
        Scanner scan = new Scanner(System.in);

        String nif = null;
        do{
            System.out.println("Insira o nif da casa");
            nif = scan.next();
            if(e.existeCasa(nif)){
                System.out.println("O nif desta casa ja existe, insira outro");
                nif = null;
            }
        }while(nif == null);

        System.out.println("Insira o nome do proprietario:");
        String nome = scan.next();

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
        Casa casa = new Casa(nome, nif, rooms, empresa);
        try {
            e.adicionaCasa(casa);
        }
        catch(ExisteCasaException exc){
            System.out.println(exc.getMessage());
        }
    }

    protected static void run(EstadoPrograma e) {
        int choice = 0;
        while (choice != 3) {
            choice = MenuCasas.gestaoCasas();
            if (choice == 1) {
                MenuCasas.criacaoCasa(e);
                MenuCasas.mudaFornecedorCasa(e);
            }
        }
    }
}
