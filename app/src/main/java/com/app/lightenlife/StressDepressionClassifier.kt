import android.content.Context
import android.util.Log
import org.tensorflow.lite.Interpreter
import java.io.FileInputStream
import java.nio.MappedByteBuffer
import java.nio.channels.FileChannel

class StressDepressionClassifier(context: Context) {
    private val interpreter: Interpreter
    private val TAG = "StressClassifier"

    init {
        interpreter = Interpreter(loadModelFile(context, "SDOM1.tflite"))
        Log.d(TAG, "Model loaded successfully")
        logModelInfo()
    }

    private fun loadModelFile(context: Context, modelName: String): MappedByteBuffer {
        val fileDescriptor = context.assets.openFd(modelName)
        val inputStream = FileInputStream(fileDescriptor.fileDescriptor)
        val fileChannel = inputStream.channel
        val startOffset = fileDescriptor.startOffset
        val declaredLength = fileDescriptor.declaredLength
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength)
    }

    private fun logModelInfo() {
        try {
            val inputTensor = interpreter.getInputTensor(0)
            val outputTensor = interpreter.getOutputTensor(0)

            Log.d(TAG, "Input shape: ${inputTensor.shape().contentToString()}")
            Log.d(TAG, "Input data type: ${inputTensor.dataType()}")
            Log.d(TAG, "Output shape: ${outputTensor.shape().contentToString()}")
            Log.d(TAG, "Output data type: ${outputTensor.dataType()}")
        } catch (e: Exception) {
            Log.e(TAG, "Error getting model info: ${e.message}")
        }
    }

    fun predict(inputData: FloatArray): FloatArray {
        Log.d(TAG, "Raw input data: ${inputData.contentToString()}")

        // Create input [1,6] - using raw data as requested
        val input = arrayOf(inputData)
        // Create output [1,2]
        val output = Array(1) { FloatArray(2) }

        try {
            // Run inference
            interpreter.run(input, output)

            val rawOutput = output[0]
            Log.d(TAG, "Raw model output: ${rawOutput.contentToString()}")

            // Check if output values are already in percentage range
            if (rawOutput[0] > 1f || rawOutput[1] > 1f) {
                Log.d(TAG, "Model outputs seem to be in percentage/raw range already")
                return rawOutput
            }

            // If values are small (0-1 range), treat as probabilities
            if (rawOutput[0] <= 1f && rawOutput[1] <= 1f) {
                Log.d(TAG, "Model outputs appear to be probabilities (0-1 range)")

                val percentages = floatArrayOf(
                    rawOutput[0] * 100f,
                    rawOutput[1] * 100f
                )
                Log.d(TAG, "Converted to percentages: ${percentages.contentToString()}")
                return percentages
            }

            // Fallback - return raw values
            Log.d(TAG, "Returning raw output values")
            return rawOutput

        } catch (e: Exception) {
            Log.e(TAG, "Error during prediction: ${e.message}")
            e.printStackTrace()
            return floatArrayOf(0f, 0f)
        }
    }

    fun close() {
        interpreter.close()
        Log.d(TAG, "Model closed")
    }
}