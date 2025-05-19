package ru.aqa.data;

import java.util.Random;

public class DataHelper {
    private DataHelper() {
    }

    public static AuthInfo getAuthInfoOfTestUser() {
        return new AuthInfo("vasya", "qwerty123");
    }

    public static CardInfo getCard1Info() {
        return new CardInfo("5559 0000 0000 0001", "92df3f1c-a033-48e6-8390-206f6b1f56c0");
    }

    public static CardInfo getCard2Info() {
        return new CardInfo("5559 0000 0000 0002", "0f3f5c2a-249e-4c3d-8287-09f7a039391d");
    }

    public static int generateValidAmountToTransfer(int balance) {
        return new Random().nextInt(Math.abs(balance)) + 1;
    }

    public static int generateInvalidAmountToTransfer(int balance) {
        return Math.abs(balance) + new Random().nextInt(10000);
    }

    public record AuthInfo(
            String login,
            String password
    ) {}

    public record CardInfo(
            String number,
            String id
    ) {}

    public record VerificationCode(
            String code
    ) {}


    public record VerificationInfo(
            String login,
            String code
    ) {}
}
