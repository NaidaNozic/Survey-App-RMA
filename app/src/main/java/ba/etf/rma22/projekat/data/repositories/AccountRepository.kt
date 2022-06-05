package ba.etf.rma22.projekat.data.repositories

object AccountRepository {
    var acHash:String = "23e5b59e-996d-47d3-85c2-88651e9e95a2"
    fun postaviHash(acHash:String):Boolean{
        //postavlja lokalno hash studenta koji će biti korišten u drugim repozitorijima, vraća true ukoliko je hash
        //postavljen, false inače.
        this.acHash=acHash
        return true
    }
    fun getHash():String{ //vraca hash studenta koji je postavljen
        return acHash
    }
}