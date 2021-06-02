package com.proyecto.microservicios.app.cursos.services;

import java.util.List;
import com.proyecto.microservicios.app.cursos.model.entity.Curso;
import com.proyecto.microservicios.commons.alumnos.models.entity.Alumno;
import com.proyecto.microservicios.commons.services.CommonService;

public interface CursoService extends CommonService<Curso> {
	public Curso findCursoByAlumnoId(Long id);
	
	public Iterable<Long> obtenerExamenesIdsConRespuestaAlumno(Long alumnoId);
	
	public List<Alumno> obtenerAlumnosPorCurso(List<Long> ids);
	
	public void eliminarCursoAlumnoPorId(Long id);

}
