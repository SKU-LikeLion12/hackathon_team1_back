package spring.likelionpractice.service;

import java.util.Base64;

public class ImageUtility {

    // 프론트로 넘겨줄때는 String으로 반환
    // 가져온 문자열을 byte로 처리한다.
    public static String encodeImage(byte[] imageBytes) {
        if (imageBytes == null) {
            return null;
        }
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
