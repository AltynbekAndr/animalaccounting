package kz.putinbyte.iszhfermer.model.data.server

import kz.putinbyte.iszhfermer.entities.BaseFormat
import kz.putinbyte.iszhfermer.entities.animals.*
import kz.putinbyte.iszhfermer.entities.animals.owners.OwnersList
import kz.putinbyte.iszhfermer.entities.animals.owners.OwnersResponse
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInj
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.entities.db.Identity
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.animals.rvl.SaveRvlInventoryModel
import kz.putinbyte.iszhfermer.entities.animals.search.SearchResponse
import kz.putinbyte.iszhfermer.entities.animals.AnimalMastItem
import kz.putinbyte.iszhfermer.entities.animals.deposit.Deposit
import kz.putinbyte.iszhfermer.entities.animals.deposit.DepositList
import kz.putinbyte.iszhfermer.entities.animals.fattening.Fattening
import kz.putinbyte.iszhfermer.entities.animals.fattening.FatteningList
import kz.putinbyte.iszhfermer.entities.animals.fattening.FatteningSquare
import kz.putinbyte.iszhfermer.entities.animals.history.HistoryList
import kz.putinbyte.iszhfermer.entities.animals.individuals.*
import kz.putinbyte.iszhfermer.entities.animals.replaceinj.ReplaceInjList
import kz.putinbyte.iszhfermer.entities.animals.success.SuccessList
import kz.putinbyte.iszhfermer.entities.animals.unregister.Unregister
import kz.putinbyte.iszhfermer.entities.db.BaseFormatReferences
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.ResearchKindItem
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessCauseItem
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.entities.db.places.Kato
import kz.putinbyte.iszhfermer.entities.network.*
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.entities.requests.OwnersByKato
import kz.putinbyte.iszhfermer.entities.requests.PropertyType
import kz.putinbyte.iszhfermer.entities.requests.UserInfoList
import kz.putinbyte.iszhfermer.entities.research.AnimalResearchModelItem
import kz.putinbyte.iszhfermer.entities.research.GroupAnimalResearch
import kz.putinbyte.iszhfermer.entities.sickness.*
import kz.putinbyte.iszhfermer.ui.rvl.create.bottomFragment.instruments.Units
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.http.*

interface ApiClient {

    // Справочник вид болезни"
    @GET("admin/api/DicSickness/Get")
    suspend fun getSicknessType(): List<SicknessKindItem>

    // Справочник вид животного"
    @GET("admin/api/DicAnimalKind/Get")
    suspend fun getAnimalKind(): List<KindOfAnimal>

    //Справочник по Id направлений животных"
    @GET("admin/api/DicDirection/Get")
    suspend fun getAllDirectionById(
        @Query("animalKindId") animalKindId: Int?
    ): List<BaseFormat>

    //Справочник всех направлений
    @GET("admin/api/DicDirection/GetAll")
    suspend fun getAllDirection(): List<BaseFormat>

    //Справочник вид иммунизации"
    @GET("admin/api/DicImmunKind/Get")
    suspend fun getImmunKind(): List<BaseFormatReferences>

    // Справочник всех докторов
    @GET("admin/api/Doctor")
    suspend fun getDoctorType(
        @Query("PageSize")pageSize: Int? = 1000
    ): DoctorType

    // Справочник вид исследование"
    @GET("admin/api/DicResearchKind/Get")
    suspend fun getResearchKind(): List<ResearchKindItem>

    // Справочник результатов исследование
    @GET("admin/api/DicRvlReason/GetRvlTubeResult")
    suspend fun getResearchResult(): List<ResearchResultItem>

    // Справочник страна происхождение
    @GET("admin/api/DicCounty/Get")
    suspend fun getAllCountries(): List<Country>

    //Справочник тип идентификации"
    @GET("admin/api/DicIdent/Get")
    suspend fun getIdentification(): List<Identity>

    // порода
    @GET("admin/api/DicMast/Get")
    suspend fun getAnimalMast(
        @Query("AnimalKindId") animalKindId: Int?,
        @Query("DirectionId") directionId: Int?
    ): List<AnimalMastItem>

    // справочник пород
    @GET("admin/api/DicMast/GetAll")
    suspend fun getAllMast(): List<AnimalMastItem>

    // Registry api
    @POST("registry/api/RegisteredAnimals/GetListAnimalByIndividual?KatoId=3&IndividualId=1&AnimalKind=Cattle&Culture=2")
    suspend fun getListAnimals(@Body individualListAnimal: IndividualListAnimal): AnimalList

    // Информация о животном
    @GET("registry/api/RegisteredAnimals/GetAnimalInfo")
    suspend fun getAnimalInfo(
        @Query("animalId") animalId: Int?
    ): RegAnimal

    // Список откорм
    @GET("registry/api/Fattening/GetAnimalFattening")
    suspend fun getAnimalsFatteningList(
        @Query("animalId") animalId: Int
    ): List<FatteningList>

    // Добавление откорм
    @POST("registry/api/Fattening/AddAnimalFattening")
    suspend fun setFattening(
        @Body fattening: Fattening
    ): Response<Int>

    // Список откорм площадок
    @POST("admin/api/DicArea/GetAreas")
    suspend fun getFatteningSquare(
        @Query("type") type: Int,
        @Body square: AnimalListBody
    ): FatteningSquare

    // Справочник исход(убой,падеж)
    @GET("registry/api/PreventiveActions/GetSicknessCause")
    suspend fun getSicknessCause(): List<SicknessCauseItem>

    // Болезни животного
    @GET("registry/api/PreventiveActions/ListAnimalSickness")
    suspend fun getAnimalSicknessById(
        @Query("animalId") animalId: Int,
        @Query("culture") culture: Int
    ): List<AnimalSicknessModelItem>

    // Исследование животного
    @GET("registry/api/PreventiveActions/ListAnimalResearch")
    suspend fun getAnimalResearchById(
        @Query("animalId") animalId: Int,
        @Query("culture") culture: Int
    ): List<AnimalResearchModelItem>

    // Профилактика животного
    @GET("registry/api/PreventiveActions/ListAnimalPreventiveAction")
    suspend fun getAnimalPreventiveActionById(
        @Query("animalId") animalId: Int,
        @Query("culture") culture: Int
    ): List<AnimalPreventiveActionModelItem>

    // Добавление болезнь
    @POST("registry/api/GroupPreventiveActions/AddAnimalSickness")
    suspend fun setAnimalSickness(@Body animalAddSicknesses: GroupAnimalSickness): GroupPreventiveActions

    // Добавление исследование
    @POST("registry/api/GroupPreventiveActions/AddAnimalResearch")
    suspend fun setAnimalResearch(@Body animalAddSicknesses: GroupAnimalResearch): GroupResearchActions

    // Добавление профилактики
    @POST("registry/api/GroupPreventiveActions/AddAnimalPreventiveActions")
    suspend fun setPrevention(@Body animalAddSickness: GroupAnimalPrevention): GroupPreventionActions

    // Справочник регионов
    @GET("admin/api/DicKato/GetKatoByUserId")
    suspend fun getRegionsByUserId(): List<BaseFormat>

    @GET("admin/api/DicKato/GetKato")
    suspend fun getRegions(): List<Kato>

    // Справочник като по ID
    @GET("admin/api/DicKato/GetKatoById")
    suspend fun getKatoById(
        @Query("id") katoId: Int
    ): Kato

    // Добавления животного
    @POST("registry/api/GroupRegisterAnimal/PrimaryRegisterAnimals")
    suspend fun setAnimals(@Body addAnimalModel: AddAnimalModel): AddAnimalModel

    /////////////////////////////////////////////
    //Справочник всех владельцев
    @POST("admin/api/Individual/GetPhysicalIndividuals")
    suspend fun getAllOwners(
        @Query("searchName") searchName: String?,
        @Query("searchIin") searchIin: Long?,
        @Body ownersList: OwnersResponse
    ): OwnersList

    // Список владельцев по ИНН
    @POST("admin/api/Individual/GetPhysicalIndividuals")
    suspend fun findOwnerByINN(
        @Query("searchIin") searchIin: Long?,
        @Body ownersList: OwnersResponse
    ): OwnersList


    // Список владельцев по ID
    @GET("admin/api/Individual/GetPhysicalIndividualById")
    suspend fun getOwnersById(
        @Query("id") ownerId: Int
    ): Owners

    // список владельцев по КАТО
    @POST("registry/api/RegisteredAnimals/GetindividualsByKato")
    suspend fun getOwnersByKato(
        @Query("PageIndex") PageIndex: Int = 0,
        @Query("PageSize") PageSize: Int = 1000,
        @Body request: OwnersByKato
    ): OwnersListByKato
/////////////////////////////////////////////////////


    // Список животных по владельцам
    @POST("registry/api/RegisteredAnimals/GetListAnimalByIndividual")
    suspend fun getAnimalsByOwner(
        @Body animalListBody: AnimalListBody,
        @Query("KatoId") katoId: Int,
        @Query("IndividualId") ownerId: Int,
        @Query("AnimalKind") animalKind: String,
        @Query("Culture") culture: Int = 2
    ): AnimalList

    // Замена ИНЖ
    @POST("registry/api/RegisteredAnimals/ReplaceInj")
    suspend fun setReplaceInj(@Body replaceInj: ReplaceInj): Response<Unit>

    // Список заменных ИНЖ
    @GET("registry/api/RegisteredAnimals/GetReplaceInjs")
    suspend fun getReplacedInjsById(
        @Query("animalId") animalId: Int
    ): List<ReplaceInjList>

    // Получение списка животных для РВЛ
    @POST("registry/api/Rvl/GetRvlAnimalList")
    suspend fun listAnimals(
        @Query("isSPK") isSPK: Boolean?,
        @Query("isDead") isDead: Boolean?,
        @Body animalsModel: AnimalsModel?
    ): ListAnimalsModel

    //Получение крови
    @GET("admin/api/DicRvlReceivedSampleName/Get")
    suspend fun getReceivedSampleName(): List<ReceivedSampleNameModel>

    //Доктор
    @GET("admin/api/Doctor")
    suspend fun getDoctor(
        @Query("Column") column: String?,
        @Query("Search") search: String?,
        @Query("PageIndex") pageIndex: Int?,
        @Query("PageSize") pageSize: Int?,
    ): Doctor

    //Филиал лаборатории и пунктов приема проб
    @GET("admin/api/DicRvlBranch/Get")
    suspend fun getDicRvlBranch(): List<DicRvlBranchModel>

    //Израсходованные материалы
    @GET("admin/api/DicRvlInstrument/Get")
    suspend fun getDicRvlInstrument(): List<DicRvlInstrumentModel>

    // Ед измерения
    @GET("admin/api/DicUnit/Get")
    suspend fun getUnits():List<BaseFormat>

    // Добавление ед измерения
    @POST("admin/api/DicRvlInstrument/Add")
    suspend fun setUnits(
        @Body units: Units
    ):Int

    //Справочник: Като
    @GET("admin/api/DicKato/GetKato")
    suspend fun getKato(
        @Query("kato") kato: Int?,
    ): List<Kato>

    // Сохранение описи РВЛ
    @POST("registry/api/Rvl/SaveRvlInventory")
    suspend fun saveRvlInventory(
        @Body saveRvlInventoryModel: SaveRvlInventoryModel?
    ): Response<Int>

    //Справочник: Вид животного
    @GET("admin/api/DicAnimalKind/Get")
    suspend fun getTypeAnimal(): List<TypeAnimalModel>

    //Области
    @GET("registry/api/RegisteredAnimals")
    suspend fun getRegisteredAnimals(@Query("culture") culture: Int = 2): Response<Region>

    @GET("registry/api/RegisteredAnimals/ListCountByKato")
    suspend fun getRegisteredAnimalsByKato(
        @Query("kato") kato: Long,
        @Query("isSPK") isSPK: Boolean = false,
        @Query("culture") culture: Int = 2
    ): Response<Region>

    // Поиск животного
    @POST("registry/api/RegisteredAnimals/FindAnimals")
    suspend fun findAnimals(
        @Body findAnimals: AnimalListBody
    ): SearchResponse

    // Деталь пользователя по ID
    @GET("identity/getById")
    suspend fun getUserInfoById(
        @Query("id") userId: Int
    ): UserInfoList.UserInfo

    // Справочник всех пользователей
    @POST("identity/get")
    suspend fun getAllUsers(
        @Body body: AnimalListBody
    ): UserInfoList

    // Снятие с учета
    @POST("registry/api/GroupUnRegisterAnimal/UnRegisterAnimals")
    suspend fun setUnregisterAnimals(
        @Body unregister: List<Unregister>
    ): List<Unregister>

    // Список залогов
    @GET("registry/api/Pledge/GetPledges")
    suspend fun getDepositById(
        @Query("animalId") animalId: Int
    ): List<DepositList>

    //Добавить Залог
    @POST("registry/api/Pledge/AddPledge")
    suspend fun setDeposit(
        @Body deposit: Deposit
    ): Response<Int>

    @POST("registry/api/File/UploadFile")
    suspend fun uploadFile(
        @Body string: MultipartBody
    ): Response<Int>

    // История
    @GET("registry/api/AnimalHistory/GetAnimalHistory")
    suspend fun getAnimalHistoryById(
        @Query("animalId") animalId: Int,
        @Query("culture") culture: Int
    ): List<HistoryList>

    @GET("registry/api/Rvl/GetRvlSuccesResults")
    suspend fun getDiagnosticSuccess(
        @Query("animalId") animalId: Int
    ): List<SuccessList>

    @GET("registry/api/Rvl/GetRvlСanceledResults")
    suspend fun getDiagnosticCanceled(
        @Query("animalId") animalId: Int
    ): List<SuccessList>

    // Список физ лица
    @POST("admin/api/Individual/GetPhysicalIndividuals")
    suspend fun getPhysicalList(
        @Body ownersBody: OwnersBody
    ): PhysicalList

    // Добавление физ лица
    @POST("admin/api/Individual/AddIndividualPhysical")
    suspend fun setPhysical(
        @Body physical: Physical
    ): PhysicalResponse

    // Справочник вид собственности
    @GET("admin/api/DicOpf/GetPhysicOpf")
    suspend fun getTypeProperty():List<PropertyType>

    // Добавление юр лица
    @POST("admin/api/Individual/AddIndividualJuridical")
    suspend fun setJuridical(
        @Body juridical: Juridical
    ): PhysicalResponse

    // Справочник вид производства
    @GET("admin/api/DicOked/Get")
    suspend fun getProduction(): List<BaseFormat>

    // Справочник вид собственности
    @GET("admin/api/DicOpf/Get")
    suspend fun getPropertyType(): List<BaseFormat>

    // Справочник тип предприятия
    @GET("admin/api/DicEnterpriseType/Get")
    suspend fun getEnterpriseType(): List<BaseFormat>

    // Запрос получения данных по ИИН
    @GET("integration/api/Service/GetFL")
    suspend fun searchOwnersByIin(
        @Query("identifier") inn:Int
    )

}