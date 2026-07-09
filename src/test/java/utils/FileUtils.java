package utils;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

public class FileUtils {

    public static void saveProductsToFile(String filePath, List<Map<String, String>> products) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(filePath, StandardCharsets.UTF_8, false))) {
            if (products.isEmpty()) {
                writer.println("Товары не найдены.");
            } else {
                for (Map<String, String> product : products) {
                    writer.println(product.get("name") + ", " + product.get("price"));
                }
            }
        }
    }
}