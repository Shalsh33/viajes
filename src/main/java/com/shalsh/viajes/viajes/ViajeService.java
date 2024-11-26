package com.shalsh.viajes.viajes;


import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
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
	@Value("")
	private String APIParadas;
	
	public ResponseEntity<ViajeDTO> finalizar(String id, ViajeDTO viaje, HttpServletRequest request) {
		
		try {
			if (viaje.getId() != (Integer.valueOf(id))) 
				throw new IllegalArgumentException("solicitud mal parametrizada");
			Viaje v = vr.findById(viaje.getId()).get();
			if(v.getFin() != null)
				throw new IllegalArgumentException("Viaje ya finalizado");
			//ResponseEntity<MonopatinDTO> validacionMonopatin = requestMonopatinUse(viaje.getMonopatin(),request.getHeader(HttpHeaders.AUTHORIZATION), false);
			//if(validacionMonopatin.getStatusCode().equals(HttpStatus.OK)) {
				v.setFin(new Date());
				//v.setDistancia(Double.valueOf(validacionMonopatin.getBody().getDistance()));
				v.setDistancia(1);
				ResponseEntity<TarifaDTO> valorViaje = requestTarifa(request.getHeader(HttpHeaders.AUTHORIZATION));
				if (valorViaje != null) 
					//v.setCosto(Double.valueOf(valorViaje.getBody().getTarifa()*v.getDistancia()));
					v.setCosto(15.0);
				else
					throw new Exception("Error al recuperar la tarifa");
				//Comprobar que las pausas se hayan cerrado (TODO)
				v = vr.save(v);
				return new ResponseEntity<>(convert(v),HttpStatus.OK);
			//}
			//throw new Exception();
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
			//ResponseEntity<MonopatinDTO> validacionMonopatin = requestMonopatinUse(viaje.getMonopatin(),request.getHeader(HttpHeaders.AUTHORIZATION), true);
			//if(validacionMonopatin.getStatusCode().equals(HttpStatus.OK)) {
				v = vr.save(v);
				ViajeDTO response = convert(v);
				return new ResponseEntity<>(response,HttpStatus.CREATED);
			//}
			//throw new IllegalArgumentException();
			
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

	

	public ResponseEntity<ReporteDTO> reporteMonopatin(int id_monopatin, HttpServletRequest request) {
		
		ReporteDTO reporte = new ReporteDTO();
		
		try {
			int id = Integer.valueOf(id_monopatin);
			//ResponseEntity<MonopatinDTO> monopatin = requestMonopatinInfo(id,request.getHeader(HttpHeaders.AUTHORIZATION));
			//if(monopatin.getStatusCode().equals(HttpStatus.OK)) {
				//reporte.setMonopatin(monopatin.getBody());
				List<ViajeDTO> viajesDto = vr.reporte(id);
				for(ViajeDTO v : viajesDto) {
					if (v.getFin() != null)
						v.setFinalizado(true);
					v.setPausas(ps.reporte(v.getId()));
				}
				reporte.setViajes(viajesDto);
				return new ResponseEntity<>(reporte,HttpStatus.OK);
			//}
			//return new ResponseEntity<>(HttpStatus.NOT_FOUND);
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
	
	private ResponseEntity<List<MonopatinDTO>> requestMonopatinList(String token) {
		if (token != null && token.startsWith("Bearer ")) {
			HttpHeaders header = new HttpHeaders();
	        header.set(HttpHeaders.AUTHORIZATION, token);
	        HttpEntity<String> req = new HttpEntity<>(header);
	        String url = APIMonopatines;
	        return restTemplate.exchange(url, HttpMethod.GET, req, new ParameterizedTypeReference<List<MonopatinDTO>>(){});
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
		return new ResponseEntity<>(HttpStatus.OK);
	}
	
	private ResponseEntity<MonopatinDTO> requestMonopatinUse(int id_monopatin, String token, boolean viaje){
		if (token != null && token.startsWith("Bearer ")) {
			HttpHeaders header = new HttpHeaders();
	        header.set(HttpHeaders.AUTHORIZATION, token);
	        ResponseEntity<MonopatinDTO> m = requestMonopatinInfo(id_monopatin,token);
	        //si la solicitud se completó
	        if(m.getStatusCode().equals(HttpStatus.OK) && 
	        		//Y el valor de is disponible es igual al que recibo como parámetro
	        		// (si va a iniciar un viaje tiene que estar disponible y vice versa)
	        		m.getBody().isDisponible() == viaje) {
	        	m.getBody().setDisponible(!viaje);
	        
		        HttpEntity<MonopatinDTO> req = new HttpEntity<>(m.getBody(),header);
		        
		        /*Ajustar url*/
		        
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



		public ResponseEntity<List<ReporteDTO>> reporte(HttpServletRequest request, Map<String, String> params) {

			boolean pausas = (params.containsKey("pausas") && params.get("pausas").equalsIgnoreCase("true"));
			int viajesMinimos = (params.containsKey("viajesMin")) ? Integer.valueOf(params.get("viajesMin")) : 0;
			try {
				//ResponseEntity<List<MonopatinDTO>> monopatines = requestMonopatinList(request.getHeader(HttpHeaders.AUTHORIZATION));
				//if (monopatines.getStatusCode().equals(HttpStatus.OK)) {
					List<ReporteDTO> reporte = vr.reporteUnico(viajesMinimos);
					for(ReporteDTO r:reporte) {						
						List<ViajeDTO> viajes = vr.reporte(r.getMonopatin());
						for(ViajeDTO v:viajes) {
							v.setPausas(ps.reporte(v.getId()));
							if(!pausas)
								r.setTiempoViaje(r.getTiempoViaje() - ps.getTiempoPausa(v.getId()));
						}
						r.setViajes(viajes);
					}
					
					return new ResponseEntity<>(reporte,HttpStatus.OK);
					
				//}
			} catch (Exception e) {
				e.printStackTrace();	
				return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
			}
		}
		
}
