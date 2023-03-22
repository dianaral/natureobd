package com.example.myapplication.adapter

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.myapplication.*

class PlantAdapter(
    val context : MainActivity,
    private val plantList: List<PlantModel>,
    private val layoutId : Int
    ) : RecyclerView.Adapter<PlantAdapter.ViewHolder>(){

    //Boite pour ranger tous les composants à controler
    class ViewHolder(view: View) : RecyclerView.ViewHolder(view){
        // image de la plante
        val plantImage = view.findViewById<ImageView>(R.id.image_item)
        val plantName:TextView? = view.findViewById(R.id.name_item)
        val plantDescription:TextView? = view.findViewById(R.id.description_item)
        val starIcon = view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater
            .from(parent.context)
            .inflate(layoutId, parent, false)

        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // Recuperer les informations de la plante
        val currentPlant = plantList[position]

        // Recuperer le repository
        val repo = PlantRepository()

        // utiliser glide pour recuperer l'image à partir de son lien -> composant
        Glide.with(context).load(Uri.parse(currentPlant.imageUrl)).into(holder.plantImage)

        // Mise a jour nom de la plante
        holder.plantName?.text = currentPlant.name

        // Mise a jour description de la plante
        holder.plantDescription?.text = currentPlant.description

        // Verifier si la plante a été liké ou non
        if(currentPlant.liked) {
            holder.starIcon.setImageResource(R.drawable.ic_like)
        }
        else {
            holder.starIcon.setImageResource(R.drawable.ic_unlike)
        }

        // Rajouter une interaction sur cette étoile
        holder.starIcon.setOnClickListener {
            // inverse si le bouton est like ou non
            currentPlant.liked = !currentPlant.liked
            // mettre à jur l'objet plante
            repo.updatePlant(currentPlant)
        }

        // Interaction lors du click sur une plante
        holder.itemView.setOnClickListener {
            // Afficher la popup
            PlantPopup(this, currentPlant).show()
        }

    }

    override fun getItemCount(): Int = plantList.size

}