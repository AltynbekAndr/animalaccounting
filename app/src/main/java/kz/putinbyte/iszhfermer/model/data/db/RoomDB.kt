package kz.putinbyte.iszhfermer.model.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import kz.putinbyte.iszhfermer.entities.db.*
import kz.putinbyte.iszhfermer.entities.db.animals.KindOfAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.RegAnimal
import kz.putinbyte.iszhfermer.entities.db.animals.prevention.AnimalPrevention
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.AnimalResearch
import kz.putinbyte.iszhfermer.entities.db.animals.reseach.ResearchKindItem
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.AnimalSickness
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessCauseItem
import kz.putinbyte.iszhfermer.entities.db.animals.sickness.SicknessKindItem
import kz.putinbyte.iszhfermer.entities.db.places.Country
import kz.putinbyte.iszhfermer.entities.db.places.Kato
import kz.putinbyte.iszhfermer.entities.db.rvl.Owners
import kz.putinbyte.iszhfermer.entities.network.ResearchResultItem
import kz.putinbyte.iszhfermer.model.data.db.converters.DataConverters
import kz.putinbyte.iszhfermer.model.data.db.converters.EnumConverters
import kz.putinbyte.iszhfermer.model.data.db.converters.ListDataConverters
import kz.putinbyte.iszhfermer.model.data.db.dao.*

@Database(
    entities = [
        //new entities
        (RegAnimal::class),
        (KindOfAnimal::class),
        (Country::class),
        (Kato::class),
        (Identity::class),
        (SicknessKindItem::class),
        (SicknessCauseItem::class),
        (AnimalSickness::class),
        (DoctorTypes::class),
        (AnimalResearch::class),
        (ResearchKindItem::class),
        (ResearchResultItem::class),
        (AnimalPrevention::class),
        (BaseFormatReferences::class),
        (Owners::class),
    ], version = 23, exportSchema = false
)

@TypeConverters(
    EnumConverters::class,
    DataConverters::class,
    ListDataConverters::class
)

abstract class RoomDB : RoomDatabase() {
    // new dao
    abstract fun kindOfAnimalDao(): KindOfAnimalDao
    abstract fun countryDao(): CountryDao
    abstract fun katoDao(): KatoDao
    abstract fun identityDao(): IdentityDao
    abstract fun animalDao(): AnimalDao
    abstract fun doctorTypesDao():DoctorTypesDao
    abstract fun sicknessDao():SicknessDao
    abstract fun researchDao():ResearchDao
    abstract fun preventionDao():PreventionDao
    abstract fun referencesDao():BaseFormatReferencesDao
    abstract fun ownersDao():OwnersDao
}