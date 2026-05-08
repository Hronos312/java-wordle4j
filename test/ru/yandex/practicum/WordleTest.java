package ru.yandex.practicum;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


class WordleTest {

    private WordleDictionary dictionary;

    @BeforeEach
    void setUp() {
        dictionary = new WordleDictionary(List.of("герой", "гонец", "чайка", "банка", "земля",
                        "ежики", "купол", "лампа", "город", "молот", "арена"));
    }

    @Test
    void dictionaryShouldReturnCorrectSize() {
        assertEquals(11, dictionary.size());
    }

    @Test
    void dictionaryShouldContainExistingWord() {
        assertTrue(dictionary.contains("герой"));
    }

    @Test
    void dictionaryShouldNotContainUnknownWord() {
        assertFalse(dictionary.contains("парта"));
    }

    @Test
    void dictionaryShouldNormalizeUpperCaseWordInContains() {
        assertTrue(dictionary.contains("ГЕРОЙ"));
    }

    @Test
    void dictionaryShouldNormalizeYoLetterInContains() {
        assertTrue(dictionary.contains("ЁЖИКИ"));
    }

    @Test
    void dictionaryShouldTrimSpacesInContains() {
        assertTrue(dictionary.contains("  герой  "));
    }

    @Test
    void dictionaryShouldReturnRandomWordFromDictionary() {
        String randomWord = dictionary.getRandomWord();
        assertTrue(dictionary.contains(randomWord));
    }

    @Test
    void gameShouldCreateWithSixSteps() {
        WordleGame game = new WordleGame(dictionary, "герой");
        assertEquals(6, game.getSteps());
        assertFalse(game.isWin());
        assertFalse(game.isFinished());
        assertEquals("герой", game.getAnswer());
    }

    @Test
    void gameShouldAnalyzeCorrectAnswerAsFivePluses() throws WordleGameException {
        WordleGame game = new WordleGame(dictionary, "герой");
        String result = game.makeMove("герой");
        assertEquals("+++++", result);
        assertTrue(game.isWin());
        assertTrue(game.isFinished());
        assertEquals(5, game.getSteps());
        assertEquals("герой", game.getLastWord());
    }

    @Test
    void gameShouldAnalyzeWordWithCorrectAndWrongPositions() throws WordleGameException {
        WordleGame game = new WordleGame(dictionary, "герой");
        String result = game.makeMove("гонец");
        assertEquals("+^-^-", result);
        assertFalse(game.isWin());
        assertFalse(game.isFinished());
        assertEquals(5, game.getSteps());
        assertEquals("гонец", game.getLastWord());
    }

    @Test
    void gameShouldNotDecreaseStepsAfterIncorrectLongWord() {
        WordleGame game = new WordleGame(dictionary, "герой");
        try {
            game.makeMove("кот");
        } catch (WordleGameException exception) {
        }
        assertEquals(6, game.getSteps());
        assertFalse(game.isFinished());
    }

    @Test
    void gameShouldNotDecreaseStepsAfterUnknownWord() {
        WordleGame game = new WordleGame(dictionary, "герой");
        try {
            game.makeMove("парта");
        } catch (WordleGameException exception) {
        }
        assertEquals(6, game.getSteps());
        assertFalse(game.isFinished());
    }

    @Test
    void gameShouldNormalizeUpperCaseInput() throws WordleGameException {
        WordleGame game = new WordleGame(dictionary, "герой");
        String result = game.makeMove("ГЕРОЙ");
        assertEquals("+++++", result);
        assertTrue(game.isWin());
        assertEquals("герой", game.getLastWord());
    }

    @Test
    void gameShouldNormalizeYoLetterInput() throws WordleGameException {
        WordleGame game = new WordleGame(dictionary, "ежики");
        String result = game.makeMove("ЁЖИКИ");
        assertEquals("+++++", result);
        assertTrue(game.isWin());
        assertEquals("ежики", game.getLastWord());
    }

    @Test
    void gameShouldFinishAfterSixFailedValidMoves() throws WordleGameException {
        WordleGame game = new WordleGame(dictionary, "герой");
        game.makeMove("гонец");
        game.makeMove("чайка");
        game.makeMove("банка");
        game.makeMove("земля");
        game.makeMove("ежики");
        game.makeMove("купол");
        assertTrue(game.isFinished());
        assertFalse(game.isWin());
        assertEquals(0, game.getSteps());
    }

    @Test
    void gameShouldUseHintAsMoveWhenInputIsBlank() throws WordleGameException {
        WordleGame game = new WordleGame(dictionary, "герой");
        String result = game.makeMove("");
        assertNotNull(result);
        assertEquals(5, result.length());
        assertEquals(5, game.getSteps());
        assertNotNull(game.getLastWord());
        assertTrue(dictionary.contains(game.getLastWord()));
    }
}
