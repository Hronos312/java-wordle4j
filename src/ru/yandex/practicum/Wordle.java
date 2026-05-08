package ru.yandex.practicum;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/*
в главном классе нам нужно:
    создать лог-файл (он должен передаваться во все классы)
    создать загрузчик словарей WordleDictionaryLoader
    загрузить словарь WordleDictionary с помощью класса WordleDictionaryLoader
    затем создать игру WordleGame и передать ей словарь
    вызвать игровой метод в котором в цикле опрашивать пользователя и передавать информацию в игру
    вывести состояние игры и конечный результат
 */
public class Wordle {

    public static void main(String[] args) {
        try (PrintWriter  writer = new PrintWriter(new FileWriter("wordle.log", StandardCharsets.UTF_8));
             Scanner scanner = new Scanner(System.in)) {
            try {
                System.out.println("Вас приветствует игра Wordle!");
                writer.println("Программа начала работу");

                WordleDictionaryLoader wordleDictionaryLoader = new WordleDictionaryLoader();
                WordleDictionary dictionary = wordleDictionaryLoader.load("words_ru.txt");
                writer.println("Словарь загружен");

                WordleGame game = new WordleGame(dictionary);
                writer.println("Игра создана");
                writer.println("Ответ: " + game.getAnswer());
                System.out.println("Слово загадано!");

                while (!game.isFinished()) {
                    System.out.println("Осталось ходов: " + game.getSteps());
                    System.out.println("Введите Ваше слово или нажмите Enter для подсказки: ");
                    String word = scanner.nextLine();
                    try {
                        String guess = game.makeMove(word);
                        if (word.isBlank()) {
                            word = game.getLastWord();
                            System.out.printf("Подсказка компьютера: %s \n", word);
                            System.out.printf("Совпадение с загаданным словом: %s \n", guess);
                        } else {
                            System.out.printf("Совпадение с загаданным словом: %s \n", guess);
                        }
                        writer.println("Сделан ход: " + word + " - " + guess);
                        writer.println("Осталось ходов: " + game.getSteps());
                    } catch (WordleGameException exception) {
                        System.out.println("Ошибка: " + exception.getMessage());
                        writer.println("Ошибка: " + exception.getMessage());
                        writer.println("Ввод: " + word);
                    }
                }
                if (game.isWin()) {
                    System.out.println("Поздравляю, Вы победили!");
                    System.out.println("Загаданное слово: " + game.getAnswer());
                    writer.println("Пользователь победил");
                } else {
                    System.out.println("Ходы закончились. Вы проиграли");
                    System.out.println("Загаданное слово: " + game.getAnswer());
                    writer.println("Пользователь проиграл");
                }
            } catch (Exception exception) {
                System.out.println("Произошла техническая ошибка");
                writer.println("Техническая ошибка: " + exception.getMessage());
                exception.printStackTrace(writer);
            }
        } catch (IOException exception) {
            System.out.println("Ошибка при работе с файлом:" + exception.getMessage());
        }
    }
}
