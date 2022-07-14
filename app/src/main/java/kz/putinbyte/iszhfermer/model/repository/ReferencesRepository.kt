package kz.putinbyte.iszhfermer.model.repository

import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.network.*
import kz.putinbyte.iszhfermer.entities.animals.owners.OwnersList
import kz.putinbyte.iszhfermer.entities.animals.owners.OwnersResponse
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.animals.search.SearchResponse
import kz.putinbyte.iszhfermer.entities.animals.AnimalMastItem
import kz.putinbyte.iszhfermer.entities.animals.deposit.DepositList
import kz.putinbyte.iszhfermer.entities.animals.fattening.FatteningList
import kz.putinbyte.iszhfermer.entities.animals.fattening.FatteningSquare
import kz.putinbyte.iszhfermer.entities.animals.history.HistoryList
import kz.putinbyte.iszhfermer.entities.animals.individuals.*
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInj
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInjList
import kz.putinbyte.iszhfermer.entities.animals.success.SuccessList
import kz.putinbyte.iszhfermer.entities.animals.unregister.Unregister
import kz.putinbyte.iszhfermer.entities.db.BaseFormatReferences
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.ResearchKindItem
import kz.putinbyte.iszhfermer.entities.db.places.Kato
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.entities.requests.OwnersByKato
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessCauseItem
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.entities.requests.PropertyType
import kz.putinbyte.iszhfermer.entities.requests.UserInfoList
import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import kz.putinbyte.iszhfermer.model.data.db.dao.*
import kz.putinbyte.iszhfermer.model.data.server.ApiClient
import kz.putinbyte.iszhfermer.model.data.server.Result
import kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.instruments.Units
import kz.putinbyte.iszhfermer.utils.LogUtils
import retrofit2.Response
import javax.inject.Inject

class ReferencesRepository @Inject constructor(
    private val apiClient: ApiClient,
    private val kindOfAnimalDao: KindOfAnimalDao,
    private val countryDao: CountryDao,
    private val identityDao: IdentityDao,
    private val katoDao: KatoDao,
    private val doctorTypesDao: DoctorTypesDao,
    private val sicknessDao: SicknessDao,
    private val researchDao: ResearchDao,
    private val referencesDao: BaseFormatReferencesDao,
    private val ownersDao: OwnersDao
) : IReferencesRepository, BaseRepository() {

    var sync: Boolean = false
    val TAG = javaClass.simpleName

    // offline dates

    override suspend fun setSyncValue(syncValue: Boolean) {
        sync = syncValue
    }

    override suspend fun getKindOfAnimals(): Result<List<KindOfAnimal>> {
        val data = kindOfAnimalDao.getAll()
        return when {
            !data.isNullOrEmpty() && !sync -> {
                Result.Success(data)
            }
            else -> {
                val result = safeApiCall { apiClient.getAnimalKind() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        kindOfAnimalDao.insertAll(result.data)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }

    override suspend fun getCountries(): Result<List<Country>> {
        val data = countryDao.getAll()
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(data)
            else -> {
                val result = safeApiCall { apiClient.getAllCountries() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        countryDao.insertAll(result.data)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }

    override suspend fun getTypeIdentification(): Result<List<Identity>> {
        val data = identityDao.getAll()
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(data)
            else -> {
                val result = safeApiCall { apiClient.getIdentification() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        identityDao.insertAll(result.data)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }

    override suspend fun getRegions(): Result<List<Kato>> {
        val data = katoDao.getAll()
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(data)
            else -> {
                val result = safeApiCall { apiClient.getRegions() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        katoDao.insertAll(result.data)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }

    override suspend fun getDoctorsType(): Result<DoctorType> {
        val data = doctorTypesDao.getAll()
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(
                DoctorType(
                    count = data.count(),
                    lists = data
                )
            )
            else -> {
                val result = safeApiCall { apiClient.getDoctorType() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        doctorTypesDao.insertAll(result.data.lists)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }

    override suspend fun getSicknesses(): Result<List<SicknessKindItem>> {
        val data = sicknessDao.getAllSicknessKindItem()
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(data)
            else -> {
                val result = safeApiCall { apiClient.getSicknessType() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        sicknessDao.insertAllSicknessKindItem(result.data)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }

    override suspend fun getSicknessCause(): Result<List<SicknessCauseItem>> {
        val data = sicknessDao.getAllSicknessCause()
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(data)
            else -> {
                val result = safeApiCall { apiClient.getSicknessCause() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        sicknessDao.insertAllSicknessCause(result.data)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }


    override suspend fun getResearchKind(): Result<List<ResearchKindItem>> {

        val data = researchDao.getAllResearchKind()
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(data)
            else -> {
                val result = safeApiCall { apiClient.getResearchKind() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        researchDao.insertAllResearchKind(result.data)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }

    override suspend fun getResearchResult(): Result<List<ResearchResultItem>> {
        val data = researchDao.getAllResearchResult()
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(data)
            else -> {
                val result = safeApiCall { apiClient.getResearchResult() }
                if (result is Result.Success) GlobalScope.launch {
                    try {
                        researchDao.insertAllResearchResult(result.data)
                    } catch (e: Exception) {
                        LogUtils.error(TAG, e.message)
                    }
                }
                result
            }
        }
    }

    private suspend fun saveReferences(
        type: BaseFormatReferences.ReferenceType,
        dataList: List<BaseFormatReferences>
    ) {
        try {
            val references = dataList.map { it.copy(type = type) }
            referencesDao.insertAll(references)
        } catch (e: Exception) {
            LogUtils.error(TAG, e.message)
        }
    }

    override suspend fun getImmunKind(): Result<List<BaseFormatReferences>> {
        val rType = BaseFormatReferences.ReferenceType.IMMUN
        val data = referencesDao.getAllByType(referenceType = rType)
        return when {
            !data.isNullOrEmpty() && !sync -> Result.Success(data)
            else -> {
                val result = safeApiCall { apiClient.getImmunKind() }
                if (result is Result.Success) GlobalScope.launch {
                    saveReferences(type = rType, result.data)
                }
                result
            }
        }
    }

    // online Date

    override suspend fun getAllMast(): Result<List<AnimalMastItem>> = safeApiCall {
        apiClient.getAllMast()
    }

    override suspend fun getRegisteredAnimals(): Result<Response<Region>> =
        safeApiCall { apiClient.getRegisteredAnimals() }

    override suspend fun getRegisteredAnimalsByKato(kato: Long): Result<Response<Region>> =
        safeApiCall { apiClient.getRegisteredAnimalsByKato(kato) }

    override suspend fun getOwnersByKato(request: OwnersByKato): Result<OwnersListByKato> =
        safeApiCall { apiClient.getOwnersByKato(request = request) }


    override suspend fun getDoctorsType(offline: Boolean): Result<DoctorType> =
        safeApiCall { apiClient.getDoctorType() }


    override suspend fun getAnimalInfoById(animalId: Int): Result<RegAnimal> =
        safeApiCall { apiClient.getAnimalInfo(animalId) }


    override suspend fun getAllDirections(): Result<List<BaseFormat>> =
        safeApiCall { apiClient.getAllDirection() }

    override suspend fun getDirectionById(animalKindId: Int?): Result<List<BaseFormat>> =
        safeApiCall { apiClient.getAllDirectionById(animalKindId) }

    override suspend fun getAllOwners(
        searchName: String?, searchIin: Long?, ownersList: OwnersResponse
    ): Result<OwnersList> =
        safeApiCall { apiClient.getAllOwners(searchName, searchIin, ownersList) }

    override suspend fun getAnimalsByOwner(
        katoId: Int,
        ownerId: Int,
        animalKind: String,
        request: AnimalListBody
    ): Result<AnimalList> =
        safeApiCall { apiClient.getAnimalsByOwner(request, katoId, ownerId, animalKind) }

    override suspend fun findOwnerByINN(
        searchIin: Long?,
        ownersList: OwnersResponse
    ): Result<OwnersList> =
        safeApiCall {
            apiClient.findOwnerByINN(searchIin, ownersList)
        }

    override suspend fun getAllOfflineOwners(): Result<List<Owners>> {
        val offlineOwners = ownersDao.getAll()
        return if (offlineOwners.isNullOrEmpty())
            Result.Error(Exception("Маршрут не установлен"))
        else
            Result.Success(offlineOwners)
    }

    override suspend fun getFindAnimals(request: AnimalListBody): Result<SearchResponse> {
        return safeApiCall { apiClient.findAnimals(request) }
    }

    override suspend fun getOwnersById(ownerId: Int): Result<Owners> {
        return safeApiCall { apiClient.getOwnersById(ownerId) }
    }

    override suspend fun getkatoById(katoId: Int?): Result<Kato> {
        return safeApiCall { apiClient.getKatoById(katoId!!) }
    }

    override suspend fun getUserById(id: Int): UserInfoList.UserInfo {
        return apiClient.getUserInfoById(id)
    }

    override suspend fun getAllUsers(body: AnimalListBody): Result<UserInfoList> {
        return safeApiCall { apiClient.getAllUsers(body) }
    }

    override suspend fun getAnimalMast(
        animalKindId: Int?,
        directionId: Int?
    ): Result<List<AnimalMastItem>> =
        safeApiCall { apiClient.getAnimalMast(animalKindId, directionId) }

    override suspend fun getAnimalSicknessById(
        animalId: Int,
        culture: Int
    ): Result<List<AnimalSicknessModelItem>> =
        safeApiCall { apiClient.getAnimalSicknessById(animalId, culture) }


    override suspend fun getAnimalResearchById(
        animalId: Int,
        culture: Int
    ): Result<List<AnimalResearchModelItem>> =
        safeApiCall { apiClient.getAnimalResearchById(animalId, culture) }

    override suspend fun setPhysical(physical: Physical): Result<PhysicalResponse> =
        safeApiCall { apiClient.setPhysical(physical) }

    override suspend fun setJuridical(juridical: Juridical): Result<PhysicalResponse> =
        safeApiCall { apiClient.setJuridical(juridical) }

    override suspend fun getProuction(): List<BaseFormat> {
        return apiClient.getProduction()
    }

    override suspend fun getProperty(): List<BaseFormat> {
        return apiClient.getPropertyType()
    }

    override suspend fun getEnterprise(): List<BaseFormat> {
        return apiClient.getEnterpriseType()
    }

    override suspend fun getUnits(): List<BaseFormat> {
        return apiClient.getUnits()
    }

    override suspend fun getAnimalPreventionById(
        animalId: Int,
        culture: Int
    ): Result<List<AnimalPreventiveActionModelItem>> =
        safeApiCall { apiClient.getAnimalPreventiveActionById(animalId, culture) }

    override suspend fun getReplaceInjsById(animalId: Int): Result<List<ReplaceInjList>> =
        safeApiCall { apiClient.getReplacedInjsById(animalId) }

    override suspend fun setReplaceInj(replaceInj: ReplaceInj) =
        safeApiCall { apiClient.setReplaceInj(replaceInj) }

    override suspend fun setUnregister(unregister: List<Unregister>): Result<List<Unregister>> {
        return safeApiCall { apiClient.setUnregisterAnimals(unregister = unregister) }
    }

    override suspend fun setUnits(units: Units): Result<Int> {
        return safeApiCall { apiClient.setUnits(units) }
    }

    override suspend fun getTypeProperty(): Result<List<PropertyType>> {
        return safeApiCall { apiClient.getTypeProperty() }
    }

    override suspend fun getAnimalFatteningById(animalId: Int): Result<List<FatteningList>> {
        return safeApiCall { apiClient.getAnimalsFatteningList(animalId) }
    }

    override suspend fun getAnimalDepositById(animalId: Int): Result<List<DepositList>> {
        return safeApiCall { apiClient.getDepositById(animalId) }
    }

    override suspend fun getAnimalHistoryById(animalId: Int): Result<List<HistoryList>> {
        return safeApiCall { apiClient.getAnimalHistoryById(animalId, 1) }
    }

    override suspend fun getSuccessById(animalId: Int): Result<List<SuccessList>> {
        return safeApiCall { apiClient.getDiagnosticSuccess(animalId) }
    }

    override suspend fun getCanceledById(animalId: Int): Result<List<SuccessList>> {
        return safeApiCall { apiClient.getDiagnosticCanceled(animalId) }
    }

    override suspend fun getFatteningSquare(request: AnimalListBody): Result<FatteningSquare> {
        return safeApiCall { apiClient.getFatteningSquare(2, request) }
    }


}