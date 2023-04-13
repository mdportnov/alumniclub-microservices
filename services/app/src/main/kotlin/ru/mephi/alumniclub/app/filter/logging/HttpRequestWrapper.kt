package ru.mephi.alumniclub.app.filter.logging

import org.apache.commons.io.IOUtils
import java.io.ByteArrayInputStream
import java.io.IOException
import javax.servlet.ReadListener
import javax.servlet.ServletInputStream
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletRequestWrapper

class HttpRequestWrapper(request: HttpServletRequest) : HttpServletRequestWrapper(request) {
    private var body: ByteArray

    init {
        body = try {
            IOUtils.toByteArray(request.inputStream)
        } catch (ex: IOException) {
            ByteArray(0)
        }
    }

    override fun getInputStream(): ServletInputStream {
        return object : ServletInputStream() {
            override fun isFinished(): Boolean {
                return false
            }

            override fun isReady(): Boolean {
                return false
            }

            override fun setReadListener(listener: ReadListener) {}
            var bais = ByteArrayInputStream(body)
            override fun read(): Int {
                return bais.read()
            }
        }
    }

    fun getBody(): String {
        return String(body)
    }
}