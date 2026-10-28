package com.ag.apicadastropessoas.exceptions;

public class BusinessException extends RuntimeException {
    
    public BusinessException(String mensagem) {
        super(mensagem);
    }

}
