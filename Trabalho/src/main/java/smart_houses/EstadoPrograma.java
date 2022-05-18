package smart_houses;

import smart_houses.exceptions.*;
import smart_houses.modulo_casas.Casa;
import smart_houses.modulo_fornecedores.Fornecedor;
import smart_houses.smart_devices.SmartBulb;
import smart_houses.smart_devices.SmartCamera;
import smart_houses.smart_devices.SmartDevice;
import smart_houses.smart_devices.SmartSpeaker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;
import java.util.function.Consumer;
import java.util.stream.Collectors;

public class EstadoPrograma implements Serializable {
    private final Map<String, Casa> casas;
    private final Map<String, Fornecedor> fornecedores;

    private Queue<SerializableConsumer> pedidos;

    private LocalDate data_atual;

    public static final double custoEnergia = 0.15;
    public static final double imposto = 0.06;


    /**
     *
     */
    public EstadoPrograma() {
        this.casas = new TreeMap<>();
        this.fornecedores = new HashMap<>();
        this.data_atual = LocalDate.now();
        this.pedidos = new LinkedList<>();
    }

    /**
     * @param c
     */
    public EstadoPrograma(EstadoPrograma c) {
        this.casas = c.getCasas();
        this.fornecedores = c.getFornecedores();
        this.data_atual = c.getDataAtual();
        this.pedidos = c.getPedidos();
    }

    /**
     * @return
     */
    public Queue<SerializableConsumer> getPedidos() {
        return new LinkedList<>(this.pedidos);
    }

    /**
     * @param pedidos
     */
    public void setPedidos(Queue<SerializableConsumer> pedidos) {
        this.pedidos = new LinkedList<>(pedidos);
    }

    /**
     * @param pedido
     */
    public void addPedido(SerializableConsumer pedido){
        this.pedidos.add(pedido);
    }

    /**
     *
     */
    private void runAllRequests(){
        while(!this.pedidos.isEmpty()){
            this.pedidos.poll().accept(this);
        }
    }

    /**
     * @return
     */
    public LocalDate getDataAtual() {
        return data_atual;
    }

    /**
     * @param data_atual
     */
    public void setDataAtual(LocalDate data_atual) {
        this.data_atual = data_atual;
    }

    /**
     * @param fim
     * @throws FornecedorErradoException
     */
    private void geraFaturas(LocalDate fim) throws FornecedorErradoException{
        for (Casa casa : this.casas.values()) {
            Fatura f = this.fornecedores.get(casa.getFornecedor()).criaFatura(casa.clone(), this.data_atual, fim);
            casa.adicionaFatura(f);
            this.fornecedores.get(casa.getFornecedor()).adicionaFatura(f);
        }
    }

    /**
     * @param nome
     * @return
     * @throws FornecedorInexistenteException
     */
    public List<Fatura> getFaturasFornecedor(String nome) throws FornecedorInexistenteException {
        if(!this.fornecedores.containsKey(nome)) throw new FornecedorInexistenteException("Nao existe fornecedor: " + nome);
        return this.fornecedores.get(nome).getFaturas().stream().map(Fatura::clone).collect(Collectors.toList());
    }

    /**
     * @return
     */
    public Optional<Casa> getCasaMaisGastadora() {

        return this.casas.values().stream().max(Comparator.comparingDouble(c -> {
            List<Fatura> faturas = c.getFaturas();
            return faturas.stream().mapToDouble(Fatura::getConsumo).sum();
        })).map(Casa::clone);

    }

    /**
     * @param N
     * @return
     */
    public List<String> maiorConsumidorPeriodo(int N) {
        Comparator<Casa> comp = Comparator.comparingDouble(Casa::consumoPeriodo);
        return this.casas.values()
                .stream()
                .sorted(comp.reversed())
                .limit(N)
                .map(Casa::getNif)
                .collect(Collectors.toList());
    }

    /**
     * @param inicio
     * @param fim
     * @param N
     * @return
     */
    public List<String> maiorConsumidorPeriodo(LocalDate inicio, LocalDate fim, int N) {
        Comparator<Casa> comp = Comparator.comparingDouble(c -> c.consumoPeriodo(inicio, fim));
        return this.casas.values()
                .stream()
                .sorted(comp.reversed())
                .limit(N)
                .map(Casa::getNif)
                .collect(Collectors.toList());
    }

    /**
     * @param o
     * @return
     */
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EstadoPrograma that = (EstadoPrograma) o;

        if (!getCasas().equals(that.getCasas())) return false;
        if (!getFornecedores().equals(that.getFornecedores())) return false;
        if (!getPedidos().equals(that.getPedidos())) return false;
        return data_atual.equals(that.data_atual);
    }

    /**
     * @return
     */
    public int hashCode() {
        int result = getCasas().hashCode();
        result = 31 * result + getFornecedores().hashCode();
        result = 31 * result + getPedidos().hashCode();
        result = 31 * result + data_atual.hashCode();
        return result;
    }

    /**
     * @return
     */
    public String toString() {
        return "EstadoPrograma{" +
                "casas=" + casas +
                ", fornecedores=" + fornecedores +
                ", pedidos=" + pedidos +
                ", data_atual=" + data_atual +
                '}';
    }

    /**
     * @return
     */
    public String getFornecedorMaiorFaturacao(){
        Comparator<Map.Entry<String, Fornecedor>> comp = (f1, f2) -> {
            double faturacao1 = f1.getValue().faturacao();
            double faturacao2 = f2.getValue().faturacao();
            return Double.compare(faturacao1, faturacao2);
        };
        return this.fornecedores.entrySet().stream().max(comp).map(Map.Entry::getKey).orElse("Nao existe nenhum fornecedor");
    }

    /**
     * @param date
     * @throws DataInvalidaException
     * @throws FornecedorErradoException
     */
    public void avancaData(LocalDate date) throws DataInvalidaException, FornecedorErradoException {
        if(date.isBefore(this.data_atual)) throw new DataInvalidaException("Esta data é anterior à atual");
        this.geraFaturas(date);
        this.data_atual = date;
        this.runAllRequests();
    }

    /**
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public static EstadoPrograma carregaDados() throws IOException, ClassNotFoundException {
        ObjectInputStream ois = new ObjectInputStream(new FileInputStream("./src/main/resources/data.obj"));
        int next_idDevice = ois.readInt();
        int next_idFatura = ois.readInt();
        EstadoPrograma programa = (EstadoPrograma) ois.readObject();
        Fatura.next_codigoFatura = next_idFatura;
        SmartDevice.next_id = next_idDevice;
        ois.close();
        return programa;
    }

    /**
     * @return
     */
    public Map<String, Casa> getCasas() {
        return this.casas.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    /**
     * @return
     */
    public Map<String, Fornecedor> getFornecedores() {
        return this.fornecedores.entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, e -> e.getValue().clone()));
    }

    /**
     * @param c
     * @throws ExisteCasaException
     * @throws FornecedorInexistenteException
     */
    public void adicionaCasa(Casa c) throws ExisteCasaException, FornecedorInexistenteException{
        if (this.casas.containsKey(c.getNif())) throw new ExisteCasaException("Esta casa tem um nif que ja existe");
        if (!this.fornecedores.containsKey(c.getFornecedor())) throw new FornecedorInexistenteException("Nao existe este fornecedores");
        this.casas.put(c.getNif(), c.clone());
    }

    /**
     * @param nome
     * @return
     */
    public boolean existeFornecedor(String nome) {
        return this.fornecedores.containsKey(nome);
    }

    /**
     * @param code
     * @return
     */
    public boolean existeCasa(String code) {
        return this.casas.containsKey(code);
    }

    /**
     * @return
     */
    public EstadoPrograma clone() {
        return new EstadoPrograma(this);
    }

    /**
     *
     */
    public void guardaDados() throws IOException {
            FileOutputStream file = new FileOutputStream("./src/main/resources/data.obj");
            ObjectOutputStream oos = new ObjectOutputStream(file);
            oos.writeInt(SmartDevice.next_id);
            oos.writeInt(Fatura.next_codigoFatura);
            oos.writeObject(this);
            oos.close();
    }

    /**
     * @param nif
     * @return
     * @throws CasaInexistenteException
     */
    public List<String> getRoomsHouse(String nif) throws CasaInexistenteException {
      Casa casa = this.casas.get(nif);
      if(casa == null) throw new CasaInexistenteException("Esta casa com nif : " + nif);
        return casa.getListRooms();
    }

    /**
     * @param f
     * @throws ExisteFornecedorException
     */
    public void addFornecedor(Fornecedor f) throws ExisteFornecedorException {
        if (this.fornecedores.containsKey(f.getName()))
            throw new ExisteFornecedorException("Este fornecedor já existe");
        this.fornecedores.put(f.getName(), f.clone());
    }

    /**
     * @param casa
     * @param fornecedor
     * @throws CasaInexistenteException
     * @throws FornecedorInexistenteException
     */
    public void mudaFornecedorCasa(String casa, String fornecedor) throws CasaInexistenteException, FornecedorInexistenteException{
        Casa c = this.casas.get(casa);
        if(c == null) throw new CasaInexistenteException("Nao existe esta casa");
        if(!this.fornecedores.containsKey(fornecedor)) throw new FornecedorInexistenteException("Nao existe este Fornecedor");
        c.setFornecedor(fornecedor);
    }

    /**
     * @param nif
     * @throws CasaInexistenteException
     */
    public void removeCasa(String nif) throws CasaInexistenteException {
        if(this.casas.remove(nif) == null) throw new CasaInexistenteException("Nao existe casa com o nif de " + nif);
    }

    /**
     * @return
     */
    public List<String> getListNIFs(){
        return new ArrayList<>(this.casas.keySet());
    }

    /**
     * @return
     */
    public List<Casa> listaCasas(){
        return this.casas.values().stream().map(Casa::clone).collect(Collectors.toList());
    }

    /**
     * @param nif
     * @return
     * @throws CasaInexistenteException
     */
    public Casa getCasa(String nif) throws CasaInexistenteException{
        Casa c = this.casas.get(nif);
        if(c == null) throw new CasaInexistenteException("Nao existe casa com NIF " + nif);
        return c.clone();
    }

    /**
     * @param nif
     * @return
     * @throws CasaInexistenteException
     */
    public List<Fatura> faturasCasa(String nif) throws CasaInexistenteException{
        if(!this.casas.containsKey(nif)) throw new CasaInexistenteException("Nao existe casa com o nif de " + nif);
        return this.casas.get(nif).getFaturas();
    }

    /**
     * @return
     */
    public List<Fornecedor> getListFornecedores(){
        return this.fornecedores.values().stream().map(Fornecedor::clone).collect(Collectors.toList());
    }

    /**
     * @param nome
     * @return
     * @throws FornecedorInexistenteException
     */
    public Fornecedor getFornecedor(String nome) throws FornecedorInexistenteException{
        Fornecedor f = this.fornecedores.get(nome);
        if(f == null) throw new FornecedorInexistenteException("Não existe nenhum fornecedor com o nome: " + nome);
        return f;
    }

    /**
     * @param nome
     * @param desconto
     * @throws FornecedorInexistenteException
     */
    public void mudaDescontoFornecedor(String nome, double desconto) throws FornecedorInexistenteException{
        this.fornecedores.get(nome).setDesconto(desconto);
    }

    /**
     * @return
     */
    public List<String> podiumDeviceMaisUsado(){
        return this.casas.values()
                .stream()
                .flatMap(c -> c.getListDevices()
                        .stream()
                        .map(d -> d.getClass().getSimpleName()))
                .collect(Collectors.groupingBy(d  -> d, Collectors.counting()))
                .entrySet().stream()
                .sorted((e1, e2) -> (int) (e2.getValue() - e1.getValue())).map(Map.Entry::getKey).limit(3).collect(Collectors.toList());
    }

    /**
     * @param nif
     * @param id
     * @param mapperBulb
     * @throws CasaInexistenteException
     * @throws DeviceInexistenteException
     * @throws TipoDeviceErradoException
     */
    public void alteraInfoBulbCasa(String nif, int id, Consumer<SmartBulb> mapperBulb) throws CasaInexistenteException, DeviceInexistenteException, TipoDeviceErradoException{
      if(!this.casas.containsKey(nif)) throw new CasaInexistenteException("Não existe casa com o nif de " + nif);

      this.casas.get(nif).alteraInfoBulb(id, mapperBulb);

    }

    /**
     * @param nif
     * @param id
     * @param mapperSpeaker
     * @throws DeviceInexistenteException
     * @throws TipoDeviceErradoException
     * @throws CasaInexistenteException
     */
    public void alteraInfoSpeakerCasa(String nif, int id, Consumer<SmartSpeaker> mapperSpeaker) throws DeviceInexistenteException, TipoDeviceErradoException, CasaInexistenteException{

      if(!this.casas.containsKey(nif)) throw new CasaInexistenteException("Não existe casa com o nif de " + nif);

      this.casas.get(nif).alteraInfoSpeaker(id, mapperSpeaker);
    }

    /**
     * @param nif
     * @param id
     * @param mapperCamera
     * @throws CasaInexistenteException
     * @throws DeviceInexistenteException
     * @throws TipoDeviceErradoException
     */
    public void alteraInfoCameraCasa(String nif, int id, Consumer<SmartCamera> mapperCamera) throws CasaInexistenteException, DeviceInexistenteException, TipoDeviceErradoException{
      if(!this.casas.containsKey(nif)) throw new CasaInexistenteException("Não existe casa com o nif de " + nif);

      this.casas.get(nif).alteraInfoCamera(id, mapperCamera);
    }

    /**
     * @param nif
     * @param mapperCasa
     * @throws CasaInexistenteException
     */
    public void alteraInfoCasa(String nif, Consumer<Casa> mapperCasa) throws CasaInexistenteException{
        if(!this.casas.containsKey(nif)) throw new CasaInexistenteException("Não existe nenhuma casa com este nif: " + nif);

        mapperCasa.accept(this.casas.get(nif));
    }
}
