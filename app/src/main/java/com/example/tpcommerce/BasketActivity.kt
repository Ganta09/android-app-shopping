package com.example.tpcommerce

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class BasketActivity : AppCompatActivity() {
    // Déclarez les variables pour le recyclerView et l'adapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var basketAdapter: BasketAdapter
    private lateinit var textViewTotalPrice: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_basket)

        // Initialisation de recyclerView
        recyclerView = findViewById(R.id.recyclerViewBasket)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Initialisation de basketAdapter avec les éléments du panier
        basketAdapter = BasketAdapter(CartManager.cartItems.toMutableList())

        recyclerView.adapter = basketAdapter

        basketAdapter.onItemRemoved = { position ->
            CartManager.cartItems.removeAt(position)
            basketAdapter.removeItem(position)
        }
        // Configuration du FloatingActionButton pour le retour en arrière
        val fabBack: FloatingActionButton = findViewById(R.id.fab_back)
        fabBack.setOnClickListener {
            finish() // Ferme l'activité et revient à MainActivity
        }
        textViewTotalPrice = findViewById(R.id.textViewTotalPrice)
        updateTotalPrice()
        basketAdapter.onItemRemoved = { position ->
            CartManager.cartItems.removeAt(position)
            basketAdapter.removeItem(position)
            updateTotalPrice()
        }
    }
    private fun updateTotalPrice() {
        val totalPrice = CartManager.cartItems.sumOf { it.price }
        textViewTotalPrice.text = getString(R.string.total_price, totalPrice)
    }
    override fun onResume() {
        super.onResume()
        basketAdapter.notifyDataSetChanged() // Pour rafraîchir la liste si des articles ont été ajoutés
    }

}
