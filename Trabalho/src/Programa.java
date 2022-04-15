import javax.swing.plaf.synth.SynthTextAreaUI;
import java.util.Optional;
import java.util.Scanner;

public class Programa {


    public static void run(){
        ColecoesPrograma colections = new ColecoesPrograma();
        Scanner scan = new Scanner(System.in);
        int choice = 0;

        while(choice != 4){
            choice = Menu.initialMenu();
            switch (choice){
                case 1 : {
                    Casa casa = Menu.criacaoCasa();
                    colections.getCasas().addCasa(casa);
                    break;
                }
                case 2 : {
                    Optional<SmartDevice> dispositivo = Menu.criacaoSmartDevice();
                    dispositivo.ifPresent(d -> {
                        System.out.println("Insira o nif da casa em que quer inserir o dispostivo");
                        String nif = scan.next();
                        if(colections.getCasas().existCasa(nif)){
                            colections.getCasas().addDeviceToCasa(nif, d);
                        }
                        else System.out.println("Esta casa nao existe");
                    });
                }
                case 3 : {
                    System.out.println(colections.getCasas().toString());
                }
            }
        }

        System.out.println("Saindo...");
    }

}
