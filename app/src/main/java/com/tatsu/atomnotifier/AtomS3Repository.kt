package com.tatsu.atomnotifier

import io.ktor.client.HttpClient
import io.ktor.client.engine.cio.CIO
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText

class AtomS3Repository {
    private val client = HttpClient(CIO)

    suspend fun sendAck(): String {
        return client.get(
            "http://192.168.11.33/ack"
        ).bodyAsText()
    }
}