package school.sptech.re_vest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class EmailJaCadastradoException extends RuntimeException {
    public EmailJaCadastradoException() {
        super("O e-mail já está cadastrado.");
    }
}
