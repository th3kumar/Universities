package com.fridayhouse.universities.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.fridayhouse.universities.R
import com.fridayhouse.universities.activities.WebViewActivity
import com.fridayhouse.universities.model.UniversityItem

class UniversityAdapter: RecyclerView.Adapter<UniversityAdapter.UniversityViewHolder>(){

    // Add a member variable to store the context
    var context: Context? = null

    inner class UniversityViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        //val tvWebsite = itemView.findViewById<TextView>(R.id.textViewWebPages)
        val tvName = itemView.findViewById<TextView>(R.id.textViewUniversityName)
        val tvCountry = itemView.findViewById<TextView>(R.id.textViewCountry)
        val tvDomain = itemView.findViewById<TextView>(R.id.textViewWebPages)
    }

    private val differCallback = object : DiffUtil.ItemCallback<UniversityItem>() {
        override fun areItemsTheSame(
            oldItem: UniversityItem,
            newItem: UniversityItem
        ): Boolean {
            return oldItem.domains == newItem.domains
        }

        override fun areContentsTheSame(
            oldItem: UniversityItem,
            newItem: UniversityItem
        ): Boolean {
            return oldItem == newItem
        }
    }

    val differ = AsyncListDiffer(this, differCallback)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UniversityViewHolder {
        return UniversityViewHolder(
            LayoutInflater.from(parent.context).inflate(
                R.layout.item_university,
                parent,
                false
            )
        )
    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    override fun onBindViewHolder(holder: UniversityViewHolder, position: Int) {
        val schoolsResponseItem = differ.currentList[position]
        //holder.tvWebsite.text = schoolsResponseItem.web_pages.toString()
        holder.tvName.text = schoolsResponseItem.name
        holder.tvCountry.text = schoolsResponseItem.country
        holder.tvDomain.text = schoolsResponseItem.domains.toString()

        holder.itemView.setOnClickListener {
            // Start the WebViewActivity when a university item is clicked
            context?.let { context ->
                val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("domain", schoolsResponseItem.domains.firstOrNull())
                context.startActivity(intent)
            }
        }
    }

    private var onItemClickListener: ((UniversityItem) -> Unit)? = null

    fun setOnItemClickListener(listener: (UniversityItem) -> Unit) {
        onItemClickListener = listener
    }
}