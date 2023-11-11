package com.danaga.generator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class RandomStringGenerator {
    private static final String CHARACTERS = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!#$%^&*()_+";

    public static String generateRandomString() throws NoSuchAlgorithmException {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder(10);

        // 최소 하나의 영문, 숫자 및 특수 문자를 추가
        sb.append(CHARACTERS.charAt(random.nextInt(26))); // 영문
        sb.append(CHARACTERS.charAt(52 + random.nextInt(26))); // 영문 (소문자)
        sb.append(CHARACTERS.charAt(2 * 26 + random.nextInt(10))); // 숫자
        sb.append(CHARACTERS.charAt(2 * 26 + 10 + random.nextInt(7))); // 특수 문자

        for (int i = 4; i < 10; i++) {
            sb.append(CHARACTERS.charAt(random.nextInt(CHARACTERS.length())));
        }

        // 문자열을 무작위로 섞음
        String shuffledString = RandomStringGenerator.shuffleString(sb.toString());
        return shuffledString;
    }

    // 문자열을 무작위로 섞는 메서드
    private static String shuffleString(String input) throws NoSuchAlgorithmException {
        char[] characters = input.toCharArray();
        for (int i = characters.length - 1; i > 0; i--) {
            int index = SecureRandom.getInstanceStrong().nextInt(i + 1);
            char temp = characters[i];
            characters[i] = characters[index];
            characters[index] = temp;
        }
        return new String(characters);
    }

}