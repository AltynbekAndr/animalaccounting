package kz.putinbyte.iszhfermer.model.interactors


import android.content.Context
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
import kz.putinbyte.iszhfermer.model.data.server.Result
import kz.putinbyte.iszhfermer.model.repository.IReferencesRepository
import kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.instruments.Units
import retrofit2.Response
import javax.inject.Inject

class ReferencesInteractor @Inject constructor(
    private val referencesRepository: IReferencesRepository,
    private val context: Context
) {

    var phoneNumber: String = ""
    private val TAG = javaClass.simpleName

    // online dates for register animals
    private var animalsByRegion: Response<Region>? = null
    private var kindsOfAnimalCache: List<KindOfAnimal> = listOf()
    private var countryCache: List<Country> = listOf()
    private var identityCache: List<Identity> = listOf()
    private var katoCache: List<Kato> = listOf()
    private var allMastCache: List<AnimalMastItem> = listOf()
    private var allSicknessCache: List<SicknessKindItem> = listOf()
    private var allSicknessCauseCache: List<SicknessCauseItem> = listOf()
    private var allResearchKindCache: List<ResearchKindItem> = listOf()
    private var allResearchResultCache: List<ResearchResultItem> = listOf()
    private var allImmunTypeCache: List<BaseFormatReferences> = listOf()


    // online dates for register animals

    private var doctorTypes: DoctorType? = null


    // offline data
    suspend fun getKindsOfAnimals(): List<KindOfAnimal> {
        if (kindsOfAnimalCache.isNullOrEmpty()) {
            val result = referencesRepository.getKindOfAnimals()
            if (result is Result.Success) kindsOfAnimalCache = result.data
        }
        return kindsOfAnimalCache
    }

    suspend fun getCountries(): List<Country> {
        if (countryCache.isNullOrEmpty()) {
            val result = referencesRepository.getCountries()
            if (result is Result.Success) countryCache = result.data
        }
        return countryCache
    }

    suspend fun getTypeIdentifications(): List<Identity> {
        if (identityCache.isNullOrEmpty()) {
            val result = referencesRepository.getTypeIdentification()
            if (result is Result.Success) identityCache = result.data
        }
        return identityCache
    }

    suspend fun getRegions(): List<Kato> {
        if (katoCache.isNullOrEmpty()) {
            val result = referencesRepository.getRegions()
            if (result is Result.Success) katoCache = result.data
        }
        return katoCache
    }

    suspend fun getDoctorType(): DoctorType {
        if (doctorTypes == null) {
            val result = referencesRepository.getDoctorsType()
            if (result is Result.Success) doctorTypes = result.data
        }
        return doctorTypes!!
    }

    suspend fun getSicknesses(): List<SicknessKindItem> {
        if (allSicknessCache.isNullOrEmpty()) {
            val result = referencesRepository.getSicknesses()
            if (result is Result.Success) allSicknessCache = result.data
        }
        return allSicknessCache
    }

    suspend fun getSicknessCause(): List<SicknessCauseItem> {
        if (allSicknessCauseCache.isNullOrEmpty()) {
            val result = referencesRepository.getSicknessCause()
            if (result is Result.Success) allSicknessCauseCache = result.data
        }
        return allSicknessCauseCache
    }

    suspend fun getResearchKind(): List<ResearchKindItem> {
        if (allResearchKindCache.isNullOrEmpty()) {
            val result = referencesRepository.getResearchKind()
            if (result is Result.Success) allResearchKindCache = result.data
        }
        return allResearchKindCache
    }


    suspend fun getResearchResult(): List<ResearchResultItem> {
        if (allResearchResultCache.isNullOrEmpty()) {
            val result = referencesRepository.getResearchResult()
            if (result is Result.Success) allResearchResultCache = result.data
        }
        return allResearchResultCache
    }

    suspend fun getImmunKind(): List<BaseFormatReferences> {
        if (allImmunTypeCache.isNullOrEmpty()) {
            val result = referencesRepository.getImmunKind()
            if (result is Result.Success) allImmunTypeCache = result.data
        }
        return allImmunTypeCache
    }


    suspend fun dataSynchronization() {
        referencesRepository.setSyncValue(true)
        getKindsOfAnimals()
        getDoctorType()
        getCountries()
        getSicknesses()
        getSicknessCause()
        getRegions()
        getResearchKind()
        getTypeIdentifications()
        getImmunKind()
        referencesRepository.setSyncValue(false)
    }

    // online Data
    suspend fun getRegisteredAnimals(): Response<Region>? {
        if (animalsByRegion == null) {
            val result = referencesRepository.getRegisteredAnimals()
            result
            if (result is Result.Success) animalsByRegion = result.data
        }
        return animalsByRegion
    }

    suspend fun getRegAnimals(): Result<Response<Region>>{
        return if (animalsByRegion == null) {
            val result = referencesRepository.getRegisteredAnimals()
            if (result is Result.Success) animalsByRegion = result.data
            result
        } else {
            Result.Success(animalsByRegion!!)
        }
    }

    suspend fun getRegAnimalsByKato(kato: Long): Result<Response<Region>> = referencesRepository.getRegisteredAnimalsByKato(kato)

    suspend fun getRegisteredAnimalsByKato(kato: Long): Response<Region>? {
        val result = referencesRepository.getRegisteredAnimalsByKato(kato)
        return if (result is Result.Success) result.data else null
    }

    suspend fun getResearchKindType(): List<ResearchKindItem>? {
        val result = referencesRepository.getResearchKind()
        return if (result is Result.Success) result.data else null
    }

    suspend fun getAnimalInfoById(animalId: Int): RegAnimal? {
        val result = referencesRepository.getAnimalInfoById(animalId)
        return if (result is Result.Success) result.data else null

    }

    suspend fun getAnimalMast(animalKindId: Int?, directionId: Int?): List<AnimalMastItem> {
        val result = referencesRepository.getAnimalMast(animalKindId, directionId)
        return if (result is Result.Success) result.data else listOf()
    }

    suspend fun getAnimalSicknessById(
        animalId: Int,
        culture: Int
    ): Result<List<AnimalSicknessModelItem>> =
        referencesRepository.getAnimalSicknessById(animalId, culture)

    suspend fun getAnimalFatteningById(
        animalId: Int
    ): Result<List<FatteningList>> =
        referencesRepository.getAnimalFatteningById(animalId)

    suspend fun getDepositById(
        animalId: Int
    ): Result<List<DepositList>> =
        referencesRepository.getAnimalDepositById(animalId)

    suspend fun getHistoryById(
        animalId: Int
    ): Result<List<HistoryList>> =
        referencesRepository.getAnimalHistoryById(animalId)

    suspend fun getSuccessById(
        animalId: Int
    ): Result<List<SuccessList>> =
        referencesRepository.getSuccessById(animalId)

    suspend fun getCanceledById(
        animalId: Int
    ): Result<List<SuccessList>> =
        referencesRepository.getCanceledById(animalId)

    suspend fun getReplaceInjsById(animalId: Int): Result<List<ReplaceInjList>> =
        referencesRepository.getReplaceInjsById(animalId)

    suspend fun getAnimalResearchById(
        animalId: Int,
        culture: Int
    ): Result<List<AnimalResearchModelItem>> =
        referencesRepository.getAnimalResearchById(animalId, culture)

    suspend fun setPhysical(physical: Physical): Result<PhysicalResponse> =
        referencesRepository.setPhysical(physical)

    suspend fun setJuridical(juridical: Juridical): Result<PhysicalResponse> =
        referencesRepository.setJuridical(juridical)

    suspend fun getAnimalPreventionById(
        animalId: Int,
        culture: Int
    ): Result<List<AnimalPreventiveActionModelItem>> =
        referencesRepository.getAnimalPreventionById(animalId, culture)


    suspend fun getAllDirection(): List<BaseFormat>? {
        val result = referencesRepository.getAllDirections()
        return if (result is Result.Success) result.data else null
    }

    suspend fun getDirectionById(animalKindId: Int?): List<BaseFormat> {
        val result = referencesRepository.getDirectionById(animalKindId)
        return if (result is Result.Success) result.data else listOf()
    }

    suspend fun getAnimalAllMast(): List<AnimalMastItem> {
        if (allMastCache.isNullOrEmpty()) {
            val result = referencesRepository.getAllMast()
            if (result is Result.Success) allMastCache = result.data
        }
        return allMastCache
    }

    suspend fun getAlOwners(
        searchName: String?, searchIin: Long?, ownersList: OwnersResponse
    ): Result<OwnersList> {
        return referencesRepository.getAllOwners(searchName, searchIin, ownersList)
    }

    suspend fun getOfflineOwners() = referencesRepository.getAllOfflineOwners()


    suspend fun findOwnerByINN(
        searchIin: Long?
    ): Result<OwnersList> {
        return referencesRepository.findOwnerByINN(
            searchIin,
            OwnersResponse(page = 1, pageSize = 20, paged = true, "")
        )
    }

    suspend fun getOwnersByKato(
        kato: Int,
    ): OwnersListByKato? {
        val request = OwnersByKato(katoId = kato)
        val result = referencesRepository.getOwnersByKato(request)
        return if (result is Result.Success) result.data else null
    }

    suspend fun getAnimalsByOwner(
        katoId: Int,
        ownerId: Int,
        animalKind: String
    ): Result<AnimalList> {
        val request = AnimalListBody(null, 0, 1000)
        return referencesRepository.getAnimalsByOwner(katoId, ownerId, animalKind, request)
    }

    suspend fun getFindAnimals(multiSearch: MultiSearchSearch, pageIndex: Int): SearchResponse? {
        val request = AnimalListBody(multiSearch, pageIndex, 10)
        val result = referencesRepository.getFindAnimals(request)
        return if (result is Result.Success) result.data else null
    }

    suspend fun getOwnersById(ownerId: Int): Result<Owners> {
      return  referencesRepository.getOwnersById(ownerId)
    }

    suspend fun getKatoById(katoId: Int?): Kato? {
        val result = referencesRepository.getkatoById(katoId)
        return if (result is Result.Success) result.data else null
    }

    suspend fun getUserbyId(id: Int): UserInfoList.UserInfo {
        return referencesRepository.getUserById(id)
    }

    suspend fun getAllUsers(): UserInfoList? {
        val request = AnimalListBody(null, 0, 1000)
        val result = referencesRepository.getAllUsers(request)
        return if (result is Result.Success) result.data else null
    }

    suspend fun setReplaceInj(replaceInj: ReplaceInj) =
        referencesRepository.setReplaceInj(replaceInj)

    suspend fun setUnregister(unregister: List<Unregister>) =
        referencesRepository.setUnregister(unregister)

    suspend fun getFatteningSquares(): FatteningSquare? {
        val request = AnimalListBody(null, 0, 100)
        val result = referencesRepository.getFatteningSquare(request)
        return if (result is Result.Success) result.data else null
    }

    suspend fun getPruction(): List<BaseFormat> {
        return referencesRepository.getProuction()
    }

    suspend fun getProperty(): List<BaseFormat> {
        return referencesRepository.getProperty()
    }

    suspend fun getEnterprise(): List<BaseFormat> {
        return referencesRepository.getEnterprise()
    }

    suspend fun getUnits(): List<BaseFormat> {
        return referencesRepository.getUnits()
    }

    suspend fun setUnits(units: Units) =
        referencesRepository.setUnits(units)

   suspend fun getTypeProperty(): Result<List<PropertyType>> {
       return referencesRepository.getTypeProperty()
    }


}