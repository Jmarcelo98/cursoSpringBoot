package com.estudos.backend.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.estudos.backend.domain.Produto;
import com.estudos.backend.dto.ProdutoDTO;
import com.estudos.backend.resources.utils.URL;
import com.estudos.backend.services.ProdutoService;

@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {

	@Autowired
	private ProdutoService service;

	@GetMapping(value = "/{id}")
	public ResponseEntity<Produto> buscarPeloId(@PathVariable Integer id) {
		Produto obj = service.buscarPorId(id);
		return ResponseEntity.ok().body(obj);
	}

	@GetMapping
	public ResponseEntity<Page<ProdutoDTO>> procurarPagina(@RequestParam(value = "nome", defaultValue = "") String nome,
			@RequestParam(value = "categorias", defaultValue = "") String categorias,
			@RequestParam(value = "pagina", defaultValue = "0") Integer pagina,
			@RequestParam(value = "linhasPorPagina", defaultValue = "24") Integer linhasPorPagina,
			@RequestParam(value = "ordernarPor", defaultValue = "nome") String ordernarPor,
			@RequestParam(value = "direcaoOrdernar", defaultValue = "ASC") String direcaoOrdernar) {
		String nomeDecodificado = URL.decodificarParam(nome);
		List<Integer> ids = URL.decodificarIntList(categorias);

		Page<Produto> list = service.procurar(nomeDecodificado, ids, pagina, linhasPorPagina, ordernarPor,
				direcaoOrdernar);
		Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj));
		return ResponseEntity.ok().body(listDto);
	}

}
