package ru.yandex.practicum;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.io.*;


/*
этот класс содержит в себе всю рутину по работе с файлами словарей и с кодировками
    ему нужны методы по загрузке списка слов из файла по имени файла
    на выходе должен быть класс WordleDictionary
 */
public class WordleDictionaryLoader {

    public WordleDictionary load(String filename) throws IOException {
        List<String> words = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                line = line.toLowerCase().replace("ё", "е").trim();
                if (line.length() == 5) {
                    words.add(line);
                }
            }
        }

        return new WordleDictionary(words);
    }

}
