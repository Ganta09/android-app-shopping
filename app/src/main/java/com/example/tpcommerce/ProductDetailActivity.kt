package com.example.tpcommerce

import android.os.Bundle
import android.widget.ImageView
import android.widget.RatingBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ProductDetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product_detail)

        val product = intent.getParcelableExtra<Product>("product") ?: return // Add safe call or handle null scenario

        val imageView: ImageView = findViewById(R.id.productDetailImage)
        val titleView: TextView = findViewById(R.id.productDetailTitle)
        val priceView: TextView = findViewById(R.id.productDetailPrice)
        val descriptionView: TextView = findViewById(R.id.productDetailDescription)
        val ratingView: TextView = findViewById(R.id.productDetailRating)
        val fabBack: FloatingActionButton = findViewById(R.id.fab_back)
        fabBack.setOnClickListener {
            finish() // This will close the current activity and return to MainActivity
        }

        // Set up the "Ajouter au Panier" button (no action for now)
        val fabAddToCart: FloatingActionButton = findViewById(R.id.fab_add_to_cart)
        val ratingBar: RatingBar = findViewById(R.id.productDetailRatingBar)
        ratingBar.rating = product.rating.rate.toFloat()
        Glide.with(this).load(product.image).into(imageView)
        titleView.text = product.title
        priceView.text = "${product.price} €"
        descriptionView.text = product.description
        ratingView.text = "Rating: (${product.rating.count})"
    }
}
