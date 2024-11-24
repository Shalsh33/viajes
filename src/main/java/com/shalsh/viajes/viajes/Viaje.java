package com.shalsh.viajes.viajes;

import java.util.Date;
import java.util.List;

import com.shalsh.viajes.pausas.Pausa;

import jakarta.persistence.*;

@Entity
@Table(name="viajes")
public class Viaje {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column
	private Date inicio;
	
	@Column
	private Date fin;
	
	@Column
	private double costo;
	
	@Column
	private int cliente;
	
	@Column
	private double distancia;
	
	@Column
	private int monopatin;
	
	@OneToMany(mappedBy = "viaje")
	private List<Pausa> pausas;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
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

	public double getCosto() {
		return costo;
	}

	public void setCosto(double costo) {
		this.costo = costo;
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

	public List<Pausa> getPausas() {
		return pausas;
	}

	public void setPausas(List<Pausa> pausas) {
		this.pausas = pausas;
	}

	public double getDistancia() {
		return distancia;
	}

	public void setDistancia(double distancia) {
		this.distancia = distancia;
	}
	
	
}
