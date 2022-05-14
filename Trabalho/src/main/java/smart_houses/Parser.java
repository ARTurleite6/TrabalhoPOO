package smart_houses;

import smart_houses.exceptions.*;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartSpeaker;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Parser {

    public EstadoPrograma parse() throws AlreadyExistDeviceException, ExisteFornecedorException, FornecedorInexistenteException, ExisteCasaException, RoomAlreadyExistsException, DeviceInexistenteException, RoomInexistenteException {
        List<String> linhas = lerFicheiro("./src/main/resources/log.txt");
        String[] linhaPartida;
        String divisao = null;
        Casa casaMaisRecente = null;
        EstadoPrograma estado = new EstadoPrograma();
        for (String linha : linhas) {
            linhaPartida = linha.split(":", 2);
            switch (linhaPartida[0]) {
                case "Fornecedor":
                    Fornecedor f = new Fornecedor(linhaPartida[1]);
                        estado.addFornecedor(f);
                    break;
                case "Casa":
                    if(casaMaisRecente != null) {
                            estado.adicionaCasa(casaMaisRecente);
                    }
                    casaMaisRecente = parseCasa(linhaPartida[1]);
                    break;
                case "Divisao":
                    if (casaMaisRecente == null) System.out.println("Linha inválida.");
                    if(divisao != null) {
                            casaMaisRecente.addRoom(divisao);
                    }
                    divisao = linhaPartida[1];
                        casaMaisRecente.addRoom(divisao);
                    break;
                case "SmartBulb":
                    if (divisao == null) System.out.println("Linha inválida.");
                    SmartBulb sd = parseSmartBulb(linhaPartida[1]);
                    casaMaisRecente.addDevice(sd);
                        casaMaisRecente.addDeviceOnRoom(divisao, sd.getId());
                    break;
                case "SmartCamera":
                    if (divisao == null) System.out.println("Linha inválida.");
                    SmartCamera sc = parseSmartCamera(linhaPartida[1]);
                    casaMaisRecente.addDevice(sc);
                        casaMaisRecente.addDeviceOnRoom(divisao, sc.getId());
                    break;
                case "SmartSpeaker":
                    if(divisao == null) System.out.println("Linha Invalida.");
                    SmartSpeaker ss = parseSmartSpeaker(linhaPartida[1]);
                    casaMaisRecente.addDevice(ss);
                        casaMaisRecente.addDeviceOnRoom(divisao, ss.getId());
                    break;
                default:
                    System.out.println("Linha inválida.");
                    break;
            }
        }
        System.out.println("done!");
        return estado;
    }

    private SmartSpeaker parseSmartSpeaker(String input) {
        Random r = new Random();
        String[] campos = input.split(",");
        int volume = Integer.parseInt(campos[0]);
        String estacao = campos[1];
        String marca = campos[2];
        double consumo = Double.parseDouble(campos[3]);
        return new SmartSpeaker(r.nextBoolean(), consumo, volume, estacao, marca);
    }

    private SmartCamera parseSmartCamera(String s) {
        Random r = new Random();
        String[] campos = s.split(",");
        String res = campos[0];
        String[] camposRes = res.split("x");
        int resX = Integer.parseInt(camposRes[0].substring(1));
        int resY = Integer.parseInt(camposRes[1].substring(0, camposRes[1].length() - 1));
        int tam = Integer.parseInt(campos[1]);
        double consumo = Double.parseDouble(campos[2]);
        return new SmartCamera(r.nextBoolean(), consumo, resX, resY, tam);
    }

    public SmartBulb parseSmartBulb(String linha){
        Random r = new Random();
        String[] campos = linha.split(",");
        String tone = campos[0];
        SmartBulb.Tones t = SmartBulb.Tones.WARM;
        if(tone.equals("Warm")) t = SmartBulb.Tones.WARM;
        else if(tone.equals("Neutral")) t = SmartBulb.Tones.NEUTRAL;
        else t = SmartBulb.Tones.COLD;
        int dimensao = Integer.parseInt(campos[1]);
        double consumo = Double.parseDouble(campos[2]);
        return new SmartBulb(r.nextBoolean(), consumo, t, dimensao);
    }

    public List<String> lerFicheiro(String nomeFich) {
        List<String> lines;
        try { lines = Files.readAllLines(Paths.get(nomeFich), StandardCharsets.UTF_8); }
        catch(IOException exc) { lines = new ArrayList<>(); }
        return lines;
    }

    public Casa parseCasa(String input){
        String[] campos = input.split(",");
        String nome = campos[0];
        String nif = campos[1];
        String fornecedor = campos[2];
        return new Casa(nome, nif, fornecedor);
    }
}
