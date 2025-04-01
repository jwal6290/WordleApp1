package com.example.wordleapp



import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

object FourLetterWordList {
    private val words =
        listOf("Game", "Play", "Code", "Apps", "Hope", "Grin", "Cake", "Bake", "Late")

    fun getRandomFourLetterWord(): String {
        return words.random()
    }
}

class MainActivity : AppCompatActivity() {
    private lateinit var wordToGuess: String
    private var attempts = 0
    private val maxAttempts = 3

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val guessInput = findViewById<EditText>(R.id.guessInput)
        val submitButton = findViewById<Button>(R.id.submitButton)
        val resultText = findViewById<TextView>(R.id.resultText)

        wordToGuess = FourLetterWordList.getRandomFourLetterWord()

        submitButton.setOnClickListener {
            val userGuess = guessInput.text.toString().uppercase()
            if (userGuess.length != 4) {
                Toast.makeText(this, "Enter a 4-letter word", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            attempts++

            val feedback = checkGuess(userGuess, wordToGuess)
            resultText.text = "Guess $attempts: $userGuess → $feedback"

            if (feedback == "OOOO" || attempts == maxAttempts) {
                resultText.append("\nGame Over! The word was: $wordToGuess")
                submitButton.isEnabled = false
            }

            guessInput.text.clear()
            hideKeyboard()
        }
    }

    private fun checkGuess(guess: String, target: String): String {
        val result = StringBuilder()
        for (i in guess.indices) {
            result.append(
                when {
                    guess[i] == target[i] -> 'O' // Correct letter, correct position
                    guess[i] in target -> '+'  // Correct letter, wrong position
                    else -> 'X'          // Incorrect letter
                }
            )
        }
        return result.toString()
    }

    private fun hideKeyboard() {
        val view = currentFocus
        if (view != null) {
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }
}

