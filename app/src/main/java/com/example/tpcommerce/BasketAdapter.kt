package com.example.tpcommerce

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class BasketAdapter(private val items: MutableList<Product>) : RecyclerView.Adapter<BasketAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_basket_product, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val product = items[position]
        holder.bind(product) {
            onItemRemoved(it)
        }
    }

    fun removeItem(position: Int) {
        items.removeAt(position)
        notifyItemRemoved(position)
        notifyItemRangeChanged(position, items.size)
    }
    override fun getItemCount() = items.size
    var onItemRemoved: (Int) -> Unit = {}

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val imageViewProduct: ImageView = view.findViewById(R.id.imageViewProduct)
        private val textViewProductTitle: TextView = view.findViewById(R.id.textViewProductTitle)
        private val textViewProductPrice: TextView = view.findViewById(R.id.textViewProductPrice)
        private val btnRemoveProduct: Button = view.findViewById(R.id.btnRemoveProduct)

        fun bind(product: Product, onItemRemoved: (Int) -> Unit) {
            textViewProductTitle.text = product.title
            textViewProductPrice.text = "${product.price} €"
            Glide.with(itemView.context)
                .load(product.image)
                .into(imageViewProduct)
            btnRemoveProduct.setOnClickListener {
                onItemRemoved(adapterPosition)
            }
        }

    }
}
