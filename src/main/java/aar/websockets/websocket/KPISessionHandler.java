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

import aar.websockets.model.Device;
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
	                .add("description", kpi.getDescription())
	                .build();
	            sendToSession(session, addMessage);
	        }
	    }

	    public void removeSession(Session session) {
	        sessions.remove(session);
	    }
	    
	    public void addDevice(KPI kpi) {
	    	kpi.setId(deviceId);
	        Kpis.add(kpi);
	        deviceId++;
	        JsonProvider provider = JsonProvider.provider();
	        
	        JsonObject addMessage = provider.createObjectBuilder()
	                .add("action", "add")
	                .add("id", kpi.getId())
	                .add("name", kpi.getName())
	                .add("type", kpi.getType())
	                .add("description", kpi.getDescription())
	                .build();
	        sendToAllConnectedSessions(addMessage);   
	    }

	    public void removeDevice(int id) {
	        KPI kpi = getDeviceById(id);
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

	    public void toggleDevice(int id) {
	        JsonProvider provider = JsonProvider.provider();
	        KPI kpi = getDeviceById(id);
	        if (kpi != null) {
	            if ("On".equals(""/*kpi.getStatus()*/)) {
	            	//kpi.setStatus("Off");
	            } else {
	            	//kpi.setStatus("On");
	            }
	            JsonObject updateDevMessage = provider.createObjectBuilder()
	                    .add("action", "toggle")
	                    .add("id", kpi.getId())
	                    .build();
	            sendToAllConnectedSessions(updateDevMessage);
	        }      
	    }

	    private KPI getDeviceById(int id) {
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
	            Logger.getLogger(DeviceSessionHandler.class.getName()).
	                    log(Level.SEVERE, null, ex);
	        }
	    }
}
