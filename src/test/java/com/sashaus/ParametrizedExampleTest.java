package com.sashaus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.util.List;
import java.util.stream.Stream;

import static com.codeborne.selenide.CollectionCondition.texts;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

@Tag("SMOKE")
@DisplayName("Примеры параметризированных тестов с различными датапровайдерами")
public class ParametrizedExampleTest {

    @BeforeEach
    void setup() {
        open("https://qa.guru/");
    }


    @ValueSource(strings = {
            "Курсы Java+", "Курсы Python", "Программа", "Цена"
    })
    @ParameterizedTest(name = "Пример с '@ValueSource'. Проверка названий кнопок на сайте qa.guru {0}")
    void checkButtonNamesOnQaGuruWebsiteTest(String buttonName) {
        $$("#menu a").find(text(buttonName)).click();
        $$(".main-menu-pages a").filter(visible).shouldHave(texts(buttonName));
    }

    @CsvSource(value = {
            "Курсы Java+,  Автоматизация тестирования на Java Advanced",
            "Курсы Python,  Курс инженеров по автоматизации тестирования на Python"
    })
    @ParameterizedTest(name = "Пример с '@CsvSource'. На сайте qa.guru при клике на кнопку {0} должен отображаться заголовок {1}")
    void checkTitleForCoursesOnQaGuruWebsiteTest(String buttonName, String titleText) {
        $$("li a").find(text(buttonName)).click();
        $("h1").shouldHave(text(titleText));
    }

    @CsvFileSource(resources = "/testdata/checkTitleForCoursesOnQaGuruWebsiteWithCsvFileSourceTest.csv")
    @ParameterizedTest(name = "Пример с '@CsvFileSource'. На сайте qa.guru при клике на кнопку {0} должен отображаться заголовок {1}")
    void checkTitleForCoursesOnQaGuruWebsiteWithCsvFileSourceTest(String buttonName, String titleText) {
        $$("li a").find(text(buttonName)).click();
        $("h1").shouldHave(text(titleText));
    }

    static Stream<Arguments> checkJavaCourseVariantsAndOptionsOnQaGuruWebsiteTest() {
        return Stream.of(
                Arguments.of("Первый вариант",
                        List.of("Часть занятий в записи + занятия в прямом эфире с возможностью задать вопросы преподавателям",
                                "Записи занятий с тайм-кодами, полезные ссылки к ним и домашние задания (без проверки)",
                                "Сертификат слушателя курса по окончании курса")),
                Arguments.of("Второй вариант",
                        List.of("Часть занятий в записи + занятия в прямом эфире с возможностью задать вопросы преподавателям",
                                "Записи занятий с тайм-кодами, полезные ссылки и домашние задания с проверкой и обратной связью",
                                "Чат со студентами и преподавателями потока",
                                "Консультации наставников и преподавателей при необходимости",
                                "Участие в живых разборах ДЗ",
                                "Сертификат участника по окончании курса (при успешной сдаче дипломного проекта)")),
                Arguments.of("Третий вариант",
                        List.of("Часть занятий в записи + занятия в прямом эфире с возможностью задать вопросы преподавателям",
                                "Записи занятий с тайм-кодами, полезные ссылки и домашние задания с проверкой и обратной связью",
                                "Чат со студентами и преподавателями потока",
                                "Консультации наставников и преподавателей при необходимости",
                                "Участие в живых разборах ДЗ",
                                "Сертификат участника по окончании курса (при успешной сдаче дипломного проекта)"
                        )
                ));
    }


    @MethodSource
    @ParameterizedTest(name = "Пример с '@MethodSource'. Проверка списка опций: {1} варианта {0} для курса Java на сайте qa.guru")
    void checkJavaCourseVariantsAndOptionsOnQaGuruWebsiteTest(String variant, List<String> options) {
        $("#course_price").$(byText(variant)).parent()
                .$$("li").shouldHave(texts(options));
    }
}
