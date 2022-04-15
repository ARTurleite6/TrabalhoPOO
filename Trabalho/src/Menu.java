import java.util.Optional;
import java.util.Scanner;

public class Menu {

    public static int initialMenu(){
        StringBuilder s = new StringBuilder();
        s.append("Menu Principal:\n").append("1: Criar Casa: \n").append("2: Criar Dispositivo\n").append("3: Listar casas\n").append("4: Sair do Programa");
        System.out.println(s.toString());
        Scanner input = new Scanner(System.in);
        return input.nextInt();
    }

    public static Optional<SmartDevice> criacaoSmartBulb(){
        System.out.println("Insira o custo de instalacao do dispostivo");
        Scanner scan = new Scanner(System.in);
        double custoInstalacao = scan.nextDouble();
        System.out.println("Insira a dimensao do dispositivo em centimetros");
        int dimensao = scan.nextInt();
        return Optional.of(new SmartBulb(false, custoInstalacao, SmartBulb.Tones.NEUTRAL, dimensao, 10));
    }

    public static void criacaoSmartDevice(CollectionCasas casas){
       System.out.println("Que tipo de Dispositivo pretende criar: \n1 : SmartBulb;\n2 : SmartCamera;\n3 : SmartSpeaker");
       Scanner scan = new Scanner(System.in);
       int tipoDispositivo = scan.nextInt();
       Optional<SmartDevice> dispositivo = Optional.empty();
       if(tipoDispositivo == 1){
           dispositivo = criacaoSmartBulb();
       }
       else if(tipoDispositivo == 2){

       }
       else if(tipoDispositivo == 3){

       }
       else{
           System.out.println("Tipo invalido de dispositivo");
       }
       dispositivo.ifPresent(d -> {
           System.out.println("Insira o nif do proprietario da casa a instalar");
           String nif = scan.next();
           if(casas.existCasa(nif)){
              casas.addDeviceToCasa(nif, d);
              System.out.println("Deseja inserir em uma divisao da casa? S(1)/N(0)");
              int choice = scan.nextInt();
              if(choice == 1){
                  System.out.println("Insira o nome da divisao a inserir");
                  String room = scan.next();
                  if(casas.getCasa(nif).existRoom(room)) casas.addDeviceToCasaOnRoom(nif, room, d.getId());
              }
           }
           else System.out.println("A casa nao existe");
       });
    }

    public static Casa criacaoCasa(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Insira o nome do proprietario:");
        String nome = scan.nextLine();
        System.out.println("Insira o nif do proprietario:");
        String nif = scan.next();

        Casa casa = new Casa(nome, nif);
        System.out.println("Pretende adicionar divisoes à casa? S(1)/N(0)");
        int keep = scan.nextInt();
        while(keep == 1){
            System.out.println("Insira o nome da divisao(Nao pode ser repetido)");
            String room = scan.next();
            if(casa.existRoom(room)){
                System.out.println("Esta divisao ja existe");
            }
            else casa.addRoom(room);
            System.out.println("Pretende continuar a adicionar? S(1)/N(2)");
            keep = scan.nextInt();
        }
        return casa;
    }

}
