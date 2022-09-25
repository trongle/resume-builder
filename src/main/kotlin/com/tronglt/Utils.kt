package com.tronglt

import at.favre.lib.crypto.bcrypt.BCrypt
import kotlinx.datetime.Clock
import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun instant(): Instant = Clock.System.now()

fun now(zoneId: String = "Asia/Ho_Chi_Minh") = instant().toLocalDateTime(TimeZone.of(zoneId))

fun hashPassword(password: CharArray): String = BCrypt.withDefaults().hashToString(12, password)