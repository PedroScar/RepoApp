package com.example.repoapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.repoapp.ui.repolist.RepositoriesListFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.container, RepositoriesListFragment.newInstance())
                .commitNow()
        }
    }
}