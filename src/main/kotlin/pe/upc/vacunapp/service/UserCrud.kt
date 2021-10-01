package pe.upc.vacunapp.service

interface UserCrud<T,ID>{
    fun findAll():List<T>
    fun findByUserPass(id: ID,pass:ID):T?
    fun save(t:T): T
    fun update(t:T):T
}