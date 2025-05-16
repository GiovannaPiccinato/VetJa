package com.example.vetJa.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.vetJa.R

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class EditUsuarioFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_edit_usuario, container, false)
    }
}