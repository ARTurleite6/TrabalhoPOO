package smart_houses.input_output;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu {
    private int opcao;
    private List<String> opcoes;
    private Scanner scan;

    public Menu(List<String> opcoes){
        this.opcoes = new ArrayList<>(opcoes);
        this.opcao = -1;
        this.scan = new Scanner(System.in);
    }

    public int getOpcao() {
        return opcao;
    }

    public void run(){
        do{
            this.showMenu();
            this.opcao = this.scan.nextInt();
            this.scan.nextLine();
        } while(this.opcao == -1);
    }

    private void showMenu(){
        for(String opcao : this.opcoes){
            System.out.println(opcao);
        }
    }

}
