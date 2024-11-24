package com.shalsh.viajes.pausas;

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

import com.shalsh.viajes.dto.PausaDTO;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/pausas")
@Tag(name = "Pausa Controller", description = "API REST para iniciar o finalizar pausas")
public class PausaController {

	@Autowired
	PausaService ps;
	
	@PostMapping("/{id_viaje}")
	@Operation(summary = "Iniciar Pausa", description= "Inicia una pausa en un viaje determinado")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "201", description = "Pausa iniciada"),
			@ApiResponse(responseCode = "400", description = "Solicitud mal parametrizada"),
			@ApiResponse(responseCode = "401", description = "Autenticación fallida"),
			@ApiResponse(responseCode = "404", description = "Viaje no encontrado"),
			@ApiResponse(responseCode = "500", description = "Error del servidor al procesar la solicitud")
	})
	public ResponseEntity<PausaDTO> iniciarPausa(@PathVariable("id_viaje") String id_viaje, @RequestBody PausaDTO pausa){
		return ps.iniciar(id_viaje,pausa);
	}
	
	@PatchMapping("/{id_viaje}/{id_pausa}")
	@PutMapping("/{id_viaje}/{id_pausa}")
	@Operation(summary = "Finalizar Pausa", description= "Finaliza la pausa")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Pausa finalizada"),
			@ApiResponse(responseCode = "400", description = "Solicitud mal parametrizada"),
			@ApiResponse(responseCode = "401", description = "Autenticación fallida"),
			@ApiResponse(responseCode = "404", description = "Pausa no encontrada"),
			@ApiResponse(responseCode = "500", description = "Error del servidor al procesar la solicitud")
	})
	public ResponseEntity<PausaDTO> finalizarPausa(@PathVariable("id_viaje")String id_viaje, @PathVariable("id_pausa")String id_pausa){
		return ps.finalizar(id_viaje,id_pausa);
	}
	
	@GetMapping("/{id_viaje}/{id_pausa}")
	@Operation(summary = "Consultar Pausa", description= "Consulta la información de una pausa")
	@ApiResponses(value = {
			@ApiResponse(responseCode = "200", description = "Información obtenida"),
			@ApiResponse(responseCode = "400", description = "Solicitud mal parametrizada"),
			@ApiResponse(responseCode = "401", description = "Autenticación fallida"),
			@ApiResponse(responseCode = "404", description = "Pausa/viaje no encontrado"),
			@ApiResponse(responseCode = "500", description = "Error del servidor al procesar la solicitud")
	})
	public ResponseEntity<PausaDTO> consultarPausa(@PathVariable("id_viaje")String id_viaje, @PathVariable("id_pausa")String id_pausa){
		return ps.consultar(id_viaje,id_pausa);
	}
}
