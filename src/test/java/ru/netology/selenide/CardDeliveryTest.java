package ru.netology.selenide;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CardDeliveryTest {

    @BeforeEach
    void setup() {
        open("http://localhost:7777/");
    }

    String dateGenerator(int dayToAdd) {
        return java.time.LocalDate.now()
                .plusDays(dayToAdd).format(java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy"));
    }

    @Test
    void shouldBeSuccessfullDeliveryCard() {
        $("[data-test-id='city'] input").setValue("Мурманск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(dateGenerator(7));
        $("[data-test-id='name'] input").setValue("Львов Лев");
        $("[data-test-id='phone'] input").setValue("+79219998877");
        $("[data-test-id='agreement'] .checkbox__text").click();
        $("button.button_theme_alfa-on-white").click();
        $("[data-test-id='notification']").should(visible, Duration.ofMillis(15000))
                .shouldHave(text("Успешно! Встреча успешно забронирована на " + dateGenerator(7)));
    }

    @Test
    void shouldIncorrectCityInCityInput() {
        $("[data-test-id='city'] input").setValue("Ревда");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(dateGenerator(4));
        $("[data-test-id='name'] input").setValue("Львов Лев");
        $("[data-test-id='phone'] input").setValue("+79990006655");
        $("[data-test-id='agreement'] .checkbox__text").click();
        $("button.button_theme_alfa-on-white").click();
        $("[data-test-id='city'].input_invalid .input__sub")
                .shouldBe(visible).shouldHave(text("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldIncorrectDateInDateInput() {
        $("[data-test-id='city'] input").setValue("Мурманск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(dateGenerator(2));
        $("[data-test-id='name'] input").setValue("Львов Лев");
        $("[data-test-id='phone'] input").setValue("+79996665577");
        $("[data-test-id='agreement'] .checkbox__text").click();
        $("button.button_theme_alfa-on-white").click();
        $("[data-test-id='date'] .input_invalid .input__sub").shouldBe(visible)
                .shouldHave(text("Заказ на выбранную дату невозможен"));
    }

    @Test
    void shouldBeIncorrectNameInNameInput() {
        $("[data-test-id='city'] input").setValue("Мурманск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(dateGenerator(3));
        $("[data-test-id='name'] input").setValue("Lvov Lev");
        $("[data-test-id='phone'] input").setValue("+79212223355");
        $("[data-test-id='agreement'] .checkbox__text").click();
        $("button.button_theme_alfa-on-white").click();
        $("[data-test-id='name'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(text("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldIncorrectPhoneInPhoneInput() {
        $("[data-test-id='city'] input").setValue("Мурманск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(dateGenerator(3));
        $("[data-test-id='name'] input").setValue("Иванов Иван");
        $("[data-test-id='phone'] input").setValue("89996667788");
        $("[data-test-id='agreement'] .checkbox__text").click();
        $("button.button_theme_alfa-on-white").click();
        $("[data-test-id='phone'].input_invalid .input__sub").shouldBe(visible)
                .shouldHave(text("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldUncheckedAgreement() {
        $("[data-test-id='city'] input").setValue("Мурманск");
        $("[data-test-id='date'] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.DELETE);
        $("[data-test-id='date'] input").setValue(dateGenerator(3));
        $("[data-test-id='name'] input").setValue("Львов Лев");
        $("[data-test-id='phone'] input").setValue("+79998887766");
        $("button.button_theme_alfa-on-white").click();
        assertTrue($("[data-test-id='agreement'].input_invalid .checkbox__text").isDisplayed());
    }
}