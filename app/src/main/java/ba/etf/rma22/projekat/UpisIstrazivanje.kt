package ba.etf.rma22.projekat

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.TextView
import ba.etf.rma22.projekat.viewmodel.KorisnikListViewModel

class UpisIstrazivanje : AppCompatActivity() {
    private var istrazivanjeListViewModel=KorisnikListViewModel()
    private lateinit var dodajIstrazivanjeDugme:Button
    private lateinit var spinnerGodine: Spinner
    private lateinit var spinnerIstrazivanja: Spinner
    private var godine = arrayOf(1,2,3,4,5)
    private lateinit var istrazivanja:List<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_upis_istrazivanje)

        //spinner za godine
        spinnerGodine=findViewById(R.id.odabirGodina)
        val arrayAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, godine)
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerGodine!!.setAdapter(arrayAdapter)

        //spinner za istrazivanja
        istrazivanja=istrazivanjeListViewModel.getNeupisane()
        var nazivIstrazivanja=intent.getStringExtra("poruka")
        if(nazivIstrazivanja=="Prazno"){
            istrazivanja=istrazivanjeListViewModel.getNeupisane()
        }else if(nazivIstrazivanja!=null){
            //prikazat ce se sva istrazivanja gdje nije upisan i takodje se nece prikazati
           // istrazivanje na koje se prije trenutnog prijavio
            istrazivanja=istrazivanjeListViewModel.getNeupisane(nazivIstrazivanja)
        }
        spinnerIstrazivanja=findViewById(R.id.odabirIstrazivanja)
        val arrayAdapter1 = ArrayAdapter(this, android.R.layout.simple_spinner_item, istrazivanja)
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerIstrazivanja!!.setAdapter(arrayAdapter1)

        //klik na upisi me
        dodajIstrazivanjeDugme=findViewById(R.id.dodajIstrazivanjeDugme)
        dodajIstrazivanjeDugme.setOnClickListener{
            var istr=spinnerIstrazivanja.selectedItem.toString()
            var god=spinnerGodine.selectedItem.toString()
            val rez:String=god+"::"+istr
            val intent=Intent()
            intent.putExtra("rezultat",rez)
            setResult(RESULT_OK,intent)
            finish()
        }

    }
}