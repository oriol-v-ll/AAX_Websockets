package aar.websockets.websocket;

import java.io.StringReader;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


import aar.websockets.model.KPI;

@ApplicationScoped
@ServerEndpoint("/actions")
public class KPIWebSocketServer {
    
    private static KPISessionHandler sessionHandler = new KPISessionHandler();
    
    public KPIWebSocketServer() {
        System.out.println("class loaded " + this.getClass());
    }
    
  
    
    @OnOpen
    public void onOpen(Session session) {
        sessionHandler.addSession(session);
        System.out.println("cliente suscrito, sesion activa");
        
    }

    @OnClose
    public void onClose(Session session) {   
        sessionHandler.removeSession(session);
        System.out.println("cliente cierra conexión, sesion eliminada");
    }

    @OnError
    public void onError(Throwable error) {
        Logger.getLogger(KPIWebSocketServer.class.getName()).
                log(Level.SEVERE, null, error);
    }

    @OnMessage
    public void onMessage(String message, Session session) {
    	System.out.println("llega");
        try (JsonReader reader = Json.createReader(new StringReader(message))) {
            JsonObject jsonMessage = reader.readObject();
            System.out.println("llega2");
            if ("compare".equals(jsonMessage.getString("action"))) {
            	KPI kpi = new KPI();
            	kpi.setName(jsonMessage.getString("name1"));
            	kpi.setType(jsonMessage.getString("type1"));
            	kpi.setName2(jsonMessage.getString("name2"));
            	kpi.setType2(jsonMessage.getString("type2"));
            	sessionHandler.addComparation(kpi);
            }
            if ("remove".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeKPI(id);
                System.out.println(id);
            }
            if ("toggle".equals(jsonMessage.getString("action"))) {
                int id = (int) jsonMessage.getInt("id");
                sessionHandler.removeKPI(id);
            }
        } 
    }
    
    
}
