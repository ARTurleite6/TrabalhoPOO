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

import java.time.LocalDate;
import java.util.*;

public class Programa {

    private EstadoPrograma log;
    private Scanner scan;

    public Programa(){
        /*
        try {
            this.log = EstadoPrograma.carregaDados();
        } catch (IOException | ClassNotFoundException e) {
            this.log = new Parser().parse();
        }
         */
        this.log = new EstadoPrograma();
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
        System.out.println("Insira o custo de instalacao do dispostivo");
        double custoInstalacao = scan.nextDouble();
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
        return new SmartCamera(ligado, custoInstalacao, resolucaoX, resolucaoY, dimensao);
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
        SmartDevice device;
        switch(tipoDispositivo){
            case 1 : device = criacaoSmartBulb();
                break;
            case 2 : device = criacaoSmartCamera();
                break;
            case 3 : device = criacaoSmartSpeaker();
                break;
            default: device = null;
        }
        return device;
    }

    public void gestaoDispositivos(){
        Menu menuDispositivos = new Menu(List.of("MENU GESTÃO DISPOSITIVOS", "1. Criar Dispositivo", "2. Ligar/Desligar Dispositivo", "0. Sair"));
        do{
            menuDispositivos.run();
            switch(menuDispositivos.getOpcao()){
                case 1 : SmartDevice device = criaDispositivo();
                    if(device == null) System.out.println("Ocorreu algum erro a criar o dispositivo");
                    else {
                        try {
                            System.out.println("Insira o nif onde quer adicionar o dispositivo");
                            String nif = this.scan.nextLine();
                            this.log.addDeviceToCasa(nif, device);

                            System.out.println("Deseja inserir o device em alguma divisao?(S/N)");
                            String opcao = this.scan.nextLine();
                            if (opcao.equals("S")) {
                                System.out.println("Insira o nome da divisao a inserir o dispositivo");
                                String divisao = this.scan.nextLine();
                                this.log.addDeviceToCasaOnRoom(nif, divisao, device.getId());
                            }
                        }
                        catch (CasaInexistenteException exc){
                            System.out.println("Nao existe nenhuma casa com um proprietario com o nif inserido");
                        }
                        catch (RoomInexistenteException exc){
                            System.out.println("Nao existe a divisao inserida na casa pretendida");
                        }
                    }
                    break;
                case 2 :
                    break;
            }
        } while(menuDispositivos.getOpcao() != 0);
    }

    public Casa criacaoCasa() {

        System.out.println("Insira o nome do proprietário da casa");
        String nome = scan.nextLine();
        System.out.println("Insira o nif do proprietário da casa");
        String nif = scan.nextLine();
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

    public void gestaoCasas(){
        Menu menu = new Menu(List.of("Menu Gestao Casas:", "1. Criar Casa", "2. Mudar Fornecedor Casa", "0. Voltar"));

        do{
            menu.run();
            switch (menu.getOpcao()){
                case 1 : try {
                    this.log.adicionaCasa(criacaoCasa());
                    System.out.println("Casa criada com sucesso");
                    } catch (ExisteCasaException e) {
                        System.out.println("Ja existe uma casa com o proprietário inserido");
                    } catch (FornecedorInexistenteException e) {
                        System.out.println("Nao existe o fornecedor inserido");
                    }
                    break;
                case 2 :
                    System.out.println("Diga o fornecedor para o qual quer mudar");
                    String fornecedor = this.scan.nextLine();
                    System.out.println("Diga insira o nif do prorietário da casa onde quer mudar de fornecedor");
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
        } while(menu.getOpcao() != 0);
    }

    public void gestaoData(){
        Menu data = new Menu(List.of("AVANCAR DATA: ", "Avancar 1 dia", "Avancar x dias", "Avancar para uma data", "0. Voltar"));
        LocalDate date = null;
        do{
            data.run();
            switch (data.getOpcao()){
                case 1 : date = this.log.getDataAtual().plusDays(1);
                break;
                case 2 : System.out.println("Insira o número de dias que quer avancar");
                        int days = scan.nextInt();
                        scan.nextLine();
                    date = this.log.getDataAtual().plusDays(days);
                        break;
                case 3 : System.out.println("Insira a data para onde quer avancar AAAA/MM/DD");
                    String d = scan.nextLine();
                    date = LocalDate.parse(d);
                    break;
            }
        } while(data.getOpcao() != 0);
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

    public void gestaoPrograma(){
        Menu menuP = new Menu(List.of("MENU ESTADO PROGRAMA", "1. Apresentar estado", "2. Apresentar Estatísticas", "3. Avancar data", "0. Voltar"));
        do{
            menuP.run();
            switch (menuP.getOpcao()){
                case 1: System.out.println(this.log);
                    break;
                case 2:
                    break;
                case 3: gestaoData();
                    break;
                case 4:
                    break;
            }
        } while(menuP.getOpcao() != 0);
    }

    public void gestaoFornecedor(){
        Menu menuF = new Menu(List.of("Menu Gestao Fornecedores", "1: Criar Fornecedores", "0. Voltar"));
        do{
            menuF.run();
            switch (menuF.getOpcao()){
                case 1 : System.out.println("Insira o nome do fornecedor a inserir");
                    String fornecedor = this.scan.nextLine();
                    try {
                        this.log.addFornecedor(new Fornecedor(fornecedor));
                        System.out.println("Fornecedor criado com sucesso");
                    } catch (ExisteFornecedorException e) {
                        System.out.println("Ja existe este fornecedor");
                    }
                    break;
            }
        }while(menuF.getOpcao() != 0);
    }

    public void run(){
        Menu menuPrincipal = new Menu(List.of("MENU PRINCIPAL", "1. Gerir Casas", "2. Gerir Dispositivos", "3. Gerir Fornecedores", "4. Estado Programa", "0. Sair"));
        do{
            menuPrincipal.run();
            switch(menuPrincipal.getOpcao()){
                case 1:
                    gestaoCasas();
                    break;
                case 2:
                    gestaoDispositivos();
                    break;
                case 3:
                    gestaoFornecedor();
                    break;
                case 4:
                    gestaoPrograma();
                    break;
            }
        } while(menuPrincipal.getOpcao() != 0);
    }

    public static void main(String[] args) {
        new Programa().run();
    }

}
