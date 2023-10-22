package io.sim;

import org.json.JSONObject;

import com.google.gson.Gson;

class Transferencias { // usado para pagar os ubers e pagar a gasolina
        private String contaRemetente;
        private String contaDestinatario;
        private double valor;

        public void setcontaRemetente (String contaRemetente){
            this.contaRemetente = contaRemetente;
        }
        
        public void setcontaDestinatario (String contaDestinatario){
            this.contaDestinatario = contaDestinatario;
        }

        public void setvalor (double valor){
            this.valor = valor;
        }

    }

class Conta { // Usado para criar as contas
    private String idConta;
    private double valorInicial;

    public void setidConta (String conta){
            this.idConta = conta;
    }
        
    public void setvalorInicial (double valor){
        this.valorInicial = valor;
    }
}

public class JsonManager {

    private Gson gson = new Gson();
    private JSONObject obj = new JSONObject();

    public JsonManager (){} // Construtor padrão

    public String JsonTransferencia (String idPagador, String idRecebedor, double quantia) {
        Transferencias pix = new Transferencias();
        pix.setcontaRemetente(idPagador);
        pix.setcontaDestinatario(idRecebedor);
        pix.setvalor(quantia);
        return gson.toJson(pix);
    }

    public String JsonCriarConta (String idConta, double quantia) {
        Conta conta = new Conta();
        conta.setidConta(idConta);
        conta.setvalorInicial(quantia);
        return gson.toJson(conta);
    }

}