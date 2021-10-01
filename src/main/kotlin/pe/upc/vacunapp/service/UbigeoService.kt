package pe.upc.vacunapp.service

import org.springframework.boot.origin.SystemEnvironmentOrigin
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import pe.upc.vacunapp.domain.Persona
import pe.upc.vacunapp.domain.Ubigeo
import java.io.BufferedReader
import java.io.InputStreamReader
import java.io.OutputStream
import java.lang.StringBuilder
import java.net.URL
import javax.net.ssl.HttpsURLConnection

@Service
class UbigeoService(): BasicCrud<Ubigeo,String> {
    override fun findAll(): List<Ubigeo> {
        var ubigeos = mutableListOf<Ubigeo>()

        var os:OutputStream
        var url: URL = URL("http://publico.oefa.gob.pe/Portalpifa/dataListUbigeos.do")

        var conn: HttpsURLConnection = url.openConnection() as HttpsURLConnection
        conn.doOutput = true
        conn.requestMethod = "GET"
        conn.setRequestProperty("Accept","application/json")

        if( conn.responseCode != HttpStatus.OK.value() ){

        }
        var inputStream = InputStreamReader(conn.inputStream)
        var br = BufferedReader(inputStream)

        System.out.println(br.readLine())




        return ubigeos
    }

    override fun findById(id: String): Ubigeo? {
        return null
    }

    override fun save(t: Ubigeo): Ubigeo {
        return t
    }

    override fun update(t: Ubigeo): Ubigeo {
        return t
    }

    override fun deleteById(id: String): Ubigeo {
        return Ubigeo("","",0)
    }
}