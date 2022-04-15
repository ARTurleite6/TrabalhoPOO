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
                    Menu.criacaoSmartDevice(colections.getCasas());
                }
                case 3 : {
                    System.out.println(colections.getCasas().toString());
                }
            }
        }

        System.out.println("Saindo...");
    }

}
