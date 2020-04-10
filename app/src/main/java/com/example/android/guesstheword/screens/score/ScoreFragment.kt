/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.android.guesstheword.screens.score

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.android.guesstheword.R
import com.example.android.guesstheword.databinding.ScoreFragmentBinding

/**
 * Fragment where the final score is shown, after the game is over
 */
class ScoreFragment : Fragment() {
    private lateinit var scoreViewModel: ScoreViewModel
    private lateinit var scoreViewModelFactory: ScoreViewModelFactory
    val scoreFragmentArgs by navArgs<ScoreFragmentArgs>()

    /*
     * Fragment Initialization
     */
    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        // Create ViewModel using factory and navigation argments
        val score = scoreFragmentArgs.score
        scoreViewModelFactory = ScoreViewModelFactory(scoreFragmentArgs.score)
        scoreViewModel = ViewModelProvider(this, scoreViewModelFactory).get(ScoreViewModel::class.java)

        // Inflate view and obtain an instance of the binding class.
        val binding: ScoreFragmentBinding = DataBindingUtil.inflate(
                inflater,
                R.layout.score_fragment,
                container,
                false
        )

        // Add the view model to the layout data binding
        binding.scoreViewModel = scoreViewModel

        // Setup observers for score and play again event
        scoreViewModel.score.observe(viewLifecycleOwner, Observer<Int> { score ->
            binding.scoreText.text = score.toString() } )

        scoreViewModel.playAgain.observe(viewLifecycleOwner, Observer<Boolean>{playAgain ->
            if (playAgain) restartGame()})

        // Get args using by navArgs property delegate
// replaced by scoreViewModel observer        binding.scoreText.text = scoreFragmentArgs.score.toString()
// replaced by event observer                 binding.playAgainButton.setOnClickListener { playAgain() }



        return binding.root
    }

    private fun restartGame() {
        findNavController().navigate(ScoreFragmentDirections.actionRestart())
    }
}
