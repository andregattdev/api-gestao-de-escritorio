package com.ag.apicadastropessoas.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.ag.apicadastropessoas.exceptions.BusinessException;
import com.ag.apicadastropessoas.exceptions.ResourceNotFoundException;
import com.ag.apicadastropessoas.model.Endereco;
import com.ag.apicadastropessoas.repository.EnderecoRepository;

@Service 
public class EnderecoService {

    private final EnderecoRepository enderecoRepository;
    private final RestTemplate restTemplate;

    public EnderecoService(EnderecoRepository enderecoRepository) {
        this.enderecoRepository = enderecoRepository;
        this.restTemplate = new RestTemplate();
    }

    @Transactional(readOnly = true)
    public List<Endereco> listarTodos() {
        return enderecoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Optional<Endereco> buscarPorId(Long id) {
        return enderecoRepository.findById(id);
    }

    @Transactional(readOnly = true)
    public Endereco buscarPorIdOuFalhar(Long id) {
        return enderecoRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Endereço não encontrado com o ID: " + id));
    }

    @Transactional(readOnly = true)
    public Optional<Endereco> buscarPorPessoaId(Long pessoaId) {
        return enderecoRepository.findByPessoaId(pessoaId);
    }

    public Endereco buscarEnderecoPorCep(String cep) {
        if (cep == null || cep.trim().isEmpty()) {
            throw new BusinessException("O CEP é obrigatório.");
        }

        // Remove quaisquer caracteres não numéricos
        String cepLimpo = cep.replaceAll("\\D", "");

        if (cepLimpo.length() != 8) {
            throw new BusinessException("CEP inválido. O CEP deve conter exatamente 8 dígitos numéricos.");
        }

        String url = "https://viacep.com.br/ws/" + cepLimpo + "/json/";

        try {
            Endereco endereco = restTemplate.getForObject(url, Endereco.class);

            // Verifica se o ViaCEP retornou {"erro": true} ou dados vazios
            if (endereco == null || Boolean.TRUE.equals(endereco.getErro()) ||
               (endereco.getLocalidade() == null && endereco.getLogradouro() == null)) {
                throw new ResourceNotFoundException("CEP não encontrado: " + cep);
            }

            return endereco;
        } catch (RestClientException ex) {
            throw new BusinessException("Erro de comunicação ao consultar o serviço de CEP: " + ex.getMessage());
        }
    }

    @Transactional
    public Endereco salvar(Endereco endereco) {
        return enderecoRepository.save(endereco);
    }

    @Transactional
    public void deletar(Long id) {
        if (!enderecoRepository.existsById(id)) {
            throw new ResourceNotFoundException("Endereço não encontrado com o ID: " + id);
        }
        enderecoRepository.deleteById(id);
    }
}
