package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.exceptions.CasaInexistenteException;
import smart_houses.exceptions.DeviceInexistenteException;
import smart_houses.exceptions.RoomInexistenteException;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;
import smart_houses.smart_devices.SmartSpeaker;

import java.util.List;
import java.util.Scanner;
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
        return new SmartSpeaker(ligado, baseConsumption, volume, estacao, brand);
    }

    private static SmartDevice criacaoSmartCamera(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        System.out.println("Insira o custo de instalacao do dispostivo");
        double custoInstalacao = scan.nextDouble();
        System.out.println("Insira a dimensao dos ficheiros em bytes");
        int dimensao = scan.nextInt();
        System.out.println("Insira a resolucao height da camera");
        int resolucaoX= scan.nextInt();
        System.out.println("Insira a resolucao width da camera");
        int resolucaoY = scan.nextInt();
        return new SmartCamera(ligado, custoInstalacao, resolucaoX, resolucaoY, dimensao);
    }

    private static SmartDevice criacaoSmartBulb(){
        Scanner scan = new Scanner(System.in);
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        System.out.println("Insira o consumo base da lampada");
        double consumoBase = scan.nextDouble();
        System.out.println("Insira a dimensao do dispositivo em centimetros");
        int dimensao = scan.nextInt();
        System.out.println("Insira a tonalidade do dispositivo(Neutral(0), Warm(1) ou Cold(2)");
        int tonalidade = scan.nextInt();
        SmartBulb.Tones tone = SmartBulb.Tones.NEUTRAL;
        if(tonalidade == 1) tone = SmartBulb.Tones.WARM;
        else if(tonalidade == 2) tone = SmartBulb.Tones.COLD;
        return new SmartBulb(ligado, consumoBase, tone, dimensao);
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
        System.out.println("Insira o nif do proprietario da casa a instalar");
        String nif = scan.next();
        if (e.existeCasa(nif)) {
            try {
                e.addDeviceToCasa(nif, dispositivo);
            } catch (CasaInexistenteException ex) {
                System.out.println("Casa inexistente");
            }
            System.out.println("Insira o nome da divisao a inserir");
            String room = scan.next();
            try{
              e.addDeviceToCasaOnRoom(nif, room, dispositivo.getId());
            }
            catch(CasaInexistenteException exc){
              System.out.println("Nao existe uma casa em que o proprietário tem o nif " + nif);
            }
            catch(RoomInexistenteException exc){
              System.out.println("Não existe a divisão " + room + " na casa em que o proprietário é o " + nif);
            }
        } else System.out.println("A casa nao existe");
    }

        private static void ligarDesligarDispositivos(EstadoPrograma e){
          Scanner scan = new Scanner(System.in);
          System.out.println("Deseja ligar/Desligar os dispositivos de uma divisao, dispositivos em especifico, ou os dispositivos todos de uma casa?, Divisao(0), Especifico(1), Todos(2)");
          int divisao = scan.nextInt();
          if(divisao == 0){
            System.out.println("Insira o nif da casa onde deseja editar");
            String nif = scan.next();
            String room = null;
            try{
                System.out.println("Divisões da casa: " + e.getRoomsHouse(nif));
                System.out.println("Insira o nome da divisão onde deseja ligar/desligar os dispositivos");
                scan.nextLine();
                room = scan.nextLine();
                System.out.println(room);
                System.out.println("Deseja ligar ou desligar os dispositivos (Ligar(True)/Desligar(False)");
                while(scan.hasNext() && !scan.hasNextBoolean()){
                    scan.next();
                }
                boolean on_off = scan.nextBoolean();
                String finalRoom = room;
                e.addPedido(estado -> {
                    try {
                        estado.setAllDevicesHouseOnRoom(nif, finalRoom, on_off);
                    } catch (RoomInexistenteException | CasaInexistenteException ex) {
                        System.out.println(ex.getMessage());
                    }
                });
            }
            catch(CasaInexistenteException exc){
              System.out.println("Nao existe casa com o nif: " + nif);
            }
          }
          else if(divisao == 1){

              System.out.println("Insira o nif da casa ");
              String nif = scan.next();
              boolean keep = true;
              int id = -1;

              while(keep){
                  try{
                      System.out.println("Lista de devices da casa : " + e.getListDevicesHouse(nif));
                      System.out.println("Insira o codigo do device a alterar");
                      id = scan.nextInt();
                      scan.next();
                      System.out.println("Pretende ligar ou desligar o dispositivo: Ligar(True)/Desligar(False)");
                      boolean on_off = scan.nextBoolean();
                      scan.next();
                      int finalId = id;
                      e.addPedido(estado -> {
                          try {
                              estado.setDeviceHouseOn(nif, finalId, on_off);
                          } catch (DeviceInexistenteException | CasaInexistenteException ex) {
                              System.out.println(ex.getMessage());
                          }
                      });
                  }
                  catch(CasaInexistenteException exc){
                        System.out.println("Nao existe casa com o nif: " + nif);
                  }
                  System.out.println("Deseja continuar a editar os dispositivos da casa? Sim(True)/Nao(False)");
                  keep = scan.nextBoolean();
                  scan.next();
              }
          }
          else if(divisao == 2){
              System.out.println("Insira o nif da casa");
              String nif = scan.next();
              System.out.println("Deseja ligar ou desligar os dispositivos");
              boolean on_off = scan.nextBoolean();

              e.addPedido(estado -> {
                  try {
                      estado.setAllDevicesHouseOn(nif, on_off);
                  } catch (CasaInexistenteException ex) {
                      ex.printStackTrace();
                  }
              });
          }
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
