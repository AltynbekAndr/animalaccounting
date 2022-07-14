package kz.putinbyte.iszhfermer.model.repository


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.provider.Settings
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.startActivity
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.animals.deposit.Deposit
import kz.putinbyte.iszhfermer.entities.animals.fattening.Fattening
import kz.putinbyte.iszhfermer.entities.animals.rvl.SaveRvlInventoryModel
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.prevention.AnimalPrevention
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness
import kz.putinbyte.iszhfermer.entities.db.places.Kato
import kz.putinbyte.iszhfermer.entities.iszh.Iszh
import kz.putinbyte.iszhfermer.entities.network.GroupAnimalPrevention
import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import kz.putinbyte.iszhfermer.entities.research.GroupAnimalResearch
import kz.putinbyte.iszhfermer.entities.sickness.GroupAnimalSickness
import kz.putinbyte.iszhfermer.entities.sickness.GroupPreventionActions
import kz.putinbyte.iszhfermer.entities.sickness.GroupPreventiveActions
import kz.putinbyte.iszhfermer.entities.sickness.GroupResearchActions
import kz.putinbyte.iszhfermer.model.data.db.dao.*
import kz.putinbyte.iszhfermer.model.data.server.ApiClient
import kz.putinbyte.iszhfermer.model.data.server.Result
import kz.putinbyte.iszhfermer.model.system.IDirectoryManager
import kz.putinbyte.iszhfermer.utils.LogUtils
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import retrofit2.Response
import java.io.*
import javax.inject.Inject


class RegAnimalRepository @Inject constructor(
    private val apiClient: ApiClient,
    private val directoryManager: IDirectoryManager,
    private val regAnimalDao: AnimalDao,
    private val sicknessDao: SicknessDao,
    private val researchDao: ResearchDao,
    private val preventionDao: PreventionDao
) : IRegAnimalRepository, BaseRepository() {

    lateinit var iszh: Iszh
    lateinit var animal: RegAnimal

    private val TAG = javaClass.simpleName

    private val pickedFilesDirectory =
        File("${directoryManager.pickedFilePath}${File.separator}regAnimal")


    init {
        if (!pickedFilesDirectory.exists()) {
            pickedFilesDirectory.mkdir()
        }
    }

    override suspend fun getAnimalById(id: Int): Result<RegAnimal> {
        val animalInfo = regAnimalDao.getById(id)
        return if (animalInfo != null) {
            Result.Success(animalInfo)
        } else {
            safeApiCall {
                apiClient.getAnimalInfo(id)
            }
        }
    }

    override suspend fun uploadFilee(file: File): Result<Response<Int>> = safeApiCall {

        val body = MultipartBody.Part.create(file.asRequestBody("file/pdf".toMediaTypeOrNull()))
        val builder = MultipartBody.Builder().apply {
            setType(MultipartBody.FORM)
        }
        return@safeApiCall apiClient.uploadFile(builder.build())
    }

    override suspend fun setAnimal(animal: RegAnimal): Result<AddAnimalModel> {
        return safeApiCall {
            apiClient.setAnimals(AddAnimalModel(regAnimal = listOf(animal)))
        }
    }

    override suspend fun getUndeliveredAnimals(): Result<AddAnimalModel> {
        return try {
            val animals = regAnimalDao.getAll() ?: listOf()
            return Result.Success(AddAnimalModel(regAnimal = animals))
        } catch (e: Exception) {
            Result.Error(Exception("При сохранении произошла ошибка"))
        }
    }


    override suspend fun getOfflineRegisteredAnimals(): Result<AnimalList> = try {
        val data = regAnimalDao.getAll()
        when {
            !data.isNullOrEmpty() -> Result.Success(
                AnimalList(
                    count = data.count(),
                    listAnimals = AnimalList.Animals.toFormat(data)
                )
            )
            else -> {
                Result.Error(Exception("Записей не найдено"))
            }
        }
    } catch (e: Exception) {
        Result.Error(Exception("Записей не найдено"))
    }

    override suspend fun deleteAnimal(regAnimal: RegAnimal) = try {
        regAnimalDao.delete(regAnimal)
    } catch (e: Exception) {
        LogUtils.error(TAG, e.message)
    }

    override suspend fun saveAnimal(animal: RegAnimal): Result<AddAnimalModel> {
        return try {
            regAnimalDao.insert(animal)
            Result.Success(AddAnimalModel(regAnimal = listOf(animal)))
        } catch (e: Exception) {
            Result.Error(Exception("При сохранении произошла ошибка"))
        }
    }

    override suspend fun setSickness(animalSicknesses: GroupAnimalSickness): Result<GroupPreventiveActions> {
        return safeApiCall { apiClient.setAnimalSickness(animalSicknesses) }
    }

    override suspend fun saveSickness(animalSicknesses: GroupAnimalSickness): Result<GroupAnimalSickness> {
        return try {
            sicknessDao.insertAllSicknesses(animalSicknesses.animalSickness)
            Result.Success(animalSicknesses)
        } catch (e: Exception) {
            Result.Error(Exception("При сохранении произошла ошибка"))
        }
    }

    override suspend fun deleteSickness(animalSickness: AnimalSickness) =
        sicknessDao.delete(animalSickness)

    override suspend fun getUndeliveredSicknesses(): Result<List<AnimalSickness>> {
        return try {
            val sicknesses = sicknessDao.getAllSicknesses() ?: listOf()
            return Result.Success(sicknesses)
        } catch (e: Exception) {
            Result.Error(Exception("При сохранении произошла ошибка"))
        }
    }


    override suspend fun setResearches(animalResearch: GroupAnimalResearch): Result<GroupResearchActions> {
        return safeApiCall { apiClient.setAnimalResearch(animalResearch) }
    }

    override suspend fun saveResearches(animalResearch: GroupAnimalResearch): Result<GroupAnimalResearch> {
        return try {
            researchDao.insertAllResearches(animalResearch.animalResearchDtos)
            Result.Success(animalResearch)
        } catch (e: Exception) {
            Result.Error(Exception("При сохранении произошла ошибка"))
        }
    }

    override suspend fun deleteResearch(animalResearch: AnimalResearch) =
        researchDao.delete(animalResearch)

    override suspend fun getUndeliveredResearch(): Result<List<AnimalResearch>> {
        return try {
            val researches = researchDao.getAllResearches() ?: listOf()
            return Result.Success(researches)
        } catch (e: Exception) {
            Result.Error(Exception("При сохранении произошла ошибка"))
        }
    }


    override suspend fun savePrevention(animalPrevention: GroupAnimalPrevention): Result<GroupAnimalPrevention> {
        return try {
            preventionDao.insertAllPrevention(animalPrevention.animalPrevention)
            Result.Success(animalPrevention)
        } catch (e: Exception) {
            Result.Error(Exception("При сохранении произошла ошибка"))
        }
    }

    override suspend fun deletePrevention(animalPrevention: AnimalPrevention){
        preventionDao.delete(animalPrevention)
    }
    override suspend fun getUndeliveredPrevention(): Result<List<AnimalPrevention>>{
        return try {
            val prevention = preventionDao.getAllPrevention() ?: listOf()
            return Result.Success(prevention)
        } catch (e: Exception) {
            Result.Error(Exception("При сохранении произошла ошибка"))
        }
    }
    override suspend fun setPrevention(prevention: GroupAnimalPrevention): Result<GroupPreventionActions> {
        return safeApiCall { apiClient.setPrevention(prevention) }

    }

    override suspend fun saveSelectedFile(uri: Uri?): Response<Int> {
        var sourceFile:File? = null
        if(uri!=null) {
            sourceFile =  File(getPath(uri))
        }
        return  apiClient.uploadFile(MultipartBody.Builder().setType(MultipartBody.FORM).addFormDataPart(
            "file", sourceFile?.getName(),
            RequestBody.create("text/plain".toMediaTypeOrNull(), sourceFile!!)
        ).build())

    }


    var cntxt:Context? = null
    var activity_v:Activity? = null

    override fun setContext(cntxt: Context, activity_p: Activity) {
        this.cntxt = cntxt;
        this.activity_v = activity_p;
    }




    fun getPath(uri: Uri?): String? {
        val projection = arrayOf(MediaStore.Images.Media.DATA)
        val cursor: Cursor = cntxt?.getContentResolver()?.query(uri!!, projection, null, null, null) ?: return null
        val column_index: Int = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
        cursor.moveToFirst()
        val s: String = cursor.getString(column_index)
        cursor.close()
        return s
    }

    private suspend fun cleanDirectory(taskId: Long) {
        val filesList = pickedFilesDirectory.listFiles()
        for (file in filesList) {
            if (file.isFile) {
                file.delete()
            }
        }
    }

    override suspend fun deleteFile(filePath: String) {
        val file = File(filePath)
        if (file.exists()) {
            file.delete()
        }
    }

    override fun uploadFile(context: Context) {
    }

    override suspend fun listAnimals(
        isSPK: Boolean?,
        isDead: Boolean?,
        animals: AnimalsModel,
    ): Result<ListAnimalsModel> {
        return safeApiCall { apiClient.listAnimals(isSPK, isDead, animals) }
    }



    override suspend fun getRegions(id: Int): Result<List<Kato>> {
        return safeApiCall { apiClient.getKato(id) }
    }

    override suspend fun getTypeAnimal(): Result<List<TypeAnimalModel>> {
        return safeApiCall { apiClient.getTypeAnimal() }
    }

    //Получение крови
    override suspend fun getReceivedSampleName(): Result<List<ReceivedSampleNameModel>> {
        return safeApiCall { apiClient.getReceivedSampleName() }
    }

    //Филиал лаборатории и пунктов приема проб
    override suspend fun getDicRvlBranch(): Result<List<DicRvlBranchModel>> {
        return safeApiCall { apiClient.getDicRvlBranch() }
    }

    //Израсходованные материалы
    override suspend fun getDicRvlInstrument(): Result<List<DicRvlInstrumentModel>> {
        return safeApiCall { apiClient.getDicRvlInstrument() }
    }

    //Доктор
    override suspend fun getDoctor(
        column: String,
        search: String,
        pageIndex: Int,
        pageSize: Int
    ): Result<Doctor> {
        return safeApiCall { apiClient.getDoctor(column, search, pageIndex, pageSize) }
    }

    // Сохранение описи РВЛ
    override suspend fun getSaveRvlInventory(saveRvlInventoryModel: SaveRvlInventoryModel): Result<Response<Int>> {
        return safeApiCall { apiClient.saveRvlInventory(saveRvlInventoryModel) }
    }

    override suspend fun setFattening(fattening: Fattening): Result<Response<Int>> {
        return safeApiCall { apiClient.setFattening(fattening) }
    }

    override suspend fun setDeposit(deposit: Deposit): Result<Response<Int>> {
        return safeApiCall { apiClient.setDeposit(deposit) }
    }

    override fun getCurrentIszh(): Iszh {
        return iszh
    }

    override fun setCurrentIszh(newIszh: Iszh) {
        iszh = newIszh
    }

    override fun getCurrentAnimal(): RegAnimal {
        return animal
    }

    override fun setCurrentAnimal(newAnimal: RegAnimal) {
        animal = newAnimal
    }

    override suspend fun getResearchById(
        animalId: Int,
        culture: Int
    ): List<AnimalResearchModelItem> {
        return apiClient.getAnimalResearchById(animalId, culture)
    }

    override suspend fun getImmunKindById(
        animalId: Int,
        culture: Int
    ): List<AnimalPreventiveActionModelItem> {

        return apiClient.getAnimalPreventiveActionById(animalId, culture)

    }
}