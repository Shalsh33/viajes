package com.shalsh.viajes.viajes;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.shalsh.viajes.dto.ReporteDTO;
import com.shalsh.viajes.dto.ViajeDTO;

@Repository
public interface ViajeRepository extends JpaRepository<Viaje,Integer> {

	@Query("Select new com.shalsh.viajes.dto.ViajeDTO(v.id, v.cliente, v.monopatin, v.inicio, v.fin, v.distancia, v.costo) "
			+ "from Viaje v WHERE v.monopatin = :id_monopatin")
	List<ViajeDTO> reporte(int id_monopatin);

	@Query("SELECT new com.shalsh.viajes.dto.ReporteDTO(COUNT(v.id) as cantViajes, SUM(v.distancia) as distanciaTotal, "
			+ "SUM(v.fin - v.inicio) as tiempoViajeTotal, v.monopatin as monopatin) "
			//+ ", (SELECT new com.shalsh.viajes.dto.ViajeDTO(vi.id, v.cliente, vi.monopatin, vi.inicio, vi.fin, vi.distancia, vi.costo) "
			//+ "FROM Viaje vi WHERE v.monopatin = vi.monopatin) as viajes) "
			+ "FROM Viaje v  "
			+ "GROUP BY v.monopatin "
			+ "HAVING COUNT(v.id) >= :viajesMinimos")
	List<ReporteDTO> reporteUnico(int viajesMinimos);

}
