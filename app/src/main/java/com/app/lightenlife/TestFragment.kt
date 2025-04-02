package com.app.lightenlife

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.cardview.widget.CardView
import android.widget.Button

class TestFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Set up click listeners for test cards
        setupTestCards(view)
    }

    private fun setupTestCards(view: View) {
        // Find card views
        val cardTest1 = view.findViewById<CardView>(R.id.card_test1)
        val cardTest2 = view.findViewById<CardView>(R.id.card_test2)
        val cardTest3 = view.findViewById<CardView>(R.id.card_test3)

        // Find start buttons (if using the improved layout)
        val btnStartTest1 = view.findViewById<Button?>(R.id.btn_start_test1)
        val btnStartTest2 = view.findViewById<Button?>(R.id.btn_start_test2)
        val btnStartTest3 = view.findViewById<Button?>(R.id.btn_start_test3)

        // Set click listeners for cards
        cardTest1.setOnClickListener {
            navigateToQuestions(1)
        }

        cardTest2.setOnClickListener {
            navigateToQuestions(2)
        }

        cardTest3.setOnClickListener {
            navigateToQuestions(3)
        }

        // Set click listeners for buttons if they exist
        btnStartTest1?.setOnClickListener {
            navigateToQuestions(1)
        }

        btnStartTest2?.setOnClickListener {
            navigateToQuestions(2)
        }

        btnStartTest3?.setOnClickListener {
            navigateToQuestions(3)
        }
    }

    private fun navigateToQuestions(testIndex: Int) {
        // Create intent to navigate to QuestionsActivity
        val intent = Intent(activity, questionsActivity::class.java)

        // Add the test index as an extra
        intent.putExtra(EXTRA_TEST_INDEX, testIndex)

        // Start the activity
        startActivity(intent)
    }

    companion object {
        // Intent extra key for test index
        const val EXTRA_TEST_INDEX = "com.app.lightenlife.EXTRA_TEST_INDEX"

        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            TestFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}