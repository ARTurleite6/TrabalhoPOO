import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class GestaoCasas {

    private static Casa criacaoCasa(Fornecedores f){
        Scanner scan = new Scanner(System.in);
        System.out.println("Insira o nome do proprietario:");
        String nome = scan.nextLine();
        System.out.println("Insira o nif do proprietario:");
        String nif = scan.next();

        System.out.println("Pretende adicionar divisoes à casa? S(1)/N(0)");
        Set<String> rooms = new TreeSet<>();
        int keep = scan.nextInt();
        while(keep == 1){
            System.out.println("Insira o nome da divisao(Nao pode ser repetido)");
            String room = scan.next();
            if(rooms.contains(room)){
                System.out.println("Esta divisao ja existe");
            }
            else rooms.add(room);
            System.out.println("Pretende continuar a adicionar? S(1)/N(2)");
            keep = scan.nextInt();
        }
        System.out.println("Insira o nome do Fornecedor da casa");
        String empresa = scan.next();
        Casa casa = null;
        if(f.existeFornecedor(empresa)){
            casa = new Casa(nome, nif, rooms, empresa);
        }
        else {
            System.out.println("Este Fornecedor nao existe tente novamente");
        }

        return casa;
    }

    public static void run(ColecoesPrograma c){
        int choice = 0;
        while(choice != 2){
           choice = Menu.gestaoCasas();
            if (choice == 1) {
                Casa casa = GestaoCasas.criacaoCasa(c.getFornecedores());
                if (casa != null) c.getCasas().addCasa(casa);
            }
        }
    }
}
