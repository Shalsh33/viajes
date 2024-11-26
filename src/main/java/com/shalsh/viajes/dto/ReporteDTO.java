package com.shalsh.viajes.dto;

import java.math.BigDecimal;
import java.util.List;

public class ReporteDTO {

	Integer monopatin;
	Long cantViajes;
	Double distanciaTotal;
	Long tiempoViaje;
	List<ViajeDTO> viajes;
	
	final static long TIMESTAMP_CONVERT = 1000000000;
	
	public ReporteDTO() {
	}
	public ReporteDTO(Long cantViajes, Double distanciaTotal, BigDecimal tiempoViajeTotal, Integer monopatin) {
		this.cantViajes = cantViajes;
		this.distanciaTotal = distanciaTotal;
		this.tiempoViaje = (tiempoViajeTotal != null) ? tiempoViajeTotal.longValue() / TIMESTAMP_CONVERT : 0;
		this.monopatin = monopatin;
	}
	public Integer getMonopatin() {
		return monopatin;
	}
	public void setMonopatin(Integer monopatin) {
		this.monopatin = monopatin;
	}
	public List<ViajeDTO> getViajes() {
		return viajes;
	}
	public void setViajes(List<ViajeDTO> viajes) {
		this.viajes = viajes;
	}
	public Double getDistanciaTotal() {
		return distanciaTotal;
	}
	public void setDistanciaTotal(Double distanciaTotal) {
		this.distanciaTotal = distanciaTotal;
	}
	public Long getTiempoViaje() {
		return tiempoViaje;
	}
	public void setTiempoViaje(Long tiempoViaje) {
		this.tiempoViaje = tiempoViaje;
	}
	public Long getCantViajes() {
		return cantViajes;
	}
	public void setCantViajes(Long cantViajes) {
		this.cantViajes = cantViajes;
	}

	
	
}
