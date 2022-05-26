package com.example.firebasedb

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasedb.databinding.ActivityFirestoreBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFirestoreBinding
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private var snapshotListener: ListenerRegistration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirestoreBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val secondIntent = intent
        val itemID = secondIntent.getStringExtra("itemID")

        if (itemID != null) {
            itemsCollectionRef.document(itemID).get()
                .addOnSuccessListener {
                    binding.textItemName.text = it["name"].toString()
                    binding.textItemPrice.text = it["price"].toString()
                    if(it["cart"]==null || it["cart"]==false)
                        binding.cartBtn.text = "add to cart"
                    else
                        binding.cartBtn.text = "remove from cart"

                }.addOnFailureListener {
                }
        }

        binding.cartBtn.setOnClickListener {
            if (itemID != null) {
                changeCart(itemID)
            }
        }
    }


    private fun changeCart(itemID: String){
        itemsCollectionRef.document(itemID).get()
            .addOnSuccessListener {
                if(it["cart"] == null || it["cart"] == false) {
                    itemsCollectionRef.document(itemID).update("cart", true)
                    binding.cartBtn.text = "remove from cart"
                }
                else {
                    itemsCollectionRef.document(itemID).update("cart", false)
                    binding.cartBtn.text = "add to cart"
                }
            }
    }
}
