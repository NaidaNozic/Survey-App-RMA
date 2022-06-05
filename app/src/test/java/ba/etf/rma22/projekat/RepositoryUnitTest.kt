package ba.etf.rma22.projekat

import android.util.Log
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import ba.etf.rma22.projekat.data.repositories.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import org.hamcrest.CoreMatchers
import org.junit.Test
import okhttp3.Response
import java.net.URL

class RepositoryUnitTest {
    suspend fun obrisi(){
        var client: OkHttpClient = OkHttpClient()
        var builder: Request.Builder = Request.Builder()
            .url(URL(ApiConfig.baseURL + "/student/" + AccountRepository.acHash + "/upisugrupeipokusaji"))
            .delete()
        var request: Request = builder.build()
        withContext(Dispatchers.IO) {
            var response: Response = client.newCall(request).execute()
            var odgovor: String = response.body().toString()
        }
    }
    @Test
    fun a0_pripremiPocetak() = runBlocking {
        obrisi()
    }
    @Test
    fun a1_pripremiPocetak() = runBlocking{
      //var predmeti = IstrazivanjeIGrupaRepository.getIstrazivanja()
        var istr=IstrazivanjeIGrupaRepository.getIstrazivanja(1)
        if(istr!=null)for(i in istr)print(i.naziv)
        else print("NEMAA")
      assertThat(AnketaRepository.getById(0), CoreMatchers.nullValue())
     // assertThat(AnketaRepository.getUpisane()?.size,CoreMatchers.equalTo(0))
      //assertThat(AnketaRepository.getById(20),CoreMatchers.nullValue())
    }
}