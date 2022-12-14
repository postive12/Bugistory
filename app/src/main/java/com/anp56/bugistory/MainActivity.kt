package com.anp56.bugistory

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.anp56.bugistory.data.ProfileDataViewModel
import com.anp56.bugistory.databinding.ActivityMainBinding
import com.anp56.bugistory.post.PostViewModel
import com.anp56.bugistory.tools.ImageCacheManager
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater)}
    lateinit var postViewModel : PostViewModel
    lateinit var profileDataViewModel: ProfileDataViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        current = this
        setContentView(binding.root)
        postViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            PostViewModel::class.java)
        profileDataViewModel = ViewModelProvider(this, ViewModelProvider.NewInstanceFactory()).get(
            ProfileDataViewModel::class.java)

        val db = Firebase.firestore
        val usersRef = db.collection("userdata")
        usersRef.get().addOnSuccessListener {
            for (data in it){
                ImageCacheManager.requestImage(data.id)
            }
        }

        // setup actionbar with nav controller to show up button
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment) as NavHostFragment
        val navController = navHostFragment.navController
        // CAUTION: findNavController(R.id.fragment) in onCreate will fail.

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNav)
        bottomNavigationView?.setupWithNavController(navController)
    }
    fun refresh(){
        this.onResume()
    }
    companion object{
        lateinit var current : MainActivity
    }
}