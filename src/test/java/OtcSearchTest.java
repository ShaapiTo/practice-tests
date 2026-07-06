import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.*;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;

public class OtcSearchTest {

    private static String SEARCH_TEXT;
    private static String CITY;
    private static String OUTPUT_FILE;

    static {
        try {
            InputStream input = OtcSearchTest.class.getClassLoader()
                    .getResourceAsStream("config.properties");
            Properties props = new Properties();
            
            if (input == null) {
                throw new RuntimeException("config.properties not found in resources!");
            }
            
            props.load(new InputStreamReader(input, StandardCharsets.UTF_8));
            SEARCH_TEXT = props.getProperty("search.text");
            CITY = props.getProperty("search.city");
            OUTPUT_FILE = props.getProperty("output.file");
            
            System.out.println("Config loaded: search.text=" + SEARCH_TEXT +
                    ", search.city=" + CITY +
                    ", output.file=" + OUTPUT_FILE);
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    @BeforeEach
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        open("https://otc.ru/");
        try { Thread.sleep(2000); } catch (InterruptedException e) {}
    }

    @Test
    public void searchTest() throws IOException {
        // Бургер-меню
        SelenideElement burgerMenu = $("[class*='burger'], [class*='menu-icon'], [class*='hamburger'], button[aria-label*='menu']");
        if (burgerMenu.exists() && burgerMenu.isDisplayed()) {
            burgerMenu.click();
            try { Thread.sleep(1000); } catch (InterruptedException e) {}
        }

        // Переход в "OTC товары"
        SelenideElement otcLink = $(byText("OTC товары"));
        if (otcLink.exists() && otcLink.isDisplayed()) {
            otcLink.click();
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
        }

        // Выбор города
        SelenideElement cityButton = $(byText("Москва"));
        if (!cityButton.exists()) {
            cityButton = $("[class*='city'] button, [class*='region'] button");
        }
        
        if (cityButton.exists() && cityButton.isDisplayed()) {
            cityButton.click();
            try { Thread.sleep(2000); } catch (InterruptedException e) {}
            
            SelenideElement cityOption = $(byText("г. Краснодар"));
            if (!cityOption.exists()) {
                cityOption = $(byText(CITY));
            }
            
            if (cityOption.exists() && cityOption.isDisplayed()) {
                cityOption.click();
                try { Thread.sleep(1000); } catch (InterruptedException e) {}
                System.out.println("Город выбран: " + CITY);
            } else {
                System.out.println("Город " + CITY + " не найден!");
                return;
            }
            
            SelenideElement moscowOption = $(byText("г. Москва"));
            if (moscowOption.exists() && moscowOption.isDisplayed()) {
                moscowOption.click();
                try { Thread.sleep(500); } catch (InterruptedException e) {}
                System.out.println("Галочка с Москвы снята.");
            }
            
            SelenideElement applyButton = $(byText("Применить"));
            if (applyButton.exists() && applyButton.isDisplayed()) {
                applyButton.click();
                try { Thread.sleep(2000); } catch (InterruptedException e) {}
                System.out.println("Фильтр города применен.");
            } else {
                System.out.println("Кнопка 'Применить' не найдена!");
                return;
            }
        }

        // Ввод текста
        System.out.println("Ищем поле поиска...");
        SelenideElement searchInput = $("input[placeholder='Название товара']");
        if (!searchInput.exists()) {
            searchInput = $("input[type='search']");
        }
        if (!searchInput.exists()) {
            searchInput = $("input[name='q']");
        }
        if (!searchInput.exists()) {
            searchInput = $(".SearchInput-module__xN-moW__selectInput");
        }
        
        if (searchInput.exists() && searchInput.isDisplayed()) {
            System.out.println("Поле поиска найдено. Вводим текст: " + SEARCH_TEXT);
            searchInput.click();
            searchInput.clear();
            
            searchInput.sendKeys(SEARCH_TEXT);
            try { Thread.sleep(500); } catch (InterruptedException e) {}
            
            searchInput.pressEnter();
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
            
            System.out.println("Текст введен и поиск выполнен.");
        } else {
            System.out.println("Поле поиска не найдено!");
            return;
        }

        // Сбор товаров с 1-й и 2-й страницы
        List<Map<String, String>> allProducts = new ArrayList<>();
        
        System.out.println("Собираем товары с 1-й страницы...");
        allProducts.addAll(collectProductsFromCurrentPage());
        System.out.println("Собрано товаров с 1-й страницы: " + allProducts.size());
        
        SelenideElement secondPageLink = $(byText("2"));
        if (secondPageLink.exists() && secondPageLink.isDisplayed()) {
            System.out.println("Переходим на 2-ю страницу...");
            secondPageLink.click();
            try { Thread.sleep(3000); } catch (InterruptedException e) {}
            
            System.out.println("Собираем товары со 2-й страницы...");
            allProducts.addAll(collectProductsFromCurrentPage());
            System.out.println("Всего собрано товаров: " + allProducts.size());
        } else {
            System.out.println("Вторая страница не найдена.");
        }

        // Сохранение в файл
        System.out.println("Сохраняем результаты в файл: " + OUTPUT_FILE);
        try (PrintWriter writer = new PrintWriter(new FileWriter(OUTPUT_FILE, StandardCharsets.UTF_8, false))) {
            if (allProducts.isEmpty()) {
                writer.println("Товары не найдены по запросу: " + SEARCH_TEXT + " в городе " + CITY);
            } else {
                for (Map<String, String> product : allProducts) {
                    writer.println(product.get("name") + ", " + product.get("price"));
                }
            }
        }

        System.out.println("UI test passed! Saved " + allProducts.size() + " products.");
        System.out.println("File: " + new File(OUTPUT_FILE).getAbsolutePath());
    }

    private List<Map<String, String>> collectProductsFromCurrentPage() {
        List<Map<String, String>> products = new ArrayList<>();
        
        try {
            $("body").shouldBe(visible, Duration.ofSeconds(10));
        } catch (Exception e) {
            System.out.println("Страница не загрузилась!");
            return products;
        }
        
        ElementsCollection items = $$("div[itemtype='http://schema.org/Product']");
        
        System.out.println("Найдено карточек: " + items.size());
        
        for (SelenideElement item : items) {
            String name = "";
            String price = "0";
            
            try {
                SelenideElement nameEl = item.$("a[itemprop='name']");
                if (nameEl.exists()) {
                    name = nameEl.getText().trim();
                }
                
                SelenideElement priceEl = item.$("h3[itemprop='price']");
                if (priceEl.exists()) {
                    price = priceEl.getText().trim().replaceAll("[^\\d.,]", "");
                }
            } catch (Exception e) {
                // Игнорируем ошибки
            }
            
            if (!name.isEmpty() && name.length() > 1) {
                Map<String, String> product = new LinkedHashMap<>();
                product.put("name", name);
                product.put("price", price);
                products.add(product);
                System.out.println("Товар: " + name + " | Цена: " + price);
            }
        }
        
        return products;
    }

    @AfterEach
    public void tearDown() {
        Selenide.closeWebDriver();
    }
}