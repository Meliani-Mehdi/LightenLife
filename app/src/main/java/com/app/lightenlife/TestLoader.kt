package com.app.lightenlife
import java.time.LocalTime
import java.time.Duration
import kotlin.math.min

class TestLoader(private val testId: Int) {

    private val availableTests = mapOf(
        1 to createTaylorManifestAnxietyScaleTest(),
        2 to createPCL5PTSDChecklist(),
        3 to createTaylorAnxietyScale(),
        4 to createSpenceAnxietyScale(),
        5 to createBeckDepressionInventory(),
        6 to createPittsburghSleepQualityIndex().second,
        7 to createPerceivedStressScale()

    )

    fun getQuestions(): ArrayList<Tests> {
        return availableTests[testId] ?: createDefaultTest()
    }

    fun createPerceivedStressScale(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "In the last month, how often have you been upset because of something that happened unexpectedly?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you felt that you were unable to control the important things in your life?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you felt nervous and stressed?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you felt confident about your ability to handle your personal problems?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you felt that things were going your way?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you found that you could not cope with all the things that you had to do?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you been able to control irritations in your life?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you felt that you were on top of things?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you been angered because of things that were outside of your control?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            ),
            Tests(
                "In the last month, how often have you felt difficulties were piling up so high that you could not overcome them?",
                arrayListOf("Never", "Almost Never", "Sometimes", "Fairly Often", "Very Often"),
                -1
            )
        )
    }

    // Function to calculate and interpret PSS-10 score
    fun interpretPSSScore(responses: ArrayList<Tests>): Pair<Int, String> {
        if (responses.size != 10 || responses.any { it.answers == -1 }) {
            return Pair(0, "Incomplete test")
        }

        var totalScore = 0

        // Items 1, 2, 3, 6, 9, and 10 are scored normally (0-4)
        val directScoredItems = listOf(0, 1, 2, 5, 8, 9)
        // Items 4, 5, 7, and 8 are reverse scored (4-0)
        val reverseScoredItems = listOf(3, 4, 6, 7)

        for (i in responses.indices) {
            val score = when {
                directScoredItems.contains(i) -> responses[i].answers
                reverseScoredItems.contains(i) -> 4 - responses[i].answers
                else -> 0 // Should never happen
            }
            totalScore += score
        }

        // Interpret the score
        val interpretation = when {
            totalScore <= 13 -> "Low perceived stress"
            totalScore <= 26 -> "Moderate perceived stress"
            else -> "High perceived stress"
        }

        return Pair(totalScore, interpretation)
    }

    data class PSQITimeQuestion(
        val question: String,
        var answer: String = ""
    )

    data class PSQIResult(
        val totalScore: Int,
        val componentScores: Map<String, Int>,
        val sleepQuality: String
    )

    fun createPittsburghSleepQualityIndex(): Pair<ArrayList<PSQITimeQuestion>, ArrayList<Tests>> {
        // Time-based questions that need text input
        val timeQuestions = arrayListOf(
            PSQITimeQuestion("What time do you usually go to bed at night?"),
            PSQITimeQuestion("How long (in minutes) does it usually take you to fall asleep?"),
            PSQITimeQuestion("What time do you usually get up in the morning?"),
            PSQITimeQuestion("How many hours of actual sleep do you get at night?")
        )

        // Multiple choice questions
        val multipleChoiceQuestions = arrayListOf(
            Tests(
                "During the past month, how often have you had trouble sleeping because you cannot fall asleep within 30 minutes?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble sleeping because you wake up in the middle of the night or early morning?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble sleeping because you have to get up to use the bathroom?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble sleeping because you cannot breathe comfortably?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble sleeping because you cough or snore loudly?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble sleeping because you feel too cold?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble sleeping because you feel too hot?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble sleeping because you had bad dreams?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble sleeping because you have pain?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how would you rate your overall sleep quality?",
                arrayListOf(
                    "Very good",
                    "Fairly good",
                    "Fairly bad",
                    "Very bad"
                )
            ),
            Tests(
                "During the past month, how often have you taken medicine (prescribed or over the counter) to help you sleep?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how often have you had trouble staying awake while driving, eating meals, or engaging in social activities?",
                arrayListOf(
                    "Not during the past month",
                    "Less than once a week",
                    "Once or twice a week",
                    "Three or more times a week"
                )
            ),
            Tests(
                "During the past month, how much of a problem has it been for you to keep up enthusiasm to get things done?",
                arrayListOf(
                    "No problem at all",
                    "Only a very slight problem",
                    "Somewhat of a problem",
                    "A very big problem"
                )
            )
        )

        return Pair(timeQuestions, multipleChoiceQuestions)
    }

    fun calculatePSQIScore(timeAnswers: ArrayList<PSQITimeQuestion>, multipleChoiceAnswers: ArrayList<Tests>): PSQIResult {
        try {
            // Component 1: Subjective sleep quality (question 9 in our array, index 9)
            val component1Score = multipleChoiceAnswers[9].answers

            // Component 2: Sleep latency
            // Combines question about time to fall asleep and question about trouble falling asleep
            var sleepLatencyScore = 0
            val timeToFallAsleep = timeAnswers[1].answer.toIntOrNull() ?: 0
            sleepLatencyScore += when {
                timeToFallAsleep <= 15 -> 0
                timeToFallAsleep <= 30 -> 1
                timeToFallAsleep <= 60 -> 2
                else -> 3
            }

            // Add score from question about trouble falling asleep (question 0 in our array)
            val troubleFallingAsleepScore = multipleChoiceAnswers[0].answers
            sleepLatencyScore += troubleFallingAsleepScore

            // Calculate final component 2 score (0-6 scale to 0-3 scale)
            val component2Score = when {
                sleepLatencyScore == 0 -> 0
                sleepLatencyScore in 1..2 -> 1
                sleepLatencyScore in 3..4 -> 2
                else -> 3
            }

            // Component 3: Sleep duration
            val hoursOfSleep = timeAnswers[3].answer.toFloatOrNull() ?: 0f
            val component3Score = when {
                hoursOfSleep > 7 -> 0
                hoursOfSleep >= 6 -> 1
                hoursOfSleep >= 5 -> 2
                else -> 3
            }

            // Component 4: Sleep efficiency
            val sleepEfficiency = calculateSleepEfficiency(timeAnswers)
            val component4Score = when {
                sleepEfficiency >= 85 -> 0
                sleepEfficiency >= 75 -> 1
                sleepEfficiency >= 65 -> 2
                else -> 3
            }

            // Component 5: Sleep disturbances
            // Sum of scores for sleep disturbance questions (1-8)
            var sleepDisturbanceSum = 0
            for (i in 1..8) {
                sleepDisturbanceSum += multipleChoiceAnswers[i].answers
            }

            val component5Score = when {
                sleepDisturbanceSum == 0 -> 0
                sleepDisturbanceSum in 1..9 -> 1
                sleepDisturbanceSum in 10..18 -> 2
                else -> 3
            }

            // Component 6: Use of sleep medication (question 10 in our array)
            val component6Score = multipleChoiceAnswers[10].answers

            // Component 7: Daytime dysfunction
            // Combines questions about staying awake and keeping up enthusiasm (questions 11 and 12)
            val stayingAwakeScore = multipleChoiceAnswers[11].answers
            val enthusiasmScore = multipleChoiceAnswers[12].answers
            val daytimeDysfunctionSum = stayingAwakeScore + enthusiasmScore

            val component7Score = when {
                daytimeDysfunctionSum == 0 -> 0
                daytimeDysfunctionSum in 1..2 -> 1
                daytimeDysfunctionSum in 3..4 -> 2
                else -> 3
            }

            // Create component scores map
            val componentScores = mapOf(
                "Sleep Quality" to component1Score,
                "Sleep Latency" to component2Score,
                "Sleep Duration" to component3Score,
                "Sleep Efficiency" to component4Score,
                "Sleep Disturbances" to component5Score,
                "Sleep Medication" to component6Score,
                "Daytime Dysfunction" to component7Score
            )

            // Calculate total PSQI score (sum of all 7 components)
            val totalScore = componentScores.values.sum()

            // Interpret the score
            val sleepQuality = when {
                totalScore <= 5 -> "Good sleep quality"
                else -> "Poor sleep quality"
            }

            return PSQIResult(totalScore, componentScores, sleepQuality)
        } catch (e: Exception) {
            // Return default values if calculation fails
            return PSQIResult(
                0,
                mapOf(
                    "Sleep Quality" to 0,
                    "Sleep Latency" to 0,
                    "Sleep Duration" to 0,
                    "Sleep Efficiency" to 0,
                    "Sleep Disturbances" to 0,
                    "Sleep Medication" to 0,
                    "Daytime Dysfunction" to 0
                ),
                "Error calculating score"
            )
        }
    }

    // Helper function to calculate sleep efficiency
    private fun calculateSleepEfficiency(timeAnswers: ArrayList<PSQITimeQuestion>): Float {
        try {
            // Extract bedtime and wake time
            val bedtimeStr = timeAnswers[0].answer
            val wakeTimeStr = timeAnswers[2].answer
            val actualSleepHours = timeAnswers[3].answer.toFloatOrNull() ?: 0f

            // Parse time strings (assuming format like "23:00" or "7:30")
            val bedtimeParts = bedtimeStr.split(":")
            val waketimeParts = wakeTimeStr.split(":")

            if (bedtimeParts.size < 2 || waketimeParts.size < 2) {
                return 0f // Return default if time format is invalid
            }

            val bedtimeHour = bedtimeParts[0].toIntOrNull() ?: 0
            val bedtimeMinute = bedtimeParts[1].toIntOrNull() ?: 0
            val waketimeHour = waketimeParts[0].toIntOrNull() ?: 0
            val waketimeMinute = waketimeParts[1].toIntOrNull() ?: 0

            // Create LocalTime objects
            val bedtime = LocalTime.of(bedtimeHour, bedtimeMinute)
            val waketime = LocalTime.of(waketimeHour, waketimeMinute)

            // Calculate time in bed (in hours)
            var timeInBedMinutes = 0L
            if (waketime.isAfter(bedtime)) {
                // Normal case (e.g., bed at 22:00, wake at 6:00)
                timeInBedMinutes = Duration.between(bedtime, waketime).toMinutes()
            } else {
                // Overnight case (e.g., bed at 23:00, wake at 7:00)
                val midnightToBedtime = Duration.between(LocalTime.MIDNIGHT, bedtime).toMinutes()
                val waketimeToMidnight = Duration.between(waketime, LocalTime.MIDNIGHT).toMinutes()
                timeInBedMinutes = 24 * 60 - midnightToBedtime - waketimeToMidnight
            }

            val timeInBedHours = timeInBedMinutes / 60f

            // Calculate sleep efficiency (actual sleep time / time in bed * 100)
            return if (timeInBedHours > 0) {
                (actualSleepHours / timeInBedHours) * 100
            } else {
                0f
            }
        } catch (e: Exception) {
            return 0f // Return default if calculation fails
        }
    }

    private fun createBeckDepressionInventory(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "Sadness",
                arrayListOf(
                    "I do not feel sad",
                    "I feel sad",
                    "I am sad all the time and I can't snap out of it",
                    "I am so sad or unhappy that I can't stand it"
                )
            ),
            Tests(
                "Pessimism",
                arrayListOf(
                    "I am not particularly discouraged about the future",
                    "I feel discouraged about the future",
                    "I feel I have nothing to look forward to",
                    "I feel that the future is hopeless and that things cannot improve"
                )
            ),
            Tests(
                "Past Failure",
                arrayListOf(
                    "I do not feel like a failure",
                    "I feel I have failed more than the average person",
                    "As I look back on my life, all I can see is a lot of failures",
                    "I feel I am a complete failure as a person"
                )
            ),
            Tests(
                "Loss of Pleasure",
                arrayListOf(
                    "I get as much satisfaction out of things as I used to",
                    "I don't enjoy things the way I used to",
                    "I don't get real satisfaction out of anything anymore",
                    "I am dissatisfied or bored with everything"
                )
            ),
            Tests(
                "Guilty Feelings",
                arrayListOf(
                    "I don't feel particularly guilty",
                    "I feel guilty a good part of the time",
                    "I feel quite guilty most of the time",
                    "I feel guilty all of the time"
                )
            ),
            Tests(
                "Punishment Feelings",
                arrayListOf(
                    "I don't feel I am being punished",
                    "I feel I may be punished",
                    "I expect to be punished",
                    "I feel I am being punished"
                )
            ),
            Tests(
                "Self-Dislike",
                arrayListOf(
                    "I don't feel disappointed in myself",
                    "I am disappointed in myself",
                    "I am disgusted with myself",
                    "I hate myself"
                )
            ),
            Tests(
                "Self-Criticalness",
                arrayListOf(
                    "I don't feel I am any worse than anybody else",
                    "I criticize myself for my weaknesses or mistakes",
                    "I blame myself all the time for my faults",
                    "I blame myself for everything bad that happens"
                )
            ),
            Tests(
                "Suicidal Thoughts",
                arrayListOf(
                    "I don't have any thoughts of killing myself",
                    "I have thoughts of killing myself, but I would not carry them out",
                    "I would like to kill myself",
                    "I would kill myself if I had the chance"
                )
            ),
            Tests(
                "Crying",
                arrayListOf(
                    "I don't cry any more than usual",
                    "I cry more now than I used to",
                    "I cry all the time now",
                    "I used to be able to cry, but now I can't cry even though I want to"
                )
            ),
            Tests(
                "Agitation",
                arrayListOf(
                    "I am no more irritated than I ever am",
                    "I get annoyed or irritated more easily than I used to",
                    "I feel irritated all the time now",
                    "I don't get irritated at all by the things that used to irritate me"
                )
            ),
            Tests(
                "Loss of Interest",
                arrayListOf(
                    "I have not lost interest in other people",
                    "I am less interested in other people than I used to be",
                    "I have lost most of my interest in other people",
                    "I have lost all of my interest in other people"
                )
            ),
            Tests(
                "Indecisiveness",
                arrayListOf(
                    "I make decisions about as well as I ever could",
                    "I put off making decisions more than I used to",
                    "I have greater difficulty in making decisions than before",
                    "I can't make decisions at all anymore"
                )
            ),
            Tests(
                "Worthlessness",
                arrayListOf(
                    "I don't feel I look any worse than I used to",
                    "I am worried that I am looking old or unattractive",
                    "I feel that there are permanent changes in my appearance that make me look unattractive",
                    "I believe that I look ugly"
                )
            ),
            Tests(
                "Loss of Energy",
                arrayListOf(
                    "I can work about as well as before",
                    "It takes an extra effort to get started at doing something",
                    "I have to push myself very hard to do anything",
                    "I can't do any work at all"
                )
            ),
            Tests(
                "Changes in Sleeping Pattern",
                arrayListOf(
                    "I can sleep as well as usual",
                    "I don't sleep as well as I used to",
                    "I wake up 1-2 hours earlier than usual and find it hard to get back to sleep",
                    "I wake up several hours earlier than I used to and cannot get back to sleep"
                )
            ),
            Tests(
                "Tiredness or Fatigue",
                arrayListOf(
                    "I don't get more tired than usual",
                    "I get tired more easily than I used to",
                    "I get tired from doing almost anything",
                    "I am too tired to do anything"
                )
            ),
            Tests(
                "Changes in Appetite",
                arrayListOf(
                    "My appetite is no worse than usual",
                    "My appetite is not as good as it used to be",
                    "My appetite is much worse now",
                    "I have no appetite at all anymore"
                )
            ),
            Tests(
                "Weight Loss",
                arrayListOf(
                    "I haven't lost much weight, if any, lately",
                    "I have lost more than 5 pounds",
                    "I have lost more than 10 pounds",
                    "I have lost more than 15 pounds"
                )
            ),
            Tests(
                "Somatic Preoccupation",
                arrayListOf(
                    "I am no more worried about my health than usual",
                    "I am worried about physical problems such as aches and pains, or upset stomach, or constipation",
                    "I am very worried about physical problems and it's hard to think of much else",
                    "I am so worried about my physical problems that I cannot think about anything else"
                )
            ),
            Tests(
                "Loss of Interest in Sex",
                arrayListOf(
                    "I have not noticed any recent change in my interest in sex",
                    "I am less interested in sex than I used to be",
                    "I am much less interested in sex now",
                    "I have lost interest in sex completely"
                )
            )
        )
    }

    private fun calculateBDIScore(responses: ArrayList<Tests>): Pair<Int, String> {
        var totalScore = 0

        // Calculate total score by summing the selected answers (0-3 for each question)
        for (test in responses) {
            if (test.answers >= 0) {
                totalScore += test.answers
            }
        }

        // Interpret the score according to BDI guidelines
        val interpretation = when (totalScore) {
            in 0..9 -> "No depression"
            in 10..18 -> "Mild depression"
            in 19..29 -> "Moderate depression"
            in 30..63 -> "Severe depression"
            else -> "Invalid score"
        }

        return Pair(totalScore, interpretation)
    }


    private fun createSpenceAnxietyScale(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "I feel tense about some things that are not clear to me",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I am afraid of darkness at night",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "When I face a problem, I feel pain in my stomach",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel afraid",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel scared when I am alone at home",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel fearful when going to exams",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel afraid when I use the bathroom outside my home",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel distressed when I'm away from my parents",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I'm afraid of looking stupid in front of others",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I get tense when I don't complete my homework perfectly",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I am well-liked among peers my age",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I have thoughts that something bad will happen to my family",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I experience episodes of shortness of breath without an obvious reason",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I keep checking things I've done several times (like making sure the lights are off, the house door is locked)",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel scared when I sleep alone",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I find it difficult to go to school in the morning because I feel scared and tense",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I am good at sports",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I am afraid of dogs and cats",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I can't overcome some silly or bad thoughts in my head",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "When I have a problem, my heart beats strongly",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I suddenly experience trembling throughout my body without a reason",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel that bad things will happen to me",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I fear going to the dentist or the doctor",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel trembling when I'm in trouble",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I'm afraid of high places and riding elevators",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I am a good person",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "To stop bad things from happening to me, I think about things like certain numbers and words",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I am scared of traveling in cars or buses",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I worry about what people think of me",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I'm afraid of crowded places",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel happy",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I suddenly feel intense fear without reason",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I am afraid of insects",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I suddenly feel dizzy without reason",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I fear standing in class and speaking in front of my classmates",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "My heart starts to beat rapidly for no reason",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel frightened of something that doesn't exist",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I love myself",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I'm afraid of tight spaces like small rooms",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I do things repeatedly (like washing hands, cleaning, arranging things in a certain way)",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I get tense from the silly and bad thoughts and images in my head",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I do the right things to avoid bad things happening to me",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I am proud of my school work",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "I feel frightened at night when I'm outside the house",
                arrayListOf("No", "Sometimes", "Usually", "Always"),
                -1
            ),
            Tests(
                "Are there other things you're afraid of?",
                arrayListOf("No", "Yes"),
                -1
            )
        )
    }

    // Function to score the Spence Anxiety Scale
    private fun scoreSpenceAnxietyScale(responses: List<Int>): SpenceAnxietyResult {
        // Score values: No=0, Sometimes=1, Usually=2, Always=3

        // Initialize subscale scores
        val obsessiveCompulsive = calculateSubScore(responses, listOf(13, 18, 26, 39, 40, 41))
        val socialFears = calculateSubScore(responses, listOf(5, 6, 8, 9, 28, 34))
        val panicAgoraphobia = calculateSubScore(responses, listOf(12, 19, 27, 29, 31, 33, 35, 36, 38))
        val separationAnxiety = calculateSubScore(responses, listOf(4, 7, 11, 14, 15, 43))
        val physicalInjuryFears = calculateSubScore(responses, listOf(1, 17, 22, 24, 32))
        val generalizedAnxiety = calculateSubScore(responses, listOf(0, 2, 3, 19, 21, 23))

        // Calculate total anxiety score (all items except positive filler items)
        val totalScore = responses.filterIndexed { index, _ ->
            index !in listOf(10, 16, 25, 30, 37, 42)
        }.sum()

        // Determine anxiety level
        val anxietyLevel = if (totalScore <= 60) "Normal" else "High"

        return SpenceAnxietyResult(
            obsessiveCompulsive = obsessiveCompulsive,
            socialFears = socialFears,
            panicAgoraphobia = panicAgoraphobia,
            separationAnxiety = separationAnxiety,
            physicalInjuryFears = physicalInjuryFears,
            generalizedAnxiety = generalizedAnxiety,
            totalScore = totalScore,
            anxietyLevel = anxietyLevel
        )
    }

    // Helper function to calculate subscale scores
    private fun calculateSubScore(responses: List<Int>, indices: List<Int>): Int {
        return indices.sumOf { responses[it] }
    }

    // Result data class for Spence Anxiety Scale
    data class SpenceAnxietyResult(
        val obsessiveCompulsive: Int,
        val socialFears: Int,
        val panicAgoraphobia: Int,
        val separationAnxiety: Int,
        val physicalInjuryFears: Int,
        val generalizedAnxiety: Int,
        val totalScore: Int,
        val anxietyLevel: String
    )



    private fun createTaylorAnxietyScale(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "My sleep is disturbed and interrupted",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I've experienced times when I couldn't sleep due to anxiety",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "My fears are much less compared to my friends",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I think I am more nervous than most people",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I experience disturbing dreams (or nightmares) every few nights",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I sometimes have stomach troubles",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often notice my hands trembling when I try to do something",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I sometimes suffer from episodes of diarrhea",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Work matters and workers cause me anxiety",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I experience episodes of nausea (or lightheadedness)",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often fear that my face will redden from embarrassment",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I feel hungry almost all the time",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I trust myself a lot",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I get tired quickly",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Waiting makes me nervous",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I feel so excited that sleep becomes difficult for me",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am usually calm",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I go through periods of instability to the extent that I cannot sit for long in my seat",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I don't feel happy most of the time",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "It's easy for me to focus my mind on a task",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I feel anxious about something or someone almost all the time",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I don't fear crises and hardships",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I wish I could be as happy as others appear to be",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often find myself anxious about something",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Sometimes I definitely feel that I am of no use",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Sometimes I feel like I'm falling apart",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I sweat easily even on cold days",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Life is difficult for me most of the time",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I'm not worried about potential bad luck I might encounter",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am unusually sensitive",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I've noticed that my heart beats strongly and sometimes I get agitated",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I don't cry easily",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I've been afraid of things or people I know cannot harm me",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am highly susceptible to being affected by events",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often get headaches",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I must admit that I've worried about things of no value",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I cannot focus my thinking on one thing",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I don't get confused easily",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Sometimes I think I'm not good at all",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am a very tense person",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Sometimes I get so confused that sweat drips from me in a way that really bothers me",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "My face reddens with embarrassment to a greater degree when I talk to others",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am more sensitive than most people",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I've experienced times when I felt difficulties pile up to the extent that I couldn't overcome them",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am extremely tense when performing a task",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "My hands and feet are usually cold",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Sometimes I dream about things I prefer to keep to myself",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I don't lack self-confidence",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I sometimes suffer from constipation",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "My face never reddens from embarrassment",
                arrayListOf("Yes", "No"),
                -1
            )
        )
    }

    // Function to score the Taylor Anxiety Scale
    private fun scoreTaylorAnxietyScale(responses: List<Int>): AnxietyResult {
        var totalScore = 0

        // Process each response
        for (i in responses.indices) {
            // For regular items, "Yes" (index 0) adds 1 point
            if (i !in listOf(2, 12, 16, 19, 21, 28, 31, 37, 47, 49)) {
                if (responses[i] == 0) totalScore += 1 // "Yes" is index 0
            }
            // For reverse scored items, "No" (index 1) adds 1 point
            else {
                if (responses[i] == 1) totalScore += 1 // "No" is index 1
            }
        }

        // Determine anxiety level based on score
        val anxietyLevel = when {
            totalScore in 0..16 -> "Very Low Anxiety"
            totalScore in 17..19 -> "Low Anxiety (Normal)"
            totalScore in 20..24 -> "Moderate Anxiety"
            totalScore in 25..29 -> "Above Average Anxiety"
            else -> "High Anxiety" // 30+
        }

        return AnxietyResult(totalScore, anxietyLevel)
    }

    // Result data class
    data class AnxietyResult(
        val score: Int,
        val anxietyLevel: String
    )


    fun createPCL5PTSDChecklist(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "Repeated, disturbing, and unwanted memories of the stressful experience.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Repeated disturbing dreams of the stressful experience.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Suddenly feeling or acting as if the stressful experience were happening again (as if you were actually back there reliving it).",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Feeling very upset when something reminded you of the stressful experience.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Having strong physical reactions when something reminded you of the stressful experience (for example, heart pounding, trouble breathing, sweating).",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Avoiding memories, thoughts, or feelings related to the stressful experience.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Avoiding external reminders of the stressful experience (for example, people, places, conversations, activities, objects, or situations).",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Trouble remembering important parts of the stressful experience.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Having strong negative beliefs about yourself, other people, or the world (for example, having thoughts such as: I am bad, there is something seriously wrong with me, no one can be trusted, the world is completely dangerous).",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Blaming yourself or someone else for the stressful experience or what happened after it.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Having strong negative feelings such as fear, horror, anger, guilt, or shame.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Loss of interest in activities that you used to enjoy.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Feeling distant or cut off from other people.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Trouble experiencing positive feelings (for example, being unable to feel happiness or having loving feelings for people close to you).",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Irritable behavior, angry outbursts, or acting aggressively.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Taking too many risks or doing things that could cause you harm.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Being \"superalert\" or watchful or on guard.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Feeling jumpy or easily startled.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Having difficulty concentrating.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            ),
            Tests(
                "Trouble falling or staying asleep.",
                arrayListOf("Not at all", "A little bit", "Moderately", "Quite a bit", "Extremely"),
                -1
            )
        )
    }

    // Function to interpret the PCL-5 PTSD score
    fun interpretPCL5Score(score: Int): String {
        return when {
            score < 33 -> "Below threshold for probable PTSD diagnosis"
            score >= 33 -> "At or above threshold for probable PTSD diagnosis"
            else -> "Invalid score"
        }
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
    fun createTaylorManifestAnxietyScaleTest(): ArrayList<Tests> {
        return arrayListOf(
            Tests(
                "My sleep is disturbed and interrupted.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "My fears are much more than my friends'.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I experience days where I can't sleep due to anxiety.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I believe I am more nervous than others.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I suffer from disturbing nightmares most nights.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often suffer from stomach pains.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I notice my hands tremble often when I do any work.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I frequently suffer from diarrhea.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Work and money matters provoke my anxiety.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I experience bouts of nausea.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I fear my face will turn red from embarrassment.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I always feel hungry.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I don't trust myself.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I get tired easily.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Waiting makes me very nervous.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often feel so tense that I can't sleep.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am usually calm and nothing excites me.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I experience periods of tension where I can't sit for long.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am unhappy all the time.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "It's very difficult for me to concentrate during work.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I always feel anxious without reason.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "When I see a fight, I walk away from it.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I wish I could be as happy as others.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I'm always overwhelmed with anxiety about vague things.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I feel I am useless.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often feel like I will explode from distress or boredom.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I sweat easily even in cold days.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "Life is tiring and troublesome for me.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am always busy worrying about the unknown.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I usually feel ashamed of myself.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often feel my heart beats rapidly.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I cry easily.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I fear people or things that cannot harm me.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am strongly affected by events.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I suffer from headaches frequently.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I feel anxious about matters or things of no value.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I cannot concentrate on one thing.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "It's very easy for me to get confused and make mistakes when I do something easily.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I feel I am useless; I sometimes think I am not good for anything.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am a very tense person.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "When I get confused sometimes, I sweat and it bothers me.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "My face turns red from embarrassment when I talk to others.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I am more sensitive than others.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I've had nervous times that I couldn't overcome.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I feel tense while carrying out my usual activities.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "My hands and feet are usually cold.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I often dream of better things that I don't tell anyone about.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I lack self-confidence.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "I rarely have constipation issues that bother me.",
                arrayListOf("Yes", "No"),
                -1
            ),
            Tests(
                "My face turns red from embarrassment.",
                arrayListOf("Yes", "No"),
                -1
            )
        )
    }

    // Function to interpret the Taylor Manifest Anxiety Scale score
    fun interpretTaylorAnxietyScore(score: Int): String {
        return when {
            score in 0..16 -> "Free from anxiety"
            score in 17..20 -> "Mild anxiety"
            score in 21..26 -> "Moderate anxiety"
            score in 27..29 -> "Severe anxiety"
            score >= 30 -> "Very severe anxiety"
            else -> "Invalid score"
        }
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