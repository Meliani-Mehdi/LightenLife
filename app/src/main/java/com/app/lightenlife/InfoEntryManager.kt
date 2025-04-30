package com.app.lightenlife

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class InfoEntryManager(private val userId: String) : BaseDatabaseManager<InfoEntry>("clients") {

    private val userStatsRef = db
        .collection("clients")
        .document(userId)
        .collection("info") // Each stat is a document: "Heart rate", "Sleep", etc.

    // Upload or update a single stat entry (append-style)
    suspend fun addEntry(statType: String, entry: InfoEntry) {
        userStatsRef.document(statType)
            .collection("entries")
            .document(entry.date.replace(":", "-").replace(" ", "_"))
            .set(entry) // Use sanitized date string as ID
            .await()
    }

    // Get all entries for a given stat (e.g., "Heart rate")
    suspend fun getEntries(statType: String): List<InfoEntry> {
        val snapshot = userStatsRef
            .document(statType)
            .collection("entries")
            .get()
            .await()

        return snapshot.documents.mapNotNull { it.toObject(InfoEntry::class.java) }
    }

    // Delete all entries under a stat
    suspend fun deleteAllEntriesForStat(statType: String) {
        val entries = userStatsRef.document(statType).collection("entries").get().await()
        for (entry in entries.documents) {
            entry.reference.delete().await()
        }
    }

    override fun getType(): Class<InfoEntry> = InfoEntry::class.java
}
