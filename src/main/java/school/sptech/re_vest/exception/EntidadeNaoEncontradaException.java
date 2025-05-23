package school.sptech.re_vest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND)
public class EntidadeNaoEncontradaException extends RuntimeException {
    public EntidadeNaoEncontradaException() {
        super();
    }

    public EntidadeNaoEncontradaException(String message) {
        super(message + " não encontrado");
    }
}
