package com.estudos.backend.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.estudos.backend.domain.ItemPedido;
import com.estudos.backend.domain.PagamentoComBoleto;
import com.estudos.backend.domain.Pedido;
import com.estudos.backend.domain.enums.EstadoPagamento;
import com.estudos.backend.repositories.ItemPedidoRepository;
import com.estudos.backend.repositories.PagamentoRepository;
import com.estudos.backend.repositories.PedidoRepository;
import com.estudos.backend.services.exception.ObjectNotFoundException;

@Service
public class PedidoService {

	@Autowired
	private PedidoRepository repo;

	@Autowired
	private BoletoService boletoService;

	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ClienteService clienteService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private EmailService emailService;

	public Pedido buscarPorId(Integer id) {
		Optional<Pedido> obj = repo.findById(id);

		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado! Id: " + id + ", Tipo: " + Pedido.class.getName()));

	}

	@Transactional
	public Pedido adicionar(Pedido obj) {

		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.buscarPorId(obj.getCliente().getId()));
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj);
		if (obj.getPagamento() instanceof PagamentoComBoleto) {
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante());
		}
		obj = repo.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.buscarPorId(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.enviarConfirmacaoEmail(obj);
		return obj;
	}

}
