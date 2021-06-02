package com.proyecto.microservicios.app.cursos.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.microservicios.app.cursos.clients.AlumnoFeignClient;
import com.proyecto.microservicios.app.cursos.clients.RespuestaFeignClient;
import com.proyecto.microservicios.app.cursos.model.entity.Curso;
import com.proyecto.microservicios.app.cursos.models.repository.CursoRepository;
import com.proyecto.microservicios.commons.alumnos.models.entity.Alumno;
import com.proyecto.microservicios.commons.services.CommonServiceImpl;

@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso, CursoRepository> implements CursoService{

	@Autowired
	private RespuestaFeignClient client;
	
	
	@Autowired
	private AlumnoFeignClient clientAlumno;
	
	@Override
	@Transactional(readOnly=true)
	public Curso findCursoByAlumnoId(Long id) {
		return repository.findCursoByAlumnoId(id);
	}

	@Override
	public Iterable<Long> obtenerExamenesIdsConRespuestaAlumno(Long alumnoId) {
		return client.obtenerExamenesIdsConRespuestaAlumno(alumnoId);
	}

	@Override
	public List<Alumno> obtenerAlumnosPorCurso(List<Long> ids) {
		return clientAlumno.obtenerAlumnosPorCurso(ids);
	}
	
	@Transactional
	@Override
	public void eliminarCursoAlumnoPorId(Long id) {
		repository.eliminarCursoAlumnoPorId(id);
	}
}
