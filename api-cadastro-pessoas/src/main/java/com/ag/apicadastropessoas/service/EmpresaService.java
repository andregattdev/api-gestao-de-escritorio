package com.ag.apicadastropessoas.service;

import com.ag.apicadastropessoas.exceptions.ResourceNotFoundException;
import com.ag.apicadastropessoas.model.Empresa;
import com.ag.apicadastropessoas.model.Endereco;
import com.ag.apicadastropessoas.repository.EmpresaRepository;
import java.util.List;
import java.util.ArrayList;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class EmpresaService {
    private final EmpresaRepository repository;

    public EmpresaService(EmpresaRepository repository) { this.repository = repository; }

    @Transactional(readOnly = true)
    public List<Empresa> listarTodas() { return repository.findAll(); }

    @Transactional
    public Empresa salvar(Empresa empresa) { return repository.save(empresa); }

    @Transactional
    public Empresa atualizar(Long id, Empresa dados) {
        Empresa empresa = buscar(id);
        empresa.setRazaoSocial(dados.getRazaoSocial());
        empresa.setNomeFantasia(dados.getNomeFantasia());
        empresa.setCnpj(dados.getCnpj());
        empresa.setInscricaoEstadual(dados.getInscricaoEstadual());
        if (dados.getEnderecos() != null) {
            List<Endereco> atuais = new ArrayList<>(empresa.getEnderecos());
            List<Endereco> atualizados = new ArrayList<>();
            for (Endereco recebido : dados.getEnderecos()) {
                Endereco endereco = atuais.stream()
                        .filter(atual -> recebido.getId() != null && recebido.getId().equals(atual.getId()))
                        .findFirst().orElseGet(Endereco::new);
                copiarEndereco(recebido, endereco);
                endereco.setEmpresa(empresa);
                atualizados.add(endereco);
            }
            empresa.setEnderecos(atualizados);
        }
        return repository.save(empresa);
    }

    @Transactional(readOnly = true)
    public Empresa buscar(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Empresa não encontrada com o ID: " + id));
    }

    private void copiarEndereco(Endereco origem, Endereco destino) {
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

    @Transactional
    public void excluir(Long id) {
        if (!repository.existsById(id)) throw new ResourceNotFoundException("Empresa não encontrada com o ID: " + id);
        repository.deleteById(id);
    }
}
