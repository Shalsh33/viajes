package com.shalsh.viajes.dto;

public class TarifaDTO {


	private int id;
	private double tarifa;
	private double tarifa_pausa;

	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public double getTarifa_pausa() {
		return tarifa_pausa;
	}

	public void setTarifa_pausa(double tarifa_pausa) {
		this.tarifa_pausa = tarifa_pausa;
	}

	public double getTarifa() {
		return tarifa;
	}

	public void setTarifa(double tarifa) {
		this.tarifa = tarifa;
	}
	
	
}
