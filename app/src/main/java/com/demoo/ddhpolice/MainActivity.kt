package com.demoo.ddhpolice

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.ddhpolice.databinding.ActivityMainBinding
import com.demoo.ddhpolice.showingData.dataShow
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.opencsv.CSVWriter
import java.io.BufferedWriter
import java.io.File
import java.io.FileWriter
import java.io.InputStreamReader


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var databasePositionSpinner: DatabaseReference
    private lateinit var databaseUnitSpinner: DatabaseReference
    private lateinit var auth: FirebaseAuth
    var SpinnerListPosition: ArrayList<String> = arrayListOf("")
    var SpinnerListUn: ArrayList<String> = arrayListOf("")
    lateinit var padapter: ArrayAdapter<String>
    lateinit var uadapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)



        val pspinner = binding.PositionSpinner
        val uspinner = binding.Unitspinner

            auth = FirebaseAuth.getInstance()
            database = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://policeapp-95039-default-rtdb.firebaseio.com").child("person")

            databasePositionSpinner = FirebaseDatabase.getInstance()
                .getReferenceFromUrl("https://policeapp-95039-default-rtdb.firebaseio.com")
                .child("positions")

        databaseUnitSpinner = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://policeapp-95039-default-rtdb.firebaseio.com")
            .child("unit")

        binding.Unitsearch.setOnClickListener {
            val temp = binding.Unitspinner.getSelectedItem().toString()
            val intent = Intent(this, dataShow::class.java)
            intent.putExtra("value",temp)
            intent.putExtra("type","unit")
            startActivity(intent)
        }
            binding.Positionsearch.setOnClickListener {
                val temp = binding.PositionSpinner.getSelectedItem().toString()
                val intent = Intent(this, dataShow::class.java)
                intent.putExtra("value",temp)
                intent.putExtra("type","position")
                startActivity(intent)
            }


        binding.Ranksearch.setOnClickListener {
            val temp = binding.RankEdittext.text.toString()
            val intent = Intent(this, dataShow::class.java)
            intent.putExtra("value",temp)
            intent.putExtra("type","rank")
            startActivity(intent)
        }

        binding.Remarkssearch.setOnClickListener {
            val temp = binding.RemarksEdittext.text.toString()
            val intent = Intent(this, dataShow::class.java)
            intent.putExtra("value",temp)
            intent.putExtra("type","remarks")
            startActivity(intent)
        }

        binding.Phonesearch.setOnClickListener {
            val temp = binding.PhoneEdittext.text.toString()
            val intent = Intent(this, dataShow::class.java)
            intent.putExtra("value",temp)
            intent.putExtra("type","phone")
            startActivity(intent)
        }
//            database.get().addOnSuccessListener {
//                Toast.makeText(this, it.value.toString(), Toast.LENGTH_SHORT).show()
//                var vv = 0
//                for (i in listOf(it.value)){
//                    vv = vv+1;
//                    Toast.makeText(this, vv, Toast.LENGTH_SHORT).show()
//                }
//            }.addOnFailureListener {
//                Toast.makeText(this, "net failed", Toast.LENGTH_SHORT).show()
//            }
//        }
//        databasePositionSpinner.get().addOnSuccessListener() {
//            SpinnerListPosition.addAll(listOf(it.value.toString()))
//        }.addOnFailureListener {
//            Toast.makeText(
//                this,
//                "this phone number is not registered",
//                Toast.LENGTH_SHORT
//            ).show()
//        }

//        database.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // TODO Auto-generated method stub
//                var data = ""
//                for (ds in snapshot.children) {
//                    val ind = ds.value.toString().indexOf("position=")
//                    var i = ind+9
//                    var d = ""
//                    while (ds.value.toString()[i] != '}') {
//                        d += ds.value.toString()[i]
//                        i++
//                    }
//                    if(d=="hiii") data += ds.value.toString()
//                }
//
//            }

//            override fun onCancelled(error: DatabaseError) {
////                SpinnerListPre.clear()
//            }
//        })

        databasePositionSpinner.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // TODO Auto-generated method stub
                for (ds in snapshot.children) {
                    val data = ds.value.toString()
                    SpinnerListPosition.add(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // TODO Auto-generated method stub
            }
        })
        databaseUnitSpinner.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                SpinnerListUn.clear()
                // TODO Auto-generated method stub
                for (ds in snapshot.children) {
                    val data = ds.value.toString()
                    SpinnerListUn.add(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                SpinnerListUn.clear()
            }
        })

        padapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, SpinnerListPosition
        )
        pspinner.adapter = padapter

        uadapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, SpinnerListUn
        )

        uspinner.adapter = uadapter


//        databaseUnitSpinner.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                // TODO Auto-generated method stub
//                for (ds in snapshot.children) {
//                    val data = ds.value.toString()
//                    SpinnerListUnit.add(data)
//                }
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                // TODO Auto-generated method stub
//            }
//        })
//
//        uadapter = ArrayAdapter(
//            this,
//            android.R.layout.simple_spinner_item, SpinnerListUnit
//        )
//        uspinner.adapter = uadapter

//        databasePositionSpinner.get().addOnSuccessListener() {
//            SpinnerListPosition =
//                    if (spinner != null) {
//
//                    }
//        }.addOnFailureListener {
//            Toast.makeText(
//                this,
//                "this phone number is not registered",
//                Toast.LENGTH_SHORT
//            ).show()
//        }


        binding.add.setOnClickListener {
            val intent = Intent(this, AddingNewPerson::class.java)
            startActivity(intent)
        }


//            binding.text.setOnClickListener {
//                database.child("todos").child("8534").child("title").get().addOnSuccessListener() {
//                    if (it.value == null) {
//                        Toast.makeText(
//                            this,
//                            "this phone number is not registered",
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    } else {
//                        Toast.makeText(
//                            this,
//                            it.value.toString() +" "+ spinner.getSelectedItem().toString() ,
//                            Toast.LENGTH_SHORT
//                        ).show()
//                        val key = databasePositionSpinner.push().key
//                        if (key != null) {
//                            databasePositionSpinner.child(key).setValue("abb")
//                            SpinnerListPosition.clear()
//                        }
//                    }
//                }.addOnFailureListener {
//                    Toast.makeText(
//                        this,
//                        "net Problem",
//                        Toast.LENGTH_SHORT
//                    ).show()
//                }
//            }
        }


    }