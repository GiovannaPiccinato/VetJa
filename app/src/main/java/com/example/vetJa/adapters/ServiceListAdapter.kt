package com.example.vetJa.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.vetJa.R
import com.example.vetJa.models.service.Service
import androidx.core.net.toUri
import java.net.URLEncoder

class ServicoAdapter(private val servicos: List<Service>) :
    RecyclerView.Adapter<ServicoAdapter.ServicoViewHolder>() {

    private var selectedPosition = RecyclerView.NO_POSITION

    @SuppressLint("QueryPermissionsNeeded")
    inner class ServicoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nome: TextView = itemView.findViewById(R.id.tvTituloServico)
        val descricao: TextView = itemView.findViewById(R.id.tvDescricaoServico)

        init {
            itemView.setOnClickListener {
                val context = itemView.context
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val servico = servicos[position]

                    // Atualiza item selecionado para efeito visual
                    val previousPosition = selectedPosition
                    selectedPosition = position
                    notifyItemChanged(previousPosition)
                    notifyItemChanged(selectedPosition)

                    // Abrir WhatsApp com link personalizado
                    val link = "Olá, gostaria de saber mais sobre ${servico.nome}"
                    val msgCodificada = URLEncoder.encode(link, "UTF-8")
                    val url = "https://wa.me/message/GI7NF6M3VMVWD1?text=$msgCodificada"

                    val intent = Intent(Intent.ACTION_VIEW, url.toUri())
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ServicoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.service_card, parent, false)
        return ServicoViewHolder(view)
    }

    override fun onBindViewHolder(holder: ServicoViewHolder, position: Int) {
        val servico = servicos[position]
        holder.nome.text = servico.nome ?: ""
        holder.descricao.text = servico.descricao ?: ""

        // Visual feedback (por exemplo, fundo azul se selecionado)
        holder.itemView.isSelected = (position == selectedPosition)
    }

    override fun getItemCount(): Int = servicos.size
}
