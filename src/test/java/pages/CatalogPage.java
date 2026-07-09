package pages;

import com.codeborne.selenide.SelenideElement;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.Duration;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class CatalogPage extends BasePage {

    private final SelenideElement searchInput =
            $("input[placeholder='Название товара']");

    public CatalogPage selectCity(String city) {

        // Открываем выбор города
        $(byText("Москва"))
                .shouldBe(visible, Duration.ofSeconds(10))
                .click();

        // Ждем открытия окна выбора
        $(byText("г. Москва"))
                .shouldBe(visible, Duration.ofSeconds(10));

        // Выбираем Краснодар
        $(byText("г. Краснодар"))
                .shouldBe(visible, Duration.ofSeconds(10))
                .click();

        System.out.println("Город выбран: " + city);

        // Снимаем галочку с Москвы
        $(byText("г. Москва"))
                .shouldBe(visible, Duration.ofSeconds(10))
                .click();

        System.out.println("Москва отключена.");

        // Применяем фильтр
        $(byText("Применить"))
                .shouldBe(visible, Duration.ofSeconds(10))
                .click();

        System.out.println("Фильтр применен.");

        return this;
    }

    public SearchResultPage searchProduct(String productName) {

        searchInput
                .shouldBe(visible, Duration.ofSeconds(10))
                .clear();

        searchInput.setValue(productName);
        searchInput.pressEnter();

        // Кодируем текст для URL
        String encodedProductName = URLEncoder.encode(productName, StandardCharsets.UTF_8);

        // Ждем изменения URL
        webdriver().shouldHave(
                urlContaining("p=" + encodedProductName),
                Duration.ofSeconds(20)
        );

        // Ждем, пока появится товар с "Принтер" в названии
        $("a[itemprop='name']").shouldBe(visible, Duration.ofSeconds(20));
        
        // Ждем, пока количество карточек станет 12 (или больше)
        // Проверяем, что карточки обновились
        $$("div[itemtype='http://schema.org/Product']").shouldHave(size(12), Duration.ofSeconds(15));

        return new SearchResultPage();
    }
}