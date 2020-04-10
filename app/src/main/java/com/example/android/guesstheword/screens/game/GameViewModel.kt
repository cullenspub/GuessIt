package com.example.android.guesstheword.screens.game

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import timber.log.Timber

class GameViewModel : ViewModel() {
    companion object {
        private const val DONE = 0L
        private const val ONE_SECOND = 1000L
        private const val COUNTDOWN_TIME = 60 * ONE_SECOND
        private const val CRITICAL_TIME: Long = 15
        private const val MORSE_DOT: Long = 100
        private const val MORSE_DASH: Long = 3 * MORSE_DOT
        private const val MORSE_PATTERN_SPACE: Long = 100
        private const val MORSE_LETTER_SPACE: Long = 3 * MORSE_DOT
        private const val MORSE_WORD_SPACE: Long = 7 * MORSE_DOT

        private object vibrationPatterns {
            val correct: LongArray = longArrayOf(
                    0,
                    MORSE_DASH, MORSE_PATTERN_SPACE,
                    MORSE_DOT, MORSE_PATTERN_SPACE,
                    MORSE_DASH, MORSE_PATTERN_SPACE,
                    MORSE_DASH, // Y
                    MORSE_LETTER_SPACE,
                    MORSE_DOT, // E
                    MORSE_LETTER_SPACE,
                    MORSE_DOT, MORSE_PATTERN_SPACE,
                    MORSE_DOT, MORSE_PATTERN_SPACE,
                    MORSE_DOT // S
            )
            val gameOver = longArrayOf(
                    0,
                    MORSE_DASH, MORSE_PATTERN_SPACE,
                    MORSE_DASH, MORSE_PATTERN_SPACE,
                    MORSE_DASH, MORSE_PATTERN_SPACE,
                    MORSE_DASH, MORSE_PATTERN_SPACE,
                    MORSE_DOT)
        }
    }

    private val timer: CountDownTimer

    // Buzzer Patterns
    enum class BuzzType(val pattern: LongArray) {
        correct(vibrationPatterns.correct),
        gameOver(vibrationPatterns.gameOver)
    }

    // The current word
    private var _word = MutableLiveData<String>()
    val word: LiveData<String>
        get() = _word

    // Time Remaining - raw seconds
    private var _remainingTime = MutableLiveData<Long>()
    val remainingTime: LiveData<Long>
        get() = _remainingTime

    // Time Remaining - String
    val remainingTimeString = Transformations.map(remainingTime) { DateUtils.formatElapsedTime(it)}

    // The current score
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    /*
     * LiveData used to communicate Events
     */

    // Game Over Event
    private var _gameFinished = MutableLiveData<Boolean>()
    val gameFinished: LiveData<Boolean>
        get() = _gameFinished

    // Critical Phase Event
    val criticalPhase = Transformations.map(remainingTime) { it < CRITICAL_TIME }

    // Correct Answer Event
    private var _correctAnswer = MutableLiveData<Boolean>()
    val correctAnswer: LiveData<Boolean>
        get() = _correctAnswer

    // The list of words - the front of the list is the next word to guess
    private lateinit var wordList: MutableList<String>

    init {
        resetList()
        nextWord()
        _score.value = 0
        timer = object : CountDownTimer(COUNTDOWN_TIME, ONE_SECOND) {
            override fun onTick(millisUntilFinished: Long) {
                _remainingTime.value =  millisUntilFinished / 1000
            }

            override fun onFinish() {
                _remainingTime.value = DONE
                gameFinished()
            }
        }

        timer.start()
    }

    override fun onCleared() {
        super.onCleared()
        Timber.i("I'm in onClear - GameViewModel destroyed")
        timer.cancel()
    }

    /**
     * Resets the list of words and randomizes the order
     */
    private fun resetList() {
        wordList = mutableListOf(
                "queen",
                "hospital",
                "basketball",
                "cat",
                "change",
                "snail",
                "soup",
                "calendar",
                "sad",
                "desk",
                "guitar",
                "home",
                "railway",
                "zebra",
                "jelly",
                "car",
                "crow",
                "trade",
                "bag",
                "roll",
                "bubble"
        )
        wordList.shuffle()
    }

    /**
     * Moves to the next word in the list
     */
    private fun nextWord() {
        //Select and remove a word from the list
        if (wordList.isEmpty()) {
            resetList()
        }
        _word.value = wordList.removeAt(0)
    }

    private fun gameFinished() {
        _gameFinished.value = true
    }

    /** Methods for buttons presses **/

    fun onSkip() {
        _score.value = (score.value)?.minus(1)
        nextWord()
    }

    fun onCorrect() {
        _correctAnswer.value = true
        _score.value = (score.value)?.plus(1)
        nextWord()
        _correctAnswer.value = false
    }
}
