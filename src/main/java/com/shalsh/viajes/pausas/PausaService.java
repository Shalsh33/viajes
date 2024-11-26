package com.shalsh.viajes.pausas;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.shalsh.viajes.dto.PausaDTO;
import com.shalsh.viajes.viajes.Viaje;
import com.shalsh.viajes.viajes.ViajeRepository;

@Service
public class PausaService {
	
	final static long TIMESTAMP_CONVERT = 1000000000;
	@Autowired
	PausaRepository pr;
	@Autowired
	ViajeRepository vr;

	public ResponseEntity<PausaDTO> iniciar(String id, PausaDTO pausa) {
		try {
			if (pausa.getViaje() != Integer.valueOf(id)) {
				throw new IllegalArgumentException("Solicitud mal parametrizada");
			}
			Pausa p = new Pausa();
			Viaje v = vr.findById(Integer.valueOf(id)).get();
			if (v.getFin() != null) 
				throw new IllegalArgumentException("Viaje ya finalizado, no se pueden agregar pausas");
			p.setViaje(v);
			p.setInicio(new Date());
			p = pr.save(p);
			PausaDTO response = convert(p);
			return new ResponseEntity<>(response,HttpStatus.CREATED);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	private PausaDTO convert(Pausa p) {
		PausaDTO dto = new PausaDTO();
		dto.setId(p.getId());
		dto.setInicio(p.getInicio());
		dto.setViaje(p.getViaje().getId());
		dto.setFin(p.getFin());
		dto.setFinalizada(p.getFin()!=null);
		return dto;
	}

	public ResponseEntity<PausaDTO> finalizar(String id_viaje, String id_pausa) {
		try {
			Pausa p = pr.findById(Integer.valueOf(id_pausa)).get();
			if (p.getViaje().getId() != Integer.valueOf(id_viaje) || p.getFin() != null) {
				throw new IllegalArgumentException();
			}
			p.setFin(new Date());
			p = pr.save(p);
			PausaDTO response = convert(p);
			return new ResponseEntity<>(response,HttpStatus.OK);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	public List<PausaDTO> getPausas(Viaje viaje) {
		if(viaje != null) { 
			List<PausaDTO> response = pr.findAllByViaje(viaje);
			return response;
		}
		return null;
	}

	public List<PausaDTO> reporte(int id_viaje) {
		try {
			Viaje v = vr.findById(id_viaje).get();
			return getPausas(v);
		} catch (Exception e) {
			return null;
		}
		
	}

	public ResponseEntity<PausaDTO> consultar(String id_viaje, String id_pausa) {
		try {
			Pausa p = pr.findById(Integer.valueOf(id_pausa)).get();
			return new ResponseEntity<>(convert(p),HttpStatus.OK);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	public Long getTiempoPausa(Integer viaje) {
		BigDecimal tiempo = pr.getTiempoPausa(vr.findById(viaje).get());
		return (tiempo != null) ? tiempo.longValue()/TIMESTAMP_CONVERT : 0;
	}

}
