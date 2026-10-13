package deeroot.dev.project_5

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.textfield.TextInputEditText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var adapter: FoodAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        db = AppDatabase.getInstance(this)

        adapter = FoodAdapter(mutableListOf())
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)

        // load any saved food entries from the database
        loadFoodsFromDb()

        // open the add food dialog when the button is clicked
        findViewById<Button>(R.id.add_food_btn).setOnClickListener {
            showAddFoodDialog()
        }
    }

    // read all foods out of the database and show them in the list
    private fun loadFoodsFromDb() {
        lifecycleScope.launch(Dispatchers.IO) {
            val foods = db.foodDao().getAll()
            withContext(Dispatchers.Main) {
                adapter.updateList(foods)
            }
        }
    }

    private fun showAddFoodDialog() {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.add_food_dialog, null)
        val nameEt = dialogView.findViewById<TextInputEditText>(R.id.food_name_et)
        val caloriesEt = dialogView.findViewById<TextInputEditText>(R.id.calories_et)

        AlertDialog.Builder(this)
            .setTitle("Add a food")
            .setView(dialogView)
            .setPositiveButton("Add") { dialog, _ ->
                val name = nameEt.text?.toString() ?: ""
                // if they type something weird just treat it as 0 calories
                val calories = caloriesEt.text?.toString()?.toIntOrNull() ?: 0

                // save the new food to the database, then reload the list from the db
                // (the database is the one source of truth for our data)
                lifecycleScope.launch(Dispatchers.IO) {
                    db.foodDao().insert(Food(foodName = name, calories = calories))
                    val foods = db.foodDao().getAll()
                    withContext(Dispatchers.Main) {
                        adapter.updateList(foods)
                        dialog.dismiss()
                    }
                }
            }
            .setNegativeButton("Cancel") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}
