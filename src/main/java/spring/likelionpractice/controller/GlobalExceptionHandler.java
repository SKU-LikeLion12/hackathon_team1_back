package spring.likelionpractice.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import spring.likelionpractice.Exception.*;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(InvalidCredentialsException.class)
    public ResponseEntity<String> handleInvalidCredentials(InvalidCredentialsException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }

    @ExceptionHandler(InvalidIdException.class)
    public ResponseEntity<String> handleInvalidIdException(InvalidIdException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Id를 찾지 못했습니다.");
    }

    @ExceptionHandler(InvalidArticleNotFound.class)
    public ResponseEntity<String> handleInvalidArticleNotFound(InvalidArticleNotFound e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("게시물을 찾지 못했습니다.");
    }

    @ExceptionHandler(DuplicatedException.class)
    public ResponseEntity<String> handleDuplicatedException(DuplicatedException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("중복 되었습니다.");
    }

    @ExceptionHandler(CustomSignatureException.class)
    public ResponseEntity<String> handleSignatureException(CustomSignatureException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("유효하지 않은 토큰입니다.");
    }

    @ExceptionHandler(CustomExpiredJwtException.class)
    public ResponseEntity<String> handleExpiredJwtException(CustomExpiredJwtException e) {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("만료된 토큰입니다.");
    }

    @ExceptionHandler(InvalidIdPassword.class)
    public ResponseEntity<String> handleInvalidIdPasswordException(InvalidIdPassword e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("아이디 또는 패스워드가 잘못되었습니다.");
    }

    @ExceptionHandler(InvalidCommentException.class)
    public ResponseEntity<String> handleInvalidCommentException(InvalidCommentException e) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("댓글을 찾지 못했습니다.");
    }
}
