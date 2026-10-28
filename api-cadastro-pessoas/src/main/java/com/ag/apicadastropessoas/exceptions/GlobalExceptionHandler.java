package com.ag.apicadastropessoas.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BusinessException.class)
    public ResponseEntity<ErroResposta> handleBusinessException(BusinessException ex) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Regra de Negócio",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErroResposta> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.NOT_FOUND.value(),
            "Recurso não encontrado",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
    }

    // Trata erros de validação dos campos (ex: @NotBlank, @Pattern, @NotNull, @Size, @Past)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroRespostaComDetalhes> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errosCampos = new HashMap<>();
        
        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errosCampos.put(error.getField(), error.getDefaultMessage());
        }

        ErroRespostaComDetalhes erro = new ErroRespostaComDetalhes(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Erro de Validação",
            "Há campos inválidos no preenchimento do formulário.",
            errosCampos
        );
        
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    // Trata erros de formato de dados no corpo da requisição (ex: formato de data inválido)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResposta> handleHttpMessageNotReadable(HttpMessageNotReadableException ex) {
        String detalhe = "Dados enviados na requisição estão em formato inválido ou malformados.";
        Throwable causa = ex.getMostSpecificCause();
        if (causa != null && causa.getMessage() != null) {
            detalhe = causa.getMessage();
        }

        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Formato de Dados Inválido",
            detalhe
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErroResposta> handleIllegalArgument(IllegalArgumentException ex) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.BAD_REQUEST.value(),
            "Argumento Inválido",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResposta> handleGenericException(Exception ex) {
        ErroResposta erro = new ErroResposta(
            LocalDateTime.now(),
            HttpStatus.INTERNAL_SERVER_ERROR.value(),
            "Erro interno do servidor",
            ex.getMessage()
        );
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(erro);
    }
}