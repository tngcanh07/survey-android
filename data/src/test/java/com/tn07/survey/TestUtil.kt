package com.tn07.survey

import java.io.File
import java.io.FileInputStream
import java.io.IOException

/**
 * Created by toannguyen
 * Jul 19, 2021 at 20:01
 */

@Throws(IOException::class)
internal fun Any.openResource(filename: String): FileInputStream {
    val classLoader = requireNotNull(this.javaClass.classLoader)
    val resource = classLoader.getResource(filename)
    val file = File(resource.path)
    return FileInputStream(file)
}