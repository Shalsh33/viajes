package com.shalsh.viajes.dto;

import java.util.Date;

public class PausaDTO {

	int id;
	Date inicio;
	Date fin;
	int viaje;
	boolean finalizada;
	
	public PausaDTO() {
		
	}
	
	public PausaDTO(int id, Date inicio, Date fin, int viaje) {
		super();
		this.id = id;
		this.inicio = inicio;
		this.fin = fin;
		this.viaje = viaje;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date inicio) {
		this.inicio = inicio;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getViaje() {
		return viaje;
	}
	public void setViaje(int viaje) {
		this.viaje = viaje;
	}
	public boolean isFinalizada() {
		return finalizada;
	}
	public void setFinalizada(boolean finalizada) {
		this.finalizada = finalizada;
	}
	
	
}
