package com.estudos.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.estudos.backend.domain.Categoria;
import com.estudos.backend.domain.Produto;
import com.estudos.backend.repositories.CategoriaRepository;
import com.estudos.backend.repositories.ProdutoRepository;
import com.estudos.backend.services.exception.ObjectNotFoundException;

@Service
public class ProdutoService {

	@Autowired
	private ProdutoRepository repo;

	@Autowired
	private CategoriaRepository categoriaRepository;

	public Produto buscarPorId(Integer id) {
		Optional<Produto> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Produto.class.getName()));

	}

	public Page<Produto> procurar(String nome, List<Integer> ids, Integer pagina, Integer linhasPorPagina,
			String ordernarPor, String direcaoOrdernar) {

		PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Direction.valueOf(direcaoOrdernar),
				ordernarPor);
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		return repo.findDistinctByNomeContainingAndCategoriasIn(nome, categorias, pageRequest);

	}

}
