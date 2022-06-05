package ba.etf.rma22.projekat.data.repositories

object ApiConfig {
    var baseURL:String="https://rma22ws.herokuapp.com"

    fun postaviBaseURL(baseUrl:String){
        this.baseURL =baseUrl
    }
    fun getBaseUrl():String{
        return baseURL
    }
}