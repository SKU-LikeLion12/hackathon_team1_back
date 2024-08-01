package spring.likelionpractice.Exception;

public class CustomSignatureException extends RuntimeException {
    public CustomSignatureException(String invalidJwtSignature) {
        super(invalidJwtSignature);
    }
}
