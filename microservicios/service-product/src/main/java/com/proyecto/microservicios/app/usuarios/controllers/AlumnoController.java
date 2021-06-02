package com.proyecto.microservicios.app.usuarios.controllers;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.microservicios.app.usuarios.services.AlumnoService;
import com.proyecto.microservicios.commons.Controllers.CommonController;
import com.proyecto.microservicios.commons.alumnos.models.entity.Alumno;

@RestController
public class AlumnoController extends CommonController<Alumno, AlumnoService> {

	
	@GetMapping("/alumnos-por-curso")
	public ResponseEntity<?> obtenerAlumnosPorCurso(@RequestParam List<Long> ids){
		
		return ResponseEntity.ok().body(service.findAllById(ids));
	}
	
	
	@GetMapping("/upload/img/{id}")
	public ResponseEntity<?> verFoto(@PathVariable Long id){
		Optional<Alumno> alumnoFound = service.findById(id);
		
		if(!alumnoFound.isPresent() || alumnoFound.get().getFoto()==null) {
			return ResponseEntity.notFound().build();
		}
		
		Resource imagen = new ByteArrayResource(alumnoFound.get().getFoto());
		
		return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(imagen);
	}
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Alumno alumno, BindingResult result, @PathVariable Long id){
		
		if(result.hasErrors()) {
			return validar(result);
		}
		
		Optional<Alumno> alumnoFound = service.findById(id);
		
		if(!alumnoFound.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Alumno alumnoDb = alumnoFound.get();
		alumnoDb.setNombre(alumno.getNombre());
		alumnoDb.setApellido(alumno.getApellido());
		alumnoDb.setEmail(alumno.getEmail());
				
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDb));
		
	}
	
	@GetMapping("/filtrar/{term}")
	public ResponseEntity<?> filtrar(@PathVariable String term){
		return ResponseEntity.ok(service.findByNombreOrApellido(term));
	}

	@PostMapping("/crear-con-foto")
	public ResponseEntity<?> crearConFoto(@Valid Alumno alumno, BindingResult result, 
			@RequestParam MultipartFile archivo) throws IOException {
		
		if(!archivo.isEmpty()) {
			alumno.setFoto(archivo.getBytes());
		}
		
		return super.crear(alumno, result);
	}
	
	@PutMapping("/editar-con-foto/{id}")
	public ResponseEntity<?> editarConFoto(@Valid Alumno alumno, BindingResult result, 
			@PathVariable Long id, @RequestParam MultipartFile archivo) throws IOException{
		
		if(result.hasErrors()) {
			return validar(result);
		}
		
		Optional<Alumno> alumnoFound = service.findById(id);
		
		if(!alumnoFound.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Alumno alumnoDb = alumnoFound.get();
		alumnoDb.setNombre(alumno.getNombre());
		alumnoDb.setApellido(alumno.getApellido());
		alumnoDb.setEmail(alumno.getEmail());
		
		if(!archivo.isEmpty()) {
			alumnoDb.setFoto(archivo.getBytes());
		}
				
		return ResponseEntity.status(HttpStatus.CREATED).body(service.save(alumnoDb));
		
	}
	
}
