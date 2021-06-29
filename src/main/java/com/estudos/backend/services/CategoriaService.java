package com.estudos.backend.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.estudos.backend.domain.Categoria;
import com.estudos.backend.repositories.CategoriaRepository;

@Service
public class CategoriaService {

	@Autowired
	private CategoriaRepository repo;

	public Categoria buscarPorId(Integer id) {
		Optional<Categoria> obj = repo.findById(id);
		return obj.orElse(null);
	}

}
