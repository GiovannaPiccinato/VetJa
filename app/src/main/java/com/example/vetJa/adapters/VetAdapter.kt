package com.example.vetJa.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetJa.R
import com.example.vetJa.models.Vet.VetDTO

class VetAdapter(
    private var veterinarios: List<VetDTO>, // ✅ Aqui estava List<Vet>, agora é List<VetDTO>
    private val onItemClicked: (VetDTO) -> Unit
) : RecyclerView.Adapter<VetAdapter.VetViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_vet, parent, false)
        return VetViewHolder(view)
    }

    override fun onBindViewHolder(holder: VetViewHolder, position: Int) {
        val veterinario = veterinarios[position]
        holder.txtNomeVeterinario.text = veterinario.nome
        holder.txtCrmvVeterinario.text = "CRMV: ${veterinario.crmv}"
        holder.txtEspecialidadeVeterinario.text = veterinario.especialidade

        holder.itemView.setOnClickListener {
            onItemClicked(veterinario)
        }
    }

    override fun getItemCount(): Int = veterinarios.size

    inner class VetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val txtNomeVeterinario: TextView = itemView.findViewById(R.id.textNomeVeterinario)
        val txtCrmvVeterinario: TextView = itemView.findViewById(R.id.textCrmvVeterinario)
        val txtEspecialidadeVeterinario: TextView = itemView.findViewById(R.id.textEspecialidadeVeterinario)
    }
}
