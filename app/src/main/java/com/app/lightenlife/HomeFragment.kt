package com.app.lightenlife

import ClientStatsAdapter
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

class HomeFragment : Fragment() {

    private lateinit var adapter: ClientStatsAdapter
    private lateinit var infoEntryManager: InfoEntryManager
    private lateinit var recyclerView: RecyclerView
    private lateinit var nameHolder: TextView
    private lateinit var AI_form: Button
    private val handler = Handler(Looper.getMainLooper())
    private val intervalMillis = 30_000L
    private val statTypes = listOf("Blood Pressure(Sys)", "Blood Pressure(Dias)", "Temperature", "SpO2", "EDA")
    private var client: Client? = null

    private var data = listOf(0, 0, 0, 0, 0, 0)

    private val iconsMap = mapOf(
        "Blood Pressure(Sys)" to R.drawable.heartbeat,
        "SpO2" to R.drawable.o2,
        "Sleep" to R.drawable.sleeping_face_1024_535404208,
        "EDA" to R.drawable.bolt,
        "Blood Pressure(Dias)" to R.drawable.blood_pressure,
        "Temperature" to R.drawable.temperature
    )

    private val colorsMap = mapOf(
        "Blood Pressure(Sys)" to R.color.heart,
        "SpO2" to R.color.light_blue,
        "Sleep" to R.color.light_blue,
        "EDA" to R.color.yellow,
        "Blood Pressure(Dias)" to R.color.heart,
        "Temperature" to R.color.light_blue
    )

    private val updateTask = object : Runnable {
        override fun run() {
            lifecycleScope.launch {
                fetchAndUpdateUI()
                simulateAndStoreData()
            }
            handler.postDelayed(this, intervalMillis)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)
        recyclerView = view.findViewById(R.id.recyclerView)
        nameHolder = view.findViewById(R.id.user_name_holder)
        AI_form = view.findViewById(R.id.btn_ai_eval)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val authId = AuthenticationManager(UserManager()).getCurrentUserId() ?: return

        Log.d("Auth", "authId = $authId")

        UserManager().getUser(authId) { user ->
            Log.d("UserFetch", "User fetched: $user")
            if (user is Client) {
                Log.d("UserFetch", "User is a Client")
                client = user
                nameHolder.setText(client?.name)
                infoEntryManager = InfoEntryManager(authId)
                setupRecyclerView()
                handler.post(updateTask)
            }
            else{
                Log.w("UserFetch", "User is not a Client or is null")
            }
        }

        AI_form.setOnClickListener {
            val intent = Intent(requireContext(), AIFormActivity::class.java)
            intent.putExtra("user_data", data.toIntArray())
            startActivity(intent)
        }

    }

    private fun setupRecyclerView() {
        adapter = ClientStatsAdapter(client ?: return, iconsMap, colorsMap)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
    }

    private suspend fun simulateAndStoreData() {
        val date = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val stress = Random.nextBoolean()

        val entries = mapOf(
            "Sleep" to InfoEntry(date, 8),
            "EDA" to InfoEntry(date, if (stress) Random.nextInt(10, 15) else Random.nextInt(1, 5)),
            "Blood Pressure(Sys)" to InfoEntry(date, if (stress) 130 + Random.nextInt(0, 20) else 100 + Random.nextInt(0, 20)),
            "Blood Pressure(Dias)" to InfoEntry(date, if (stress) Random.nextInt(80, 125) else Random.nextInt(60, 85)),
            "Temperature" to InfoEntry(date, if (stress) Random.nextInt(35, 41) else Random.nextInt(36, 38)),
            "SpO2" to InfoEntry(date, if (stress) Random.nextInt(94, 96) else Random.nextInt(96, 100)),
        )

        data = entries.values.map { it.value }

        entries.forEach { (stat, entry) ->
            try {
                infoEntryManager.addEntry(stat, entry)
                Log.d("FirebaseWrite", "Saved $stat -> $entry")
            } catch (e: Exception) {
                Log.e("FirebaseWrite", "Failed to save $stat: ${e.message}", e)
            }
        }

    }

    private suspend fun fetchAndUpdateUI() {
        val statData = mutableMapOf<String, List<InfoEntry>>()
        for (stat in statTypes) {
            val entries = infoEntryManager.getEntries(stat)
            statData[stat] = entries
        }

        client = client?.copy(info = statData)

        withContext(Dispatchers.Main) {
            adapter = ClientStatsAdapter(client!!, iconsMap, colorsMap)
            recyclerView.adapter = adapter
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        handler.removeCallbacks(updateTask)
    }
}
