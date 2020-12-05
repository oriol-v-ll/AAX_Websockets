package aar.websockets.websocket;


import java.util.HashSet;
import java.util.Set;


import javax.json.JsonObject;
import javax.json.spi.JsonProvider;
import javax.websocket.Session;



import aar.websockets.model.KPI;

public class KPIThreadManager extends Thread{
	
	private  Set<KPI> Kpis = new HashSet<>();
	private int TIEMPO = 1000; //en milisegundos
	
	public KPIThreadManager(Set<KPI> Kpis, Set<Session> sessions) {
		this.Kpis = Kpis;
		
	}
	
	@Override
	public void run() {
		int numero = calcularValor();
    	JsonProvider provider = JsonProvider.provider();
        for (KPI kpi : Kpis) {
        	//Borrar de todas las sesiones
        	KPISessionHandler.removeKPI(kpi.getId());
            JsonObject addMessage = provider.createObjectBuilder()
	                .add("action", "add")
	                .add("id", kpi.getId())
	                .add("name", kpi.getName())
	                .add("type", kpi.getType())
	                .add("name2", kpi.getName2())
	                .add("type2", kpi.getType2())
	                .add("comparacion", numero)
	                .build();
            //Añadir en todas las sesiones
            KPISessionHandler.sendToAllConnectedSessions(addMessage);
        }
        try {
			Thread.sleep(TIEMPO);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

    public int calcularValor() {
    	int numero = (int)(Math.random()*100+1);
    	return numero;
    }
    
    

}
