package spring.likelionpractice.service;

import java.util.Base64;

public class ImageUtility {

    // 실제로 프론트에서 넘겨주는 자료형은 String으로 가져오고
    // 가져온 문자열을 byte로 처리한다.
    public static String encodeImage(byte[] imageBytes) {
        return Base64.getEncoder().encodeToString(imageBytes);
    }
}
