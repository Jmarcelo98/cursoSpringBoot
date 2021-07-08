package com.estudos.backend.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.estudos.backend.domain.Categoria;
import com.estudos.backend.dto.CategoriaDTO;
import com.estudos.backend.services.CategoriaService;

@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResource {

	@Autowired
	private CategoriaService service;

//	BUSCAR CATEGORIA POR ID
	@GetMapping(value = "/{id}")
	public ResponseEntity<Categoria> buscarPorId(@PathVariable Integer id) {
		Categoria obj = service.buscarPorId(id);
		return ResponseEntity.ok().body(obj);
	}

//	ADICIONAR UMA NOVA CATEGORIA
	@PostMapping
	public ResponseEntity<Void> adicionar(@Valid @RequestBody CategoriaDTO categoriaDto) {
		Categoria obj = service.apartirDeUmDto(categoriaDto);
		obj = service.adicionar(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

//	ATUALIZAR UMA CATEGORIA
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> atualizar(@Valid @RequestBody CategoriaDTO categoriaDto, @PathVariable Integer id) {
		Categoria categoria = service.apartirDeUmDto(categoriaDto);
		categoria.setId(id);
		categoria = service.atualizar(categoria);
		return ResponseEntity.noContent().build();
	}

//	DELETAR UMA CATEGORIA
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Integer id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}

//	BUSCAR TODAS AS CATEGORIAS SEM OS PRODUTOS
	@GetMapping
	public ResponseEntity<List<CategoriaDTO>> buscarTodas() {
		List<Categoria> list = service.buscarTodas();
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

//	PAGINACAO DE CATEGORIA 
	@GetMapping(value = "/page")
	public ResponseEntity<Page<CategoriaDTO>> procurarPagina(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina,
			@RequestParam(value = "ordernarPor", defaultValue = "nome") String ordernarPor,
			@RequestParam(value = "direcaoOrdernar", defaultValue = "ASC") String direcaoOrdernar) {
		Page<Categoria> page = service.procurarPagina(pagina, linhasPorPagina, ordernarPor, direcaoOrdernar);
		Page<CategoriaDTO> pageDto = page.map(obj -> new CategoriaDTO(obj));
		return ResponseEntity.ok().body(pageDto);
	}

}
