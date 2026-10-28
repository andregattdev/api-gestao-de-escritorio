package com.ag.apicadastropessoas.service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.ag.apicadastropessoas.exceptions.BusinessException;
import com.ag.apicadastropessoas.exceptions.ResourceNotFoundException;
import com.ag.apicadastropessoas.model.Endereco;
import com.ag.apicadastropessoas.model.Pessoa;
import com.ag.apicadastropessoas.repository.PessoaRepository;

@Service
public class PessoaService {

    private final PessoaRepository pessoaRepository;

    public PessoaService(PessoaRepository pessoaRepository) {
        this.pessoaRepository = pessoaRepository;
    }

    @Transactional(readOnly = true)
    public List<Pessoa> listarTodas() {
        return pessoaRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Pessoa> buscarPorId(Long id) {
        return pessoaRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Pessoa buscarPorIdOuFalhar(Long id) {
        return pessoaRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pessoa não encontrada com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public List<Pessoa> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) {
            return listarTodas();
        }
        return pessoaRepository.findByNomeContainingIgnoreCase(nome.trim());
    }

    @Transactional(readOnly = true)
    public Optional<Pessoa> buscarPorCpf(String cpf) {
        if (cpf == null || cpf.trim().isEmpty()) {
            return Optional.empty();
        }
        String cpfLimpo = cpf.replaceAll("\\D", "");
        Optional<Pessoa> pessoa = pessoaRepository.findByCpf(cpf);
        if (pessoa.isEmpty() && !cpf.equals(cpfLimpo)) {
            pessoa = pessoaRepository.findByCpf(cpfLimpo);
        }
        return pessoa;
    }

    @Transactional
    public Pessoa salvar(Pessoa pessoa) {
        // Valida as regras de negócio
        validarCadastro(pessoa);

        // Verifica unicidade do CPF
        if (pessoaRepository.existsByCpf(pessoa.getCpf())) {
            throw new BusinessException("Já existe uma pessoa cadastrada com o CPF informado: " + pessoa.getCpf());
        }

        // Garante a bidirecionalidade do endereço
        if (pessoa.getEndereco() != null) {
            pessoa.getEndereco().setPessoa(pessoa);
        }

        return pessoaRepository.save(pessoa);
    }

    @Transactional
    public Pessoa atualizar(Long id, Pessoa pessoaAtualizada) {
        validarCadastro(pessoaAtualizada);

        Pessoa pessoaExistente = buscarPorIdOuFalhar(id);

        // Verifica duplicidade de CPF em outro registro
        if (pessoaRepository.existsByCpfAndIdNot(pessoaAtualizada.getCpf(), id)) {
            throw new BusinessException("Já existe outra pessoa cadastrada com o CPF informado: " + pessoaAtualizada.getCpf());
        }

        pessoaExistente.setNome(pessoaAtualizada.getNome());
        pessoaExistente.setSobrenome(pessoaAtualizada.getSobrenome());
        pessoaExistente.setCpf(pessoaAtualizada.getCpf());
        pessoaExistente.setDataNascimento(pessoaAtualizada.getDataNascimento());

        // Atualiza os dados do endereço de forma segura
        if (pessoaAtualizada.getEndereco() != null) {
            Endereco enderecoAtual = pessoaExistente.getEndereco();
            if (enderecoAtual == null) {
                enderecoAtual = new Endereco();
            }
            copiarDadosEndereco(pessoaAtualizada.getEndereco(), enderecoAtual);
            enderecoAtual.setPessoa(pessoaExistente);
            pessoaExistente.setEndereco(enderecoAtual);
        } else {
            pessoaExistente.setEndereco(null);
        }

        return pessoaRepository.save(pessoaExistente);
    }

    @Transactional
    public void deletar(Long id) {
        if (!pessoaRepository.existsById(id)) {
            throw new ResourceNotFoundException("Pessoa não encontrada com o ID: " + id);
        }
        pessoaRepository.deleteById(id);
    }

    public void validarCadastro(Pessoa pessoa) {
        if (pessoa == null) {
            throw new BusinessException("Os dados da pessoa não podem ser nulos.");
        }
        if (pessoa.getDataNascimento() == null) {
            throw new BusinessException("A data de nascimento é obrigatória.");
        }
        if (pessoa.getDataNascimento().isAfter(LocalDate.now())) {
            throw new BusinessException("A data de nascimento não pode ser uma data futura.");
        }
        if (pessoa.getIdade() < 18) {
            throw new BusinessException("A pessoa precisa ser maior de idade (18 anos ou mais) para este procedimento.");
        }
    }

    private void copiarDadosEndereco(Endereco origem, Endereco destino) {
        destino.setCep(origem.getCep());
        destino.setLogradouro(origem.getLogradouro());
        destino.setNumero(origem.getNumero());
        destino.setComplemento(origem.getComplemento());
        destino.setBairro(origem.getBairro());
        destino.setLocalidade(origem.getLocalidade());
        destino.setUf(origem.getUf());
        destino.setIbge(origem.getIbge());
        destino.setGia(origem.getGia());
        destino.setDdd(origem.getDdd());
        destino.setSiafi(origem.getSiafi());
    }
}