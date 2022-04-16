import java.util.Optional;
import java.util.Scanner;

public class GestaoDispositivos {

    private static Optional<SmartDevice> criacaoSmartSpeaker(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        System.out.println("Insira a marca do speaker");
        String brand = scan.next();
        System.out.println("Insira o volume do speaker");
        int volume = scan.nextInt();
        System.out.println("Insira a estacao de radio a ligar");
        String estacao = scan.next();
        System.out.println("Insira o consumo base do dispositivo");
        double baseConsumption = scan.nextDouble();
        System.out.println("Insira o custo de instalacao do dispositivo");
        double instalacao = scan.nextDouble();
        return Optional.of(new SmartSpeaker(ligado, instalacao, volume, estacao, brand, baseConsumption));
    }

    private static Optional<SmartDevice> criacaoSmartCamera(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        System.out.println("Insira o custo de instalacao do dispostivo");
        double custoInstalacao = scan.nextDouble();
        System.out.println("Insira a dimensao dos ficheiros em bytes");
        int dimensao = scan.nextInt();
        System.out.println("Insira a resolucao da camera");
        int resolucao = scan.nextInt();
        return Optional.of(new SmartCamera(ligado, custoInstalacao, resolucao, dimensao));
    }

    private static Optional<SmartDevice> criacaoSmartBulb(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        System.out.println("Insira o custo de instalacao do dispostivo");
        double custoInstalacao = scan.nextDouble();
        System.out.println("Insira o custo base da lampada");
        double custoBase = scan.nextDouble();
        System.out.println("Insira a dimensao do dispositivo em centimetros");
        int dimensao = scan.nextInt();
        System.out.println("Insira a tonalidade do dispositivo(Neutral(0), Warm(1) ou Cold(2)");
        int tonalidade = scan.nextInt();
        SmartBulb.Tones tone = SmartBulb.Tones.NEUTRAL;
        if(tonalidade == 1) tone = SmartBulb.Tones.WARM;
        else if(tonalidade == 2) tone = SmartBulb.Tones.COLD;
        return Optional.of(new SmartBulb(ligado, custoInstalacao, tone, dimensao, custoBase));
    }

    private static void criacaoSmartDevice(CollectionCasas casas) {
        System.out.println("Que tipo de Dispositivo pretende criar: \n1 : SmartBulb;\n2 : SmartCamera;\n3 : SmartSpeaker");
        Scanner scan = new Scanner(System.in);
        int tipoDispositivo = scan.nextInt();
        Optional<SmartDevice> dispositivo = Optional.empty();
        if (tipoDispositivo == 1) {
            dispositivo = criacaoSmartBulb();
        } else if (tipoDispositivo == 2) {
            dispositivo = criacaoSmartCamera();
        } else if (tipoDispositivo == 3) {
            dispositivo = criacaoSmartSpeaker();
        } else {
            System.out.println("Tipo invalido de dispositivo");
        }
        dispositivo.ifPresent(d -> {
            System.out.println("Insira o nif do proprietario da casa a instalar");
            String nif = scan.next();
            if (casas.existCasa(nif)) {
                casas.addDeviceToCasa(nif, d);
                System.out.println("Deseja inserir em uma divisao da casa? S(1)/N(0)");
                int choice = scan.nextInt();
                if (choice == 1) {
                    System.out.println("Insira o nome da divisao a inserir");
                    String room = scan.next();
                    if (casas.getCasa(nif).existRoom(room)) casas.addDeviceToCasaOnRoom(nif, room, d.getId());
                }
            } else System.out.println("A casa nao existe");
        });
    }

        public static void run(ColecoesPrograma collections){
        int choice = 0;
        while(choice != 2){
            choice = Menu.gestaoDispositivos();

            if (choice == 1) {
                GestaoDispositivos.criacaoSmartDevice(collections.getCasas());
            }
        }
    }
}
