package com.app.lightenlife

class TestLoader(private val testId: Int) {

    private val availableTests = mapOf(
        1 to createStressAndMindfulnessTest(),
        2 to createPhysicalWellbeingTest(),
        3 to createEmotionalHealthTest()
    )

    fun getQuestions(): ArrayList<Tests> {
        return availableTests[testId] ?: createDefaultTest()
    }

    private fun createStressAndMindfulnessTest(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "How would you rate your current stress levels?",
                arrayListOf("Very low", "Low", "Moderate", "High", "Very high"),
                -1
            ),
            Tests(
                "How often do you practice mindfulness meditation?",
                arrayListOf("Never", "Rarely", "Sometimes", "Often", "Daily"),
                -1
            ),
            Tests(
                "How satisfied are you with your current sleep quality?",
                arrayListOf("Very unsatisfied", "Unsatisfied", "Neutral", "Satisfied", "Very satisfied"),
                -1
            ),
            Tests(
                "How often do you feel overwhelmed by your responsibilities?",
                arrayListOf("Never", "Rarely", "Sometimes", "Often", "Always"),
                -1
            ),
            Tests(
                "How would you describe your ability to focus throughout the day?",
                arrayListOf("Very poor", "Poor", "Moderate", "Good", "Excellent"),
                -1
            )
        )
    }

    private fun createPhysicalWellbeingTest(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "How often do you engage in physical activity?",
                arrayListOf("Never", "1-2 times a month", "1-2 times a week", "3-4 times a week", "Daily"),
                -1
            ),
            Tests(
                "How would you describe your overall energy levels?",
                arrayListOf("Very low", "Low", "Moderate", "High", "Very high"),
                -1
            ),
            Tests(
                "How would you rate the quality of your diet?",
                arrayListOf("Very poor", "Poor", "Average", "Good", "Excellent"),
                -1
            ),
            Tests(
                "How much water do you drink daily?",
                arrayListOf("Less than 1 liter", "1-2 liters", "2-3 liters", "3-4 liters", "More than 4 liters"),
                -1
            ),
            Tests(
                "How often do you experience physical discomfort or pain?",
                arrayListOf("Daily", "Several times a week", "Weekly", "Monthly", "Rarely or never"),
                -1
            )
        )
    }

    private fun createEmotionalHealthTest(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "How would you rate your overall emotional wellbeing?",
                arrayListOf("Very poor", "Poor", "Average", "Good", "Excellent"),
                -1
            ),
            Tests(
                "How often do you experience feelings of joy and contentment?",
                arrayListOf("Rarely", "Occasionally", "Sometimes", "Often", "Very often"),
                -1
            ),
            Tests(
                "How well do you manage negative emotions?",
                arrayListOf("Very poorly", "Poorly", "Moderately", "Well", "Very well"),
                -1
            ),
            Tests(
                "How often do you connect with friends or family?",
                arrayListOf("Rarely or never", "Monthly", "Weekly", "Several times a week", "Daily"),
                -1
            ),
            Tests(
                "How satisfied are you with your work-life balance?",
                arrayListOf("Very unsatisfied", "Unsatisfied", "Neutral", "Satisfied", "Very satisfied"),
                -1
            )
        )
    }

    private fun createDefaultTest(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "This test is not available. Please select a different test.",
                arrayListOf("Ok"),
                -1
            )
        )
    }
}