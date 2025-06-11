package com.app.lightenlife

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import com.github.lzyzsd.circleprogress.ArcProgress

class ResultDialogFragment : DialogFragment() {

    private val TAG = "ResultDialogFragment"

    companion object {
        fun newInstance(stress: Float, depression: Float): ResultDialogFragment {
            val args = Bundle().apply {
                putFloat("stress", stress)
                putFloat("depression", depression)
            }
            return ResultDialogFragment().apply { arguments = args }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "onCreateView called")
        return inflater.inflate(R.layout.result_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val stress = arguments?.getFloat("stress") ?: 0f
        val depression = arguments?.getFloat("depression") ?: 0f

        Log.d(TAG, "Received values from arguments:")
        Log.d(TAG, "Raw stress value: $stress")
        Log.d(TAG, "Raw depression value: $depression")

        val stressProgress = view.findViewById<ArcProgress>(R.id.circle_stress)
        val depressionProgress = view.findViewById<ArcProgress>(R.id.circle_depression)
        val tvStressDesc = view.findViewById<TextView>(R.id.tv_stress_desc)
        val tvDepressionDesc = view.findViewById<TextView>(R.id.tv_depression_desc)

        // Check if views are found
        if (stressProgress == null) {
            Log.e(TAG, "Stress progress view not found!")
            return
        }
        if (depressionProgress == null) {
            Log.e(TAG, "Depression progress view not found!")
            return
        }

        // Determine if values are already percentages or need conversion
        var stressPercent: Float
        var depressionPercent: Float

        if (stress > 1f || depression > 1f) {
            // Values are already percentages
            Log.d(TAG, "Values appear to be percentages already")
            stressPercent = stress
            depressionPercent = depression
        } else {
            // Values are 0-1 range, convert to percentages
            Log.d(TAG, "Converting 0-1 values to percentages")
            stressPercent = stress * 100f
            depressionPercent = depression * 100f
        }

        // Ensure values are within valid range (0-100)
        stressPercent = stressPercent.coerceIn(0f, 100f)
        depressionPercent = depressionPercent.coerceIn(0f, 100f)

        Log.d(TAG, "Final percentage values:")
        Log.d(TAG, "Stress percentage: $stressPercent")
        Log.d(TAG, "Depression percentage: $depressionPercent")

        val stressInt = stressPercent.toInt()
        val depressionInt = depressionPercent.toInt()

        Log.d(TAG, "Integer values for progress bars:")
        Log.d(TAG, "Stress int: $stressInt")
        Log.d(TAG, "Depression int: $depressionInt")

        // Configure progress bars with enhanced styling
        configureProgressBar(stressProgress, "Stress")
        configureProgressBar(depressionProgress, "Depression")

        // Set initial progress to 0 and log
        stressProgress.progress = 0
        depressionProgress.progress = 0
        Log.d(TAG, "Initial progress set to 0")

        // Animate progress bars with dynamic colors
        animateProgressWithColor(stressProgress, stressInt, "Stress")
        animateProgressWithColor(depressionProgress, depressionInt, "Depression")

        // Set descriptions (use original 0-1 values for description logic)
        val stressFor0to1 = if (stress > 1f) stress / 100f else stress
        val depressionFor0to1 = if (depression > 1f) depression / 100f else depression

        val stressDesc = getDescription("Stress", stressFor0to1)
        val depressionDesc = getDescription("Depression", depressionFor0to1)

        Log.d(TAG, "Descriptions:")
        Log.d(TAG, "Stress description: $stressDesc")
        Log.d(TAG, "Depression description: $depressionDesc")

        tvStressDesc.text = stressDesc
        tvDepressionDesc.text = depressionDesc

        // Add subtle animation to description text
        animateTextAppearance(tvStressDesc)
        animateTextAppearance(tvDepressionDesc)
    }

    private fun configureProgressBar(progressView: ArcProgress, label: String) {
        // Set initial styling with your color scheme
        progressView.apply {
            // Background color - light blue/white tone
            unfinishedStrokeColor = ContextCompat.getColor(requireContext(), R.color.lighter_gray)
            // Initial progress color - will be updated dynamically
            finishedStrokeColor = ContextCompat.getColor(requireContext(), R.color.white)
            // Text color
            textColor = ContextCompat.getColor(requireContext(), R.color.dark_gray)
            // Stroke width for better visibility
            strokeWidth = 12f
            // Text size
            textSize = 48f
            // Suffix
            suffixText = "%"
            suffixTextSize = 24f
        }
        Log.d(TAG, "$label progress bar configured")
    }

    private fun getColorForValue(value: Int): Int {
        return when {
            value == 0 -> ContextCompat.getColor(requireContext(), R.color.white)
            value < 30 -> ContextCompat.getColor(requireContext(), R.color.light_blue) // Low - Blue
            value < 70 -> Color.parseColor("#FFA500") // Medium - Orange
            else -> ContextCompat.getColor(requireContext(), R.color.heart) // High - Red
        }
    }

    private fun getTextColorForValue(value: Int): Int {
        return when {
            value < 30 -> ContextCompat.getColor(requireContext(), R.color.light_blue)
            value < 70 -> Color.parseColor("#FF8C00") // Darker orange
            else -> ContextCompat.getColor(requireContext(), R.color.heart)
        }
    }

    @SuppressLint("ObjectAnimatorBinding")
    private fun animateProgressWithColor(progressView: ArcProgress, target: Int, label: String) {
        Log.d(TAG, "Starting enhanced animation for $label: 0 -> $target")

        val animator = ObjectAnimator.ofInt(progressView, "progress", 0, target).apply {
            duration = 2000 // Slightly longer for smoother effect
            interpolator = DecelerateInterpolator()
        }

        // Add color animation listener
        animator.addUpdateListener { animation ->
            val currentValue = animation.animatedValue as Int

            // Update progress bar color based on current value
            val currentColor = getColorForValue(currentValue)
            val currentTextColor = getTextColorForValue(currentValue)

            progressView.finishedStrokeColor = currentColor
            progressView.textColor = currentTextColor

            Log.v(TAG, "$label animation progress: $currentValue, color updated")
        }

        animator.addListener(object : android.animation.Animator.AnimatorListener {
            override fun onAnimationStart(animation: android.animation.Animator) {
                Log.d(TAG, "$label animation started")
                // Add a subtle glow effect at start
                progressView.alpha = 0.7f
                progressView.animate().alpha(1f).setDuration(500).start()
            }

            override fun onAnimationEnd(animation: android.animation.Animator) {
                Log.d(TAG, "$label animation ended. Final progress: ${progressView.progress}")
                // Add a subtle pulse effect at end
                progressView.animate()
                    .scaleX(1.05f)
                    .scaleY(1.05f)
                    .setDuration(200)
                    .withEndAction {
                        progressView.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(200)
                            .start()
                    }
                    .start()
            }

            override fun onAnimationCancel(animation: android.animation.Animator) {
                Log.d(TAG, "$label animation cancelled")
            }

            override fun onAnimationRepeat(animation: android.animation.Animator) {
                Log.d(TAG, "$label animation repeated")
            }
        })

        animator.start()

        // Also check current progress value after a short delay
        progressView.postDelayed({
            Log.d(TAG, "$label progress after 100ms: ${progressView.progress}")
        }, 100)
    }

    private fun animateTextAppearance(textView: TextView) {
        // Fade in text with a slight delay for a staggered effect
        textView.alpha = 0f
        textView.translationY = 20f
        textView.animate()
            .alpha(1f)
            .translationY(0f)
            .setDuration(800)
            .setStartDelay(1000) // Start after progress animation begins
            .setInterpolator(DecelerateInterpolator())
            .start()
    }

    private fun getDescription(label: String, value: Float): String {
        Log.d(TAG, "Getting description for $label with value: $value")

        val (description, emoji) = when {
            value < 0.3 -> Pair("$label Level: Low – You're in a healthy range!", "😊")
            value < 0.7 -> Pair("$label Level: Moderate – Some signs present, stay mindful.", "⚠️")
            else -> Pair("$label Level: High – Consider consulting with a professional.", "🔴")
        }

        val finalDescription = "$emoji $description"
        Log.d(TAG, "Generated description: $finalDescription")
        return finalDescription
    }
}