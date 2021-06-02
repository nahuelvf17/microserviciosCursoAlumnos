package com.proyecto.microservicios.commons.Controllers;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.proyecto.microservicios.commons.services.CommonService;


public class CommonController<E, S extends CommonService<E>> {
	@Autowired
	protected S service;
	
	@GetMapping
	public ResponseEntity<?> listar(){
		return ResponseEntity.ok().body(service.findAll());
	}
	
	@GetMapping("/pagina")
	public ResponseEntity<?> listar(Pageable pageable){
		return ResponseEntity.ok().body(service.findAll(pageable));
	}
	
	@GetMapping("/{id}")
	public ResponseEntity<?> ver (@PathVariable Long id){
		Optional<E> entity = service.findById(id);
		
		if(!entity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		return ResponseEntity.ok(entity.get());
	}
	
	@PostMapping
	public ResponseEntity<?> crear(@Validated @RequestBody E entity, BindingResult result){
		
		if(result.hasErrors()) {
			return validar(result);
		}
		
		E entityDb = service.save(entity);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(entityDb);
	}
	
	@DeleteMapping("/{id}")
	public ResponseEntity<?> eliminar(@PathVariable Long id){
		service.deleteById(id);
		return ResponseEntity.noContent().build();
	}
	
	protected ResponseEntity<?> validar(BindingResult result){
		Map<String, Object> errores = new HashMap<>();
		result.getFieldErrors().forEach(err->{
			errores.put(err.getField(), "El campo " + err.getField() +  " " + err.getDefaultMessage());
		});
		
		return ResponseEntity.badRequest().body(errores);
	}
	
}
