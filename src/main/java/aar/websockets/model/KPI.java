package aar.websockets.model;

public class KPI {

    private int id;
	private int valor;
    private String name;
    private String suscrito;
    private String type;
    private String description;
    
    public KPI() {
    	
    }
        
    public KPI(int id, int valor, String name, String suscrito, String type, String description) {
		super();
		this.id = id;
		this.valor = valor;
		this.name = name;
		this.suscrito = suscrito;
		this.type = type;
		this.description = description;
	}
    
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((suscrito == null) ? 0 : suscrito.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		result = prime * result + valor;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		KPI other = (KPI) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (suscrito == null) {
			if (other.suscrito != null)
				return false;
		} else if (!suscrito.equals(other.suscrito))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		if (valor != other.valor)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "KPI [id=" + id + ", valor=" + valor + ", name=" + name + ", suscrito=" + suscrito + ", type=" + type
				+ ", description=" + description + "]";
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getValor() {
		return valor;
	}
	public void setValor(int valor) {
		this.valor = valor;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSuscrito() {
		return suscrito;
	}
	public void setSuscrito(String suscrito) {
		this.suscrito = suscrito;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

    
}
