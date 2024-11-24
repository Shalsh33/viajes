package com.shalsh.viajes.dto;

import java.util.Date;
import java.util.List;

public class ViajeDTO {

	int id;
	int cliente;
	int monopatin;
	Date inicio;
	Date fin;
	double distancia;
	double costo;
	boolean finalizado;
	List<PausaDTO> pausas;
	
	public ViajeDTO() {
		
	}
	
	public ViajeDTO(int cliente, int monopatin) {
		super();
		this.cliente = cliente;
		this.monopatin = monopatin;
	}

	public ViajeDTO(int id, int cliente, int monopatin, Date inicio, Date fin, double distancia, double costo) {
		super();
		this.id = id;
		this.cliente = cliente;
		this.monopatin = monopatin;
		this.inicio = inicio;
		this.fin = fin;
		this.distancia = distancia;
		this.costo = costo;
	}

	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getCliente() {
		return cliente;
	}
	public void setCliente(int cliente) {
		this.cliente = cliente;
	}
	public int getMonopatin() {
		return monopatin;
	}
	public void setMonopatin(int monopatin) {
		this.monopatin = monopatin;
	}
	public Date getInicio() {
		return inicio;
	}
	public void setInicio(Date date) {
		this.inicio = date;
	}
	public Date getFin() {
		return fin;
	}
	public void setFin(Date fin) {
		this.fin = fin;
	}
	public double getDistancia() {
		return distancia;
	}
	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	public double getCosto() {
		return costo;
	}
	public void setCosto(double costo) {
		this.costo = costo;
	}
	public boolean isFinalizado() {
		return finalizado;
	}
	public void setFinalizado(boolean finalizado) {
		this.finalizado = finalizado;
	}
	public List<PausaDTO> getPausas() {
		return pausas;
	}
	public void setPausas(List<PausaDTO> pausas) {
		this.pausas = pausas;
	}
	
	
}
