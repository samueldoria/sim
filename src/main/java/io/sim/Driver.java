package io.sim;

import java.time.Instant;
import java.util.ArrayList;

import org.json.JSONObject;

public class Driver implements Runnable {
    
    private boolean isAlive = false;

    private ArrayList <Route> rotasAseremExecutadas;
    private ArrayList <Route> rotasEmExecucao;
    private ArrayList <Route> rotasExecutadas;

    private JsonManager jsonMaker = new JsonManager();
    private Cryptographer encriptador = new Cryptographer();
    private SharedMemory memoriaCompartilhada = new SharedMemory();
    private JSONObject json = new JSONObject();
    private Instant timestamp;
    private Long timestampDriver;
    private AlphaBank banco = new AlphaBank();

    private BotPayment bot;

    private Car carro;
    private final int cadastroDriver; 
    private float kmRodado;
    private FuelSatation posto;

    //Dados da conta alphabank
    private String idConta;
    private double valorInicialDaConta;
    private double saldo; 

    public Driver(int cadastroDriver) {
        this.isAlive = true;
        this.idConta = ("Driver_" + cadastroDriver);
        this.valorInicialDaConta = 0;
        this.saldo = 0;
        this.kmRodado = 0;
        this.cadastroDriver = cadastroDriver;

        bot = new BotPayment(idConta);
        posto = new FuelSatation();
        carro = new Car();
        criarConta();

        run();
    }

    public void run() {
        rotasAseremExecutadas = getRoutes();
        while (rotasAseremExecutadas.size() == 0) {}
        while (isAlive){
            try {
                System.out.println("Driver");
                // funções a serem processadas
                if (carro.getAbastecer()){
                    abastecer();
                }
                verificaCorrida();
                Thread.sleep(500);
            } catch (Exception e) {
                System.out.println(e);
            }
            // throw new UnsupportedOperationException("Unimplemented method 'run'"); -> diz q o run n foi implementado
    }}

    private void verificaCorrida() {

    }

    private void abastecer() {
        // Parar o carro
        double preco = 0;
        this.saldo = getSaldo();
        if (saldo > 5.87){
            preco = posto.abastecer(saldo, carro.getFuelTank(), carro);
            if (preco > 0) { // Abasteceu e temos valor
                bot.setPagarPosto(true, preco);
                preco = 0;
            }
        }else{
            System.out.println("Carro_" + cadastroDriver + ": Sem saldo suficiente, ainda e necessario abastecer");
        }

        // Liberar o carro
    }

    private ArrayList<Route> getRoutes() {
        // Faz um json solicitando as rotas, espera elas chegarem e dps retorna
        return null;
    }

    private void criarConta() {
        timestamp = Instant.now();
        long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L;
        json = jsonMaker.JsonCriarConta(encriptador.criptografarString(idConta), encriptador.criptografarDouble(valorInicialDaConta), encriptador.criptografarTimestamp(timestampNanos));
        memoriaCompartilhada.write(json, "CriarConta");
    }

    private double getSaldo(){
        double saldoNovo = banco.getSaldo(idConta);
        return saldoNovo;
    }

    class BotPayment {

        private boolean isAlive = false;
        private Instant timestamp;
        private JsonManager jsonMaker = new JsonManager();
        private Cryptographer encriptador = new Cryptographer();
        private JSONObject json = new JSONObject();
        private SharedMemory memoriaCompartilhada = new SharedMemory();
        private float kmPago;
        private float kmRodado;
        private boolean pagarPosto;
        //public static float kmAtual = 0;;

        private String idConta;
        private double valorAPagar;

        public BotPayment(String idConta) {
            this.isAlive = true;
            this.idConta = idConta;
            this.pagarPosto = false;
            run();
        }
        
        public void run() {
            // Processos iniciais...
            while (isAlive){
                try {
                    //System.out.println("Thread botpayment");
                    // adicionar verificação de km percorrido 
                    if (pagarPosto){
                        pay();
                        kmPago = kmRodado;
                    }
                    
                    Thread.sleep(1000);
                } catch (Exception e) {
                    // TODO: handle exception
                }
        }}

        public void setPagarPosto(boolean pagarPosto, double quantia) {
            this.pagarPosto = pagarPosto;
            this.valorAPagar = quantia;
        }

        public void pay() {
            timestamp = Instant.now();
            long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L;
            json = jsonMaker.JsonTransferencia(encriptador.criptografarString(idConta), encriptador.criptografarString("Fuel_Station"), encriptador.criptografarDouble(valorAPagar), encriptador.criptografarTimestamp(timestampNanos));
            memoriaCompartilhada.write(json, "6");
            pagarPosto = false;
            valorAPagar = 0;
        }
        }
}