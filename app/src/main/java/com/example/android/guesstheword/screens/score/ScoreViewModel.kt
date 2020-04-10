package com.example.android.guesstheword.screens.score

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import timber.log.Timber

class ScoreViewModel(finalScore: Int) : ViewModel() {

    // Score of game
    private var _score = MutableLiveData<Int>()
    val score: LiveData<Int>
        get() = _score

    // Contrived event - play again
    private var _playAgain = MutableLiveData<Boolean>()
    val playAgain: LiveData<Boolean>
        get() = _playAgain


    init {
        _score.value = finalScore
        _playAgain.value = false
        Timber.d("Constructing the ViewModel with $finalScore")
    }

    fun playAgain() {
        _playAgain.value = true
    }

}