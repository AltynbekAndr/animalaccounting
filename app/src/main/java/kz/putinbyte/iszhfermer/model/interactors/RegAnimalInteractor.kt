package kz.putinbyte.iszhfermer.model.interactors

import android.app.Activity
import android.content.Context
import android.net.Uri
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.animals.deposit.Deposit
import kz.putinbyte.iszhfermer.entities.animals.fattening.Fattening
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.animals.rvl.SaveRvlInventoryModel
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.entities.iszh.Iszh
import kz.putinbyte.iszhfermer.entities.network.GroupAnimalPrevention
import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import kz.putinbyte.iszhfermer.entities.research.GroupAnimalResearch
import kz.putinbyte.iszhfermer.model.repository.IRegAnimalRepository
import kz.putinbyte.iszhfermer.entities.sickness.GroupAnimalSickness
import kz.putinbyte.iszhfermer.entities.sickness.GroupPreventionActions
import kz.putinbyte.iszhfermer.entities.sickness.GroupPreventiveActions
import kz.putinbyte.iszhfermer.entities.sickness.GroupResearchActions
import kz.putinbyte.iszhfermer.model.data.server.Result
import kz.putinbyte.iszhfermer.utils.LogUtils
import okhttp3.MultipartBody
import retrofit2.Response
import java.io.File
import javax.inject.Inject

class RegAnimalInteractor @Inject constructor(
    private val regAnimalRepository: IRegAnimalRepository,
    private val referencesInteractor: ReferencesInteractor
) {

    var scanData: ScanData? = null
    var katoId: Int? = null
    var ownerId: Int? = null

    fun setContext(cntxt: Context,activity_p: Activity) = regAnimalRepository.setContext(cntxt,activity_p)
    suspend fun saveSelectedFile(uri: Uri?): Response<Int>? = regAnimalRepository.saveSelectedFile(uri)

    fun getCurrentIszh(): Iszh {
        return regAnimalRepository.getCurrentIszh()
    }

    fun setCurrentIszh(newIszh: Iszh) {
        regAnimalRepository.setCurrentIszh(newIszh)
    }

    suspend fun getAnimalInfoById(id: Int): Result<RegAnimal> {
        return regAnimalRepository.getAnimalById(id)
    }

    suspend fun setAnimals(animal: RegAnimal, saveLocal: Boolean = false): Result<AddAnimalModel> {
        var result: Result<AddAnimalModel> = regAnimalRepository.setAnimal(animal)
        return if (result is Result.Success) {
            result.data.regAnimal?.firstOrNull()?.let { res ->
                if (!res.comment.isNullOrEmpty()) {
                    result = Result.Error(Exception(res.comment))
                    //проверить сервер
                    res.ownerINN = animal.ownerINN
                    if (saveLocal) regAnimalRepository.saveAnimal(res)
                } else {
                    regAnimalRepository.deleteAnimal(animal)
                }
            }
            result
        } else regAnimalRepository.saveAnimal(animal)
    }

    fun decodeInj(inj: String): HashMap<InjDecode, String>? {
        return try {
            val injArr = inj.toCharArray()
            val result: HashMap<InjDecode, String> = hashMapOf()
            var injLast = ""
            result.put(InjDecode.COUNTRY, "${injArr[0]}${inj[1]}")
            result.put(InjDecode.REGION, "${injArr[2]}")
            result.put(InjDecode.KIND, "${injArr[3]}")
            result.put(InjDecode.MAIN, "${injArr[0]}${inj[1]}${inj[2]}${inj[3]}")
            result
        } catch (e: Exception) {
            LogUtils.error(javaClass.simpleName, e.message)
            null
        }

    }

    enum class InjDecode {
        MAIN,
        COUNTRY,
        REGION,
        KIND
    }

    suspend fun setSickness(
        animalSicknesses: GroupAnimalSickness,
        saveLocal: Boolean = false
    ): Result<GroupPreventiveActions> {
        var result: Result<GroupPreventiveActions> =
            regAnimalRepository.setSickness(animalSicknesses)
        if (result is Result.Success) {
            result.data.item1.animalSickness.map { res ->
                if (!res.comment.isNullOrEmpty()) {
                    result = Result.Error(Exception(res.comment))
                    if (saveLocal) regAnimalRepository.saveSickness(
                        GroupAnimalSickness(
                            animalSickness = listOf(res)
                        )
                    )
                } else {
                    regAnimalRepository.deleteSickness(res)
                }
            }
        } else {
            val saveResult = regAnimalRepository.saveSickness(animalSicknesses)
            if (saveResult is Result.Success) result =
                Result.Success(GroupPreventiveActions(item1 = saveResult.data, item2 = null))
        }

        return result

    }

    suspend fun setResearch(
        animalResearch: GroupAnimalResearch,
        saveLocal: Boolean = false
    ): Result<GroupResearchActions> {
        var result: Result<GroupResearchActions> = regAnimalRepository.setResearches(animalResearch)
        if (result is Result.Success) {
            result.data.item1.animalResearchDtos.map { res ->
                if (!res.comment.isNullOrEmpty()) {
                    result = Result.Error(Exception(res.comment))
                    if (saveLocal) regAnimalRepository.saveResearches(
                        GroupAnimalResearch(
                            animalResearchDtos = listOf(res)
                        )
                    )
                } else {
                    regAnimalRepository.deleteResearch(res)
                }
            }
        } else {
            val saveResult = regAnimalRepository.saveResearches(animalResearch)
            if (saveResult is Result.Success) result =
                Result.Success(GroupResearchActions(item1 = saveResult.data, item2 = null))
        }
        return result
    }

    suspend fun setPrevention(
        prevention: GroupAnimalPrevention,
        saveLocal: Boolean = false
    ): Result<GroupPreventionActions> {
        var result: Result<GroupPreventionActions> = regAnimalRepository.setPrevention(prevention)
        if (result is Result.Success) {
            result.data.item1.animalPrevention.map { res ->
                if (!res.comment.isNullOrEmpty()) {
                    result = Result.Error(Exception(res.comment))
                    if (saveLocal) regAnimalRepository.savePrevention(
                        GroupAnimalPrevention(
                            animalPrevention = listOf(res)
                        )
                    )
                } else {
                    regAnimalRepository.deletePrevention(res)
                }
            }
        } else {
            val saveResult = regAnimalRepository.savePrevention(prevention)
            if (saveResult is Result.Success) result =
                Result.Success(GroupPreventionActions(item1 = saveResult.data, item2 = null))
        }
        return result
    }

    suspend fun deleteAnimal(animal: RegAnimal) {
        regAnimalRepository.deleteAnimal(animal)
        deleteSickness(animalId = animal.id)
        deleteResearch(animalId = animal.id)
        deletePrevention(animalId = animal.id)
    }

    private suspend fun deleteSickness(animalId: Int? = null) {
        val result = regAnimalRepository.getUndeliveredSicknesses()
        if (result is Result.Success) {
            result.data.filter { if (animalId != null) it.animalId == animalId else true }.map {
                regAnimalRepository.deleteSickness(it)
            }
        }
    }

    private suspend fun deletePrevention(animalId: Int? = null) {
        val result = regAnimalRepository.getUndeliveredPrevention()
        if (result is Result.Success) {
            result.data.filter { if (animalId != null) it.animalId == animalId else true }.map {
                regAnimalRepository.deletePrevention(it)
            }
        }
    }

    suspend fun deleteResearch(animalId: Int? = null) {
        val result = regAnimalRepository.getUndeliveredResearch()
        if (result is Result.Success) {
            result.data.filter { if (animalId != null) it.animalId == animalId else true }.map {
                regAnimalRepository.deleteResearch(it)
            }
        }
    }


    private suspend fun sendUndeliveredSickness() {
        val result = regAnimalRepository.getUndeliveredSicknesses()
        if (result is Result.Success) {
            setSickness(GroupAnimalSickness(animalSickness = result.data), true)
        }
    }

    private suspend fun sendUndeliveredResearch() {
        val result = regAnimalRepository.getUndeliveredResearch()
        if (result is Result.Success) {
            setResearch(GroupAnimalResearch(animalResearchDtos = result.data), true)
        }
    }

    private suspend fun sendUndeliveredPrevention() {
        val result = regAnimalRepository.getUndeliveredPrevention()
        if (result is Result.Success) {
            setPrevention(GroupAnimalPrevention(animalPrevention = result.data), true)
        }
    }

    suspend fun sendUndeliveredData() {
        sendUndeliveredAnimals()
        sendUndeliveredSickness()
        sendUndeliveredResearch()
        sendUndeliveredPrevention()
    }

    private suspend fun sendUndeliveredAnimals() {

        val result = regAnimalRepository.getUndeliveredAnimals()
        if (result is Result.Success) {
            result.data.regAnimal?.filter { it.comment.isNullOrEmpty() }?.map { animal ->
                when {
                    animal.ownerId != null -> {
                        setAnimals(animal, true)
                    }
                    animal.ownerINN != null -> {
                        val ownerResult =
                            referencesInteractor.findOwnerByINN(searchIin = animal.ownerINN!!.toLong())
                        val owner: Owners? = if (ownerResult is Result.Success) {
                            ownerResult.data.list?.firstOrNull()
                        } else null

                        if (owner != null)
                            setAnimals(animal.copy(ownerId = owner.id), true)
                        else
                            regAnimalRepository.saveAnimal(animal.copy(comment = "Пользователь не найден"))
                    }
                    else -> {}
                }
            }
        }
    }

    suspend fun getResearchById(animalId: Int, culture: Int): List<AnimalResearchModelItem> {
        return regAnimalRepository.getResearchById(animalId, culture)
    }

    suspend fun getOfflineRegisteredAnimals() = regAnimalRepository.getOfflineRegisteredAnimals()

    suspend fun setListAnimals(
        isSPK: Boolean?,
        isDead: Boolean?,
        animals: AnimalsModel
    ): Result<ListAnimalsModel> {
        return regAnimalRepository.listAnimals(isSPK, isDead, animals)
    }

    suspend fun setDoctor(
        column: String,
        search: String,
        pageIndex: Int,
        pageSize: Int
    ): Result<Doctor> {
        return regAnimalRepository.getDoctor(column, search, pageIndex, pageSize)
    }

    suspend fun setReceivedSampleName(): Result<List<ReceivedSampleNameModel>> {
        return regAnimalRepository.getReceivedSampleName()
    }

    // Сохранение описи РВЛ
    suspend fun setSaveRvlInventoryModel(saveRvlInventoryModel: SaveRvlInventoryModel): Result<Response<Int>> {
        return regAnimalRepository.getSaveRvlInventory(saveRvlInventoryModel)
    }

    suspend fun setDicRvlBranch(): Result<List<DicRvlBranchModel>> {
        return regAnimalRepository.getDicRvlBranch()
    }

    suspend fun setDicRvlInstrument(): Result<List<DicRvlInstrumentModel>> {
        return regAnimalRepository.getDicRvlInstrument()
    }

    fun uploadFileClicked(context: Context) {
        regAnimalRepository.uploadFile(context)
    }

    suspend fun setFattening(fattening: Fattening): Response<Int>? {
        val result = regAnimalRepository.setFattening(fattening)
        return if (result is Result.Success) return result.data else null
    }

    suspend fun setDeposit(deposit: Deposit): Response<Int>? {
        val result = regAnimalRepository.setDeposit(deposit)
        return if (result is Result.Success) return result.data else null
    }

    suspend fun uploadFile(file: File): Result<Response<Int>> {
        return regAnimalRepository.uploadFilee(file)
    }

}