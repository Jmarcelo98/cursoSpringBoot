package com.estudos.backend.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.estudos.backend.domain.Cliente;
import com.estudos.backend.dto.ClienteDTO;
import com.estudos.backend.dto.ClienteNovoDTO;
import com.estudos.backend.services.ClienteService;

@RestController
@RequestMapping(value = "/clientes")
public class ClienteResource {

	@Autowired
	private ClienteService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Cliente> buscarPeloId(@PathVariable Integer id) {
		Cliente obj = service.buscarPorId(id);
		return ResponseEntity.ok().body(obj);
	}
	
//	ADICIONAR UM NOVO CLIENTE
	@PostMapping
	public ResponseEntity<Void> adicionar(@Valid @RequestBody ClienteNovoDTO clienteNovoDto) {
		Cliente obj = service.apartirDeUmDto(clienteNovoDto);
		obj = service.adicionar(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

//	ATUALIZAR UM CLIENTE
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> atualizar(@Valid @RequestBody ClienteDTO  clienteDto, @PathVariable Integer id) {
		Cliente categoria = service.apartirDeUmDto(clienteDto);
		categoria.setId(id);
		categoria = service.atualizar(categoria);
		return ResponseEntity.noContent().build();
	}

//	DELETAR UMA CATEGORIA
	@PreAuthorize("hasAnyRole('ADMIN')")
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deletar(@PathVariable Integer id) {
		service.deletar(id);
		return ResponseEntity.noContent().build();
	}

//	BUSCAR TODAS AS CATEGORIAS SEM OS PRODUTOS
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping
	public ResponseEntity<List<ClienteDTO>> buscarTodas() {
		List<Cliente> list = service.buscarTodas();
		List<ClienteDTO> listDto = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		return ResponseEntity.ok().body(listDto);
	}

//	PAGINACAO DE CATEGORIA 
	@PreAuthorize("hasAnyRole('ADMIN')")
	@GetMapping(value = "/page")
	public ResponseEntity<Page<ClienteDTO>> procurarPagina(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina,
			@RequestParam(value = "ordernarPor", defaultValue = "nome") String ordernarPor,
			@RequestParam(value = "direcaoOrdernar", defaultValue = "ASC") String direcaoOrdernar) {
		Page<Cliente> page = service.procurarPagina(pagina, linhasPorPagina, ordernarPor, direcaoOrdernar);
		Page<ClienteDTO> pageDto = page.map(obj -> new ClienteDTO(obj));
		return ResponseEntity.ok().body(pageDto);
	}

}
