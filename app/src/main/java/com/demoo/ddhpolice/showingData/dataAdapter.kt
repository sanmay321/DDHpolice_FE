package com.demoo.ddhpolice.showingData

import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.ddhpolice.R

class dataAdapter (private val mList: List<personItem>) : RecyclerView.Adapter<dataAdapter.ViewHolder>() {

    private lateinit var listnr : OnItemClickListner

    interface OnItemClickListner : OnClickListener {
        fun onclick(position: Int)
        override fun onClick(v: View?) {
            TODO("Not yet implemented")
        }
    }

    fun setOnclickListner(listener : OnItemClickListner){
        listnr = listener
    }

    // create new views
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflates the card_view_design view
        // that is used to hold list item
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.data_adapter, parent, false)

        return ViewHolder(view, listnr)
    }

    // binds the list items to a view
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val ItemsViewModel = mList[position]

        // sets the image to the imageview from our itemHolder class
        holder.id.text = ItemsViewModel.id

        // sets the text to the textview from our itemHolder class
        holder.namee.text = ItemsViewModel.name
        holder.unit.text = ItemsViewModel.unit
        holder.position.text = ItemsViewModel.position
        holder.phone.text = ItemsViewModel.phone
        holder.remarks.text = ItemsViewModel.remarks




    }

    // return the number of the items in the list
    override fun getItemCount(): Int {
        return mList.size
    }

    // Holds the views for adding it to image and text
    class ViewHolder(ItemView: View,listner : OnItemClickListner) : RecyclerView.ViewHolder(ItemView) {
        val id: TextView = itemView.findViewById(R.id.id)
        val namee: TextView = itemView.findViewById(R.id.name)
        val unit: TextView = itemView.findViewById(R.id.unit)
        val position: TextView = itemView.findViewById(R.id.position)
        val phone: TextView = itemView.findViewById(R.id.phone)
        val remarks: TextView = itemView.findViewById(R.id.remarks)

        init {

            ItemView.setOnClickListener {
                listner.onclick(adapterPosition)
            }
        }
    }
}
