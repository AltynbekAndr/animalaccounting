package kz.putinbyte.iszhfermer.model.system

import android.content.Context
import android.os.Environment
import java.io.File
import java.io.FileNotFoundException
import javax.inject.Inject

class DirectoryManager @Inject constructor(private val context: Context) : IDirectoryManager {

    override val pickedFilePath: String
        get() = getStorageDirectory()?.absolutePath ?: "error"

    private fun getStorageDirectory() =
        context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    override val filesPath: File
        get() = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)

    override val cacheDirectory: File
        get() = context.externalCacheDir ?: throw FileNotFoundException()
}
