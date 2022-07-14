package kz.putinbyte.iszhfermer.model.system

interface ILogger {
    fun logException(throwable: Throwable)
}