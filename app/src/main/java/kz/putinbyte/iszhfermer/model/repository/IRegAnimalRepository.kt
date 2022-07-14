package kz.putinbyte.iszhfermer.model.repository

import android.app.Activity
import android.content.Context
import android.net.Uri
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.animals.deposit.Deposit
import kz.putinbyte.iszhfermer.entities.animals.fattening.Fattening
import kz.putinbyte.iszhfermer.entities.animals.rvl.SaveRvlInventoryModel
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.prevention.AnimalPrevention
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness
import kz.putinbyte.iszhfermer.entities.db.places.Kato
import kz.putinbyte.iszhfermer.entities.network.GroupAnimalPrevention
import kz.putinbyte.iszhfermer.entities.iszh.Iszh
import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import kz.putinbyte.iszhfermer.entities.research.GroupAnimalResearch
import kz.putinbyte.iszhfermer.entities.sickness.GroupPreventiveActions
import kz.putinbyte.iszhfermer.entities.sickness.GroupAnimalSickness
import kz.putinbyte.iszhfermer.entities.sickness.GroupPreventionActions
import kz.putinbyte.iszhfermer.entities.sickness.GroupResearchActions
import kz.putinbyte.iszhfermer.model.data.server.Result
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File

interface IRegAnimalRepository {

    suspend fun saveSelectedFile(uri: Uri?): Response<Int>?
    fun setContext(cntxt: Context,activity_p: Activity)
    suspend fun deleteFile(filePath: String)
    fun getCurrentIszh(): Iszh
    fun setCurrentIszh(newIszh: Iszh)

    fun getCurrentAnimal(): RegAnimal
    fun setCurrentAnimal(newAnimal: RegAnimal)

    suspend fun getResearchById(animalId: Int, culture: Int): List<AnimalResearchModelItem>
    suspend fun getImmunKindById(animalId: Int, culture: Int): List<AnimalPreventiveActionModelItem>

    suspend fun setAnimal(animal: RegAnimal): Result<AddAnimalModel>
    suspend fun saveAnimal(animal: RegAnimal): Result<AddAnimalModel>


    suspend fun setSickness(animalSicknesses: GroupAnimalSickness): Result<GroupPreventiveActions>
    suspend fun saveSickness(animalSicknesses: GroupAnimalSickness): Result<GroupAnimalSickness>
    suspend fun getUndeliveredSicknesses(): Result<List<AnimalSickness>>
    suspend fun deleteSickness(animalSickness: AnimalSickness)

    suspend fun setResearches(animalResearch: GroupAnimalResearch): Result<GroupResearchActions>
    suspend fun saveResearches(animalResearch: GroupAnimalResearch): Result<GroupAnimalResearch>
    suspend fun deleteResearch(animalResearch: AnimalResearch)
    suspend fun getUndeliveredResearch(): Result<List<AnimalResearch>>

    suspend fun setPrevention(prevention: GroupAnimalPrevention): Result<GroupPreventionActions>
    suspend fun savePrevention(animalPrevention: GroupAnimalPrevention): Result<GroupAnimalPrevention>
    suspend fun deletePrevention(animalPrevention: AnimalPrevention)
    suspend fun getUndeliveredPrevention(): Result<List<AnimalPrevention>>

    fun uploadFile(context: Context)
    suspend fun getAnimalById(id: Int): Result<RegAnimal>

    suspend fun getUndeliveredAnimals(): Result<AddAnimalModel>
    suspend fun deleteAnimal(regAnimal: RegAnimal)
   suspend fun getOfflineRegisteredAnimals(): Result<AnimalList>

    suspend fun listAnimals(isSPK: Boolean?, isDead: Boolean?, animals: AnimalsModel):Result<ListAnimalsModel>
    suspend fun getRegions(id: Int): Result<List<Kato>>
    suspend fun getTypeAnimal(): Result<List<TypeAnimalModel>>
    suspend fun getReceivedSampleName(): Result<List<ReceivedSampleNameModel>>
    suspend fun getDicRvlBranch(): Result<List<DicRvlBranchModel>>
    suspend fun getDicRvlInstrument(): Result<List<DicRvlInstrumentModel>>
    suspend fun getDoctor(column: String, search: String, pageIndex: Int, pageSize: Int): Result<Doctor>

    suspend fun getSaveRvlInventory(saveRvlInventoryModel: SaveRvlInventoryModel): Result<Response<Int>>
    suspend fun setFattening(fattening: Fattening): Result<Response<Int>>
    suspend fun setDeposit(deposit: Deposit): Result<Response<Int>>
    suspend fun uploadFilee(file: File): Result<Response<Int>>
}