package com.shalsh.viajes.dto;

import java.util.List;

public class ReporteDTO {

	MonopatinDTO monopatin;
	List<ViajeDTO> viajes;
	
	public MonopatinDTO getMonopatin() {
		return monopatin;
	}
	public void setMonopatin(MonopatinDTO monopatin) {
		this.monopatin = monopatin;
	}
	public List<ViajeDTO> getViajes() {
		return viajes;
	}
	public void setViajes(List<ViajeDTO> viajes) {
		this.viajes = viajes;
	}
	
	
}
