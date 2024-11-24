package com.shalsh.viajes.viajes;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.shalsh.viajes.dto.ReporteDTO;
import com.shalsh.viajes.dto.ViajeDTO;


import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/viajes")
@Tag(name = "Viaje Controller", description = "API REST para iniciar, consultar o finalizar viajes")
public class ViajeController {
	
	@Autowired
	private ViajeService vs;

	@PostMapping()
	@Operation(summary = "Iniciar viaje", description= "Inicia un viaje para una cuenta en un monopatín determinado.")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Viaje iniciado"),
			@ApiResponse(responseCode = "400", description = "Solicitud mal iniciada"),
			@ApiResponse(responseCode = "401", description = "Autenticación fallida"),
			@ApiResponse(responseCode = "500", description = "Error del servidor al procesar la solicitud")
	})
	public ResponseEntity<ViajeDTO> iniciarViaje(@RequestBody ViajeDTO viaje, HttpServletRequest request) {
		return vs.iniciar(viaje,request);
	}
	
	@PatchMapping("/{id}")
	@PutMapping("/{id}")
	@Operation(summary = "Finalizar viaje", description = "Finaliza el viaje")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Viaje finalizado correctamente"),
			@ApiResponse(responseCode = "400", description = "Solicitud mal parametrizada"),
			@ApiResponse(responseCode = "401", description = "Autenticación fallida"),
			@ApiResponse(responseCode = "404", description = "Viaje no encontrado"),
			@ApiResponse(responseCode = "500", description = "Error del servidor al procesar la solicitud")
	})
	public ResponseEntity<ViajeDTO> finalizar(@PathVariable("id") String id, @RequestBody ViajeDTO viaje,HttpServletRequest request) {
		return vs.finalizar(id,viaje,request);
	}
	
	@GetMapping("/{id}")
	@Operation(summary = "Obtener Detalles", description = "Retorna los detalles de un viaje")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Datos obtenidos correctamente"),
			@ApiResponse(responseCode = "401", description = "Autenticación fallida"),
			@ApiResponse(responseCode = "404", description = "Viaje no encontrado"),
			@ApiResponse(responseCode = "500", description = "Error del servidor al procesar la solicitud")
	})
	public ResponseEntity<ViajeDTO> obtenerDetalles(@PathVariable("id") String id){
		return vs.detalles(id);
	}
	
	@GetMapping("/reporte/{id_monopatin}")
	@Operation(summary = "Obtener Reporte", description= "Obtiene el reporte de uso de un monopatín")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Reporte obtenido exitosamente"),
			@ApiResponse(responseCode = "401", description = "Autenticación fallida"),
			@ApiResponse(responseCode = "500", description = "Error del servidor al procesar la solicitud")
	})
	public ResponseEntity<ReporteDTO> obtenerReporte(@PathVariable("id_monopatin") String id_monopatin, HttpServletRequest request){
		return vs.reporte(id_monopatin,request);
	}
}
