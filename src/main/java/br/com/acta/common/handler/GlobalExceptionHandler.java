package br.com.acta.common.handler;

import java.util.List;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.web.HttpMediaTypeNotAcceptableException;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.HandlerMethodValidationException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;
import org.springframework.web.servlet.resource.NoResourceFoundException;
import br.com.acta.common.handler.exception.BusinessRuleException;
import br.com.acta.common.handler.exception.FirebaseAccessRevokedException;
import br.com.acta.common.handler.exception.FirebaseIdTokenException;
import jakarta.validation.ConstraintViolationException;
import lombok.extern.slf4j.Slf4j;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {
    @ExceptionHandler(FirebaseIdTokenException.class)
    public ResponseEntity<ErroResponse> handleFirebaseIdToken(FirebaseIdTokenException fite){
        log.error("mensagem", fite);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErroResponse(List.of(fite.getMessage()), 401));
    }

    @ExceptionHandler(FirebaseAccessRevokedException.class)
    public ResponseEntity<ErroResponse> handleFirebaseAccessRevoked(FirebaseAccessRevokedException fare){
        log.error("mensagem", fare);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErroResponse(List.of(fare.getMessage()), 403));
    }

    @ExceptionHandler(AuthenticationCredentialsNotFoundException.class)
    public ResponseEntity<ErroResponse> handleAuthenticationCredentialsNotFound(AuthenticationCredentialsNotFoundException acnfe){
        log.error("mensagem", acnfe);
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                .body(new ErroResponse(List.of(acnfe.getMessage()), 401));
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ResponseEntity<ErroResponse> handleBusinessRule(BusinessRuleException bre){
        log.error("mensagem", bre);
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_CONTENT)
                .body(new ErroResponse(List.of(bre.getMessage()), 422));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErroResponse> handleValidation(MethodArgumentNotValidException manve){
        log.error("mensagem", manve);
        List<String> mensagens = manve.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .toList();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(mensagens, 400));
    }

    @ExceptionHandler(HandlerMethodValidationException.class)
    public ResponseEntity<ErroResponse> handleValidation(HandlerMethodValidationException hmve) {
        log.error("mensagem", hmve);
        List<String> mensagens = hmve.getParameterValidationResults()
                .stream()
                .flatMap(r -> r.getResolvableErrors().stream())
                .map(e -> e.getDefaultMessage() != null
                        ? e.getDefaultMessage()
                        : "Um parâmetro informado é inválido")
                .toList();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(mensagens, 400));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<ErroResponse> handleIllegalState(IllegalStateException ise){
        log.error("mensagem", ise);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of(ise.getMessage()), 400));
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public ResponseEntity<ErroResponse> handleConstraint(ConstraintViolationException cve){
        log.error("mensagem", cve);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of("Algum dos parâmetros informados é inválido"), 400));
    }

    @ExceptionHandler(HttpMediaTypeNotAcceptableException.class)
    public ResponseEntity<ErroResponse> handleHttpMediaNotAcceptable(HttpMediaTypeNotAcceptableException hmtnae) {
        log.error("mensagem", hmtnae);
        return ResponseEntity.status(HttpStatus.NOT_ACCEPTABLE)
                .body(new ErroResponse(List.of("O tipo de resposta solicitado não é suportado"), 406));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErroResponse> handleValidation(HttpMessageNotReadableException hmnre){
        log.error("mensagem", hmnre);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of("O corpo da requisição está inválido ou mal formado"), 400));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ErroResponse> handleMissingParam(MissingServletRequestParameterException msrpe){
        log.error("mensagem", msrpe);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of("Um parâmetro obrigatório não foi informado"), 400));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErroResponse> handleMethodMismatch(MethodArgumentTypeMismatchException matme){
        log.error("mensagem", matme);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(new ErroResponse(List.of("Um parâmetro foi informado com o tipo inválido"), 400));
    }

    @ExceptionHandler(NoResourceFoundException.class)
    public ResponseEntity<ErroResponse> handleNoResourceFound(NoResourceFoundException nrfe){
        log.error("mensagem", nrfe);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new ErroResponse(List.of("O recurso solicitado não foi encontrado"), 404));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleHttpRequest(HttpRequestMethodNotSupportedException hrmnse){
        log.error("mensagem", hrmnse);
        return ResponseEntity.status(HttpStatus.METHOD_NOT_ALLOWED)
                .body(new ErroResponse(List.of("O método HTTP utilizado não é permitido para esta rota"), 405));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public ResponseEntity<ErroResponse> handleHttpMedia(HttpMediaTypeNotSupportedException hmtnse){
        log.error("mensagem", hmtnse);
        return ResponseEntity.status(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
                .body(new ErroResponse(List.of("O tipo de conteúdo enviado não é suportado"), 415));
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErroResponse> handleAccessDenied(AccessDeniedException ade){
        log.error("mensagem", ade);
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(new ErroResponse(List.of("Você não tem permissão de acesso para este recurso"), 403));
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    public ResponseEntity<ErroResponse> handleDataIntegrity(DataIntegrityViolationException dive){
        log.error("mensagem", dive);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of("Não foi possível realizar a operação por conflito com os dados existentes"), 409));
    }

    @ExceptionHandler(OptimisticLockingFailureException.class)
    public ResponseEntity<ErroResponse> handleOptimisticLocking(OptimisticLockingFailureException olfe){
        log.error("mensagem", olfe);
        return ResponseEntity.status(HttpStatus.CONFLICT)
                .body(new ErroResponse(List.of("Este registro foi alterado por outra operação, tente novamente"), 409));
    }

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErroResponse> handleRuntime(RuntimeException re){
        log.error("mensagem", re);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(List.of("Ocorreu um erro interno inesperado"), 500));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErroResponse> handleException(Exception e){
        log.error("mensagem", e);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErroResponse(List.of("Ocorreu um erro interno inesperado"), 500));
    }
}