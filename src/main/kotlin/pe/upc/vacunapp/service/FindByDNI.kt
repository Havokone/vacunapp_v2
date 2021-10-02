package pe.upc.vacunapp.service

interface FindByDNI<T,ID> {
    fun findAll(dni:ID):List<T>
}