package com.example.vetJa.adapters
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.vetJa.R
import com.example.vetJa.models.Pet.PetDTO

class PetAdapter(
    private val petList: List<PetDTO>,
    private val onEditClick: (PetDTO) -> Unit,
    private val onDeleteClick: (PetDTO) -> Unit
) : RecyclerView.Adapter<PetAdapter.PetViewHolder>() {

    inner class PetViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgEspeciePet: ImageView = itemView.findViewById(R.id.imgEspeciePet)
        val txtPetName: TextView = itemView.findViewById(R.id.txtPetName)
        val txtPetInfo: TextView = itemView.findViewById(R.id.txtPetInfo)
        val imgEditPet: ImageView = itemView.findViewById(R.id.imgEditPet)
        val imgDeletePet: ImageView = itemView.findViewById(R.id.imgDeletePet)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PetViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pet, parent, false)
        return PetViewHolder(view)
    }

    override fun onBindViewHolder(holder: PetViewHolder, position: Int) {
        val pet = petList[position]

        // definir a imagem pela especie
        val imageResource = if (pet.gato == true) R.drawable.especiegato else R.drawable.especiecachorro


        holder.imgEspeciePet.setImageResource(imageResource)
        holder.txtPetName.text = pet.nome
        holder.txtPetInfo.text = "${pet.idade} | ${if (pet.macho == true) "Macho" else "Fêmea"} | ${if (pet.gato == true) "Gato" else "Cachorro"}"
        holder.imgEditPet.setOnClickListener { onEditClick(pet) }
        holder.imgDeletePet.setOnClickListener { onDeleteClick(pet) }
    }
    override fun getItemCount() = petList.size
}