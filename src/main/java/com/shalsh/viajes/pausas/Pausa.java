package com.shalsh.viajes.pausas;

import java.util.Date;

import com.shalsh.viajes.viajes.Viaje;

import jakarta.persistence.*;


@Entity
@Table(name="pausas")
public class Pausa {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@Column
	private Date inicio;
	
	@Column
	private Date fin;
	
	@ManyToOne
	private Viaje viaje;

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

	public Viaje getViaje() {
		return viaje;
	}

	public void setViaje(Viaje viaje) {
		this.viaje = viaje;
	}
	
	
}
