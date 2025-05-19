package ru.aqa.test;

import org.junit.jupiter.api.*;
import ru.aqa.data.APIHelper;
import ru.aqa.data.DataHelper;
import ru.aqa.data.SQLHelper;

import static ru.aqa.data.SQLHelper.*;

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
    @DisplayName("Should transfer valid amount of money between the cards")
    public void validTransfer() {
        var authInfo = DataHelper.getAuthInfoOfTestUser();
        APIHelper.makeQueryToLogin(authInfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authInfo.login(), verificationCode.code());
        var tokenInfo = APIHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.token(), 200);
        var card1BalanceBefore = cardsBalances.get(DataHelper.getCard1Info().id());
        var card2BalanceBefore = cardsBalances.get(DataHelper.getCard2Info().id());
        var amount = DataHelper.generateValidAmountToTransfer(card1BalanceBefore);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getCard1Info().number(),
                DataHelper.getCard2Info().number(), amount);
        APIHelper.generateQueryToTransfer(tokenInfo.token(), transferInfo, 200);
        cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.token(), 200);
        var card1BalanceAfter = cardsBalances.get(DataHelper.getCard1Info().id());
        var card2BalanceAfter = cardsBalances.get(DataHelper.getCard2Info().id());
        Assertions.assertEquals(card1BalanceBefore - amount, card1BalanceAfter);
        Assertions.assertEquals(card2BalanceBefore + amount, card2BalanceAfter);
    }

    @Test
    @DisplayName("Should not transfer invalid amount of money between the cards")
    public void invalidTransfer() {
        var authInfo = DataHelper.getAuthInfoOfTestUser();
        APIHelper.makeQueryToLogin(authInfo, 200);
        var verificationCode = SQLHelper.getVerificationCode();
        var verificationInfo = new DataHelper.VerificationInfo(authInfo.login(), verificationCode.code());
        var tokenInfo = APIHelper.sendQueryToVerify(verificationInfo, 200);
        var cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.token(), 200);
        var card1BalanceBefore = cardsBalances.get(DataHelper.getCard1Info().id());
        var card2BalanceBefore = cardsBalances.get(DataHelper.getCard2Info().id());
        var amount = DataHelper.generateInvalidAmountToTransfer(card1BalanceBefore);
        var transferInfo = new APIHelper.APITransferInfo(DataHelper.getCard1Info().number(),
                DataHelper.getCard2Info().number(), amount);
        APIHelper.generateQueryToTransfer(tokenInfo.token(), transferInfo, 400);
//        cardsBalances = APIHelper.sendQueryToGetCardsBalances(tokenInfo.token(), 200);
//        var card1BalanceAfter = cardsBalances.get(DataHelper.getCard1Info().id());
//        var card2BalanceAfter = cardsBalances.get(DataHelper.getCard2Info().id());
//        Assertions.assertEquals(card1BalanceBefore, card1BalanceAfter);
//        Assertions.assertEquals(card2BalanceBefore, card2BalanceAfter);
    }
}
