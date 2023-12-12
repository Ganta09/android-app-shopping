package com.example.tpcommerce

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.content.Intent
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

class ProductAdapter(private val groupedProducts: Map<String, List<Product>>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    companion object {
        private const val TYPE_HEADER = 0
        private const val TYPE_PRODUCT = 1
    }

    private var allItems: MutableList<Any> = mutableListOf()

    init {
        regenerateAllItems(groupedProducts)


    }

    private fun regenerateAllItems(groupedProducts: Map<String, List<Product>>) {
        allItems.clear()
        allItems.addAll(groupedProducts.flatMap { listOf(it.key) + it.value })
    }

    fun updateData(newProducts: List<Product>) {
        val newGroupedProducts = newProducts.groupBy { it.category }
        regenerateAllItems(newGroupedProducts)
        notifyDataSetChanged()
    }
    override fun getItemViewType(position: Int): Int {
        return if (allItems[position] is String) TYPE_HEADER else TYPE_PRODUCT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == TYPE_HEADER) {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.category_header, parent, false)
            HeaderViewHolder(itemView)
        } else {
            val itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_item, parent, false)
            ProductViewHolder(itemView)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = allItems[position]
        if (holder is HeaderViewHolder && item is String) {
            holder.categoryTitle.text = item
        } else if (holder is ProductViewHolder && item is Product) {
            Glide.with(holder.itemView.context)
                .load(item.image)
                .into(holder.productImage)
            holder.productTitle.text = item.title
            holder.productPrice.text = "${item.price} €"
        }
        when (holder) {
            is HeaderViewHolder -> if (item is String) holder.categoryTitle.text = item
            is ProductViewHolder -> if (item is Product) holder.bind(item)
        }
    }

    override fun getItemCount() = allItems.size

    // ViewHolder pour les en-têtes de catégorie
    class HeaderViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val categoryTitle: TextView = view.findViewById(R.id.categoryTitle)
    }

    // ViewHolder pour les produits
    class ProductViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val productImage: ImageView = view.findViewById(R.id.productImage)
        val productTitle: TextView = view.findViewById(R.id.productTitle)
        val productPrice: TextView = view.findViewById(R.id.productPrice)
        fun bind(product: Product) {
            Glide.with(itemView.context)
                .load(product.image)
                .into(productImage)
            productTitle.text = product.title
            productPrice.text = "${product.price} €"

            itemView.setOnClickListener {
                val context = itemView.context
                val intent = Intent(context, ProductDetailActivity::class.java).apply {
                    putExtra("product", product)
                }
                context.startActivity(intent)
            }
        }
        }



}
