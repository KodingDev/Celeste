package dev.koding.celeste.client.utils

import java.io.File

operator fun File.div(path: String) = resolve(path)