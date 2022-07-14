package kz.putinbyte.iszhfermer.entities.iszh

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Reference(
    @PrimaryKey
    @NonNull
    val id: Int,
    val referenceUUID: String?,
    val element: String?,
    val referenceKind: String?
)
