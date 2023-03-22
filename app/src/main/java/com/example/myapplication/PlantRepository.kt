package com.example.myapplication

import android.net.Uri
import com.example.myapplication.PlantRepository.Singleton.databaseRef
import com.example.myapplication.PlantRepository.Singleton.downLoadUri
import com.example.myapplication.PlantRepository.Singleton.plantList
import com.example.myapplication.PlantRepository.Singleton.storageReference
import com.google.android.gms.tasks.Continuation
import com.google.android.gms.tasks.Task
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import java.net.URI
import java.util.UUID
import javax.security.auth.callback.Callback

class PlantRepository {

    object Singleton {

        // Donner le lien pour acceder au bucket
        private val BUCKET_URL: String = "gs://my-application-71f89.appspot.com"

        // se connecter à notre espace de stockage
        val storageReference = FirebaseStorage.getInstance().getReferenceFromUrl(BUCKET_URL)
        // se connecter à la reference "plants"
        val databaseRef = FirebaseDatabase.getInstance().getReference("plants")

        // créer une liste qui va contenir nos plantes
        val plantList = arrayListOf<PlantModel>()

        // Contenir le lien de l'image courante
        var downLoadUri : Uri? = null

    }

    fun updateData(callback : () -> Unit) {
        // Absorber les données depuis la databaseRef -> liste de plantes
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // retirer les anciennes
                plantList.clear()

                // Recolter la liste
                for (ds in snapshot.children) {
                    // Construire un objet plante
                    val plant = ds.getValue(PlantModel::class.java)

                    // Verifier que la plante n'est pas null
                    if(plant != null) {
                        // Ajouter la plante à notre liste
                        plantList.add(plant)
                    }
                }
                // Actionner le callback
                callback()
            }

            override fun onCancelled(error: DatabaseError) {}

        })
    }

    // creer une fonction pour envoyer des fichiers sur le storage
    fun uploadImage(file : Uri, callback: () -> Unit) {
        // Verfier que ce fichier n'est pas null
        if(file != null) {
            val fileName = UUID.randomUUID().toString() + ".jpg"
            val ref = storageReference.child(fileName)
            val uploadTask = ref.putFile(file)

            // Demarrer la tache d'envoi
            uploadTask.continueWithTask(Continuation<UploadTask.TaskSnapshot, Task<Uri>> { task ->

                // si y a eu un probleme lors de l'envoi du fichier
                if (!task.isSuccessful) {
                    task.exception?.let { throw it }
                }

                return@Continuation ref.downloadUrl

            }).addOnCompleteListener { task ->
                // Verifier si tout a bien fonctionné
                if(task.isSuccessful) {
                    // recuperer l'image
                    downLoadUri = task.result
                    callback()
                }
            }
        }
    }

    // mettre a jour un objet plante en BDD
    fun updatePlant(plant : PlantModel) = databaseRef.child(plant.id).setValue(plant)

    // Inserer une nouvelle plante en BDD
    fun insertPlant(plant : PlantModel) = databaseRef.child(plant.id).setValue(plant)

    // supprimer une plante de la base
    fun deletePlant(plant: PlantModel) = databaseRef.child(plant.id).removeValue()

}