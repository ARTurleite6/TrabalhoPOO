
public class Programa {

    private ColecoesPrograma collections;

    public Programa(){
        this.collections = new ColecoesPrograma();
    }

    public Programa(ColecoesPrograma collections){
        this.collections = collections.clone();
    }

    public Programa(Programa p){
        this.collections = p.collections;
    }

    public void run(){
        int choice = 0;

        while(choice != 5){
            choice = Menu.initialMenu();
            switch (choice){
                case 1 : {
                    GestaoCasas.run(this.collections);
                    break;
                }
                case 2 : {
                    GestaoDispositivos.run(this.collections);
                }
                case 3 : {
                    GestaoFornecedores.run(this.collections);
                }
                case 4 : {
                    System.out.println(collections.getCasas().toString());
                }
            }
        }

        System.out.println("Saindo...");
    }

    public ColecoesPrograma getCollections() {
        return collections.clone();
    }

    public void setCollections(ColecoesPrograma collections) {
        this.collections = collections.clone();
    }
}
