package com.shalsh.viajes.dto;

public class MonopatinDTO {
	
	private Integer id;

	private boolean isDisponible;

	private boolean isEncendido;

	private double longitud;
	
	private double latitud;

	private boolean mantenimiento;
	
	private Integer parada;
	
	private double distancia;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public boolean isDisponible() {
		return isDisponible;
	}

	public void setDisponible(boolean isDisponible) {
		this.isDisponible = isDisponible;
	}

	public boolean isEncendido() {
		return isEncendido;
	}

	public void setEncendido(boolean isEncendido) {
		this.isEncendido = isEncendido;
	}

	public double getLongitud() {
		return longitud;
	}

	public void setLongitud(double longitud) {
		this.longitud = longitud;
	}

	public double getLatitud() {
		return latitud;
	}

	public void setLatitud(double latitud) {
		this.latitud = latitud;
	}

	public boolean isMantenimiento() {
		return mantenimiento;
	}

	public void setMantenimiento(boolean mantenimiento) {
		this.mantenimiento = mantenimiento;
	}

	public Integer getParada() {
		return parada;
	}

	public void setParada(Integer parada) {
		this.parada = parada;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	
	

}
