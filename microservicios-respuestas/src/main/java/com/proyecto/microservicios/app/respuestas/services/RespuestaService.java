package com.proyecto.microservicios.app.respuestas.services;

import com.proyecto.microservicios.app.respuestas.models.entity.Respuesta;

public interface RespuestaService {
	public Iterable<Respuesta> saveAll(Iterable<Respuesta> respuestas);
	
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId);
	
	public Iterable<Long> findExamenesIdConRespuestasByAlumno(Long alumnoId);

	public Iterable<Respuesta> findByAlumnoId(Long alumnoId);
}
