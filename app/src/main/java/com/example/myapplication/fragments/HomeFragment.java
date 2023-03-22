package com.example.myapplication.fragments;

public class HomeFragment (
        private val context : MainActivity
) : Fragment() {

        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        // Créer une liste qui va stocker ces plantes
        val plantList = arrayListOf<PlantModel>()

        // enregister une premiere plante dans notre liste (pissenlit)
        plantList.add(PlantModel(
        name = "Rose",
        description = "ça pique un peu",
        imageUrl = "https://cdn.pixabay.com/photo/2023/01/05/22/35/flower-7700011_960_720.jpg",
        false
        ))

        // enregister une deuxieme plante dans notre liste (rose)
        plantList.add(PlantModel(
        name = "Pissenlit",
        description = "jaune soleil",
        imageUrl = "https://cdn.pixabay.com/photo/2018/01/29/07/11/flower-3115353_960_720.jpg ",
        false
        ))

        // enregister une quatrieme plante dans notre liste (cactus)
        plantList.add(PlantModel(
        name = "Cactus",
        description = "ça pique beaucoup",
        imageUrl = "https://cdn.pixabay.com/photo/2021/10/26/12/23/cactus-6743531_960_720.jpg",
        false
        ))

        // enregister une quatrieme plante dans notre liste (tulipe)
        plantList.add(PlantModel(
        name = "Tulipe",
        description = "c'est beau",
        imageUrl = "https://cdn.pixabay.com/photo/2020/04/28/13/17/tulips-5104494_960_720.jpg",
        false
        ))

        // Recuperer le recyclerview
        val horizontalRecyclerView = view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter = PlantAdapter(context, plantList, R.layout.item_horizontal_plant)

        // Recuperer le second recycler view
        val verticalRecyclerView = view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter = PlantAdapter(context, plantList, R.layout.item_vertical_plant)
        verticalRecyclerView.addItemDecoration(PlantItemDecoration())

        return view
        }
        {
}
