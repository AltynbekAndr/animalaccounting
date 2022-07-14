package kz.putinbyte.iszhfermer.model.interactors

import android.content.Context
import kz.putinbyte.iszhfermer.entities.animals.Gender
import kz.putinbyte.iszhfermer.entities.animals.Registration
import kz.putinbyte.iszhfermer.entities.animals.ScanRegion
import kz.putinbyte.iszhfermer.entities.network.region.Region
import kz.putinbyte.iszhfermer.utils.LogUtils
import okhttp3.ResponseBody
import ru.terrakok.cicerone.Router
import java.io.FileOutputStream
import java.io.InputStream
import javax.inject.Inject

class CommonDataInteractor @Inject constructor(
    private val router: Router,
    private val context: Context
) {

    var katoId:Int? = null

    fun getDefaultRegion() = Region(
        animalAmountByKatos = listOf(
            Region.AnimalAmountByKato(
                camelsCount = 0,
                cattleCount = 49317,
                code = 111010000,
                hoofedsCount = 0,
                horsesCount = 1,
                id = 3,
                isUserKato = true,
                last = true,
                name = "Г.КОКШЕТАУ",
                pigsCount = 0,
                smallCattlesCount = 21
            )
        ),
        katos = listOf(
            Region.Kato(
                code = 110000000,
                id = 1,
                isUserKato = false,
                last = false,
                nameKz = "АҚМОЛА ОБЛЫСЫ",
                nameRu = "АКМОЛИНСКАЯ ОБЛАСТЬ"
            )
        )
    )

    val gender =
        listOf(
            Gender(1, "Male", "Самец", "Еркек"),
            Gender(2, "Female", "Самка", "Ургачы")
        )

    val typeIdentification =
        listOf(
            Gender(1, "Биркование", "Биркование", "Сыргалау"),
            Gender(2, "Чипирование", "Чипирование", "Чиптеу")
        )

    val registration =
        listOf(
            Registration(1, "Reg_Offspring", "Приплод", ""),
            Registration(2, "Reg_Acquisition", "Приобретение(закуп)", ""),
            Registration(3, "Reg_Inheritance", "Наследство", ""),
            Registration(4, "Reg_Rental", "Прием в аренду", ""),
            Registration(5, "Reg_Givig", "Дарение", ""),
            Registration(6, "Reg_MovingOwnerAnimal", "Переезд владельца животного", ""),
            Registration(7, "Reg_ReturnOwner", "Возврат владельцу", ""),
            Registration(8, "Reg_AdmissionSPK", "Прием в СПК", ""),
            Registration(9, "Reg_AcceptanceOfTheRightOfUse", "Прием права пользования, владения", ""),
            Registration(10, "Req_AcquisitionImport", "Приобретение(импорт)", "")
        )

    val unRegister =
        listOf(
            Registration(20, "UnReg_Givig", "Дарение", ""),
            Registration(21, "UnReg_Sale", "Продажа", ""),
            Registration(22, "UnReg_MovingOwnerAnimal", "Переезд владельца животного", ""),
            Registration(23, "UnReg_Withdrawal", "Иъятие", ""),
            Registration(24, "UnReg_ReturnOwner", "Возврат владельцу", ""),
            Registration(25, "UnReg_TransferSPK", "Передача в СПК", ""),
            Registration(26, "UnReg_SlaughterForSale", "Убой на реализацию", ""),
            Registration(27, "UnReg_CattleDeath", "Падеж", ""),
            Registration(28, "UnReg_SlaughterForPersonalUse", "Убой для личного пользования", ""),
            Registration(29, "UnReg_SanitarySlaughter", "Санитарный убой", ""),
            Registration(30, "UnReg_Loss", "Утеря", ""),
            Registration(31, "UnReg_Inheritance", "Наследство", ""),
            Registration(32, "UnReg_Rental", "Передача в аренду", ""),
            Registration(33, "UnReg_RoadAccident", "Дорожно-транспортное происшествие", ""),
            Registration(34, "UnReg_TransferOfTheRightOfUse", "Передача права пользования, владения", ""),
        )

    val decRegions =
        listOf(
            ScanRegion("01", 110000000, "C"),
            ScanRegion("02", 190000000, "B"),
            ScanRegion("03", 150000000, "D"),
            ScanRegion("04", 230000000, "E"),
            ScanRegion("05", 630000000, "F"),
            ScanRegion("06", 310000000, "H"),
            ScanRegion("07", 270000000, "L"),
            ScanRegion("08", 390000000, "P"),
            ScanRegion("09", 350000000, "M"),
            ScanRegion("10", 430000000, "N"),
            ScanRegion("11", 470000000, "R"),
            ScanRegion("12", 550000000, "S"),
            ScanRegion("13", 590000000, "T"),
            ScanRegion("14", 610000000, "X"),
            ScanRegion("15", 750000000, "A"),
            ScanRegion("16", 710000000, "Z"),
            ScanRegion("17", 790000000, "Y")
        )

    val decAnimalKinds = mapOf(
        1 to "Cattle",
        2 to "SmallCattle",
        3 to "Pigs",
        4 to "Horses",
        5 to "Camels",
        6 to "Hoofed"
    )


//    suspend fun getTaskFailureReasons(primarilyFromServer: Boolean = false): Result<List<TaskFailureReason>> {
//
//        if (primarilyFromServer) {
//            val r = getTaskFailureReasonsFromServer()
//            if (r is Result.Success) {
//                return r
//            }
//        }
//
//        val list = commonDataDao.getTaskFailureReasons()
//        list?.let {
//            taskFailureReasonsCache = list
//            return Result.Success(it)
//        }
//
//        taskFailureReasonsCache?.let { return Result.Success(it) }
//
//        return getTaskFailureReasonsFromServer()
//    }


//    fun getContainerLevelsList(): Result<List<ContainerLoadLevel>> {
//        return Result.Success(
//            listOfNotNull(
//                ContainerLoadLevel("Полный", 1.0, ContainerLoadLevel.Type.FULL),
//                ContainerLoadLevel("Переполнен", 1.25, ContainerLoadLevel.Type.OVERFLOWING),
//                ContainerLoadLevel("Неполный 0,75", 0.75, ContainerLoadLevel.Type.PARTIAL),
//                ContainerLoadLevel("Половина 0,5", 0.5, ContainerLoadLevel.Type.HALF),
//                ContainerLoadLevel("Четверть 0,25", 0.25, ContainerLoadLevel.Type.FOURTH),
//                ContainerLoadLevel(
//                    "Пустой 0,0",
//                    0.0,
//                    ContainerLoadLevel.Type.EMPTY
//                ).takeIf { authRepository.tourInfo?.showEmptyContainer == true },
//                ContainerLoadLevel("Невозможно вывезти", 0.0, ContainerLoadLevel.Type.FAILURE)
//            )
//        )
//    }

    fun clearAllCache() {
        // clear cache
//        availablePortersCache = null
//        containerFailureReasonsCache = null
//        taskFailureReasonsCache = null
//        visitPointsCache = null

    }

//    suspend fun getLatestVersionInfo() = safeApiCall { api.getLatestVersionInfo() }

//
//    private fun showUpdateDialog() {
//        router.navigateTo(Screens.UpdateDialog)
//    }

//    suspend fun downloadLatestVersion() = safeApiCall {
//        val newVersionDir = File(dataStorageManager.getNewVersionDirectory())
//        if (!newVersionDir.exists()) {
//            newVersionDir.mkdir()
//        }
//        val res = apiClient.latestVersion()
//        val path = dataStorageManager.getNewVersionDirectory() + File.separator + "newVersion.apk"
//        saveFile(res, path)
//        return@safeApiCall path
//    }

    private fun saveFile(body: ResponseBody?, apkFilePath: String): String {
        if (body == null)
            return ""
        var input: InputStream? = null
        try {
            input = body.byteStream()
            //val file = File(getCacheDir(), "cacheFileAppeal.srl")
            val fos = FileOutputStream(apkFilePath)
            fos.use { output ->
                val buffer = ByteArray(4 * 1024) // or other buffer size
                var read: Int
                while (input.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return apkFilePath
        } catch (e: Exception) {
            LogUtils.error("saveFile", e.toString())
        } finally {
            input?.close()
        }
        return ""
    }
}
