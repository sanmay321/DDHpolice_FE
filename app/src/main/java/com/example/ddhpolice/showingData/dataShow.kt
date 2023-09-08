package com.example.ddhpolice.showingData

import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ddhpolice.databinding.ActivityDataShowBinding
import com.example.ddhpolice.utlis.ut
import com.google.firebase.database.*
import com.opencsv.CSVWriter
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.util.*
import kotlin.collections.ArrayList

class  dataShow : AppCompatActivity() {
    lateinit var binding: ActivityDataShowBinding
    private lateinit var database: DatabaseReference
    var tempp = ""
    var temppp = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDataShowBinding.inflate(layoutInflater)
        setContentView(binding.root)
        database = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://policeapp-95039-default-rtdb.firebaseio.com").child("person")
        tempp = intent.getStringExtra("value").toString()
        temppp = intent.getStringExtra("type").toString()


//        var v = ""
        //brass
        binding.recyleViewTrackrderList.layoutManager = LinearLayoutManager(this)
        val data = ArrayList<personItem>()
        if(tempp!=""){
            binding.presentData.visibility = View.VISIBLE
            binding.noData.visibility = View.GONE
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // TODO Auto-generated method stub
                    var count = 0
                    for (ds in snapshot.children) {
                        val temp = temppp?.let { ds.child(it).value.toString() }
                        if( temp == tempp){
                            count++;
                            data.add(personItem(
                                ds.child("rank").value.toString(),
                                ds.child("name").value.toString(),
                                ds.child("unit").value.toString(),
                                ds.child("position").value.toString(),
                                ds.child("phone").value.toString(),
                                ds.child("remarks").value.toString()
                            ))
                        }
                    }
                    if(count==0){
                        binding.presentData.visibility = View.GONE
                        binding.noData.visibility = View.VISIBLE
                    }
                    val adapter = dataAdapter(data)

                    // Setting the Adapter with the recyclerview
                    binding.recyleViewTrackrderList.adapter = adapter

                    adapter.setOnclickListner(object : dataAdapter.OnItemClickListner{
                        override fun onclick(position: Int) {
                            Toast.makeText(this@dataShow, "you clicked on item no "+ data[position].id, Toast.LENGTH_SHORT).show()
                        }

                    })
                }

                override fun onCancelled(error: DatabaseError) {
                }
            })
        }

        binding.createCsv.setOnClickListener {
            save()
        }

    }


    fun save(){

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this,android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(android.Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    1
                )
            } else {
                createFolderAndCsvFile()
            }
        } else {
            createFolderAndCsvFile()
        }


//        val dirPath = Environment.getExternalStorageDirectory().absolutePath + "/MyApp"
//        val dir = File(dirPath)
//        dir.mkdirs()
//        val filePath = dirPath + "/example.csv"
//        val file = File(filePath)
//        val writer = CSVWriter(FileWriter(file))
//        val header = arrayOf("Name", "Age", "City")
//        writer.writeNext(header)
//        val row1 = arrayOf("John", "25", "New York")
//        val row2 = arrayOf("Sarah", "32", "London")
//        writer.writeNext(row1)
//        writer.writeNext(row2)
//        writer.close()
////        val cowriter = BufferedWriter(Input("/data/s/a.csv"))
////        val row = arrayOf("hi", "Sanmay")
////        cowriter.write("hi")
////        cowriter.close()
    }

    private fun createFolderAndCsvFile() {
        try {
        val directory = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "Police")
        if (!directory.exists()) {
            directory.mkdirs()
        }
            val uuid = UUID.randomUUID()
            val fileName = "$uuid.csv"
        val file = File(directory, fileName)
            if (!file.exists()) {
                file.createNewFile()
        }
        if(file.createNewFile()) Toast.makeText(this@dataShow, "true", Toast.LENGTH_SHORT).show()

            val writer = CSVWriter(FileWriter(file,true))
            val header = arrayOf(
                "rank",
                "name",
                "unit",
                "attachment",
                "phone",
                "remarks")
            writer.writeNext(header)
            database.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // TODO Auto-generated method stub
                    for (ds in snapshot.children) {
                        val temp = temppp?.let { ds.child(it).value.toString() }
                        if( temp == tempp) {
                            var re =  ds.child("remarks").value.toString()
                            if(re =="null")re = ""
                            writer.writeNext(
                                arrayOf(
                                    ds.child("rank").value.toString(),
                                    ds.child("name").value.toString(),
                                    ds.child("unit").value.toString(),
                                    ds.child("position").value.toString(),
                                    ds.child("phone").value.toString(),
                                    re
                                )
                            )
                        }
                    }
                    writer.close()
                    Toast.makeText(this@dataShow, "saved", Toast.LENGTH_SHORT).show()
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }

            })
        } catch (e: IOException) {
            e.printStackTrace()
        }
//        val row1 = arrayOf("John", "25", "New York")
//        val row2 = arrayOf("Sarah", "33", "London")
//        writer.writeNext(row1)
//        writer.writeNext(row2)
//        writer.close()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                createFolderAndCsvFile()
            }
        }
    }
}