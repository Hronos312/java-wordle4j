package ru.yandex.practicum;

import java.util.*;
/*
в этом классе хранится словарь и состояние игры
    текущий шаг
    всё что пользователь вводил
    правильный ответ

в этом классе нужны методы, которые
    проанализируют совпадение слова с ответом
    предложат слово-подсказку с учётом всего, что вводил пользователь ранее

не забудьте про специальные типы исключений для игровых и неигровых ошибок
 */
public class WordleGame {

    private final String answer;
    private int steps;
    private final WordleDictionary dictionary;
    private boolean win = false;
    private final List<String> guesses;
    private final List<String> results;
    private final Set<String> usedHints;
    private String lastWord;

    public WordleGame(WordleDictionary wordleDictionary) {
        if (wordleDictionary != null) {
            dictionary = wordleDictionary;
            answer = dictionary.getRandomWord();
            steps = 6;
            win = false;
            guesses = new ArrayList<>();
            results = new ArrayList<>();
            usedHints = new HashSet<>();
            lastWord = "";
        } else {
            throw new IllegalArgumentException("Словарь не должен быть null");
        }
    }

    public WordleGame(WordleDictionary wordleDictionary, String answer) {
        if (wordleDictionary != null) {
            dictionary = wordleDictionary;
            this.answer = answer;
            steps = 6;
            win = false;
            guesses = new ArrayList<>();
            results = new ArrayList<>();
            usedHints = new HashSet<>();
            lastWord = "";
        } else {
            throw new IllegalArgumentException("Словарь не должен быть null");
        }
    }

    public String makeMove(String word)  throws WordleGameException {
        if (!isFinished()) {
            if (word.isBlank()) {
                word = getHint();
            }
            word = word.toLowerCase().replace("ё", "е").trim();
            if (word.length() == 5 && dictionary.contains(word)) {
                String result = analyze(word);
                lastWord = word;
                results.add(result);
                guesses.add(word);
                steps--;
                if (word.equals(answer)) {
                    win = true;
                }
                return result;
            } else {
                throw new InvalidWordException("Введено неподходящее слово");
            }
        } else {
            throw new GameAlreadyFinishedException("Игра окончена");
        }
    }

    public String analyze(String word) {
        return analyze(word, answer);
    }

    private String analyze(String word, String target) {
        StringBuilder result = new StringBuilder("-----");
        boolean[] usedTargetLetters = new boolean[5];
        for (int i = 0; i < 5; i++) {
            if (word.charAt(i) == target.charAt(i)) {
                result.setCharAt(i, '+');
                usedTargetLetters[i] = true;
            }
        }
        for (int i = 0; i < 5; i++) {
            if (result.charAt(i) == '+') {
                continue;
            }
            for (int j = 0; j < 5; j++) {
                if (!usedTargetLetters[j] && word.charAt(i) == target.charAt(j)) {
                    result.setCharAt(i, '^');
                    usedTargetLetters[j] = true;
                    break;
                }
            }
        }

        return result.toString();
    }

    public boolean isFinished() {
        if (win || steps == 0) {
            return true;
        } else {
            return false;
        }
    }

    public String getAnswer() {
        return answer;
    }

    public int getSteps() {
        return steps;
    }

    private String getHint() throws WordNotFoundInDictionaryException {
        List<String> words = dictionary.getWords();

        for (String candidate : words) {
            if (guesses.contains(candidate)) {
                continue;
            }
            if (usedHints.contains(candidate)) {
                continue;
            }
            boolean suitable = true;
            for (int i = 0; i < guesses.size(); i++) {
                String oldGuess = guesses.get(i);
                String expectedResult = results.get(i);
                String actualResult = analyze(oldGuess, candidate);
                if (!actualResult.equals(expectedResult)) {
                    suitable = false;
                    break;
                }
            }
            if (suitable) {
                usedHints.add(candidate);
                return candidate;
            }
        }
        throw new WordNotFoundInDictionaryException("Не удалось найти подходящую подсказку");
    }

    public String getLastWord() {
        return lastWord;
    }

    public boolean isWin() {
        return win;
    }
}
