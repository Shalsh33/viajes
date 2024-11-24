package com.shalsh.viajes.viajes;

import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.shalsh.viajes.dto.MonopatinDTO;
import com.shalsh.viajes.dto.ReporteDTO;
import com.shalsh.viajes.dto.TarifaDTO;
import com.shalsh.viajes.dto.ViajeDTO;
import com.shalsh.viajes.pausas.PausaService;

import jakarta.servlet.http.HttpServletRequest;

@Service
public class ViajeService {

	@Autowired
	private ViajeRepository vr;
	@Autowired
	private PausaService ps;
	@Autowired
	private RestTemplate restTemplate;
	@Value("http://localhost:8081/Tarifa")
	private String APITarifas;
	@Value("")
	private String APIMonopatines;
	
	public ResponseEntity<ViajeDTO> finalizar(String id, ViajeDTO viaje, HttpServletRequest request) {
		
		try {
			if (viaje.getId() != (Integer.valueOf(id))) 
				throw new IllegalArgumentException("solicitud mal parametrizada");
			Viaje v = vr.findById(viaje.getId()).get();
			if(v.getFin() != null)
				throw new IllegalArgumentException("Viaje ya finalizado");
			ResponseEntity<MonopatinDTO> validacionMonopatin = requestMonopatinUse(viaje.getMonopatin(),request.getHeader(HttpHeaders.AUTHORIZATION), false);
			if(validacionMonopatin.getStatusCode().equals(HttpStatus.OK)) {
				v.setFin(new Date());
				v.setDistancia(Double.valueOf(validacionMonopatin.getBody().getDistancia()));
				ResponseEntity<TarifaDTO> valorViaje = requestTarifa(request.getHeader(HttpHeaders.AUTHORIZATION));
				if (valorViaje != null) 
					v.setCosto(Double.valueOf(valorViaje.getBody().getTarifa()*v.getDistancia()));
				else
					throw new Exception("Error al recuperar la tarifa");
				v = vr.save(v);
				return new ResponseEntity<>(convert(v),HttpStatus.OK);
			}
			throw new IllegalArgumentException("Fallo al apagar monopatin");
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch(NullPointerException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}catch(Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}

	

	public ResponseEntity<ViajeDTO> iniciar(ViajeDTO viaje, HttpServletRequest request) {
		try {
			
				
			Viaje v = new Viaje();
			v.setCliente(viaje.getCliente());
			v.setMonopatin(viaje.getMonopatin());
			v.setInicio(new Date());
			ResponseEntity<MonopatinDTO> validacionMonopatin = requestMonopatinUse(viaje.getMonopatin(),request.getHeader(HttpHeaders.AUTHORIZATION), true);
			if(validacionMonopatin.getStatusCode().equals(HttpStatus.OK)) {
				v = vr.save(v);
				ViajeDTO response = convert(v);
				return new ResponseEntity<>(response,HttpStatus.CREATED);
			}
			throw new IllegalArgumentException("Monopatin no disponible para su uso");
			
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(NullPointerException e) {
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		}catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		
	}
	
	

	public ResponseEntity<ViajeDTO> detalles(String id) {
		
		
		try {
			ViajeDTO response = this.convert(vr.findById(Integer.valueOf(id)).get());
			return new ResponseEntity<>(response,HttpStatus.OK);
		} catch (IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch (NoSuchElementException e){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	

	public ResponseEntity<ReporteDTO> reporte(String id_monopatin, HttpServletRequest request) {
		
		ReporteDTO reporte = new ReporteDTO();
		
		try {
			int id = Integer.valueOf(id_monopatin);
			ResponseEntity<MonopatinDTO> monopatin = requestMonopatinInfo(id,request.getHeader(HttpHeaders.AUTHORIZATION));
			if(monopatin.getStatusCode().equals(HttpStatus.OK)) {
				reporte.setMonopatin(monopatin.getBody());
				List<ViajeDTO> viajesDto = vr.reporte(id);
				for(ViajeDTO v : viajesDto) {
					if (v.getFin() != null)
						v.setFinalizado(true);
					v.setPausas(ps.reporte(v.getId()));
				}
				reporte.setViajes(viajesDto);
				return new ResponseEntity<>(reporte,HttpStatus.OK);
			}
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} catch(IllegalArgumentException e) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		} catch(NullPointerException e){
			return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
		} catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	private ResponseEntity<MonopatinDTO> requestMonopatinInfo(int id_monopatin, String token) {
		if (token != null && token.startsWith("Bearer ")) {
			HttpHeaders header = new HttpHeaders();
	        header.set(HttpHeaders.AUTHORIZATION, token);
	        HttpEntity<String> req = new HttpEntity<>(header);
	        String url = APIMonopatines + "/" + id_monopatin;
	        return restTemplate.exchange(url, HttpMethod.GET, req, MonopatinDTO.class);
		}
		return null;
	}
	
	private ResponseEntity<TarifaDTO> requestTarifa(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			HttpHeaders header = new HttpHeaders();
	        header.set(HttpHeaders.AUTHORIZATION, token);
	        HttpEntity<String> req = new HttpEntity<>(header);
	        return restTemplate.exchange(APITarifas, HttpMethod.GET, req, TarifaDTO.class);
		}
		return null;
	}
	
	private ResponseEntity<MonopatinDTO> requestMonopatinUse(int id_monopatin, String token, boolean viaje){
		if (token != null && token.startsWith("Bearer ")) {
			HttpHeaders header = new HttpHeaders();
	        header.set(HttpHeaders.AUTHORIZATION, token);
	        ResponseEntity<MonopatinDTO> m = requestMonopatinInfo(id_monopatin,token);
	        if(m.getStatusCode().equals(HttpStatus.OK) && m.getBody().isDisponible() == viaje) {
	        	m.getBody().setDisponible(!viaje);
	        
		        HttpEntity<MonopatinDTO> req = new HttpEntity<>(m.getBody(),header);
		        String url = APIMonopatines + "/" + id_monopatin;
		        //hacer con put (mando el monopatin entero)
		        return restTemplate.exchange(url, HttpMethod.PUT, req, MonopatinDTO.class);
	        }
	        return new ResponseEntity<MonopatinDTO>(HttpStatus.BAD_REQUEST);
		}
		return null;
	}
	
	//Convierte un viaje en un DTO para la salida de la API
		private ViajeDTO convert(Viaje viaje) {
			ViajeDTO result = new ViajeDTO();
			result.setId(viaje.getId());
			result.setCliente(viaje.getCliente());
			result.setMonopatin(viaje.getMonopatin());
			result.setInicio(viaje.getInicio());
			if(viaje.getFin() != null) {
				result.setFin(viaje.getFin());
				result.setFinalizado(true);
				result.setCosto(viaje.getCosto());
				result.setDistancia(viaje.getDistancia());
			} else
				result.setFinalizado(false);
			result.setPausas(ps.getPausas(viaje));
			return result;
		}
}
