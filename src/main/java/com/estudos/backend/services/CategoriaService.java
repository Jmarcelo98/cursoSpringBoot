package com.estudos.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.estudos.backend.domain.Categoria;
import com.estudos.backend.dto.CategoriaDTO;
import com.estudos.backend.repositories.CategoriaRepository;
import com.estudos.backend.services.exception.DataIntegrityException;
import com.estudos.backend.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

//	BUSCAR UMA CATEGORIA
	public Categoria buscarPorId(Integer id) {
		Optional<Categoria> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));

	}

//	ADICIONAR CATEGORIA
	public Categoria adicionar(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}

//	ATUALIZAR CATEGORIA
	public Categoria atualizar(Categoria categoria) {
		Categoria novaCategoria = buscarPorId(categoria.getId());

		atualizarDado(novaCategoria, categoria);

		return repo.save(novaCategoria);
	}

//	DELETAR CATEGORIA
	public void deletar(Integer id) {
		buscarPorId(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que possui produtos");
		}
	}

//	BUSCAR TODAS AS CATEGORIAS
	public List<Categoria> buscarTodas() {
		return repo.findAll();
	}

//	PAGINACAO
	public Page<Categoria> procurarPagina(Integer pagina, Integer linhasPorPagina, String ordernarPor,
			String direcaoOrdernar) {

		PageRequest pageRequest = PageRequest.of(pagina, linhasPorPagina, Direction.valueOf(direcaoOrdernar), ordernarPor);
		return repo.findAll(pageRequest);

	}

	public Categoria apartirDeUmDto(CategoriaDTO catDto) {
		return new Categoria(catDto.getId(), catDto.getNome());
	}

	private void atualizarDado(Categoria novaCategoria, Categoria categoria) {
		novaCategoria.setNome(categoria.getNome());
	}

}
