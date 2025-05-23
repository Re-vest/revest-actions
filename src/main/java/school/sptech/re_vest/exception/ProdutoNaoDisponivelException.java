package school.sptech.re_vest.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class ProdutoNaoDisponivelException extends RuntimeException {
    public ProdutoNaoDisponivelException() {
        super();
    }

    public ProdutoNaoDisponivelException(String messageId, String messageStatus) {
        super(String.format("O produto de ID %s está %s", messageId, messageStatus));
    }
}
