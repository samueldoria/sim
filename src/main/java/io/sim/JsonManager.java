package io.sim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.json.JSONObject;

public class JsonManager {

    private JSONObject obj = new JSONObject();

    public JsonManager (){} // Construtor padrão

    public JSONObject JsonTransferencia (String idPagador, String idRecebedor, String quantia, String timestamp) {
        obj.put("idPagador", idPagador);
        obj.put("idRecebedor", idRecebedor);
        obj.put("quantia", quantia);
        obj.put("timestamp", timestamp);
        // adcionar timestamp
        return obj;
    }

    public JSONObject JsonCriarConta (String idConta, String quantia, String timestamp) {
        obj.put("tipo_de_requisicao", "CriarConta");
        obj.put("idConta", idConta);
        obj.put("quantia", quantia);
        obj.put("timestamp", timestamp);
        return obj;
    }

    public JSONObject JsonEnvioRota (ArrayList<String> idrotas, ArrayList<String> rotas, String timestamp) {
        obj.put("timestamp", timestamp);
        for (int i=0; i<rotas.size(); i++) {
            obj.put("id_"+i, idrotas.get(i));
            obj.put("rota_"+i, rotas.get(i));
        }
        return obj;
    }

    public JSONObject JsonSolicitaRota (String idDriver, String timestamp) {
        obj.put("idDriverSolicitante", idDriver);
        obj.put("timestamp", timestamp);
        return obj;
    }
}