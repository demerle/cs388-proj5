package deeroot.dev.project_5

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FoodDao {

    @Insert
    fun insert(food: Food)

    @Query("SELECT * FROM Food")
    fun getAll(): List<Food>
}
