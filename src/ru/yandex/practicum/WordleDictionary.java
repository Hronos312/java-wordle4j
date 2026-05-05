package ru.yandex.practicum;

import java.util.*;

/*
этот класс содержит в себе список слов List<String>
    его методы похожи на методы списка, но учитывают особенности игры
    также этот класс может содержать рутинные функции по сравнению слов, букв и т.д.
 */
public class WordleDictionary {

    private final List<String> words;
    private final Set<String> wordSet;
    private final Random rand = new Random();

    public WordleDictionary(List<String> words) {
        if (words == null || words.isEmpty()) {
            throw new IllegalArgumentException("Словарь не должен быть пустым");
        }
        this.words = words;
        this.wordSet = new HashSet<>(words);
    }

    public boolean contains(String word) {
        if (word == null) {
            return false;
        }
        word = word.toLowerCase().replace("ё", "е").trim();
        return wordSet.contains(word);
    }

    public String getRandomWord() {
        return words.get(rand.nextInt(words.size()));
    }

    public int size() {
        return words.size();
    }

    public List<String> getWords() {
        return new ArrayList<>(words);
    }
}
