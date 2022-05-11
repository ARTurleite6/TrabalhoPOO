package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.Parser;
import smart_houses.exceptions.*;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;
import smart_houses.smart_devices.SmartSpeaker;
import smart_houses.smart_devices.SmartBulb.Tones;

import java.io.IOException;
import java.lang.reflect.InaccessibleObjectException;
import java.time.LocalDate;
import java.util.*;

import javax.sound.sampled.SourceDataLine;

public class Programa {

    private EstadoPrograma log;
    private Scanner scan;

    public Programa(){
        try {
            this.log = EstadoPrograma.carregaDados();
        } catch (IOException | ClassNotFoundException e) {
            this.log = new Parser().parse();
        }
        this.scan = new Scanner(System.in);
    }

    private SmartDevice criacaoSmartSpeaker(){
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        scan.nextLine();
        System.out.println("Insira a marca do speaker");
        String brand = scan.next();
        System.out.println("Insira o volume do speaker");
        int volume = scan.nextInt();
        scan.nextLine();
        System.out.println("Insira a estacao de radio a ligar");
        String estacao = scan.next();
        System.out.println("Insira o consumo base do dispositivo");
        double baseConsumption = scan.nextDouble();
        scan.nextLine();
        return new SmartSpeaker(ligado, baseConsumption, volume, estacao, brand);
    }

    private SmartDevice criacaoSmartCamera(){
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        scan.nextLine();
        System.out.println("Insira o consumo base do dispostivo");
        double consumoBase = scan.nextDouble();
        scan.nextLine();
        System.out.println("Insira a dimensao dos ficheiros em bytes");
        int dimensao = scan.nextInt();
        scan.nextLine();
        System.out.println("Insira a resolucao height da camera");
        int resolucaoX= scan.nextInt();
        scan.nextLine();
        System.out.println("Insira a resolucao width da camera");
        int resolucaoY = scan.nextInt();
        scan.nextLine();
        return new SmartCamera(ligado, consumoBase, resolucaoX, resolucaoY, dimensao);
    }

    private SmartDevice criacaoSmartBulb(){
        System.out.println("Pretende ligar o dispositivo? True/False");
        boolean ligado = scan.nextBoolean();
        scan.nextLine();
        System.out.println("Insira o consumo base da lampada");
        double consumoBase = scan.nextDouble();
        scan.nextLine();
        System.out.println("Insira a dimensao do dispositivo em centimetros");
        int dimensao = scan.nextInt();
        scan.nextLine();
        System.out.println("Insira a tonalidade do dispositivo(Neutral(0), Warm(1) ou Cold(2)");
        int tonalidade = scan.nextInt();
        scan.nextLine();
        SmartBulb.Tones tone = SmartBulb.Tones.NEUTRAL;
        if(tonalidade == 1) tone = SmartBulb.Tones.WARM;
        else if(tonalidade == 2) tone = SmartBulb.Tones.COLD;
        return new SmartBulb(ligado, consumoBase, tone, dimensao);
    }

    public SmartDevice criaDispositivo() {
        System.out.println("Que tipo de Dispositivo pretende criar: \n1 : SmartBulb;\n2 : SmartCamera;\n3 : SmartSpeaker");
        Integer tipoDispositivo;
        do{
            tipoDispositivo = scan.nextInt();
            if(tipoDispositivo < 1 || tipoDispositivo > 3){
                System.out.println("Tipo de dispositivo inválido");
                tipoDispositivo = null;
            }
        }while(tipoDispositivo == null);
        return switch (tipoDispositivo) {
            case 1 -> criacaoSmartBulb();
            case 2 -> criacaoSmartCamera();
            case 3 -> criacaoSmartSpeaker();
            default -> null;
        };
    }

    public void ligaDesDispositivo(){
        System.out.println("Lista de NIFs disponiíveis: " + this.log.setNIFs());
        System.out.println("Insira o nif da casa onde deseja ligar/desligar dispositivos");
        String nif = scan.nextLine();
        try {
            System.out.println("Casa selecionada: " + this.log.getCasa(nif));
            System.out.println("Que dispositivos pretende ligar/desligar(Casa, Individual, Divisao");
            String decisao = scan.nextLine();
            System.out.println("Pretende desligar ou ligar? Ligar(True), Desligar(False)");
            boolean on_off = scan.nextBoolean();
            scan.nextLine();
            switch (decisao) {
                case "Casa" -> this.log.addPedido(estado -> {
                    try {
                        estado.setAllDevicesHouseOn(nif, on_off);
                    } catch (CasaInexistenteException e) {
                        System.out.println("Casa nao existe");
                    }
                });
                case "Individual" -> {
                    System.out.println("Lista de dispositivos da casa: " + this.log.getCasa(nif).getListDevices());
                    System.out.println("Insira o id do dispositivo");
                    int id = scan.nextInt();
                    scan.nextLine();
                    this.log.addPedido(estado -> {
                        try {
                            estado.setDeviceHouseOn(nif, id, on_off);
                        } catch (DeviceInexistenteException e) {
                            System.out.println("Nao existe o dispositivo na casa");
                        } catch (CasaInexistenteException e) {
                            System.out.println("Nao existe a casa inserida");
                        }
                    });
                }
                case "Divisao" -> {
                    System.out.println("Lista de divisões da casa: " + this.log.getRoomsHouse(nif));
                    System.out.println("Insira a divisao que pretende selecionar: ");
                    String room = scan.nextLine();
                    this.log.addPedido(estado -> {
                        try {
                            estado.setAllDevicesHouseOnRoom(nif, room, on_off);
                        } catch (RoomInexistenteException e) {
                            System.out.println("Nao existe a divisao na casa");
                        } catch (CasaInexistenteException e) {
                            System.out.println("Nao existe a casa inserida");
                        }
                    });
                }
            }
        } catch (CasaInexistenteException e) {
            System.out.println("Nao existe uma casa com o nif " + nif);
        }
    }

    public void edicaoSmartBulb(String nif, int id) {
      System.out.println("Qual tonalidade deseja que a SmartBulb tenha? (0 - WARM, 1 - COLD, 2 - NEUTRAL");  
      int valor = this.scan.nextInt();
      this.scan.nextLine();
      switch(valor){
        case 0 -> {
          this.log.addPedido(estado -> {
            try {
              estado.alteraInfoBulbCasa(nif, id, device -> device.setTone(Tones.WARM));
            } catch (CasaInexistenteException | DeviceInexistenteException | TipoDeviceErradoException e) {
              System.out.println(e.getMessage());
            }
          });
        }
        case 1 -> {
          this.log.addPedido(estado -> {
            try {
              estado.alteraInfoBulbCasa(nif, id, device -> device.setTone(Tones.COLD));
            } catch (CasaInexistenteException | DeviceInexistenteException | TipoDeviceErradoException e) {
              System.out.println(e.getMessage());
            }
          });
        }
        case 2 -> {
          this.log.addPedido(estado -> {
            try {
              estado.alteraInfoBulbCasa(nif, id, device -> device.setTone(Tones.NEUTRAL));
            } catch (CasaInexistenteException | DeviceInexistenteException | TipoDeviceErradoException e) {
              System.out.println(e.getMessage());
            }
          });
        }
        default -> {
          System.out.println("Valor inserido inválido");
        }
      }
    }

    public void edicaoSmartCamera(String nif, int id) {
      System.out.println("O que pretende editar? (width, height, tamanhoFicheiro");      
      String opcao = this.scan.nextLine();
      switch (opcao) {
        case "width" -> {
          System.out.println("Insira o novo width");
          int width = this.scan.nextInt();
          this.scan.nextLine();
          this.log.addPedido(estado -> {
            try {
              estado.alteraInfoCameraCasa(nif, id, device -> device.setResolutionX(width));
            } catch (CasaInexistenteException | DeviceInexistenteException | TipoDeviceErradoException e) {
              System.out.println(e.getMessage());
            }
          });
        }
        case "height" -> {
          System.out.println("Insira a nova heigth");
          int height = this.scan.nextInt();
          this.scan.nextLine();
          this.log.addPedido(estado -> {
            try {
              estado.alteraInfoCameraCasa(nif, id, device -> device.setResolutionY(height));
            } catch (CasaInexistenteException | DeviceInexistenteException | TipoDeviceErradoException e) {
              System.out.println(e.getMessage());
            }
          });
        }

        case "tamanhoFicheiro" -> {
          System.out.println("Insira o novo tamanho dos ficheiros gerados");
          int tam = this.scan.nextInt();
          this.scan.nextLine();
          this.log.addPedido(estado -> {
            try {
              estado.alteraInfoCameraCasa(nif, id, device -> device.setFileDim(tam));
            } catch (CasaInexistenteException | DeviceInexistenteException | TipoDeviceErradoException e) {
              System.out.println(e.getMessage());
            }
          });
        }

        default -> System.out.println("Opção inválida");

      }
    }

    public void edicaoSmartSpeaker(String nif, int id){
        System.out.println("Insira o que pretende alterar no speaker (Volume, Estacao de Radio");
        String opcao = this.scan.nextLine();
        switch (opcao){
            case "Volume" -> {
                System.out.println("Insira o volume para o qual quer mudar");
                int volume = this.scan.nextInt();
                this.scan.nextLine();
                this.log.addPedido(estadoPrograma -> {
                    try {
                        estadoPrograma.alteraInfoSpeakerCasa(nif, id, device -> device.setVolume(volume));
                    } catch (DeviceInexistenteException | TipoDeviceErradoException | CasaInexistenteException e) {
                        System.out.println(e.getMessage());
                    }
                });
            }
            case "Estacao de Radio" -> {
                System.out.println("Insira o nome da estacao de radio para o qual quer mudar");
                String estacao = this.scan.nextLine();
                this.log.addPedido(estado -> {
                    try {
                        estado.alteraInfoSpeakerCasa(nif, id, device -> device.setRadioStation(estacao));
                    } catch (DeviceInexistenteException | CasaInexistenteException | TipoDeviceErradoException e) {
                        System.out.println(e.getMessage());
                    }
                });
            }
        }
    }

    public void edicaoDispositivos(){
      System.out.println("Lista de NIFs disponiveis no programa " + this.log.setNIFs());
      String nif = this.scan.nextLine();
      try {
        Casa c = this.log.getCasa(nif);
        System.out.println("Dispositivos da casa selecionada: " + c.getListDevices());
        System.out.println("Insira o id do dispositivo que pretende editar");
        int id = this.scan.nextInt();
        this.scan.nextLine();
        SmartDevice device = c.getDevice(id);
        switch(device.getClass().getSimpleName()){
          case "SmartBulb" -> edicaoSmartBulb(nif, id);
          case "SmartCamera" -> edicaoSmartCamera(nif, id);
          case "SmartSpeaker" -> edicaoSmartSpeaker(nif, id);
        }
      } catch (CasaInexistenteException | DeviceInexistenteException e) {
        System.out.println(e.getMessage());
      }
    }

    public void gestaoDispositivos(){
        Menu menuDispositivos = new Menu(List.of("MENU GESTÃO DISPOSITIVOS", "1. Criar Dispositivo", "2. Ligar/Desligar Dispositivo", "3. Editar Dispositivo", "0. Sair"));
        do{
            menuDispositivos.run();
            switch (menuDispositivos.getOpcao()) {
                case 1 -> {
                    SmartDevice device = criaDispositivo();
                    if (device == null) System.out.println("Ocorreu algum erro a criar o dispositivo");
                    else {
                        try {
                            System.out.println("Lista de NIFs disponíveis: " + this.log.setNIFs());
                            System.out.println("Insira o nif onde quer adicionar o dispositivo");
                            String nif = this.scan.nextLine();
                            this.log.addDeviceToCasa(nif, device);

                            System.out.println("Deseja inserir o device em alguma divisao?(S/N)");
                            String opcao = this.scan.nextLine();
                            if (opcao.equals("S")) {
                                System.out.println("Lista de divisões disponíveis na casa: " + this.log.getCasa(nif).getListRooms());
                                System.out.println("Insira o nome da divisao a inserir o dispositivo");
                                String divisao = this.scan.nextLine();
                                this.log.addDeviceToCasaOnRoom(nif, divisao, device.getId());
                            }
                        } catch (CasaInexistenteException exc) {
                            System.out.println("Nao existe nenhuma casa com um proprietario com o nif inserido");
                        } catch (RoomInexistenteException exc) {
                            System.out.println("Nao existe a divisao inserida na casa pretendida");
                        }
                    }
                }
                case 2 -> {
                    this.ligaDesDispositivo();
                }
                case 3 -> {
                  this.edicaoDispositivos();
                }
            }
        } while(menuDispositivos.getOpcao() != 0);
    }

    public Casa criacaoCasa() {

        System.out.println("Insira o nome do proprietário da casa");
        String nome = scan.nextLine();
        System.out.println("Insira o nif do proprietário da casa");
        String nif = scan.nextLine();
        System.out.println("Lista de fornecedores disponíveis no programa: " + this.log.getFornecedores().keySet());
        System.out.println("Insira o empresa fornecedora do proprietário da casa");
        String empresa = scan.nextLine();

        System.out.println("Deseja inserir divisões à casa? S/N");
        String opcao = scan.nextLine();
        if(opcao.equals("S")){
            Set<String> divisoes = new TreeSet<>();
            boolean keep = true;
            System.out.println("Insira o nome das divisoes sucessivamente(Digite Sair para terminar)");
            while(keep){
                String divisao = scan.nextLine();
                if(divisao.equals("Sair")) keep = false;
                else divisoes.add(divisao);
            }
            return new Casa(nome, nif, divisoes, empresa);
        }
        else return new Casa(nome, nif, empresa);
    }

    public void edicaoCasas(){
        Menu menu = new Menu(List.of("MENU EDICÃO CASA: ", "1. Adicionar divisões", "2. Adicionar/Mudar device de divisão", "3. Remover divisão", "4. Juntar duas divisões", "0. Sair"));
        do{
            menu.run();
            switch (menu.getOpcao()) {
                case 1 -> {
                    System.out.println("Insira o NIF associado à casa que pretende adicionar divisões");
                    System.out.println("NIFs inscritos no programa: " + this.log.setNIFs());
                    String nif = scan.nextLine();
                    try {
                        System.out.println("Casa que vai editar: " + this.log.getCasa(nif));
                        System.out.println("Insira o nome das divisões que pretende adicionar(Digite sair para parar de adicionar: ");
                        String divisao;
                        do {
                            divisao = this.scan.nextLine();
                            try {
                                this.log.addDivisaoToCasa(nif, divisao);
                                System.out.println("Divisão adicionada com sucesso");
                            } catch (RoomAlreadyExistsException e) {
                                System.out.println("Já existe a divisão " + divisao + " nesta casa");
                            }
                        }
                        while (!divisao.equals("sair"));
                    } catch (CasaInexistenteException e) {
                        System.out.println("Não existe uma casa com o nif inserido");
                    }
                }
                case 2 -> {
                    System.out.println("Insira o NIF associado à casa que pretende remover");
                    System.out.println("NIFs inscritos no programa: " + this.log.setNIFs());
                    String nif = this.scan.nextLine();
                    try {
                        System.out.println("Casa a editar: " + this.log.getCasa(nif));
                        System.out.println("Insira o codigo do dispositivo que pretende mudar de divisão");
                        int device = this.scan.nextInt();
                        scan.nextLine(); // clear buffer
                        System.out.println("Insira a divisão onde pretende colocar(Digita \"Nenhuma\" se nao quiser por em nenhum sítio)");
                        String divisao = scan.nextLine();
                        if (!divisao.equals("Nenhuma")) {
                            try {
                                this.log.mudaDeviceRoom(nif, device, divisao);
                                System.out.println("Mudança bem sucedida");
                            } catch (DeviceInexistenteException e) {
                                System.out.println("Não existe o dispositivo com o código inserido: " + device + " na casa de nif: " + nif);
                            } catch (RoomInexistenteException e) {
                                System.out.println("Não existe a divisão " + divisao + " na casa de nif " + nif);
                            }
                        } else {
                            try {
                                this.log.removeDeviceRoom(nif, device);
                                System.out.println("O device foi removido da divisão com sucesso");
                            } catch (DeviceInexistenteException e) {
                                System.out.println("Não existe o dispositivo de código " + device);
                            }
                        }
                    } catch (CasaInexistenteException e) {
                        System.out.println("Não existe a casa com o nif: " + nif);
                    }
                }
                case 3 -> {
                    System.out.println("Insira o NIF associado à casa que pretende remover");
                    System.out.println("NIFs inscritos no programa: " + this.log.setNIFs());
                    String nif = this.scan.nextLine();
                    try {
                        System.out.println("Casa a editar : " + this.log.getCasa(nif));
                        System.out.println("Insira a divisão que pretende remover");
                        String divisao = scan.nextLine();
                        this.log.removeRoomCasa(nif, divisao);
                        System.out.println("Divisão removida com sucesso");
                    } catch (CasaInexistenteException exc) {
                        System.out.println("Não existe casa com o nif " + nif);
                    }
                }
                case 4 -> {
                    System.out.println("Insira o NIF associado à casa que pretende remover");
                    System.out.println("NIFs inscritos no programa: " + this.log.setNIFs());
                    String nif = this.scan.nextLine();
                    try {
                        System.out.println("Casa a editar : " + this.log.getCasa(nif));
                        System.out.println("Insira o nome de uma das divisões a juntar");
                        String divisao = scan.nextLine();
                        System.out.println("Insira o nome da outra divisão a juntar");
                        String divisao2 = scan.nextLine();
                        System.out.println("Insira o nome da nova divisão");
                        String nova = scan.nextLine();
                        this.log.juntaRoomsHouse(nif, divisao, divisao2, nova);
                    } catch (CasaInexistenteException exc) {
                        System.out.println("Não existe casa com o nif " + nif);
                    } catch (RoomAlreadyExistsException e) {
                        e.printStackTrace();
                    }
                }
            }
        } while(menu.getOpcao() != 0);
    }

    public void gestaoCasas(){
        Menu menu = new Menu(List.of("MENU GESTÃO CASAS:", "1. Criar Casa", "2. Mudar Fornecedor Casa", "3. Remover Casa", "4. Listar Casas", "5. Listar NIFs inscritos no programa", "6. Visualizar o conteúdo de uma casa", "7. Ver Faturas de uma Casa", "8. Editar Casa", "0. Voltar"));

        do{
            menu.run();
            switch (menu.getOpcao()){
                case 1 : {
                    try {
                        this.log.adicionaCasa(criacaoCasa());
                        System.out.println("Casa criada com sucesso");
                    } catch (ExisteCasaException e) {
                        System.out.println("Ja existe uma casa com o proprietário inserido");
                    } catch (FornecedorInexistenteException e) {
                        System.out.println("Nao existe o fornecedor inserido");
                    }
                    break;
                }
                case 2 : {
                    System.out.println("Diga o fornecedor para o qual quer mudar");
                    System.out.println("Fornecedores disponiveis: " + this.log.getFornecedores().keySet());
                    String fornecedor = this.scan.nextLine();
                    System.out.println("Insira o nif do prorietário da casa onde quer mudar de fornecedor");
                    System.out.println("Lista de NIFs no programa: " + this.log.setNIFs());
                    String casa = this.scan.nextLine();
                    this.log.addPedido(l -> {
                        try {
                            l.mudaFornecedorCasa(casa, fornecedor);
                        } catch (CasaInexistenteException e) {
                            System.out.println("Nao existe casa com o nif inserido");
                        } catch (FornecedorInexistenteException e) {
                            System.out.println("Nao existe o fornecedor inserido");
                        }
                    });
                    System.out.println("Pedido de mudanca de fornecedor emitido");
                    break;
                }
                case 3: {
                    System.out.println("Insira o nif associado à casa que pretende remover");
                    System.out.println("NIFs inscritos no programa: " + this.log.setNIFs());
                    String nif = scan.nextLine();
                    try {
                        this.log.removeCasa(nif);
                        System.out.println("Casa removida com sucesso");
                    } catch (CasaInexistenteException exc) {
                        System.out.println("Não foi possivel realizar a remoção da casa, pois não existe nenhuma casa com o nif inserido");
                    }
                    break;
                }
                case 4: {
                    System.out.println("Lista de casas presentes no programa: " + this.log.listaCasas());
                    break;
                }
                case 5: {
                    System.out.println("Lista de NIFs inscritos no programa: " + this.log.setNIFs());
                    break;
                }
                case 6 : {
                    System.out.println("Insira o NIF do proprietário da casa que deseja visualizar: ");
                    System.out.println("NIFs disponíveis no programa: " + this.log.setNIFs());
                    String nif = scan.nextLine();
                    try{
                        System.out.println("Casa: " + this.log.getCasa(nif));
                    }
                    catch(CasaInexistenteException exc){
                        System.out.println("Não existe casa com um NIF inserido");
                    }
                    break;
                }
                case 7 : {
                    System.out.println("Insira o NIF do proprietário da casa: ");
                    System.out.println("NIFs disponíveis no programa: " + this.log.setNIFs());
                    String nif = scan.nextLine();
                    try {
                        System.out.println("Faturas da casa: " + this.log.faturasCasa(nif));
                    } catch (CasaInexistenteException exc) {
                        System.out.println("Casa inexistente com o nif de: " + nif);
                    }
                }
                case 8:{
                    edicaoCasas();
                    break;
                }


                }
        } while(menu.getOpcao() != 0);
    }

    public void gestaoData(){
        Menu data = new Menu(List.of("AVANCAR DATA: ", "1. Avancar 1 dia", "2. Avancar x dias", "3. Avancar para uma data", "0. Voltar"));
        LocalDate date = null;
        data.run();
        switch (data.getOpcao()) {
            case 1 -> date = this.log.getDataAtual().plusDays(1);
            case 2 -> {
                System.out.println("Insira o número de dias que quer avancar");
                int days = scan.nextInt();
                scan.nextLine();
                date = this.log.getDataAtual().plusDays(days);
            }
            case 3 -> {
                System.out.println("Insira a data para onde quer avancar AAAA-MM-DD");
                String d = scan.nextLine();
                date = LocalDate.parse(d);
            }
        }
        if(date != null) {
            try {
                this.log.avancaData(date);
                System.out.println("Avançou no tempo com sucesso");
            } catch (DataInvalidaException e) {
                System.out.println("Data inserida era inválida");
            } catch (FornecedorErradoException e) {
                System.out.println("Ocorreu algum erro no calculo das faturas");
            }
        }
    }

    public void estatisticasPrograma(){
        Menu menu = new Menu(List.of("Menu Estatisticas:", "1. Casa que mais consumiu no último avanço", "2. Comercializador com maior Faturaração", "3. Maior Consumidor de um Periodo", "4. Top de tipo de Dispositivo mais utilizado", "0. Voltar"));
        do{
            menu.run();
            switch (menu.getOpcao()) {
                case 1 -> this.log.getCasaMaisGastadora().ifPresentOrElse(
                        c -> System.out.println("A casa que mais gastou no ultimo periodo : " + c),
                        () -> System.out.println("Nao existe nenhuma casa ainda")
                );
                case 2 -> this.log.getFornecedorMaiorFaturacao().ifPresentOrElse(
                        c -> System.out.println("O fornecedor que mais faturou foi : " + c),
                        () -> System.out.println("Nao existe nenhuma casa ainda")
                );
                case 3 -> {
                    System.out.println("Insira a data inicial do periodo AAAA-MM-DD");
                    LocalDate dataInicial = LocalDate.parse(this.scan.nextLine());
                    System.out.println("Insira a data final do periodo AAAA-MM-DD");
                    LocalDate dataFinal = LocalDate.parse(this.scan.nextLine());
                    System.out.println("Insira o numero de casas que quer recolher informação");
                    int N = this.scan.nextInt();
                    this.scan.nextLine();
                    System.out.println("Top " + N + " de casas mais gastadoras neste periodo: " + this.log.maiorConsumidorPeriodo(dataInicial, dataFinal, N));
                }
                case 4 -> {
                    System.out.println("Top tipo dispositivos:");
                    Iterator<String> top = this.log.podiumDeviceMaisUsado().iterator();
                    int i = 1;
                    while (top.hasNext()) {
                        String d = top.next();
                        System.out.println(i + "º- " + d);
                        ++i;
                    }
                }
            }
        }while(menu.getOpcao() != 0);
    }

    public void gestaoPrograma(){
        Menu menuP = new Menu(List.of("MENU ESTADO PROGRAMA", "1. Apresentar estado", "2. Apresentar Estatísticas", "3. Avancar data", "0. Voltar"));
        do{
            menuP.run();
            switch (menuP.getOpcao()) {
                case 1 -> System.out.println(this.log);
                case 2 -> estatisticasPrograma();
                case 3 -> gestaoData();
            }
        } while(menuP.getOpcao() != 0);
    }

    public void gestaoFornecedor(){
        Menu menuF = new Menu(List.of("MENU GESTAO FORNECEDORES", "1: Criar Fornecedores","2. Faturas de um fornecedor", "3. Lista de Fornecedores", "4. Visualizar dados Fornecedor", "5. Mudar valor desconto Fornecedor", "0. Voltar"));
        do{
            menuF.run();
            switch (menuF.getOpcao()) {
                case 1 -> {
                    System.out.println("Insira o nome do fornecedor a inserir");
                    String fornecedor = this.scan.nextLine();
                    System.out.println("Insira o desconto que o fornecedor vai aplicar");
                    double desconto = this.scan.nextDouble();
                    this.scan.nextLine();
                    try {
                        this.log.addFornecedor(new Fornecedor(fornecedor, desconto));
                        System.out.println("Fornecedor criado com sucesso");
                    } catch (ExisteFornecedorException e) {
                        System.out.println("Ja existe este fornecedor");
                    }
                }
                case 2 -> {
                    System.out.println("Insira o nome do fornecedor");
                    String fornecedor = this.scan.nextLine();
                    System.out.println("Lista de faturas : " + this.log.getFaturasFornecedor(fornecedor));
                }
                case 3 -> System.out.println("Lista de fornecedores: " + this.log.getSetFornecedores());
                case 4 -> {
                    System.out.println("Insira o nome do fornecedor desejado");
                    System.out.println("Fornecedores existentes: " + this.log.getFornecedores().keySet());
                    String nome = this.scan.nextLine();
                    try {
                        System.out.println(this.log.getFornecedor(nome));
                    } catch (FornecedorInexistenteException exc) {
                        System.out.println("Nao existe o fornecedor com o nome inserido: " + nome);
                    }
                }
                case 5 -> {
                    System.out.println("Insira o nome do fornecedor onde quer mudar o valor de desconto");
                    System.out.println("Fornecedores disponiveis: " + this.log.getFornecedores().keySet());
                    String nome = this.scan.nextLine();
                    try {
                        System.out.println("Fornecedor selecionado : " + this.log.getFornecedor(nome));
                        System.out.println("Insira o valor novo do desconto(Insira a percentagem da forma decimal: ");
                        double desconto = this.scan.nextDouble();
                        this.scan.nextLine();
                        this.log.addPedido(estado -> {
                            try {
                                estado.mudaDescontoFornecedor(nome, desconto);
                            } catch (FornecedorInexistenteException e) {
                                e.printStackTrace();
                            }
                        });
                        System.out.println("Pedido submetido com sucesso");
                    } catch (FornecedorInexistenteException e) {
                        System.out.println("Nao existe o fornecedor de nome " + nome);
                    }
                }
            }
        }while(menuF.getOpcao() != 0);
    }

    public void run(){
        Menu menuPrincipal = new Menu(List.of("MENU PRINCIPAL", "1. Gerir Casas", "2. Gerir Dispositivos", "3. Gerir Fornecedores", "4. Estado Programa", "0. Sair"));
        do{
            menuPrincipal.run();
            switch (menuPrincipal.getOpcao()) {
                case 1 -> gestaoCasas();
                case 2 -> gestaoDispositivos();
                case 3 -> gestaoFornecedor();
                case 4 -> gestaoPrograma();
            }
        } while(menuPrincipal.getOpcao() != 0);
        this.log.guardaDados();
    }

    public static void main(String[] args) {
        new Programa().run();
    }

}
