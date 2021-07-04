package pe.upc.vacunapp.service

import com.rabbitmq.client.ConnectionFactory
import org.springframework.boot.web.servlet.server.Encoding
import org.springframework.stereotype.Service
import pe.upc.vacunapp.domain.Message

@Service
class ComunicationService: BasicCrud<Message, String> {
    override fun findAll(): List<Message> {
        TODO("Not yet implemented")
    }

    override fun findById(id: String): Message? {
        var message = Message(id,"Marco "+id)
        var factory = ConnectionFactory()
        factory.host = "snake.rmq2.cloudamqp.com"
        factory.virtualHost = "hckcffve"
        factory.username = "hckcffve"
        factory.password = "R20bbKAupYl0y5NeaQSRI65OIqEPC2HU"
        var connection = factory.newConnection()

        var channel = connection.createChannel()

        channel.queueDeclare("notificaciones",false,false,false,null)
        var byteArray:String = "Asesoria confirmada para Id="+id

        channel.basicPublish("","notificaciones",null, byteArray.encodeToByteArray())

        return message;
    }

    override fun save(t: Message): Message {
        TODO("Not yet implemented")
    }

    override fun update(t: Message): Message {
        TODO("Not yet implemented")
    }

    override fun deleteById(id: String): Message {
        TODO("Not yet implemented")
    }
}