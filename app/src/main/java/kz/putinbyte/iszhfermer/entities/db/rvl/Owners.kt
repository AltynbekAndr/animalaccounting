package kz.putinbyte.iszhfermer.entities.db.rvl

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose
import java.io.Serializable

@Entity(tableName = "owners")
data class Owners(
    @Expose
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    val birthDate: String = "",
    val citizenshipId: Int? = null,
    val documentDateIssue: String = "",
    val documentNumber: String = "",
    val doumentIssueBy: String = "",
    val email: String = "",
    val firstName: String = "",
    val flat: String = "",
    val house: String = "",
    val iin: String? = null,
    val katoId: Int? =null,
    val lastName: String = "",
    val middleName: String = "",
    val mobilePhone: String= "",
    val postIndex: String= "",
    val street: String= "",
    val tel: String= "",
    val fullName : String? = null,
    var clearDate : String? = null
):Serializable