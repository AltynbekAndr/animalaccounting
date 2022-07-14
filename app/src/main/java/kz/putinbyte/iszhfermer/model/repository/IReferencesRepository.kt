package kz.putinbyte.iszhfermer.model.repository

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
import kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.instruments.Units
import retrofit2.Response

interface IReferencesRepository {

    suspend fun setSyncValue(syncValue: Boolean)
    suspend fun getSicknesses(): Result<List<SicknessKindItem>>
    suspend fun getDoctorsType(offline: Boolean): Result<DoctorType>
    suspend fun getDoctorsType(): Result<DoctorType>
    suspend fun getImmunKind(): Result<List<BaseFormatReferences>>
    suspend fun getResearchKind(): Result<List<ResearchKindItem>>
    suspend fun getResearchResult(): Result<List<ResearchResultItem>>
    suspend fun getAnimalInfoById(animalId: Int): Result<RegAnimal>
    suspend fun getAnimalMast(animalKindId: Int?, directionId: Int?): Result<List<AnimalMastItem>>
    suspend fun getSicknessCause(): Result<List<SicknessCauseItem>>
    suspend fun getAnimalSicknessById(animalId: Int, culture: Int): Result<List<AnimalSicknessModelItem>>
    suspend fun getReplaceInjsById(animalId: Int): Result<List<ReplaceInjList>>

    suspend fun getAnimalResearchById(animalId: Int, culture: Int): Result<List<AnimalResearchModelItem>>
    suspend fun getAnimalPreventionById(animalId: Int, culture: Int): Result<List<AnimalPreventiveActionModelItem>>
    suspend fun getAllDirections(): Result<List<BaseFormat>>
    suspend fun getDirectionById(animalKindId: Int?): Result<List<BaseFormat>>
    suspend fun getAllMast(): Result<List<AnimalMastItem>>
    suspend fun getRegions(): Result<List<Kato>>
    suspend fun getRegisteredAnimals(): Result<Response<Region>>
    suspend fun getRegisteredAnimalsByKato(kato: Long): Result<Response<Region>>
    suspend fun getTypeIdentification(): Result<List<Identity>>
    suspend fun getKindOfAnimals(): Result<List<KindOfAnimal>>
    suspend fun getCountries(): Result<List<Country>>
    suspend fun getOwnersByKato(request: OwnersByKato): Result<OwnersListByKato>

    suspend fun getAllOwners(searchName: String?, searchIin: Long?, ownersList: OwnersResponse): Result<OwnersList>
    suspend fun findOwnerByINN(searchIin: Long?, ownersList: OwnersResponse): Result<OwnersList>
    suspend fun getAllOfflineOwners(): Result<List<Owners>>

    suspend fun getAnimalsByOwner(katoId: Int, ownerId: Int, animalKind: String, request: AnimalListBody): Result<AnimalList>

    suspend fun getFindAnimals(request: AnimalListBody): Result<SearchResponse>
    suspend fun getOwnersById(ownerId: Int): Result<Owners>
    suspend fun getkatoById(katoId: Int?): Result<Kato>
    suspend fun getUserById(id: Int): UserInfoList.UserInfo
    suspend fun getAllUsers(body:AnimalListBody): Result<UserInfoList>
    suspend fun setReplaceInj(replaceInj: ReplaceInj): Result<Response<Unit>>
    suspend fun setUnregister(unregister: List<Unregister>): Result<List<Unregister>>
    suspend fun getAnimalFatteningById(animalId: Int): Result<List<FatteningList>>
    suspend fun getAnimalDepositById(animalId: Int): Result<List<DepositList>>
    suspend fun getAnimalHistoryById(animalId: Int): Result<List<HistoryList>>
    suspend fun getSuccessById(animalId: Int): Result<List<SuccessList>>
    suspend fun getCanceledById(animalId: Int): Result<List<SuccessList>>
    suspend fun getFatteningSquare(request: AnimalListBody): Result<FatteningSquare>
    suspend fun setPhysical(physical: Physical): Result<PhysicalResponse>
    suspend fun setJuridical(juridical: Juridical): Result<PhysicalResponse>
    suspend fun getProuction(): List<BaseFormat>
    suspend fun getProperty(): List<BaseFormat>
    suspend fun getEnterprise(): List<BaseFormat>
    suspend fun getUnits(): List<BaseFormat>
    suspend fun setUnits(units: Units): Result<Int>
    suspend fun getTypeProperty(): Result<List<PropertyType>>
}