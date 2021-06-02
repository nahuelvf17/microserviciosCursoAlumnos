package com.proyecto.microservicios.app.examenes.models.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.microservicios.app.examenes.models.repository.AsignaturaRepository;
import com.proyecto.microservicios.app.examenes.models.repository.ExamenRepository;
import com.proyecto.microservicios.commons.examenes.models.entity.Asignatura;
import com.proyecto.microservicios.commons.examenes.models.entity.Examen;
import com.proyecto.microservicios.commons.services.CommonServiceImpl;

@Service
public class ExamenServiceImpl extends CommonServiceImpl<Examen, ExamenRepository> implements ExamenService{
	
	@Autowired
	private AsignaturaRepository asignaturaRepository;
	
	@Override
	@Transactional(readOnly=true)
	public List<Examen> findByNombre(String term) {
		return repository.findByNombre(term);
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<Asignatura> findAllAsignaturas() {
		// TODO Auto-generated method stub
		return asignaturaRepository.findAll();
	}

	@Override
	@Transactional(readOnly=true)
	public Iterable<Long> findExamenesIdConRespuestasByPreguntasIds(Iterable<Long> alumnoId) {
		return repository.findExamenesIdConRespuestasByPreguntasIds(alumnoId);
	}
}
