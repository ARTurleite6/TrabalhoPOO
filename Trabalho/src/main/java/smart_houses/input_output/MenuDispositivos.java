package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;
import smart_houses.smart_devices.SmartSpeaker;

import java.util.List;
import java.util.Scanner;
import java.util.function.Function;
import java.util.function.Supplier;

public class MenuDispositivos {

    private static SmartDevice criacaoSmartSpeaker(){
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
        return new SmartSpeaker(ligado, instalacao, volume, estacao, brand, baseConsumption);
    }

    private static SmartDevice criacaoSmartCamera(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        System.out.println("Insira o custo de instalacao do dispostivo");
        double custoInstalacao = scan.nextDouble();
        System.out.println("Insira a dimensao dos ficheiros em bytes");
        int dimensao = scan.nextInt();
        System.out.println("Insira a resolucao da camera");
        int resolucao = scan.nextInt();
        return new SmartCamera(ligado, custoInstalacao, resolucao, dimensao);
    }

    private static SmartDevice criacaoSmartBulb(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        System.out.println("Insira o custo de instalacao do dispostivo");
        double custoInstalacao = scan.nextDouble();
        System.out.println("Insira o consumo base da lampada");
        double custoBase = scan.nextDouble();
        System.out.println("Insira a dimensao do dispositivo em centimetros");
        int dimensao = scan.nextInt();
        System.out.println("Insira a tonalidade do dispositivo(Neutral(0), Warm(1) ou Cold(2)");
        int tonalidade = scan.nextInt();
        SmartBulb.Tones tone = SmartBulb.Tones.NEUTRAL;
        if(tonalidade == 1) tone = SmartBulb.Tones.WARM;
        else if(tonalidade == 2) tone = SmartBulb.Tones.COLD;
        return new SmartBulb(ligado, custoInstalacao, tone, dimensao, custoBase);
    }

    private static void criacaoSmartDevice(EstadoPrograma e) {
        System.out.println("Que tipo de Dispositivo pretende criar: \n1 : SmartBulb;\n2 : SmartCamera;\n3 : SmartSpeaker");
        Scanner scan = new Scanner(System.in);
        Integer tipoDispositivo;
        do{
           tipoDispositivo = scan.nextInt();
           if(tipoDispositivo < 1 || tipoDispositivo > 3){
               System.out.println("Tipo de dispositivo inválido");
               tipoDispositivo = null;
           }
        }while(tipoDispositivo == null);
        System.out.println("funcao");
        List<Supplier<SmartDevice>> funcoesCriacao = List.of(MenuDispositivos::criacaoSmartBulb, MenuDispositivos::criacaoSmartCamera, MenuDispositivos::criacaoSmartSpeaker);
        SmartDevice dispositivo = funcoesCriacao.get(tipoDispositivo - 1).get();
        System.out.println("Insira o codigo da casa a instalar");
        String code = scan.next();
        if (e.existeCasa(code)) {
            e.addDeviceToCasa(code, dispositivo);
            System.out.println("Deseja inserir em uma divisao da casa? S(1)/N(0)");
            int choice = scan.nextInt();
            if (choice == 1) {
                System.out.println("Insira o nome da divisao a inserir");
                String room = scan.next();
                e.addDeviceToCasaOnRoom(code, room, dispositivo.getId());
            }
        } else System.out.println("A casa nao existe");
    }

        private static void ligarDesligarDispositivos(EstadoPrograma e){
            Scanner s = new Scanner(System.in);
            System.out.println("Qual a casa que pretender ligar/desligar os dipositivos(Insira o codigo da casa)");
            String codigo = s.nextLine();
            if(e.existeCasa(codigo)){
                System.out.println("Pretende ligar ou desligar: Ligar(True)/Desligar(False)");
                boolean ligar = s.nextBoolean();
                System.out.println("Pretende Ligar/Desligar os dispositivos de uma divisao ou um dispositivo em especifio: Divisao(1)/Especifico(2)");
                int divisao = s.nextInt();
                if(divisao == 1){
                    System.out.println("Lista de divisoes da casa: " + e.getRoomsHouse(codigo));
                    System.out.println("Diga qual divisao : ");
                    String room = s.next();
                    if(e.getCasas().get(codigo).existRoom(room)){
                        e.setAllDevicesHouseOnRoom(codigo, room, ligar);
                    }
                    else{
                        System.out.println("Esta divisao não existe");
                    }
                }
                else{
                    System.out.println("Lista de devices da casa: " + e.getSetDevicesHouse(codigo));
                    System.out.println("Diga qual o dispostivo(id) : ");
                    int id = s.nextInt();
                    e.setDeviceHouseOn(codigo, id, ligar);
                }
            }
            else System.out.println("A casa nao existe");
        }

        protected static void run(EstadoPrograma collections){
        int choice = 0;
        while(choice != 3){
            choice = MenuDispositivos.gestaoDispositivos();

            if (choice == 1) {
                MenuDispositivos.criacaoSmartDevice(collections);
            }
            else if(choice == 2) {
                MenuDispositivos.ligarDesligarDispositivos(collections);
            }
        }
    }

    private static int gestaoDispositivos(){
        System.out.println("""
                Menu Gestao Dispositivos
                1: Criar Dispositivos
                2: Ligar/Desligar dispositivos
                3: Sair""");
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }
}
