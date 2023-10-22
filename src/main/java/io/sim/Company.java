package io.sim;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.json.JSONObject;
import org.python.antlr.ast.Str;
import org.w3c.dom.*;
import org.xml.sax.SAXException;

public class Company implements Runnable {
    // Deve ser uma thread
    // deve conter um conjunto de rotas -> FEITO
    // Deve ser um servidor para carros
    // Deve ser um cliente de alphaBank -> deve ter uma conta no alphaBank
    // Criar uma Classe BotPayment (Thread)
    // gerar xlsl de relatório (xlsl é sugestão minha)

    private boolean isAlive = false;

    private double precoPkm;
    private ArrayList <Route> rotasAseremExecutadas;
    private ArrayList <Route> rotasEmExecucao;
    private ArrayList <Route> rotasExecutadas;
    
    private Instant timestamp;
    private JsonManager jsonMaker = new JsonManager();
    private Cryptographer encriptador = new Cryptographer();
    private JSONObject json = new JSONObject();
    private SharedMemory memoriaCompartilhada = new SharedMemory();
    private BotPayment botDePagamentos;

    private final String caminhoPastaRotas = "map/map.rou.xml";

    //Dados da conta alphabank
    private String idConta;
    private double valorInicialDaConta;

    private ArrayList<Driver> motoristas = new ArrayList<Driver>();
    private int nRotasPorPiloto = 0;

    // private String idItinerario; 

    public Company() {
        this.isAlive = true;
        this.precoPkm = 3.25;
        this.idConta = "Company";
        this.valorInicialDaConta = 100.0;
        
        getRoutes(); // Pega as rotas
        limparArquivoDeRotas();
        nRotasPorPiloto = createDriver();
        distribuirRotas();
        // criar conta no banco
        criarConta();

        botDePagamentos = new BotPayment();
        run();
    }

    private void distribuirRotas() {
        int acumulador = 0, indexDriver =0;
         ArrayList<Route> edges = new ArrayList<Route>();
        for (int i=0; i < rotasAseremExecutadas.size(); i++) {
            if (acumulador < nRotasPorPiloto) {
               edges.add(rotasAseremExecutadas.get(i));
            } else if (acumulador == (nRotasPorPiloto-1)) {
                // Atribui
                Driver motoristaAtual = motoristas.get(indexDriver);
                motoristaAtual.setRoute(edges);
            } else {
                edges.clear();
                acumulador = 0;
            }
            
        }
    }

    private void limparArquivoDeRotas() {
    }

    private int createDriver() {
        // Verifica o número de rotas
        for (int i=0; i<100; i++) {
            motoristas.add(i, new Driver("Driver_" + i));
        }
        int numeroDeRotas = rotasAseremExecutadas.size();
        int novoNumeroDeRotas = numeroDeRotas - numeroDeRotas%100;
        List<Route> subLista = rotasAseremExecutadas.subList(0, novoNumeroDeRotas);
            this.rotasAseremExecutadas.clear(); // Limpa a lista original
            this.rotasAseremExecutadas.addAll(subLista); // Copia a sublista para a lista original
        return rotasAseremExecutadas.size()/100;
    }

    private void getRoutes() {
            try{
                DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder construtor = builderFactory.newDocumentBuilder();
                Document doc = construtor.parse(caminhoPastaRotas);
                NodeList lista = doc.getElementsByTagName("vehicle");
                for (int i=0; i<lista.getLength(); i++){
                    Node no = lista.item(i);
                    if (no.getNodeType() == Node.ELEMENT_NODE) {
                        Element elemento = (Element) no;
                        //String idRota = this.idItinerario;
                        Node node = elemento.getElementsByTagName("route").item(0);
                        Element edges = (Element) node;
                        //this.itinerario = new String[] {idRota, edges.getAttribute("edges")};
                        this.rotasAseremExecutadas.add(new Route("rota" + i, edges.getAttribute("edges")));
                    }
                }
            }catch (ParserConfigurationException | SAXException | IOException e) {
                e.printStackTrace(); // ou tratamento adequado
            }
        }

    public void run() {
        // Processos iniciais...
        while (isAlive){
            try {
                //System.out.println("Thread Company");
                Thread.sleep(1000);
            } catch (Exception e) {
                // TODO: handle exception
            }
    }}

    private void criarConta() {
        timestamp = Instant.now();
        long timestampNanos = timestamp.getNano() + timestamp.getEpochSecond() * 1_000_000_000L;
        json = jsonMaker.JsonCriarConta(encriptador.criptografarString(idConta), encriptador.criptografarDouble(valorInicialDaConta), encriptador.criptografarTimestamp(timestampNanos));
        memoriaCompartilhada.write(json, "CriarConta");
    }

    public void addRoute (Route route){
        this.rotasAseremExecutadas.add(route);
    }

    public ArrayList<Route> getCurrentRoute () {
        return rotasEmExecucao;
    }

    public ArrayList<Route> getExecutedRoutes () {
        return rotasExecutadas;
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
        private Map<String, Double> pagamentosPendentes = new HashMap<String, Double>();
        //public static float kmAtual = 0;;

        private String idConta;
        private double valorAPagar;

        public BotPayment() {
            this.isAlive = true;
            this.idConta = "Company";
            this.pagarPosto = false;
            getRoutes();
            run();
        }

        // Fazer fila de pagamentos para os drivers

        public void run() {
            // Processos iniciais...
            while (isAlive){
                try {
                    //System.out.println("Thread botpayment");
                    // adicionar verificação de km percorrido 
                    if (pagamentosPendentes.size() > 0){
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
            json = jsonMaker.JsonTransferencia(encriptador.criptografarString(idConta), encriptador.criptografarString("idCarro"), encriptador.criptografarDouble(valorAPagar), encriptador.criptografarTimestamp(timestampNanos));
            memoriaCompartilhada.write(json, "6");
            pagarPosto = false;
            valorAPagar = 0;
        }
        }

        
}