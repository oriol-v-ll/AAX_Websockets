package aar.websockets.websocket;

import java.io.IOException;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Locale;
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
	    private final static Set<Session> sessions = new HashSet<>();
	    private final static Set<KPI> Kpis = new HashSet<>();
	    private int TIEMPO = 1000; //en milisegundos
	    
	    public  KPISessionHandler() {
	    	 System.out.println("hello world!");
	    	 //Thread
	    	 main();
	    }
	    public void main() {
	    	 Runnable runnable = new Runnable() {
	    		 @Override
	    		 public void run() {
	    			 while (true) {
	    				 try {
	    					 Thread.sleep(TIEMPO);
	    					 KPISessionHandler.actualizarValorAleatorio();
	    				 }catch (InterruptedException e) {
	    					 e.printStackTrace();
	    				 }
	    			 }
	    		 }
	    	 };
	    	 
	    	 new Thread(runnable).start();
	    }
	
	    public String  fechaSistema() {
	        Calendar cal=Calendar.getInstance();
	        java.util.Date date=cal.getTime();
	        DateFormat dateFormatter=DateFormat.getDateInstance(DateFormat. FULL, Locale.getDefault());
	        String fecha=dateFormatter.format(date); 
	        return fecha;
	    }
	    
	    public static void actualizarValorAleatorio() {
			System.out.println("Actualización de comparacion");
			int numero = calcularValor();
			
	    	JsonProvider provider = JsonProvider.provider();
	        for (KPI kpi : Kpis) {

	            JsonObject addMessage = provider.createObjectBuilder()
		                .add("action", "actualizar")
		                .add("id", kpi.getId())
		                .add("comparacion", numero)
		                .build();
	            sendToAllConnectedSessions(addMessage);
	        }
	 
	    }
	    	    
	    public static int calcularValor() {
	    	int numero = (int)(Math.random()*100+1);
	    	return numero;
	    }
	    
	    public void addSession(Session session) {
	        sessions.add(session);
	        int valor = calcularValor();
	        JsonProvider provider = JsonProvider.provider();
	        for (KPI kpi : Kpis) {
	            JsonObject addMessage = provider.createObjectBuilder()
		                .add("action", "add")
		                .add("id", kpi.getId())
		                .add("name", kpi.getName())
		                .add("type", kpi.getType())
		                .add("name2", kpi.getName2())
		                .add("type2", kpi.getType2())
		                .add("comparacion", valor)
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
	        int valor = calcularValor();
	        JsonProvider provider = JsonProvider.provider();
	        JsonObject addMessage = provider.createObjectBuilder()
	                .add("action", "add")
	                .add("id", kpi.getId())
	                .add("name", kpi.getName())
	                .add("type", kpi.getType())
	                .add("name2", kpi.getName2())
	                .add("type2", kpi.getType2())
	                .add("comparacion", valor)
	                .build();
	        sendToAllConnectedSessions(addMessage); 
	        
	    }
	    
	    public  void removeKPI(int id) {
	    	System.out.println("KPI Eliminada");
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


	    private  KPI getKPIById(int id) {
	        for (KPI kpi : Kpis) {
	            if (kpi.getId() == id) {
	                return kpi;
	            }
	        }
	        return null;
	    }
	    
	    public static void sendToAllConnectedSessions(JsonObject message) {  
	        for (Session session : sessions) {
	            sendToSession(session, message);
	        }
	    }

	    public static void sendToSession(Session session, JsonObject message) {
	        try {
	            session.getBasicRemote().sendText(message.toString());
	        } catch (IOException ex) {
	            sessions.remove(session);
	            Logger.getLogger(KPISessionHandler.class.getName()).
	                    log(Level.SEVERE, null, ex);
	        }
	    }
}
