package tests;

import config.ConfigReader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import pages.MainPage;
import pages.SearchResultPage;
import utils.FileUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class OtcSearchTest {

    private static final String SEARCH_TEXT = ConfigReader.getSearchText();
    private static final String CITY = ConfigReader.getCity();
    private static final String OUTPUT_FILE = ConfigReader.getOutputFile();

    @BeforeEach
    public void setUp() {
        // WebDriverManager настраивается автоматически
    }

    @Test
    public void searchTest() throws IOException {
        // 1. Открываем сайт, переходим в OTC товары, выбираем город и ищем
        MainPage mainPage = new MainPage();
        SearchResultPage searchResultPage = mainPage
                .openMainPage()
                .goToOtcProducts()
                .selectCity(CITY)
                .searchProduct(SEARCH_TEXT);

        // 2. Собираем товары с первой страницы
        List<Map<String, String>> allProducts = new ArrayList<>(searchResultPage.collectProductsFromCurrentPage());
        System.out.println("Собрано с 1-й страницы: " + allProducts.size());

        // 3. Переходим на вторую страницу
        searchResultPage.goToSecondPage();

        // 4. Собираем товары со второй страницы
        List<Map<String, String>> secondPageProducts = searchResultPage.collectProductsFromCurrentPage();
        System.out.println("Собрано со 2-й страницы: " + secondPageProducts.size());

        // 5. Добавляем ко всем
        allProducts.addAll(secondPageProducts);
        System.out.println("Всего собрано: " + allProducts.size());

        // 6. Сохраняем результаты
        FileUtils.saveProductsToFile(OUTPUT_FILE, allProducts);

        assertFalse(allProducts.isEmpty(), "Товары не найдены!");

        System.out.println("UI тест пройден! Сохранено товаров: " + allProducts.size());
        System.out.println("Файл: " + OUTPUT_FILE);
    }
}