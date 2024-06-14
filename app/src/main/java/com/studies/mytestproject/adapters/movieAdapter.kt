package com.studies.mytestproject.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.studies.mytestproject.R
import com.studies.mytestproject.models.movieitem

class movieAdapter(val context:Context,var list:MutableList<movieitem>):RecyclerView.Adapter<movieAdapter.myviewholder>() {
    class  myviewholder(val itemview: View):RecyclerView.ViewHolder(itemview){
        val image=itemview.findViewById<ImageView>(R.id.image)
        val title=itemview.findViewById<TextView>(R.id.title)
        val rating5=itemview.findViewById<TextView>(R.id.rating5)
        val rating10=itemview.findViewById<TextView>(R.id.rating10)
        val year=itemview.findViewById<TextView>(R.id.year)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): myviewholder {
        val view= LayoutInflater.from(context).inflate(R.layout.itemview,parent,false)
        return myviewholder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: myviewholder, position: Int) {
        val currentItem= list.get(position)
        holder.title.text=currentItem.title
        holder.rating5.text="${currentItem.rating5}/5"
        holder.rating10.text="${currentItem.rating10}/10"

       holder.year.text="Year : ${currentItem.year}"
        Glide.with(context).load(currentItem.imagelink).into(holder.image)

    }
    fun ondatachange(newlist:List<movieitem>){
        this.list.clear()

        this.list.addAll(newlist)
        notifyDataSetChanged()
    }
}