package kz.putinbyte.iszhfermer.model.data.db

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

val MIGRATION_1_2 = object : Migration(1, 2) {
    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("CREATE TABLE `test` (`id` INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, `taskFinishType` INTEGER NOT NULL)")
    }
}