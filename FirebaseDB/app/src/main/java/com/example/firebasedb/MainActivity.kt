package com.example.firebasedb

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.firebasedb.databinding.ActivityMainBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private var adapter: MyAdapter? = null
    private val db: FirebaseFirestore = Firebase.firestore
    private val itemsCollectionRef = db.collection("items")
    private var snapshotListener: ListenerRegistration? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // recyclerview setup
        binding.recyclerViewItems.layoutManager = LinearLayoutManager(this)
        adapter = MyAdapter(this, emptyList())
        adapter?.setOnItemClickListener {
            queryItem(it)
        }
        binding.recyclerViewItems.adapter = adapter

        updateList()  // list items on recyclerview

    }

    override fun onStart() {
        super.onStart()

        /*
        // snapshot listener for all items
        snapshotListener = itemsCollectionRef.addSnapshotListener { snapshot, error ->
            binding.textSnapshotListener.text = StringBuilder().apply {
                for (doc in snapshot!!.documentChanges) {
                    append("${doc.type} ${doc.document.id} ${doc.document.data}")
                }
            }
        }

         */
        // sanpshot listener for single item
        /*
        itemsCollectionRef.document("1").addSnapshotListener { snapshot, error ->
            Log.d(TAG, "${snapshot?.id} ${snapshot?.data}")
        }*/
    }

    override fun onStop() {
        super.onStop()
        snapshotListener?.remove()
    }

    private fun updateList() {
        itemsCollectionRef.get().addOnSuccessListener {
            val items = mutableListOf<Item>()
            for (doc in it) {
                items.add(Item(doc))
            }
            adapter?.updateList(items)
        }
    }

    private fun queryItem(itemID: String) {
        startActivity(
            Intent(this, FirebaseActivity::class.java)
                .putExtra("itemID", itemID)
        )



    }

    companion object {
        const val TAG = "MainActivity"
    }
}