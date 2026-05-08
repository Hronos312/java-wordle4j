package ru.yandex.practicum;

public class GameAlreadyFinishedException extends WordleGameException {
    public GameAlreadyFinishedException(String message) {
        super(message);
    }
}
