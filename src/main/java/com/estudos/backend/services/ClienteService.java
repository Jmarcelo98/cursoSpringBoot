package com.estudos.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.estudos.backend.DTO.ClienteDTO;
import com.estudos.backend.domain.Cliente;
import com.estudos.backend.repositories.ClienteRepository;
import com.estudos.backend.services.exception.DataIntegrityException;
import com.estudos.backend.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	public Cliente buscarPorId(Integer id) {
		Optional<Cliente> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));

	}

//	ATUALIZAR CLIENTE
	public Cliente atualizar(Cliente cliente) {
		Cliente novoCliente = buscarPorId(cliente.getId());

		atualizarDado(novoCliente, cliente);

		return repo.save(novoCliente);
	}

//	DELETAR CLIENTE
	public void deletar(Integer id) {
		buscarPorId(id);
		try {
			repo.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque existe entidades relacionadas");
		}
	}

//	BUSCAR TODOS OS CLIENTES
	public List<Cliente> buscarTodas() {
		return repo.findAll();
	}

//	PAGINACAO
	public Page<Cliente> procurarPagina(Integer pagina, Integer linhasPorPagina, String ordernarPor,
			String direcaoOrdernar) {

		PageRequest pageRequest = PageRequest.of(0, linhasPorPagina, Direction.valueOf(direcaoOrdernar), ordernarPor);
		return repo.findAll(pageRequest);

	}

	public Cliente apartirDeUmDto(ClienteDTO cliDto) {
		return new Cliente(cliDto.getId(), cliDto.getNome(), cliDto.getEmail(), null, null);
	}

	private void atualizarDado(Cliente novoCliente, Cliente cliente) {
		novoCliente.setNome(cliente.getNome());
		novoCliente.setEmail(cliente.getEmail());
	}

}
