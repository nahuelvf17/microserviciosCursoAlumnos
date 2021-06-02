package com.proyecto.microservicios.app.usuarios;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name="microservicio-cursos")
public interface CursoFeignClient {
	
	@DeleteMapping("/eliminar-alumno/{id}")
	public void eliminarAlumnoPorId(@PathVariable Long id);
}
