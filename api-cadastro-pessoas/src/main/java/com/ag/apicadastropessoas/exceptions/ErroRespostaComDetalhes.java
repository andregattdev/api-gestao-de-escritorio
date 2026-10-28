package com.ag.apicadastropessoas.exceptions;

import java.time.LocalDateTime;
import java.util.Map;

public class ErroRespostaComDetalhes {
    
    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;
    private Map<String, String> erros; // Guarda o nome do campo e a mensagem específica

    public ErroRespostaComDetalhes(LocalDateTime timestamp, int status, String erro, String mensagem, Map<String, String> erros) {
        this.timestamp = timestamp;
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
        this.erros = erros;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public int getStatus() {
        return status;
    }

    public String getErro() {
        return erro;
    }

    public String getMensagem() {
        return mensagem;
    }

    public Map<String, String> getErros() {
        return erros;
    }
}