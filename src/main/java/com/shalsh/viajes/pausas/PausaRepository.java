package com.shalsh.viajes.pausas;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shalsh.viajes.dto.PausaDTO;
import com.shalsh.viajes.viajes.Viaje;

@Repository
public interface PausaRepository extends JpaRepository<Pausa,Integer> {

	@Query("SELECT new com.shalsh.viajes.dto.PausaDTO(p.id, p.inicio, p.fin, p.viaje.id) "
			+ "FROM Pausa p WHERE p.viaje = :viaje")
	List<PausaDTO> findAllByViaje(Viaje viaje);

}
