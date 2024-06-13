package com.controlebens.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.controlebens.enums.EstadosBem;
import com.controlebens.error.exception.BemJaExiste;
import com.controlebens.error.exception.BemNaoEncontrado;
import com.controlebens.error.exception.LocalNaoEncontrado;
import com.controlebens.error.exception.ValorBemInvalido;
import com.controlebens.model.Bem;
import com.controlebens.model.Local;
import com.controlebens.repository.BemRepository;
import com.controlebens.repository.LocalRepository;

@Service
public class BemService {
	
	@Autowired
	private BemRepository bemRepository;
	
	@Autowired
	private LocalRepository localReposoitory;

	public List<Bem> buscarTodosBens (){
		return bemRepository.findAll();
	}
	
	public Bem bucarBemPorId(Long id) throws Exception{
		Optional<Bem> optBem = bemRepository.findById(id);
		
		if(optBem.isEmpty() || id == null) throw new BemNaoEncontrado();
		
		return optBem.get();
	}
	
	public Bem salvarBem(Bem bem) throws Exception {
		if(bem.getId() != null)
			if(bemRepository.findById(bem.getId()).isPresent()) throw new BemJaExiste();
		
		if(bem.getValorAluguel()== null && bem.getValorAtual() == null) {
			throw new ValorBemInvalido();
		}
		return bemRepository.save(bem);
	}
	
	public Bem editarBem(Bem bem) throws Exception{
		boolean exists = bemRepository.findById(bem.getId()).isPresent();
		if(exists) {
			bemRepository.save(bem);
			return bem;
		} else {
			throw new BemNaoEncontrado();
		}
	}
	
	public Bem removerBem(Long id) throws Exception {
		Optional<Bem> bem = bemRepository.findById(id);
		if(bem.isPresent()) {
			bemRepository.deleteById(id);
			return bem.get();
		} else {
			throw new BemNaoEncontrado();
		}
		
	}
	
	public List<Bem> buscarBensPorEstado(EstadosBem estadoBem) throws Exception {
		return bemRepository.findByEstadoBem(estadoBem);
	}
	
	public List<Bem> buscarBensPorLocal(Long idLocal) throws Exception{
		Optional<Local> local = localReposoitory.findById(idLocal);
		if(local.isEmpty()) {
			throw new LocalNaoEncontrado();
		}		
		return bemRepository.findByLocal(local.get());
	}
	
	public Bem buscarBemPorCodigo(String codigo) throws Exception {
		Optional<Bem> bem = bemRepository.findByCodigo(codigo);
		if(bem.isEmpty()) {
			throw new BemNaoEncontrado();
		}
		
		return bem.get();
		
	}
}
