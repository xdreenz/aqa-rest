package ru.netology.aqa.test;

import org.junit.jupiter.api.*;
import ru.netology.aqa.data.APIHelper;
import ru.netology.aqa.data.DataHelper;
import ru.netology.aqa.data.SQLHelper;

import static ru.netology.aqa.data.SQLHelper.*;

public class APITest {
    @AfterEach
    void tearDownCodes() {
        cleanAuthCodes();
    }

    @AfterAll
    static void tearDownAll() {
        cleanDatabase();
    }

    @Test
    @DisplayName("Should transfer valid amount of money between cards")
    public void validTransfer() {
        var authInfo = DataHelper.getAuthInfoOfTestUser();
        APIHelper.makeQueryToLogin(authInfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authInfo.getLogin(), verificationCode.getCode());
        var tokenInfo = APIHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.getToken(), 200);
        var card1BalanceBefore = cardsBalances.get(DataHelper.getCard1Info().getId());
        var card2BalanceBefore = cardsBalances.get(DataHelper.getCard2Info().getId());
        var amount = DataHelper.generateValidAmountToTransfer(card1BalanceBefore);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getCard1Info().getNumber(),
                DataHelper.getCard2Info().getNumber(), amount);
        APIHelper.generateQueryToTransfer(tokenInfo.getToken(), transferInfo, 200);
        cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.getToken(), 200);
        var card1BalanceAfter = cardsBalances.get(DataHelper.getCard1Info().getId());
        var card2BalanceAfter = cardsBalances.get(DataHelper.getCard2Info().getId());
        Assertions.assertEquals(card1BalanceBefore - amount, card1BalanceAfter);
        Assertions.assertEquals(card2BalanceBefore + amount, card2BalanceAfter);
    }

    @Test
    @DisplayName("Should not transfer invalid amount of money between cards")
    public void invalidTransfer() {
        var authInfo = DataHelper.getAuthInfoOfTestUser();
        APIHelper.makeQueryToLogin(authInfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authInfo.getLogin(), verificationCode.getCode());
        var tokenInfo = APIHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.getToken(), 200);
        var card1BalanceBefore = cardsBalances.get(DataHelper.getCard1Info().getId());
        var card2BalanceBefore = cardsBalances.get(DataHelper.getCard2Info().getId());
        var amount = DataHelper.generateInvalidAmountToTransfer(card1BalanceBefore);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getCard1Info().getNumber(),
                DataHelper.getCard2Info().getNumber(), amount);
        APIHelper.generateQueryToTransfer(tokenInfo.getToken(), transferInfo, 400);
//        cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.getToken(), 200);
//        var card1BalanceAfter = cardsBalances.get(DataHelper.getCard1Info().getId());
//        var card2BalanceAfter = cardsBalances.get(DataHelper.getCard2Info().getId());
//        Assertions.assertEquals(card1BalanceBefore, card1BalanceAfter);
//        Assertions.assertEquals(card2BalanceBefore, card2BalanceAfter);
    }
}
