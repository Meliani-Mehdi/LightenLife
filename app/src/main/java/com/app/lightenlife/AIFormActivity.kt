package com.app.lightenlife

import StressDepressionClassifier
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity

class AIFormActivity : AppCompatActivity() {
    private lateinit var classifier: StressDepressionClassifier
    private val TAG = "AIFormActivity"

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_ai_form)

        try {
            classifier = StressDepressionClassifier(this)
            Log.d(TAG, "Classifier initialized successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error initializing classifier: ${e.message}")
            e.printStackTrace()
            return
        }

        val spinnerHeart = findViewById<Spinner>(R.id.spinner_heart_rate)
        val spinnerBP = findViewById<Spinner>(R.id.spinner_bp)
        val spinnerTemp = findViewById<Spinner>(R.id.spinner_temperature)
        val spinnerSpO2 = findViewById<Spinner>(R.id.spinner_spo2)
        val spinnerEDA = findViewById<Spinner>(R.id.spinner_eda)
        val spinnerSleep = findViewById<Spinner>(R.id.spinner_sleep)
        val btnEvaluate = findViewById<Button>(R.id.btn_ai_evaluate)

        val inputData = intent.getIntArrayExtra("user_data") ?: intArrayOf(89, 100, 37, 98, 5, 9)
        Log.d(TAG, "Initial input data: ${inputData.contentToString()}")

        // Setup Heart Rate Spinner (80-200)
        val heartRateValues = (80..200).toList()
        val heartRateAdapter = ArrayAdapter(this, R.drawable.spinner_item, heartRateValues)
        heartRateAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        spinnerHeart.adapter = heartRateAdapter
        spinnerHeart.setSelection(heartRateValues.indexOf(inputData[0].coerceIn(80, 200)))

        // Setup Blood Pressure Spinner (40-120)
        val bpValues = (40..120).toList()
        val bpAdapter = ArrayAdapter(this, R.drawable.spinner_item, bpValues)
        bpAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        spinnerBP.adapter = bpAdapter
        spinnerBP.setSelection(bpValues.indexOf(inputData[1].coerceIn(40, 120)))

        // Setup Temperature Spinner (35.0-42.0 with 0.1 increments)
        val tempValues = mutableListOf<String>()
        for (i in 350..420) {
            tempValues.add("${i / 10.0}°C")
        }
        val tempAdapter = ArrayAdapter(this, R.drawable.spinner_item, tempValues)
        tempAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        spinnerTemp.adapter = tempAdapter
        val tempIndex = ((inputData[2].coerceIn(35, 42) - 35) * 10).coerceIn(0, tempValues.size - 1)
        spinnerTemp.setSelection(tempIndex)

        // Setup SpO2 Spinner (70-100)
        val spo2Values = (70..100).map { "$it%" }.toList()
        val spo2Adapter = ArrayAdapter(this, R.drawable.spinner_item, spo2Values)
        spo2Adapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        spinnerSpO2.adapter = spo2Adapter
        spinnerSpO2.setSelection((inputData[3].coerceIn(70, 100) - 70).coerceIn(0, spo2Values.size - 1))

        // Setup EDA Spinner (0-50)
        val edaValues = (0..50).map { "$it μS" }.toList()
        val edaAdapter = ArrayAdapter(this, R.drawable.spinner_item, edaValues)
        edaAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        spinnerEDA.adapter = edaAdapter
        spinnerEDA.setSelection(inputData[4].coerceIn(0, 50))

        // Setup Sleep Spinner (0-14)
        val sleepValues = (0..14).map { "$it hrs" }.toList()
        val sleepAdapter = ArrayAdapter(this, R.drawable.spinner_item, sleepValues)
        sleepAdapter.setDropDownViewResource(R.drawable.spinner_dropdown_item)
        spinnerSleep.adapter = sleepAdapter
        spinnerSleep.setSelection(inputData[5].coerceIn(0, 14))

        btnEvaluate.setOnClickListener {
            Log.d(TAG, "Evaluate button clicked")

            // Get selected values from spinners
            val heartRate = heartRateValues[spinnerHeart.selectedItemPosition].toFloat()
            val bloodPressure = bpValues[spinnerBP.selectedItemPosition].toFloat()
            val temperature = tempValues[spinnerTemp.selectedItemPosition].replace("°C", "").toFloat()
            val spo2 = (spinnerSpO2.selectedItemPosition + 70).toFloat()
            val eda = spinnerEDA.selectedItemPosition.toFloat()
            val sleep = spinnerSleep.selectedItemPosition.toFloat()

            val input = floatArrayOf(
                sleep,
                eda,
                heartRate,
                bloodPressure,
                temperature,
                spo2,
            )

            Log.d(TAG, "User input values: ${input.contentToString()}")
            Log.d(TAG, "Heart Rate: $heartRate")
            Log.d(TAG, "Blood Pressure: $bloodPressure")
            Log.d(TAG, "Temperature: $temperature")
            Log.d(TAG, "SpO2: $spo2")
            Log.d(TAG, "EDA: $eda")
            Log.d(TAG, "Sleep Hours: $sleep")

            try {
                val result = classifier.predict(input)
                Log.d(TAG, "Prediction result: ${result.contentToString()}")
                Log.d(TAG, "Stress level: ${result[0]}%")
                Log.d(TAG, "Depression level: ${result[1]}%")

                // Ensure values are not NaN or negative
                val stressLevel = if (result[0].isNaN() || result[0] < 0) 0f else result[0]
                val depressionLevel = if (result[1].isNaN() || result[1] < 0) 0f else result[1]

                Log.d(TAG, "Cleaned values - Stress: $stressLevel, Depression: $depressionLevel")

                val dialog = ResultDialogFragment.newInstance(stressLevel, depressionLevel)
                dialog.show(supportFragmentManager, "ResultDialog")

            } catch (e: Exception) {
                Log.e(TAG, "Error during prediction: ${e.message}")
                e.printStackTrace()

                // Show error dialog or default values
                val dialog = ResultDialogFragment.newInstance(0f, 0f)
                dialog.show(supportFragmentManager, "ResultDialog")
            }
        }
    }

    override fun onDestroy() {
        try {
            classifier.close()
            Log.d(TAG, "Classifier closed successfully")
        } catch (e: Exception) {
            Log.e(TAG, "Error closing classifier: ${e.message}")
        }
        super.onDestroy()
    }
}