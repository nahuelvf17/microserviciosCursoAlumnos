package com.proyecto.microservicios.app.examenes.models.services;

import java.util.List;

import com.proyecto.microservicios.commons.examenes.models.entity.Asignatura;
import com.proyecto.microservicios.commons.examenes.models.entity.Examen;
import com.proyecto.microservicios.commons.services.CommonService;

public interface ExamenService extends CommonService<Examen>{
	public List<Examen> findByNombre(String term);

	public Iterable<Asignatura> findAllAsignaturas();
	
	public Iterable<Long> findExamenesIdConRespuestasByPreguntasIds(Iterable<Long> alumnoId);

}
