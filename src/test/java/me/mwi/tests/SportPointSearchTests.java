package me.mwi.tests;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.SelenideElement;
import me.mwi.data.Brands;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.stream.Stream;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;


public class SportPointSearchTests {

    @BeforeAll
    static void setUp() {
    Configuration.browserSize = "1920x1080";
    }

    @Disabled("Don't run while don't create variable")
    @EnumSource(Brands.class)
    @ParameterizedTest
    void searchTest(Brands brands) {
        open("https://sportpoint.ru/");
        $("#title-search-input").setValue(brands.name()).pressEnter();
        //По задумке здесь (ну или не здесь) будет тянутся информация из БД о кол-ве активных товаров конкретного бренда
        //(сейчас просто не хватает знаний, как хотя бы примерно это сделать, ДЗ же заключается в другом)))
        Integer example = 500;
        $(".query-result:not(.form)").shouldHave(text("Найдено " + example + " результатов по запросу"));
    }

    static SelenideElement mans = $x("//a[contains(text(), 'Мужчинам')]"),
            womens = $x("//a[contains(text(), 'Женщинам')]"),
            childrens = $x("//a[contains(text(), 'Детям')]");

    static Stream<Arguments> seoFilterTest() {
        return Stream.of(
                Arguments.of(mans, "Мужские спортивные товары"),
                Arguments.of(womens, "Женские спортивные товары"),
                Arguments.of(childrens, "Детские спортивные товары")
        );
    }

    @MethodSource
    @ParameterizedTest(name = "Page of catalog contains gender in H1")
    void seoFilterTest(SelenideElement selenideElement, String seo) {
        open("https://sportpoint.ru/");
        selenideElement.click();
        $x("//h1").shouldHave(text(seo));
    }

    @ValueSource(strings = {"обувь", "одежда", "аксессуары"})
    @ParameterizedTest(name = "First search result page contains 16 product-cards for popular request {0}")
    void popularSearchRequestTest(String testData) {
        open("https://sportpoint.ru/");
        $("#title-search-input").setValue(testData).pressEnter();
        $$(".products-product:not(.hover)").shouldHave(CollectionCondition.size(16));
    }

}
