package deeroot.dev.project_5

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class FoodAdapter(val foods: MutableList<Food>) : RecyclerView.Adapter<FoodAdapter.FoodViewHolder>() {

    // replace whatever is in the list with a new set of foods
    fun updateList(newFoods: List<Food>) {
        foods.clear()
        foods.addAll(newFoods)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FoodViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_food, parent, false)
        return FoodViewHolder(view)
    }

    override fun onBindViewHolder(holder: FoodViewHolder, position: Int) {
        val food = foods[position]
        holder.foodNameTv.text = food.foodName
        holder.caloriesTv.text = food.calories.toString()
    }

    override fun getItemCount(): Int {
        return foods.size
    }

    class FoodViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val foodNameTv: TextView = view.findViewById(R.id.food_name_tv)
        val caloriesTv: TextView = view.findViewById(R.id.calories_tv)
    }
}
