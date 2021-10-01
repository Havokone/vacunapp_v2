package pe.upc.vacunapp

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.ResultActions
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder


inline fun <reified T> ResultActions.bodyTo(objectMapper: ObjectMapper = jacksonObjectMapper()):T{
    return objectMapper.readValue(this.andReturn().response.contentAsByteArray)
}

fun MockHttpServletRequestBuilder.body(data:Any,
                                       mapper: ObjectMapper = jacksonObjectMapper(),
                                       mediaType: MediaType = MediaType.APPLICATION_JSON_UTF8): MockHttpServletRequestBuilder {
    return this.content(mapper.writeValueAsBytes(data)).contentType(mediaType)
}