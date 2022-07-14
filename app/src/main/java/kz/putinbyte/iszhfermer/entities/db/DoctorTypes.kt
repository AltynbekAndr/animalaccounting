package kz.putinbyte.iszhfermer.entities.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "doctor_types")
data class DoctorTypes(
    val firstName: String?,
    @PrimaryKey(autoGenerate = false)
    val id: Int,
    val identifier: String?,
    val lastName: String?,
    var middleName: String?
)