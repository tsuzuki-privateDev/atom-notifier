package com.tatsu.atomnotifier

import fi.iki.elonen.NanoHTTPD

// /alert 来たら → onAlert呼ぶ
// /reset 来たら → onReset呼ぶ
class AlertHttpServer(
    port: Int,
    val onAlert: () -> Unit,
    val onReset: () -> Unit
) : NanoHTTPD(port) {

    override fun serve(session: IHTTPSession): Response {
        val uri = session.uri
        val msg = when (uri) {
            "/" -> "OK"
            "/alert" -> {
                onAlert()
                "ALERT"
            }
            "/reset" -> {
                onReset()
                "RESET"
            }
            else -> "unknown"
        }
        return newFixedLengthResponse(msg)
    }
}