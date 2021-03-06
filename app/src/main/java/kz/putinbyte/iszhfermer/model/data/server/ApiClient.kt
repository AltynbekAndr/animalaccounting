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

    // ???????????????????? ?????? ??????????????"
    @GET("admin/api/DicSickness/Get")
    suspend fun getSicknessType(): List<SicknessKindItem>

    // ???????????????????? ?????? ??????????????????"
    @GET("admin/api/DicAnimalKind/Get")
    suspend fun getAnimalKind(): List<KindOfAnimal>

    //???????????????????? ???? Id ?????????????????????? ????????????????"
    @GET("admin/api/DicDirection/Get")
    suspend fun getAllDirectionById(
        @Query("animalKindId") animalKindId: Int?
    ): List<BaseFormat>

    //???????????????????? ???????? ??????????????????????
    @GET("admin/api/DicDirection/GetAll")
    suspend fun getAllDirection(): List<BaseFormat>

    //???????????????????? ?????? ??????????????????????"
    @GET("admin/api/DicImmunKind/Get")
    suspend fun getImmunKind(): List<BaseFormatReferences>

    // ???????????????????? ???????? ????????????????
    @GET("admin/api/Doctor")
    suspend fun getDoctorType(
        @Query("PageSize")pageSize: Int? = 1000
    ): DoctorType

    // ???????????????????? ?????? ????????????????????????"
    @GET("admin/api/DicResearchKind/Get")
    suspend fun getResearchKind(): List<ResearchKindItem>

    // ???????????????????? ?????????????????????? ????????????????????????
    @GET("admin/api/DicRvlReason/GetRvlTubeResult")
    suspend fun getResearchResult(): List<ResearchResultItem>

    // ???????????????????? ???????????? ??????????????????????????
    @GET("admin/api/DicCounty/Get")
    suspend fun getAllCountries(): List<Country>

    //???????????????????? ?????? ??????????????????????????"
    @GET("admin/api/DicIdent/Get")
    suspend fun getIdentification(): List<Identity>

    // ????????????
    @GET("admin/api/DicMast/Get")
    suspend fun getAnimalMast(
        @Query("AnimalKindId") animalKindId: Int?,
        @Query("DirectionId") directionId: Int?
    ): List<AnimalMastItem>

    // ???????????????????? ??????????
    @GET("admin/api/DicMast/GetAll")
    suspend fun getAllMast(): List<AnimalMastItem>

    // Registry api
    @POST("registry/api/RegisteredAnimals/GetListAnimalByIndividual?KatoId=3&IndividualId=1&AnimalKind=Cattle&Culture=2")
    suspend fun getListAnimals(@Body individualListAnimal: IndividualListAnimal): AnimalList

    // ???????????????????? ?? ????????????????
    @GET("registry/api/RegisteredAnimals/GetAnimalInfo")
    suspend fun getAnimalInfo(
        @Query("animalId") animalId: Int?
    ): RegAnimal

    // ???????????? ????????????
    @GET("registry/api/Fattening/GetAnimalFattening")
    suspend fun getAnimalsFatteningList(
        @Query("animalId") animalId: Int
    ): List<FatteningList>

    // ???????????????????? ????????????
    @POST("registry/api/Fattening/AddAnimalFattening")
    suspend fun setFattening(
        @Body fattening: Fattening
    ): Response<Int>

    // ???????????? ???????????? ????????????????
    @POST("admin/api/DicArea/GetAreas")
    suspend fun getFatteningSquare(
        @Query("type") type: Int,
        @Body square: AnimalListBody
    ): FatteningSquare

    // ???????????????????? ??????????(????????,??????????)
    @GET("registry/api/PreventiveActions/GetSicknessCause")
    suspend fun getSicknessCause(): List<SicknessCauseItem>

    // ?????????????? ??????????????????
    @GET("registry/api/PreventiveActions/ListAnimalSickness")
    suspend fun getAnimalSicknessById(
        @Query("animalId") animalId: Int,
        @Query("culture") culture: Int
    ): List<AnimalSicknessModelItem>

    // ???????????????????????? ??????????????????
    @GET("registry/api/PreventiveActions/ListAnimalResearch")
    suspend fun getAnimalResearchById(
        @Query("animalId") animalId: Int,
        @Query("culture") culture: Int
    ): List<AnimalResearchModelItem>

    // ???????????????????????? ??????????????????
    @GET("registry/api/PreventiveActions/ListAnimalPreventiveAction")
    suspend fun getAnimalPreventiveActionById(
        @Query("animalId") animalId: Int,
        @Query("culture") culture: Int
    ): List<AnimalPreventiveActionModelItem>

    // ???????????????????? ??????????????
    @POST("registry/api/GroupPreventiveActions/AddAnimalSickness")
    suspend fun setAnimalSickness(@Body animalAddSicknesses: GroupAnimalSickness): GroupPreventiveActions

    // ???????????????????? ????????????????????????
    @POST("registry/api/GroupPreventiveActions/AddAnimalResearch")
    suspend fun setAnimalResearch(@Body animalAddSicknesses: GroupAnimalResearch): GroupResearchActions

    // ???????????????????? ????????????????????????
    @POST("registry/api/GroupPreventiveActions/AddAnimalPreventiveActions")
    suspend fun setPrevention(@Body animalAddSickness: GroupAnimalPrevention): GroupPreventionActions

    // ???????????????????? ????????????????
    @GET("admin/api/DicKato/GetKatoByUserId")
    suspend fun getRegionsByUserId(): List<BaseFormat>

    @GET("admin/api/DicKato/GetKato")
    suspend fun getRegions(): List<Kato>

    // ???????????????????? ???????? ???? ID
    @GET("admin/api/DicKato/GetKatoById")
    suspend fun getKatoById(
        @Query("id") katoId: Int
    ): Kato

    // ???????????????????? ??????????????????
    @POST("registry/api/GroupRegisterAnimal/PrimaryRegisterAnimals")
    suspend fun setAnimals(@Body addAnimalModel: AddAnimalModel): AddAnimalModel

    /////////////////////////////////////////////
    //???????????????????? ???????? ????????????????????
    @POST("admin/api/Individual/GetPhysicalIndividuals")
    suspend fun getAllOwners(
        @Query("searchName") searchName: String?,
        @Query("searchIin") searchIin: Long?,
        @Body ownersList: OwnersResponse
    ): OwnersList

    // ???????????? ???????????????????? ???? ??????
    @POST("admin/api/Individual/GetPhysicalIndividuals")
    suspend fun findOwnerByINN(
        @Query("searchIin") searchIin: Long?,
        @Body ownersList: OwnersResponse
    ): OwnersList


    // ???????????? ???????????????????? ???? ID
    @GET("admin/api/Individual/GetPhysicalIndividualById")
    suspend fun getOwnersById(
        @Query("id") ownerId: Int
    ): Owners

    // ???????????? ???????????????????? ???? ????????
    @POST("registry/api/RegisteredAnimals/GetindividualsByKato")
    suspend fun getOwnersByKato(
        @Query("PageIndex") PageIndex: Int = 0,
        @Query("PageSize") PageSize: Int = 1000,
        @Body request: OwnersByKato
    ): OwnersListByKato
/////////////////////////////////////////////////////


    // ???????????? ???????????????? ???? ????????????????????
    @POST("registry/api/RegisteredAnimals/GetListAnimalByIndividual")
    suspend fun getAnimalsByOwner(
        @Body animalListBody: AnimalListBody,
        @Query("KatoId") katoId: Int,
        @Query("IndividualId") ownerId: Int,
        @Query("AnimalKind") animalKind: String,
        @Query("Culture") culture: Int = 2
    ): AnimalList

    // ???????????? ??????
    @POST("registry/api/RegisteredAnimals/ReplaceInj")
    suspend fun setReplaceInj(@Body replaceInj: ReplaceInj): Response<Unit>

    // ???????????? ???????????????? ??????
    @GET("registry/api/RegisteredAnimals/GetReplaceInjs")
    suspend fun getReplacedInjsById(
        @Query("animalId") animalId: Int
    ): List<ReplaceInjList>

    // ?????????????????? ???????????? ???????????????? ?????? ??????
    @POST("registry/api/Rvl/GetRvlAnimalList")
    suspend fun listAnimals(
        @Query("isSPK") isSPK: Boolean?,
        @Query("isDead") isDead: Boolean?,
        @Body animalsModel: AnimalsModel?
    ): ListAnimalsModel

    //?????????????????? ??????????
    @GET("admin/api/DicRvlReceivedSampleName/Get")
    suspend fun getReceivedSampleName(): List<ReceivedSampleNameModel>

    //????????????
    @GET("admin/api/Doctor")
    suspend fun getDoctor(
        @Query("Column") column: String?,
        @Query("Search") search: String?,
        @Query("PageIndex") pageIndex: Int?,
        @Query("PageSize") pageSize: Int?,
    ): Doctor

    //???????????? ?????????????????????? ?? ?????????????? ???????????? ????????
    @GET("admin/api/DicRvlBranch/Get")
    suspend fun getDicRvlBranch(): List<DicRvlBranchModel>

    //?????????????????????????????? ??????????????????
    @GET("admin/api/DicRvlInstrument/Get")
    suspend fun getDicRvlInstrument(): List<DicRvlInstrumentModel>

    // ???? ??????????????????
    @GET("admin/api/DicUnit/Get")
    suspend fun getUnits():List<BaseFormat>

    // ???????????????????? ???? ??????????????????
    @POST("admin/api/DicRvlInstrument/Add")
    suspend fun setUnits(
        @Body units: Units
    ):Int

    //????????????????????: ????????
    @GET("admin/api/DicKato/GetKato")
    suspend fun getKato(
        @Query("kato") kato: Int?,
    ): List<Kato>

    // ???????????????????? ?????????? ??????
    @POST("registry/api/Rvl/SaveRvlInventory")
    suspend fun saveRvlInventory(
        @Body saveRvlInventoryModel: SaveRvlInventoryModel?
    ): Response<Int>

    //????????????????????: ?????? ??????????????????
    @GET("admin/api/DicAnimalKind/Get")
    suspend fun getTypeAnimal(): List<TypeAnimalModel>

    //??????????????
    @GET("registry/api/RegisteredAnimals")
    suspend fun getRegisteredAnimals(@Query("culture") culture: Int = 2): Response<Region>

    @GET("registry/api/RegisteredAnimals/ListCountByKato")
    suspend fun getRegisteredAnimalsByKato(
        @Query("kato") kato: Long,
        @Query("isSPK") isSPK: Boolean = false,
        @Query("culture") culture: Int = 2
    ): Response<Region>

    // ?????????? ??????????????????
    @POST("registry/api/RegisteredAnimals/FindAnimals")
    suspend fun findAnimals(
        @Body findAnimals: AnimalListBody
    ): SearchResponse

    // ???????????? ???????????????????????? ???? ID
    @GET("identity/getById")
    suspend fun getUserInfoById(
        @Query("id") userId: Int
    ): UserInfoList.UserInfo

    // ???????????????????? ???????? ??????????????????????????
    @POST("identity/get")
    suspend fun getAllUsers(
        @Body body: AnimalListBody
    ): UserInfoList

    // ???????????? ?? ??????????
    @POST("registry/api/GroupUnRegisterAnimal/UnRegisterAnimals")
    suspend fun setUnregisterAnimals(
        @Body unregister: List<Unregister>
    ): List<Unregister>

    // ???????????? ??????????????
    @GET("registry/api/Pledge/GetPledges")
    suspend fun getDepositById(
        @Query("animalId") animalId: Int
    ): List<DepositList>

    //???????????????? ??????????
    @POST("registry/api/Pledge/AddPledge")
    suspend fun setDeposit(
        @Body deposit: Deposit
    ): Response<Int>

    @POST("registry/api/File/UploadFile")
    suspend fun uploadFile(
        @Body string: MultipartBody
    ): Response<Int>

    // ??????????????
    @GET("registry/api/AnimalHistory/GetAnimalHistory")
    suspend fun getAnimalHistoryById(
        @Query("animalId") animalId: Int,
        @Query("culture") culture: Int
    ): List<HistoryList>

    @GET("registry/api/Rvl/GetRvlSuccesResults")
    suspend fun getDiagnosticSuccess(
        @Query("animalId") animalId: Int
    ): List<SuccessList>

    @GET("registry/api/Rvl/GetRvl??anceledResults")
    suspend fun getDiagnosticCanceled(
        @Query("animalId") animalId: Int
    ): List<SuccessList>

    // ???????????? ?????? ????????
    @POST("admin/api/Individual/GetPhysicalIndividuals")
    suspend fun getPhysicalList(
        @Body ownersBody: OwnersBody
    ): PhysicalList

    // ???????????????????? ?????? ????????
    @POST("admin/api/Individual/AddIndividualPhysical")
    suspend fun setPhysical(
        @Body physical: Physical
    ): PhysicalResponse

    // ???????????????????? ?????? ??????????????????????????
    @GET("admin/api/DicOpf/GetPhysicOpf")
    suspend fun getTypeProperty():List<PropertyType>

    // ???????????????????? ???? ????????
    @POST("admin/api/Individual/AddIndividualJuridical")
    suspend fun setJuridical(
        @Body juridical: Juridical
    ): PhysicalResponse

    // ???????????????????? ?????? ????????????????????????
    @GET("admin/api/DicOked/Get")
    suspend fun getProduction(): List<BaseFormat>

    // ???????????????????? ?????? ??????????????????????????
    @GET("admin/api/DicOpf/Get")
    suspend fun getPropertyType(): List<BaseFormat>

    // ???????????????????? ?????? ??????????????????????
    @GET("admin/api/DicEnterpriseType/Get")
    suspend fun getEnterpriseType(): List<BaseFormat>

    // ???????????? ?????????????????? ???????????? ???? ??????
    @GET("integration/api/Service/GetFL")
    suspend fun searchOwnersByIin(
        @Query("identifier") inn:Int
    )

}