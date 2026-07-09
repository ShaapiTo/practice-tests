package pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.webdriver;
import static com.codeborne.selenide.WebDriverConditions.urlContaining;

public class SearchResultPage extends BasePage {

    private static final String PRODUCT_CARD = "div[itemtype='http://schema.org/Product']";
    private static final String PRODUCT_NAME = "a[itemprop='name']";
    private static final String PRODUCT_PRICE = "h3[itemprop='price']";

    public List<Map<String, String>> collectProductsFromCurrentPage() {

        $$(PRODUCT_CARD)
                .shouldHave(sizeGreaterThan(0), Duration.ofSeconds(20));

        List<Map<String, String>> products = new ArrayList<>();

        int count = $$(PRODUCT_CARD).size();

        for (int i = 0; i < count; i++) {

            try {

                // Каждый раз перечитываем элемент из DOM
                SelenideElement card = $$(PRODUCT_CARD).get(i);

                String name = card.$(PRODUCT_NAME)
                        .shouldBe(visible, Duration.ofSeconds(10))
                        .getText()
                        .trim();

                String price = card.$(PRODUCT_PRICE)
                        .shouldBe(visible, Duration.ofSeconds(10))
                        .getText()
                        .replaceAll("[^\\d.,]", "");

                Map<String, String> product = new LinkedHashMap<>();
                product.put("name", name);
                product.put("price", price);

                products.add(product);

            } catch (Exception e) {
                System.out.println("Карточка была обновлена React, пропускаем...");
            }
        }

        return products;
    }

    public SearchResultPage goToSecondPage() {

        // Нажимаем на ссылку "2"
        $(byText("2"))
                .shouldBe(visible, Duration.ofSeconds(10))
                .click();

        // Ждем, что URL изменился на page=2
        webdriver().shouldHave(
                urlContaining("page=2"),
                Duration.ofSeconds(20)
        );

        // Ждем, что карточки появились на новой странице
        $$(PRODUCT_CARD)
                .shouldHave(sizeGreaterThan(0), Duration.ofSeconds(20));

        return this;
    }
}