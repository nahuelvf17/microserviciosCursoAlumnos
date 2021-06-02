package com.proyecto.microservicios.app.cursos.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.microservicios.app.cursos.model.entity.Curso;
import com.proyecto.microservicios.app.cursos.model.entity.CursoAlumno;
import com.proyecto.microservicios.app.cursos.services.CursoService;
import com.proyecto.microservicios.commons.Controllers.CommonController;
import com.proyecto.microservicios.commons.alumnos.models.entity.Alumno;
import com.proyecto.microservicios.commons.examenes.models.entity.Examen;

@RestController
public class CursoController extends CommonController<Curso, CursoService> {
	
	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	
	@DeleteMapping("/eliminar-alumno/{id}")
	public ResponseEntity<?> eliminarAlumnoPorId(@PathVariable Long id){
		service.eliminarCursoAlumnoPorId(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping
	@Override
	public ResponseEntity<?> listar(){
		
		
		List<Curso> cursos = ((List<Curso>) service.findAll()).stream().map(c->{
			c.getCursoAlumnos().forEach(ca->{
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				c.addAlumno(alumno);
			});
			return c;
		}).collect(Collectors.toList());
		
		
		return ResponseEntity.ok().body(cursos);
	}
	
	@GetMapping("/{id}")
	@Override
	public ResponseEntity<?> ver (@PathVariable Long id){
		Optional<Curso> entity = service.findById(id);
		
		if(!entity.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso curso = entity.get();
		
		if(!curso.getCursoAlumnos().isEmpty()) {
			List<Long> ids = curso.getCursoAlumnos().stream().map(ca-> ca.getAlumnoId())
					.collect(Collectors.toList());
			
			List<Alumno> alumnos = service.obtenerAlumnosPorCurso(ids);
			
			curso.setAlumnos(alumnos);
		}
		
		return ResponseEntity.ok(curso);
	}
	
	@GetMapping("/pagina")
	@Override
	public ResponseEntity<?> listar(Pageable pageable){
		Page<Curso> cursos = service.findAll(pageable).map(curso->{
			curso.getCursoAlumnos().forEach(ca->{
				Alumno alumno = new Alumno();
				alumno.setId(ca.getAlumnoId());
				curso.addAlumno(alumno);
			});
			return curso;
		});
		
		return ResponseEntity.ok().body(cursos);
	}
	
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		Map<String, Object> response = new HashMap<String, Object>();
		
		response.put("baleanceador", balanceadorTest);
		response.put("cursos", service.findAll());
		return ResponseEntity.ok(response);
	}

	@PutMapping("/{id}")
	public ResponseEntity<?> editar(@Valid @RequestBody Curso curso, BindingResult result , @PathVariable Long id){
		
		if(result.hasErrors()) {
			return validar(result);
		}
		
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = o.get();
		cursoDb.setNombre(curso.getNombre());
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDb));
	}
	
	@PostMapping("/{id}/asignar-alumnos")
	public ResponseEntity<?> asignarAlumnos(@RequestBody List<Alumno> alumnos, @PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = o.get();
		
		alumnos.forEach(a->{
			
			CursoAlumno cursoAlumno = new CursoAlumno();
			
			cursoAlumno.setAlumnoId(a.getId());
			cursoAlumno.setCurso(cursoDb);
			
			cursoDb.addCursoAlumno(cursoAlumno);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDb));
	}
	
	@PutMapping("/{id}/eliminar-alumno")
	public ResponseEntity<?> eliminarAlumno(@RequestBody Alumno alumno, @PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = o.get();
		
		CursoAlumno cursoAlumno = new CursoAlumno();
		
		cursoAlumno.setAlumnoId(alumno.getId());
		
		cursoDb.removeCursoAlumno(cursoAlumno);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDb));
	}
	
	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id){
		Curso curso = service.findCursoByAlumnoId(id);
		
		if(curso!=null) {
			List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdsConRespuestaAlumno(id);
			// Fix cambiar mas adelante
			if(examenesIds!=null && examenesIds.size()>0) {
				List<Examen> examenes = curso.getExamenes().stream().map(examen->{
					if(examenesIds.contains(examen.getId())) {
						examen.setRespondido(true);
					}
					return examen;
				}).collect(Collectors.toList());
			
				curso.setExamenes(examenes);
			}
		}
		
		return ResponseEntity.ok(curso);
	}
	
	
	@PutMapping("/{id}/asignar-examenes")
	public ResponseEntity<?> asignarExamenes(@RequestBody List<Examen> examenes, @PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = o.get();
		
		examenes.forEach(e->{
			cursoDb.addExamen(e);
		});
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDb));
	}
	
	@PutMapping("/{id}/eliminar-examen")
	public ResponseEntity<?> eliminarExamen(@RequestBody Examen examen, @PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if(!o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		
		Curso cursoDb = o.get();
		
		cursoDb.removeExamen(examen);
		
		return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(cursoDb));
	}
	
}
