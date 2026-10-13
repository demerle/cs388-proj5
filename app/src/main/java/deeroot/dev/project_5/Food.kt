package deeroot.dev.project_5

import androidx.room.Entity
import androidx.room.PrimaryKey

// one food entry in the database
@Entity
data class Food(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val foodName: String,
    val calories: Int
)
