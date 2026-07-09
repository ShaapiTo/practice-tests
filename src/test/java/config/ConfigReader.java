package config;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class ConfigReader {

    private static final Properties secrets = new Properties();
    private static final Properties testData = new Properties();

    static {
        // Загружаем секреты (application.properties)
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("application.properties")) {
            if (input != null) {
                secrets.load(input);
                System.out.println("Secrets loaded from application.properties");
            } else {
                System.out.println("application.properties not found, using env variables");
            }
        } catch (Exception e) {
            System.out.println("Error loading application.properties: " + e.getMessage());
        }

        // Загружаем тестовые данные (config.properties)
        try (InputStream input = ConfigReader.class.getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (input != null) {
                // приндительно читаем В UTF-8
                testData.load(new InputStreamReader(input, StandardCharsets.UTF_8));
                System.out.println("Test data loaded from config.properties (UTF-8)");
            } else {
                System.out.println("config.properties not found!");
            }
        } catch (Exception e) {
            System.out.println("Error loading config.properties: " + e.getMessage());
        }
    }

    //Для секретов
    public static String getToken() {
        String token = System.getenv("DADATA_TOKEN");
        if (token != null && !token.isEmpty()) {
            return token;
        }

        token = secrets.getProperty("dadata.token");
        if (token != null && !token.isEmpty()) {
            return token;
        }

        throw new RuntimeException("DADATA_TOKEN not found in env or application.properties!");
    }

    //Для тестовых данных (из config.properties)
    public static String getSearchText() {
        return testData.getProperty("search.text", "Принтер");
    }

    public static String getCity() {
        return testData.getProperty("search.city", "Краснодар");
    }

    public static String getOutputFile() {
        return testData.getProperty("output.file", "results.txt");
    }
}