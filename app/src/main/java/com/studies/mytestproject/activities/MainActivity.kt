package com.studies.mytestproject.activities

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.Request
import com.android.volley.RequestQueue
import com.android.volley.Response
import com.android.volley.toolbox.JsonArrayRequest
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.studies.mytestproject.R
import com.studies.mytestproject.adapters.movieAdapter
import com.studies.mytestproject.databinding.ActivityMainBinding
import com.studies.mytestproject.models.movieitem
import java.util.Locale.filter

class MainActivity : AppCompatActivity() {
    val binding by lazy{
        ActivityMainBinding.inflate(layoutInflater)
    }
   lateinit var madapter:movieAdapter
    lateinit var requestQueue: RequestQueue
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        requestQueue=Volley.newRequestQueue(this)
        madapter= movieAdapter(this, mutableListOf<movieitem>())
        binding.RV.adapter=madapter
        getapi()
    }

    private fun getapi() {
        var list= mutableListOf<movieitem>()
        val url ="http://myvbox.uk:2052/player_api.php?username=test&password=test1&&action=get_vod_streams"
        val jsonarrayrequest:JsonArrayRequest= JsonArrayRequest(Request.Method.GET,url,null,
            Response.Listener {
                response->
                Log.d("nddddcsk",response.toString())
                         for(i in 0 until response.length()){
                             val item= response.getJSONObject(i)
                                 val mymoviewitem= movieitem(
                                     item.getString("stream_icon"),
                                     item.getString("name"),
                                     item.getString("year"),
                                     item.getDouble("rating_5based").toFloat(),
                                     item.getDouble("rating").toFloat()
                                 )
                             list.add(mymoviewitem)
                             search(list)
                             filtermovies(list)

                         }
                binding.PB.visibility= View.GONE
                madapter.ondatachange(list)
            },Response.ErrorListener {
                error->
                Toast.makeText(this,error.localizedMessage.toString(),Toast.LENGTH_LONG).show()
            })
        requestQueue.add(jsonarrayrequest)
    }

    private fun filtermovies(list: MutableList<movieitem>) {
        binding.filtericon.setOnClickListener {
            val dialog= Dialog(this)
            dialog.setContentView(R.layout.filterdialog)



            val donebutton=dialog.findViewById<Button>(R.id.donebutton)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            donebutton.setOnClickListener {
                val moviereleaseyear=dialog.findViewById<EditText>(R.id.releaseyeartext).text.toString().toString()
                val rating10=dialog.findViewById<EditText>(R.id.rating10).text.toString().toFloat()
                val rating5=dialog.findViewById<EditText>(R.id.rating5).text.toString().toFloat()
                Log.d("dkrgjgfwej","$moviereleaseyear  ${rating10.toString()}   ${rating5.toString()}")
                dialog.dismiss()
                val templist= mutableListOf<movieitem>()
                for(i in list){


                    if(i.year.equals(moviereleaseyear) && i.rating5.equals(rating5) && i.rating10.equals(rating10)){
                        templist.add(i)
                    }
                }
                madapter.ondatachange(templist)
            }
            dialog.show()


        }

    }

    private fun search(list:List<movieitem>) {
        binding.searchicon.setOnClickListener {
            val text= binding.searchtext.text.toString().toLowerCase()
            val templist= mutableListOf<movieitem>()
            if(!text.isEmpty()){

                for (i in list){
                    val smallcaps=i.title.toLowerCase()
                    if(smallcaps.contains(text)){
                        templist.add(i)
                    }
                }
                Toast.makeText(this,templist.size.toString(),Toast.LENGTH_LONG).show()
                madapter.ondatachange(templist)
            }
            else{
                madapter.ondatachange(list)
            }



        }

    }
}