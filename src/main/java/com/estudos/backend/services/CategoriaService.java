package com.estudos.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudos.backend.domain.Categoria;
import com.estudos.backend.repositories.CategoriaRepository;
import com.estudos.backend.services.exception.ObjectNotFoundException;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria buscarPorId(Integer id) {
		Optional<Categoria> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Categoria.class.getName()));

	}

	public Categoria adicionar(Categoria categoria) {
		categoria.setId(null);
		return repo.save(categoria);
	}

	public Categoria atualizarCategoria(Categoria categoria) {
		buscarPorId(categoria.getId());
		return repo.save(categoria);
	}

}
