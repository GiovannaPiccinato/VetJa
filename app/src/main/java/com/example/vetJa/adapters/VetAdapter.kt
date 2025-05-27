package com.example.vetJa.adapters

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.compose.ui.semantics.text
import androidx.recyclerview.widget.RecyclerView
import com.example.vetJa.databinding.ItemVeterinarioBinding
import com.example.vetJa.models.Veterinario

class VetAdapter(
    private var veterinarios: List<Veterinario>,
    private val onItemClicked: (Veterinario) -> Unit
) : RecyclerView.Adapter<VetAdapter.VetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VetViewHolder {
        val binding = ItemVeterinarioBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return VetViewHolder(binding)
    }

    override fun onBindViewHolder(holder: VetViewHolder, position: Int) {
        val veterinario = veterinarios[position]
        holder.bind(veterinario)
        holder.itemView.setOnClickListener {
            onItemClicked(veterinario)
        }
    }

    override fun getItemCount(): Int = veterinarios.size

    @SuppressLint("NotifyDataSetChanged")
    fun updateData(newVeterinarios: List<Veterinario>) {
        this.veterinarios = newVeterinarios
        notifyDataSetChanged()
    }

    inner class VetViewHolder(private val binding: ItemVeterinarioBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(veterinario: Veterinario) {
            binding.textNomeVeterinario.text = veterinario.nome
            binding.textCrmvVeterinario.text = "CRMV: ${veterinario.crmv}"
            binding.textEspecialidadeVeterinario.text = veterinario.especialidade
        }
    }
}