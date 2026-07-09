package pages;

import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class MainPage extends BasePage {
    // Локатор бургер-меню. Используется комбинация классов, т.к. сайт использует динамические классы.
    private final SelenideElement burgerMenu = $("[class*='burger'], [class*='menu-icon'], [class*='hamburger'], button[aria-label*='menu']");

    public MainPage openMainPage() {
        open("https://otc.ru/");
        waitForPageLoad();
        return this;
    }

    public CatalogPage goToOtcProducts() {
        burgerMenu.shouldBe(visible, Duration.ofSeconds(10)).click();
        $(byText("OTC товары")).shouldBe(visible, Duration.ofSeconds(10)).click();
        return new CatalogPage();
    }
}