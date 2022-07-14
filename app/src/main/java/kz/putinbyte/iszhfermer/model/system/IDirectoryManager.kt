package kz.putinbyte.iszhfermer.model.system

import java.io.File

interface IDirectoryManager {

    val pickedFilePath: String
    val filesPath: File
    val cacheDirectory: File
}
