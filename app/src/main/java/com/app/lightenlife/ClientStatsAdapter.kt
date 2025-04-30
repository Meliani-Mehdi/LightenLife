import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.app.lightenlife.Client
import com.app.lightenlife.InfoEntry
import com.app.lightenlife.R

class ClientStatsAdapter(
    private val client: Client,
    private val icons: Map<String, Int>,        // e.g. mapOf("Heart rate" to R.drawable.heartbeat)
    private val colors: Map<String, Int>        // e.g. mapOf("Heart rate" to R.color.heart)
) : RecyclerView.Adapter<ClientStatsAdapter.StatViewHolder>() {

    private val statKeys = client.info.keys.toList()  // For fixed order if needed

    inner class StatViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.stat_title)
        val value: TextView = itemView.findViewById(R.id.stat_persent)
        val imageHolder: TextView = itemView.findViewById(R.id.image_holder)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StatViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_stats, parent, false)
        return StatViewHolder(view)
    }

    override fun onBindViewHolder(holder: StatViewHolder, position: Int) {
        val statName = statKeys[position]
        val entries = client.info[statName].orEmpty()

        val latestEntry = entries.maxByOrNull { it.date } // Or sort by date if needed
        val displayValue = latestEntry?.value?.toString() ?: "N/A"

        holder.title.text = statName
        holder.value.text = displayValue

        // Set drawable background
        val bgResId = icons[statName] ?: R.drawable.heartbeat
        holder.imageHolder.setBackgroundResource(bgResId)

        // Set tint
        val tintColor = colors[statName] ?: R.color.heart
        holder.imageHolder.background.setTint(ContextCompat.getColor(holder.itemView.context, tintColor))
    }

    override fun getItemCount(): Int = statKeys.size
}
