package kz.putinbyte.iszhfermer.model.system

/**
 * Project Animal Accounting
 * Package kz.putinbyte.iszhfermer.model.system
 *
 *
 *
 * Created by Artem Skopincev (aka sharpyx) 12.11.2020
 * Copyright © 2020 TKOInform. All rights reserved.
 */
interface ILocationProvider {

    fun getCurrentLocation(): LocationInfo?
}