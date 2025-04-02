package com.app.lightenlife

import android.content.res.ColorStateList
import android.graphics.Typeface
import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.view.animation.AnimationUtils
import android.graphics.drawable.Drawable

class questionsActivity : AppCompatActivity() {

    // Will be initialized with questions from TestLoader
    private lateinit var questionsList: ArrayList<Tests>

    private var currentQuestionIndex = 0
    private lateinit var questionTextView: TextView
    private lateinit var questionLabel: TextView
    private lateinit var optionsContainer: LinearLayout
    private lateinit var nextButton: Button
    private lateinit var previousButton: Button
    private lateinit var statsCard: CardView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_questions)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val testId = intent.getIntExtra(TestFragment.EXTRA_TEST_INDEX, 1)
        questionsList = TestLoader(testId).getQuestions()

        questionTextView = findViewById(R.id.textView5)
        questionLabel = findViewById(R.id.QLable)
        optionsContainer = findViewById(R.id.question_holder)
        statsCard = findViewById(R.id.stat_holder)

        setupNavigationButtons()

        displayQuestion(currentQuestionIndex)
    }

    private fun setupNavigationButtons() {
        // Add a container for navigation buttons
        val navigationLayout = LinearLayout(this).apply {
            orientation = LinearLayout.HORIZONTAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 16, 16, 16)
        }

        // Create Previous button
        previousButton = Button(this).apply {
            text = "Previous"
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginEnd = 8
            }
            setOnClickListener {
                if (currentQuestionIndex > 0) {
                    currentQuestionIndex--
                    displayQuestion(currentQuestionIndex)
                }
            }
            isEnabled = false // Disabled for first question
        }

        // Create Next button

        nextButton = Button(this).apply {
            text = "Next"
            layoutParams = LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1f
            ).apply {
                marginStart = 8
            }
            setOnClickListener {
                // Get CardView first, then get RadioGroup from it
                val cardView = optionsContainer.getChildAt(0) as CardView
                val radioGroup = cardView.getChildAt(0) as RadioGroup
                val selectedId = radioGroup.checkedRadioButtonId

                if (selectedId != -1) {
                    // Save the answer
                    questionsList[currentQuestionIndex].answers = selectedId

                    // Move to next question or finish
                    if (currentQuestionIndex < questionsList.size - 1) {
                        currentQuestionIndex++
                        displayQuestion(currentQuestionIndex)
                    } else {
                        // All questions answered, show results
                        showResults()
                    }
                } else {
                    Toast.makeText(this@questionsActivity, "Please select an option", Toast.LENGTH_SHORT).show()
                }
            }
        }

        navigationLayout.addView(previousButton)
        navigationLayout.addView(nextButton)

        // Add the navigation layout below the stats card
        val mainLayout = findViewById<LinearLayout>(R.id.navigation_buttons)
        mainLayout.addView(navigationLayout)
    }

    private fun displayQuestion(index: Int) {
        // Update question number and text with improved styling
        questionLabel.apply {
            text = "Question ${index + 1}"
            setTextColor(ContextCompat.getColor(this@questionsActivity, R.color.light_blue))
            setTypeface(null, Typeface.BOLD)
            textSize = 18f
        }

        questionTextView.apply {
            text = questionsList[index].questions
            setTextColor(ContextCompat.getColor(this@questionsActivity, R.color.light_gray))
            textSize = 16f
            setPadding(16, 8, 16, 24)
        }

        // Style navigation buttons
        previousButton.apply {
            isEnabled = index > 0
            setBackgroundResource(R.drawable.signupbutton)
            setTextColor(ContextCompat.getColor(this@questionsActivity, R.color.white))
            elevation = 4f
        }

        nextButton.apply {
            text = if (index == questionsList.size - 1) "Finish" else "Next"
            setBackgroundResource(R.drawable.signupbutton)
            setTextColor(ContextCompat.getColor(this@questionsActivity, R.color.white))
            elevation = 4f
        }

        // Clear previous options
        optionsContainer.removeAllViews()

        // Add card container for options
        val cardView = CardView(this).apply {
            radius = resources.getDimension(androidx.cardview.R.dimen.cardview_default_radius)
            cardElevation = resources.getDimension(androidx.cardview.R.dimen.cardview_default_elevation)
            setCardBackgroundColor(ContextCompat.getColor(this@questionsActivity, R.color.white))
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            ).apply {
                setMargins(16, 8, 16, 8)
            }
        }

        // Create radio group for options with improved styling
        val radioGroup = RadioGroup(this).apply {
            orientation = RadioGroup.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(16, 12, 16, 12)
        }

        // Add radio buttons for each option with improved styling
        questionsList[index].options.forEachIndexed { optionIndex, optionText ->
            val radioButton = RadioButton(this).apply {
                id = optionIndex
                text = optionText
                textSize = 16f
                setTextColor(ContextCompat.getColor(this@questionsActivity, R.color.dark_gray))
                buttonTintList = ColorStateList.valueOf(
                    ContextCompat.getColor(this@questionsActivity, R.color.light_blue))

                // Add ripple effect
                background = getSelectableItemBackground()

                // Add spacing between options
                layoutParams = RadioGroup.LayoutParams(
                    RadioGroup.LayoutParams.MATCH_PARENT,
                    RadioGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    setMargins(0, 8, 0, 8)
                }

                setPadding(8, 16, 8, 16)
            }
            radioGroup.addView(radioButton)
        }

        // Set the previously selected answer if any
        if (questionsList[index].answers != -1) {
            radioGroup.check(questionsList[index].answers)
        }

        // Add radio group to card, then add card to container
        cardView.addView(radioGroup)
        optionsContainer.addView(cardView)

        // Add subtle animation
        val animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        optionsContainer.startAnimation(animation)
    }

    // Helper method to get selectable item background with ripple effect
    private fun getSelectableItemBackground(): Drawable {
        val outValue = TypedValue()
        theme.resolveAttribute(android.R.attr.selectableItemBackground, outValue, true)
        return ContextCompat.getDrawable(this, outValue.resourceId)!!
    }

    private fun showResults() {
        // Create a results view
        val inflater = LayoutInflater.from(this)
        val resultsView = inflater.inflate(R.layout.results_layout, null)

        // Replace current content with results
        val mainContainer = findViewById<LinearLayout>(R.id.question_container)
        mainContainer.removeAllViews()
        mainContainer.addView(resultsView)

        // Generate and display results
        val resultsTextView = resultsView.findViewById<TextView>(R.id.results_text)
        val resultsSummary = StringBuilder("Your Answers:\n\n")

        questionsList.forEachIndexed { index, test ->
            resultsSummary.append("Q${index + 1}: ${test.questions}\n")
            if (test.answers != -1) {
                resultsSummary.append("Your answer: ${test.options[test.answers]}\n\n")
            } else {
                resultsSummary.append("Not answered\n\n")
            }
        }

        resultsTextView.text = resultsSummary.toString()

        // Add a button to restart or go back
        val restartButton = resultsView.findViewById<Button>(R.id.restart_button)
        restartButton.setOnClickListener {
            finish()
        }
    }

    fun goBack(v: View){
        finish()
    }
}