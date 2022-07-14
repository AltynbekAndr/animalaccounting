package kz.putinbyte.iszhfermer.di.modules

import android.content.Context
import androidx.room.Room
import com.google.gson.Gson
import kz.putinbyte.iszhfermer.di.ApiPath
import okhttp3.OkHttpClient
import kz.putinbyte.iszhfermer.di.providers.*
import kz.putinbyte.iszhfermer.model.data.db.RoomDB
import kz.putinbyte.iszhfermer.model.data.db.dao.*
import kz.putinbyte.iszhfermer.model.data.server.*
import kz.putinbyte.iszhfermer.model.interactors.*
import kz.putinbyte.iszhfermer.model.repository.*
import toothpick.config.Module

class DataModule(
    context: Context,
    iszhServerUri: String,
) : Module() {

    init {
//        DB
        val database = Room.databaseBuilder(context, RoomDB::class.java, "RoomDB")
            .allowMainThreadQueries()
            .fallbackToDestructiveMigration()
            .build()
        bind(RoomDB::class.java).toInstance(database)


        // new Dao
        val kindOfAnimalDao: KindOfAnimalDao = database.kindOfAnimalDao()
        bind(KindOfAnimalDao::class.java).toInstance(kindOfAnimalDao)

        val countryDao: CountryDao = database.countryDao()
        bind(CountryDao::class.java).toInstance(countryDao)

        val katoDao: KatoDao = database.katoDao()
        bind(KatoDao::class.java).toInstance(katoDao)

        val identityDao: IdentityDao = database.identityDao()
        bind(IdentityDao::class.java).toInstance(identityDao)

        val referencesDao: BaseFormatReferencesDao = database.referencesDao()
        bind(BaseFormatReferencesDao::class.java).toInstance(referencesDao)

        val doctorTypesDao:DoctorTypesDao = database.doctorTypesDao()
        bind(DoctorTypesDao::class.java).toInstance(doctorTypesDao)

        val sicknessDao:SicknessDao = database.sicknessDao()
        bind(SicknessDao::class.java).toInstance(sicknessDao)

        val researchDao:ResearchDao = database.researchDao()
        bind(ResearchDao::class.java).toInstance(researchDao)

        val preventionDao:PreventionDao = database.preventionDao()
        bind(PreventionDao::class.java).toInstance(preventionDao)

        val animalDao: AnimalDao = database.animalDao()
        bind(AnimalDao::class.java).toInstance(animalDao)

        val ownersDao: OwnersDao = database.ownersDao()
        bind(OwnersDao::class.java).toInstance(ownersDao)

//        Api
        bind(String::class.java).withName(ApiPath::class.java).toInstance(iszhServerUri)

//        Providers
        bind(ApiClient::class.java).toProvider(ApiProvider::class.java).providesSingleton()
        bind(AuthApiClient::class.java).toProvider(AuthApiProvider::class.java).providesSingleton()
        bind(Gson::class.java).toProvider(GsonProvider::class.java).providesSingleton()
        bind(OkHttpClient::class.java).toProvider(OkHttpClientProvider::class.java)
            .providesSingleton()

//      Repositories & Interactors
        bind(IReferencesRepository::class.java).to(ReferencesRepository::class.java)
        bind(ReferencesInteractor::class.java).singleton()

        bind(IRegAnimalRepository::class.java).to(RegAnimalRepository::class.java)
        bind(RegAnimalInteractor::class.java).singleton()

        bind(IAuthRepository::class.java).to(AuthRepository::class.java)
        bind(AuthInteractor::class.java).singleton()

    }
}