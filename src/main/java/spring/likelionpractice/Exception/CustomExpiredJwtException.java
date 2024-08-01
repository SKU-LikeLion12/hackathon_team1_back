package spring.likelionpractice.Exception;

public class CustomExpiredJwtException extends RuntimeException {
    public CustomExpiredJwtException(String expiredJwtToken) {
        super(expiredJwtToken);
    }
}
