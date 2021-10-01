package pe.upc.vacunapp.domain

import pe.upc.vacunapp.utils.Constantes
import pe.upc.vacunapp.utils.getCurrentDateTime
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Min
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.Size

@Entity
@Table(name="vacuna")
data class Vacuna(  @Id
                    @GeneratedValue(strategy = GenerationType.IDENTITY)
                    @Column(name = "id_vacuna")
                    val id_vacuna: Int = 0,

                    @get:Size(min=0, max=30)
                    @get:NotEmpty
                    @Column(name = "s_nombre")
                    var s_nombre:String = Constantes.S_EMPTY,

                    @get:Size(min=0, max=30)
                    @get:NotEmpty
                    @Column(name = "s_fabricante")
                    var s_fabricante:String = Constantes.S_EMPTY,

                    @get:Min(1)
                    @Column(name = "qt_dosis")
                    var qt_dosis:Int = 0,

                    @get:Min(1)
                    @Column(name = "qt_dias")
                    var qt_dias:Int = 0,

                    @Column(name = "s_usu_crea")
                    var s_usu_crea:String? = null,

                    @Column(name = "d_fec_crea")
                    var d_fec_crea:Date? = null,

                    @Column(name = "s_usu_mod")
                    var s_usu_mod:String? = null,

                    @Column(name = "d_fec_mod")
                    var d_fec_mod: Date? = null ){


    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as Vacuna

        return (this.id_vacuna == other.id_vacuna)
    }

    override fun hashCode(): Int {
        return id_vacuna.hashCode()
    }

}

@Entity
@Table(name="localvacunacion")
data class LocalVacunacion( @Id
                            @GeneratedValue(strategy = GenerationType.IDENTITY)
                            @Column(name = "id_local")
                            val id_local: Int = 0,

                            @get:Size(min=0, max=30)
                            @get:NotEmpty
                            @Column(name = "s_nombre")
                            var s_nombre:String = Constantes.S_EMPTY,

                            @get:Size(min=0, max=30)
                            @get:NotEmpty
                            @Column(name = "s_direccion")
                            var s_direccion:String = Constantes.S_EMPTY,

                            @get:Size(min=2, max=2, message = "el tamaño debe tener 2 caracteres")
                            @get:NotEmpty
                            @Column(name = "s_ubigeo_dep")
                            var s_ubigeo_dep:String = Constantes.S_EMPTY,

                            @get:Size(min=2, max=2, message = "el tamaño debe tener 2 caracteres")
                            @get:NotEmpty
                            @Column(name = "s_ubigeo_pro")
                            var s_ubigeo_pro:String = Constantes.S_EMPTY,

                            @get:Size(min=2, max=2, message = "el tamaño debe tener 2 caracteres")
                            @get:NotEmpty
                            @Column(name = "s_ubigeo_dis")
                            var s_ubigeo_dis:String = Constantes.S_EMPTY,

                            @Column(name = "s_usu_crea")
                            var s_usu_crea:String? = null,

                            @Column(name = "d_fec_crea")
                            var d_fec_crea:Date? = null,

                            @Column(name = "s_usu_mod")
                            var s_usu_mod:String? = null,

                            @Column(name = "d_fec_mod")
                            var d_fec_mod: Date? = null ){


    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as LocalVacunacion

        return (this.id_local == other.id_local)
    }

    override fun hashCode(): Int {
        return id_local.hashCode()
    }
}

@Entity
@Table(name="persona")
data class Persona( @Id
                    @get:Size(min=8, max=8, message = "el tamaño debe tener 8 caracteres")
                    @get:NotEmpty
                    @Column(name = "s_dni")
                    var s_dni:String = Constantes.S_EMPTY,

                    @get:Size(min=0, max=30)
                    @get:NotEmpty
                    @Column(name = "s_nombres")
                    var s_nombres:String = Constantes.S_EMPTY,

                    @get:Size(min=0, max=30)
                    @get:NotEmpty
                    @Column(name = "s_apellidos")
                    var s_apellidos:String = Constantes.S_EMPTY,

                    @Column(name = "d_fec_nac")
                    var d_fec_nac:Date = getCurrentDateTime(),

                    @get:Size(min=9, max=9, message = "el tamaño debe tener 9 caracteres")
                    @get:NotEmpty
                    @Column(name = "s_num_celular")
                    var s_num_celular:String = Constantes.S_EMPTY,

                    @get:Size(min=2, max=2, message = "el tamaño debe tener 2 caracteres")
                    @get:NotEmpty
                    @Column(name = "s_ubigeo_dep")
                    var s_ubigeo_dep:String = Constantes.S_EMPTY,

                    @get:Size(min=2, max=2, message = "el tamaño debe tener 2 caracteres")
                    @Column(name = "s_ubigeo_pro")
                    var s_ubigeo_pro:String? = Constantes.S_EMPTY,

                    @get:Size(min=2, max=2, message = "el tamaño debe tener 2 caracteres")
                    @Column(name = "s_ubigeo_dis")
                    var s_ubigeo_dis:String? = Constantes.S_EMPTY,

                    @Column(name = "s_usu_crea")
                    var s_usu_crea:String? = null,

                    @Column(name = "d_fec_crea")
                    var d_fec_crea:Date? = null,

                    @Column(name = "s_usu_mod")
                    var s_usu_mod:String? = null,

                    @Column(name = "d_fec_mod")
                    var d_fec_mod: Date? = null ){


    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as Persona

        return (this.s_dni == other.s_dni)
    }

    override fun hashCode(): Int {
        return s_dni.hashCode()
    }
}

@Entity
@Table(name="campana")
data class Campana( @Id
                    @GeneratedValue(strategy = GenerationType.IDENTITY)
                    @Column(name = "id_campana")
                    val id_campana: Int = 0,

                    @get:Size(min=0, max=50)
                    @get:NotEmpty
                    @Column(name = "s_nombre")
                    var s_nombre:String = Constantes.S_EMPTY,

                    @Column(name = "d_fec_inicio")
                    var d_fec_inicio:Date = getCurrentDateTime(),

                    @get:Min(1)
                    @Column(name = "id_vacuna")
                    var id_vacuna:Int = 0,

                    @get:Min(1)
                    @Column(name = "id_local")
                    var id_local:Int = 0,

                    @get:Min(1)
                    @Column(name = "qt_dosis_disponible")
                    var qt_dosis_disponible:Int = 0,

                    @Column(name = "b_envio_notificacion")
                    var b_envio_notificacion:Boolean = false,

                    @Column(name = "s_usu_crea")
                    var s_usu_crea:String? = null,

                    @Column(name = "d_fec_crea")
                    var d_fec_crea:Date? = null,

                    @Column(name = "s_usu_mod")
                    var s_usu_mod:String? = null,

                    @Column(name = "d_fec_mod")
                    var d_fec_mod: Date? = null ){


    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as Campana

        return (this.id_campana == other.id_campana)
    }

    override fun hashCode(): Int {
        return id_campana.hashCode()
    }
}

@Entity
@Table(name="campananotificacion")
data class CampanaNotificacion( @Id
                                @GeneratedValue(strategy = GenerationType.IDENTITY)
                                @Column(name = "id_campana_notif")
                                val id_campana_notif: Int = 0,

                                @get:Min(1)
                                @Column(name = "id_campana")
                                var id_campana:Int = 0,

                                @Column(name = "b_realizo_envio")
                                var b_realizo_envio:Boolean = false,

                                @Column(name = "d_fec_envio")
                                var d_fec_envio:Date = getCurrentDateTime(),

                                @get:Min(1)
                                @Column(name = "i_sesion")
                                var i_sesion:Int = 0,

                                @Column(name = "s_usu_crea")
                                var s_usu_crea:String? = null,

                                @Column(name = "d_fec_crea")
                                var d_fec_crea:Date? = null,

                                @Column(name = "s_usu_mod")
                                var s_usu_mod:String? = null,

                                @Column(name = "d_fec_mod")
                                var d_fec_mod: Date? = null ){


    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as CampanaNotificacion

        return (this.id_campana_notif == other.id_campana_notif)
    }

    override fun hashCode(): Int {
        return id_campana_notif.hashCode()
    }
}

@Entity
@Table(name="personacampana")
data class PersonaCampana(  @Id
                            @get:Size(min=0, max=20)
                            //@get:NotEmpty
                            @Column(name = "id_persona_campana")
                            var id_persona_campana:String = Constantes.S_EMPTY,

                            @get:Min(1)
                            @Column(name = "id_campana")
                            val id_campana: Int = 0,

                            @get:Size(min=8, max=8, message = "el tamaño debe tener 8 caracteres")
                            @get:NotEmpty
                            @Column(name = "s_dni")
                            var s_dni:String = Constantes.S_EMPTY,

                            @get:Min(1)
                            @Column(name = "i_sesion")
                            var i_sesion:Int = 0,

                            @Column(name = "d_fecha_hora")
                            var d_fecha_hora:Date = getCurrentDateTime(),

                            @Column(name = "s_email")
                            var s_email:String=Constantes.S_EMPTY,

                            @Column(name = "b_completo")
                            var b_completo:Boolean = false,

                            @Column(name = "s_usu_crea")
                            var s_usu_crea:String? = null,

                            @Column(name = "d_fec_crea")
                            var d_fec_crea:Date? = null,

                            @Column(name = "s_usu_mod")
                            var s_usu_mod:String? = null,

                            @Column(name = "d_fec_mod")
                            var d_fec_mod: Date? = null ){


    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as PersonaCampana

        return (this.id_persona_campana == other.id_persona_campana)
    }

    override fun hashCode(): Int {
        return id_persona_campana.hashCode()
    }
}

@Entity
@Table(name="usuario")
data class Usuario(  @Id
                     @get:Size(min=0, max=30)
                     @Column(name = "s_email")
                     var s_email:String = Constantes.S_EMPTY,

                     @get:Size(min=0, max=50)
                     @Column(name = "s_nombres")
                     var s_nombres:String = Constantes.S_EMPTY,

                     @get:Size(min=0, max=50)
                     @Column(name = "s_apellidos")
                     var s_apellidos:String = Constantes.S_EMPTY,

                     @get:Size(min=0, max=30)
                     @Column(name = "s_rol")
                     var s_rol:String = Constantes.S_EMPTY,

                     @get:Size(min=1, max=100)
                     @Column(name = "s_password")
                     var s_password:String=Constantes.S_EMPTY,

                     @Column(name = "b_habilitado")
                     var b_habilitado:Boolean = false,

                     @Column(name = "s_usu_crea")
                     var s_usu_crea:String?=null,

                     @Column(name = "d_fec_crea")
                     var d_fec_crea:Date? = null,

                     @Column(name = "s_usu_mod")
                     var s_usu_mod:String?=null,

                     @Column(name = "d_fec_mod")
                     var d_fec_mod: Date? = null ){


    override fun equals(other: Any?): Boolean {
        other ?: return false
        if(other === this) return true
        if (this.javaClass != other.javaClass) return false
        other as Usuario

        return (this.s_email == other.s_email)
    }

    override fun hashCode(): Int {
        return s_email.hashCode()
    }
}

data class Ubigeo( var nombre:String,var codigo:String, var tipo:Int)