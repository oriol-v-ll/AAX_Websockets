package aar.websockets.websocket;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;


import aar.websockets.model.KPI;

@ApplicationScoped
public class KPISessionHandler {
	 private int deviceId = 0;
	    private final Set<Session> sessions = new HashSet<>();
	    private final Set<KPI> Kpis = new HashSet<>();
	    
	    public void addSession(Session session) {
	        sessions.add(session);
	        JsonProvider provider = JsonProvider.provider();
	        for (KPI kpi : Kpis) {
	            JsonObject addMessage = provider.createObjectBuilder()
		                .add("action", "add")
		                .add("id", kpi.getId())
		                .add("name", kpi.getName())
		                .add("type", kpi.getType())
		                .add("name2", kpi.getName2())
		                .add("type2", kpi.getType2())
		                .build();
	            sendToSession(session, addMessage);
	        }
	    }

	    public void removeSession(Session session) {
	        sessions.remove(session);
	    }
	    
	    public void addComparation(KPI kpi) {
	    	kpi.setId(deviceId);
	        Kpis.add(kpi);
	        deviceId++;
	        JsonProvider provider = JsonProvider.provider();
	        JsonObject addMessage = provider.createObjectBuilder()
	                .add("action", "add")
	                .add("id", kpi.getId())
	                .add("name", kpi.getName())
	                .add("type", kpi.getType())
	                .add("name2", kpi.getName2())
	                .add("type2", kpi.getType2())
	                .build();
	        sendToAllConnectedSessions(addMessage); 
	        
	    }
	    public void removeKPI(int id) {
	    	KPI kpi = getKPIById(id);
	        if (kpi != null) {
	        	Kpis.remove(kpi);
	            JsonProvider provider = JsonProvider.provider();
	            JsonObject removeMessage = provider.createObjectBuilder()
	                    .add("action", "remove")
	                    .add("id", id)
	                    .build();
	            sendToAllConnectedSessions(removeMessage);
	        } 
	    }
	    

	    private KPI getKPIById(int id) {
	        for (KPI kpi : Kpis) {
	            if (kpi.getId() == id) {
	                return kpi;
	            }
	        }
	        return null;
	    }
	    
	    private void sendToAllConnectedSessions(JsonObject message) {  
	        for (Session session : sessions) {
	            sendToSession(session, message);
	        }
	    }

	    private void sendToSession(Session session, JsonObject message) {
	        try {
	            session.getBasicRemote().sendText(message.toString());
	        } catch (IOException ex) {
	            sessions.remove(session);
	            Logger.getLogger(KPISessionHandler.class.getName()).
	                    log(Level.SEVERE, null, ex);
	        }
	    }
}