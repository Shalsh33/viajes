package com.shalsh.viajes.viajes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shalsh.viajes.dto.ViajeDTO;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje,Integer> {

	@Query("Select new com.shalsh.viajes.dto.ViajeDTO(v.id, v.cliente, v.monopatin, v.inicio, v.fin, v.distancia, v.costo) "
			+ "from Viaje v WHERE v.monopatin = :id_monopatin")
	List<ViajeDTO> reporte(int id_monopatin);

}
