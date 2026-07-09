package pages;

import com.codeborne.selenide.SelenideElement;
import com.codeborne.selenide.WebDriverRunner;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class BasePage {

    protected SelenideElement findElement(String cssSelector) {
        return $(cssSelector).shouldBe(visible, Duration.ofSeconds(10));
    }

    protected void waitForPageLoad() {
        WebDriverRunner.getWebDriver().manage().timeouts().pageLoadTimeout(Duration.ofSeconds(15));
    }
}