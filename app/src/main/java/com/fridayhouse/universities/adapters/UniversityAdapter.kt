package com.fridayhouse.universities.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.fridayhouse.universities.R
import com.fridayhouse.universities.model.University
import com.fridayhouse.universities.model.UniversityItem

class UniversityAdapter : RecyclerView.Adapter<UniversityAdapter.ViewHolder>() {

    private var universities: List<UniversityItem> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_university, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val university = universities[position]
        holder.bind(university)
    }

    override fun getItemCount(): Int {
        return universities.size
    }

    fun submitList(universities: List<UniversityItem>) {
        this.universities = universities
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val universityNameTextView: TextView = itemView.findViewById(R.id.textViewUniversityName)
        private val countryTextView: TextView = itemView.findViewById(R.id.textViewCountry)
        private val webPagesTextView: TextView = itemView.findViewById(R.id.textViewWebPages)

        fun bind(university: UniversityItem) {
            // Bind university data to UI elements in the item layout
            universityNameTextView.text = university.name
            countryTextView.text = "Country: ${university.country}"
            webPagesTextView.text = "Web Pages: ${university.web_pages.joinToString(", ")}"
        }
    }
}