package com.proyecto.microservicios.app.respuestas.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.microservicios.app.respuestas.models.entity.Respuesta;
import com.proyecto.microservicios.app.respuestas.models.repository.RespuestaRepository;

@Service
public class RespuestaServiceImpl implements RespuestaService{

	
	//@Autowired
	//private ExamenFeignClient examenClient;
	
	@Autowired
	private RespuestaRepository repository;
	
	@Override
	public Iterable<Respuesta> saveAll(Iterable<Respuesta> respuestas) {
		return repository.saveAll(respuestas);
	}

	@Override
	public Iterable<Respuesta> findRespuestaByAlumnoByExamen(Long alumnoId, Long examenId) {
		
		/* Se cambio pq podemos aprovechar que la base es mongodb para guardar el examen dentro del json */
		/*
		Examen examen  = examenClient.obtenerExamenPorId(examenId);
		
		List<Pregunta> preguntas = examen.getPreguntas();
		List<Long> preguntasIds = preguntas.stream().map(p->p.getId()).collect(Collectors.toList());
				
		List<Respuesta> respuestas = (List<Respuesta>) repository.findRespuestaByAlumnoByPreguntaIds(alumnoId, preguntasIds);
		respuestas = respuestas.stream().map(r->{
			preguntas.forEach(p->{
				if(p.getId()==r.getPreguntaId()) {
					r.setPregunta(p);
				}
			});
			return r;
		}).collect(Collectors.toList());
		*/
		List<Respuesta> respuestas = (List<Respuesta>) repository.findRespuestaByAlumnoByExamen(alumnoId, examenId);

		return respuestas;
	}

	@Override
	public Iterable<Long> findExamenesIdConRespuestasByAlumno(Long alumnoId) {
		/*
		List<Respuesta> respuestasAlumno = (List<Respuesta>) repository.findByAlumnoId(alumnoId);
		List<Long> examenIds = Collections.emptyList();
		if(!respuestasAlumno.isEmpty()) {
			List<Long> preguntaIds = respuestasAlumno.stream().map(r->r.getPreguntaId()).collect(Collectors.toList());
			examenIds = examenClient.obtenerExamenesIdsPorPreguntasIdsRespondidas(preguntaIds);
		}
		*/
		
		// Con distint() no repito datos en la lista.
		List<Respuesta> respuestasAlumno = (List<Respuesta>) repository.findExamenesIdsConRespuestasByAlumno(alumnoId);
		List<Long> examenIds = respuestasAlumno.stream().map(r->r.getPregunta().getExamen().getId()).distinct().collect(Collectors.toList());
		
		return examenIds;
	}

	@Override
	public Iterable<Respuesta> findByAlumnoId(Long alumnoId) {
		return repository.findByAlumnoId(alumnoId);
	}
}
