package smart_houses.input_output;

import smart_houses.EstadoPrograma;
import smart_houses.exceptions.CasaInexistenteException;
import smart_houses.exceptions.FornecedorInexistenteException;
import smart_houses.modulo_casas.Casa;

import java.util.Scanner;

public class MenuCasas {

    private int opcao;
    private Scanner scan;

    public MenuCasas(){
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
        System.out.println("""
                Menu Gestao Casas
                1: Criar Casa
                2: Mudar Fornecedor da Casa
                0: Sair""");
    }

    public Casa criacaoCasa() {
        String nome, nif, empresa;

        System.out.println("Insira o nome do proprietário da casa");
        nome = scan.nextLine();
        System.out.println("Insira o nif do proprietário da casa");
        nif = scan.nextLine();
        System.out.println("Insira o empresa fornecedora do proprietário da casa");
        empresa = scan.nextLine();

        return new Casa(nome, nif, empresa);
    }
/*
    protected static void run(EstadoPrograma e) {
        int choice = 0;
        while (choice != 3) {
            choice = MenuCasas.gestaoCasas();
            if (choice == 1) {
                MenuCasas.criacaoCasa(e);
            }
            else if(choice == 2){
                MenuCasas.mudaFornecedorCasa(e);
            }
        }
    }
    */
}
