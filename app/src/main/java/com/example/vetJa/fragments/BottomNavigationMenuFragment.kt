package com.example.vetJa.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.example.vetJa.R
import com.google.android.material.bottomnavigation.BottomNavigationView

open class BottomNavigationMenuFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_navigation_menu, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomNavigationView = view.findViewById<BottomNavigationView>(R.id.bottomNavigationView)

        bottomNavigationView.setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.homeFragment -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.indexFragmentContainer, HomeFragment())
                        .commit()
                    true
                }
                R.id.userProfileFragment -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.indexFragmentContainer, UserProfileFragment())
                        .commit()
                    true
                }
                else -> false
            }
        }

        // Definir o item inicial como selecionado
        bottomNavigationView.selectedItemId = R.id.homeFragment
    }

}