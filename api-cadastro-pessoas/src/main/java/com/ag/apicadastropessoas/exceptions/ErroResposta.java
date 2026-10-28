package com.ag.apicadastropessoas.exceptions;

import java.time.LocalDateTime;

public class ErroResposta {

    private LocalDateTime timestamp;
    private int status;
    private String erro;
    private String mensagem;

    public ErroResposta(LocalDateTime timestamp, int status, String erro, String mensagem) {
        this.timestamp = timestamp;
        this.status = status;
        this.erro = erro;
        this.mensagem = mensagem;
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

}
