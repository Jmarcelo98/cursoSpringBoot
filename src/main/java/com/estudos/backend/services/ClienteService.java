package com.estudos.backend.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import com.estudos.backend.domain.Cidade;
import com.estudos.backend.domain.Cliente;
import com.estudos.backend.domain.Endereco;
import com.estudos.backend.domain.enums.TipoCliente;
import com.estudos.backend.dto.ClienteDTO;
import com.estudos.backend.dto.ClienteNovoDTO;
import com.estudos.backend.repositories.CidadeRepository;
import com.estudos.backend.repositories.ClienteRepository;
import com.estudos.backend.repositories.EnderecoRepository;
import com.estudos.backend.services.exception.DataIntegrityException;
import com.estudos.backend.services.exception.ObjectNotFoundException;

@Service
public class ClienteService {

	@Autowired
	private ClienteRepository repo;

	@Autowired
	private CidadeRepository cidadeRepository;
	
	@Autowired 
	private EnderecoRepository enderecoRepository;

	public Cliente buscarPorId(Integer id) {

		Optional<Cliente> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Cliente.class.getName()));

	}

//	ADICIONAR CLIENTE
	public Cliente adicionar(Cliente cliente) {
		cliente.setId(null);
		cliente = repo.save(cliente);
		enderecoRepository.saveAll(cliente.getEnderecos());
		return cliente;
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
			throw new DataIntegrityException("Não é possível excluir porque existe pedidos relacionadas");
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

	public Cliente apartirDeUmDto(ClienteNovoDTO cliDto) {
		Cliente cli = new Cliente(null, cliDto.getNome(), cliDto.getEmail(), cliDto.getCpfOuCnpj(),
				TipoCliente.toEnum(cliDto.getTipoCliente()));

		Cidade cid = cidadeRepository.findById(cliDto.getCidadeId()).get();

		Endereco end = new Endereco(null, cliDto.getLogradouro(), cliDto.getNumero(), cliDto.getComplemento(),
				cliDto.getBairro(), cliDto.getCep(), cli, cid);

		cli.getEnderecos().add(end);
		
		cli.getTelefones().add(cliDto.getTelefone1());

		if (cliDto.getTelefone2() != null) {
			cli.getTelefones().add(cliDto.getTelefone2());
		}
		if (cliDto.getTelefone3() != null) {
			cli.getTelefones().add(cliDto.getTelefone3());
		}

		return cli;

	}

	private void atualizarDado(Cliente novoCliente, Cliente cliente) {
		novoCliente.setNome(cliente.getNome());
		novoCliente.setEmail(cliente.getEmail());
	}

}
