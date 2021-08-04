package com.estudos.backend.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.estudos.backend.domain.Pedido;
import com.estudos.backend.services.PedidoService;

@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {

	@Autowired
	private PedidoService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Pedido> buscarPeloId(@PathVariable Integer id) {
		Pedido obj = service.buscarPorId(id);
		return ResponseEntity.ok().body(obj);
	}

	@PostMapping
	public ResponseEntity<Void> adicionar(@Valid @RequestBody Pedido obj) {
		obj = service.adicionar(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(obj.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@GetMapping
	public ResponseEntity<Page<Pedido>> procurarPagina(
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina,
			@RequestParam(value = "ordernarPor", defaultValue = "instante") String ordernarPor,
			@RequestParam(value = "direcaoOrdernar", defaultValue = "DESC") String direcaoOrdernar) {
		Page<Pedido> page = service.procurarPagina(pagina, linhasPorPagina, ordernarPor, direcaoOrdernar);
		return ResponseEntity.ok().body(page);
	}

}
