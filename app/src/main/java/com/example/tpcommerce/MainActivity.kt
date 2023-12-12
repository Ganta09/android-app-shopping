package com.example.tpcommerce

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import retrofit2.Call
import retrofit2.Callback
import kotlinx.android.parcel.Parcelize
import android.app.AlertDialog
import android.os.Parcelable
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.io.Serializable

/********************************/
/* Classes de Produits et Évaluation */
/********************************/

@Parcelize
data class Product(
    val id: Int,
    val title: String,
    val price: Double,
    val description: String,
    val category: String,
    val image: String,
    val rating: Rating
) : Parcelable

@Parcelize
data class Rating(
    val rate: Double,
    val count: Int
) : Parcelable

/********************************/
/* Interface API */
/********************************/

interface ApiInterface {
    @GET("products")
    fun getProducts(): Call<List<Product>>
}

/********************************/
/* Service API */
/********************************/

object ApiService {
    private val BASE_URL = "https://fakestoreapi.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val apiInterface: ApiInterface = retrofit.create(ApiInterface::class.java)
}

/********************************/
/* MainActivity */
/********************************/

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var productAdapter: ProductAdapter
    private var groupedProducts: Map<String, List<Product>> = emptyMap()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Initialisation du RecyclerView
        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.setHasFixedSize(true)

        // Chargement des produits
        loadProducts()
        val fab: FloatingActionButton = findViewById(R.id.fab_filter)
        fab.setOnClickListener {
            Log.d("MainActivity", "groupedProducts before dialog: $groupedProducts")
            if (groupedProducts.isNotEmpty()) {
                showCategoryFilterDialog()
            }
        }
    }
    private fun showCategoryFilterDialog() {
        // Ajouter une option pour désactiver le filtre
        val allCategoriesOption = "Toutes les catégories"
        val categories = listOf(allCategoriesOption) + groupedProducts.keys
        val categoriesArray = categories.toTypedArray()

        AlertDialog.Builder(this)
            .setTitle("Choisir une catégorie")
            .setItems(categoriesArray) { _, which ->
                filterProductsByCategory(categoriesArray[which])
            }
            .show()
    }


    private fun filterProductsByCategory(category: String) {
        val filteredProducts = if (category == "Toutes les catégories") {
            // Si "Toutes les catégories" est sélectionné, afficher tous les produits
            groupedProducts.flatMap { it.value }
        } else {
            // Sinon, filtrer par la catégorie sélectionnée
            groupedProducts[category] ?: listOf()
        }

        productAdapter.updateData(filteredProducts)
    }
    private fun loadProducts() {
        ApiService.apiInterface.getProducts().enqueue(object : Callback<List<Product>> {
            override fun onResponse(call: Call<List<Product>>, response: Response<List<Product>>) {
                if (response.isSuccessful && response.body() != null && response.body()!!.isNotEmpty()) {
                    val products = response.body()!!
                    groupedProducts = products.groupBy { it.category }
                    Log.d("MainActivity", "Grouped products: $groupedProducts")

                    productAdapter = ProductAdapter(groupedProducts)
                    recyclerView.adapter = productAdapter              }
            }

            override fun onFailure(call: Call<List<Product>>, t: Throwable) {
                Log.e("API Error", "Error loading products: ${t.message}", t)
            }
        })
    }
}
