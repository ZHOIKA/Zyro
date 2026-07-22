/*
 *
 *  ******************************************************************
 *  *  * Copyright (C) 2022
 *  *  * UploadGalleryImageUseCase.kt is part of Zyro
 *  *  *  and can not be copied and/or distributed without the express
 *  *  * permission of zk
 *  *  *****************************************************************
 *
 *
 */

package com.my.zyro.domain.use_case.upload_galleryImage

import com.my.zyro.domain.repository.ZyroRepository
import java.io.File
import javax.inject.Inject

class UploadGalleryImageUseCase @Inject constructor(
    private val zyroRepository: ZyroRepository
) {
    suspend operator fun invoke(file: File): String? {
        return try {
            file.deleteOnExit()
            zyroRepository.uploadImage(file)
        } catch (ex: Exception) {
            null
        }
    }
}