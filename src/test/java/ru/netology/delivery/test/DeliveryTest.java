package ru.netology.delivery.test;

import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;
import ru.netology.delivery.data.DataGenerator;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;

class DeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    private void cleanForm(SelenideElement element) {
        $(element).sendKeys(Keys.CONTROL + "a");
        $(element).sendKeys(Keys.BACK_SPACE);
    }

    private final SelenideElement inputCity = $("[data-test-id=city] .input__control");
    private final SelenideElement inputDate = $("[data-test-id=date] .input__control");
    private final SelenideElement inputName = $("[data-test-id=name] .input__control");
    private final SelenideElement inputPhone = $("[data-test-id=phone] .input__control");
    private final SelenideElement checkboxAgreement = $("[data-test-id=agreement]");
    private final SelenideElement submit = $(".button_theme_alfa-on-white");
    private final SelenideElement successNotification = $("[data-test-id=success-notification]");
    private final SelenideElement notificationData = $("[class=notification__content]");
    private final SelenideElement replanNotification = $("[data-test-id=replan-notification]");
    private final SelenideElement replanNotificationButton = $("[data-test-id=replan-notification] .button_theme_alfa-on-white");

    //Задание 1 --------------------------------------------------------------------------------------------------------
    @Test
    @DisplayName("Should successful plan and replan meeting")
    void shouldSuccessfulPlanAndReplanMeeting() {
        final int daysToAddForFirstMeeting = 4;
        final int daysToAddForSecondMeeting = 7;

        var validUser = DataGenerator.Registration.generateUser("ru");
        $(inputCity).setValue(validUser.getCity());
        cleanForm(inputDate);
        $(inputDate).setValue(DataGenerator.generateDate(daysToAddForFirstMeeting));
        $(inputName).setValue(validUser.getName());
        $(inputPhone).setValue(validUser.getPhone());
        $(checkboxAgreement).click();
        $(submit).click();
        $(successNotification).shouldBe(visible);
        $(notificationData).shouldHave(exactText("Встреча успешно запланирована на " + DataGenerator.generateDate(daysToAddForFirstMeeting)));

        open("http://localhost:9999");
        $(inputCity).setValue(validUser.getCity());
        cleanForm(inputDate);
        $(inputDate).setValue(DataGenerator.generateDate(daysToAddForSecondMeeting));
        $(inputName).setValue(validUser.getName());
        $(inputPhone).setValue(validUser.getPhone());
        $(checkboxAgreement).click();
        $(submit).click();
        $(replanNotification).shouldBe(visible).shouldHave(text("У вас уже запланирована встреча на другую дату. Перепланировать?"));
        $(replanNotificationButton).click();
        $(notificationData).shouldHave(exactText("Встреча успешно запланирована на " + DataGenerator.generateDate(daysToAddForSecondMeeting)));
    }
}