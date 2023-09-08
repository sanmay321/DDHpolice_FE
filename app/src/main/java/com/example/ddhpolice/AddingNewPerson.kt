package com.example.ddhpolice

import android.app.Dialog
import android.app.ProgressDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.*
import com.example.ddhpolice.databinding.ActivityAddingNewPersonBinding
import com.google.firebase.database.*

class AddingNewPerson : AppCompatActivity() {
    lateinit var binding: ActivityAddingNewPersonBinding
    private lateinit var databasePreAttach: DatabaseReference
    private lateinit var databaseUnit: DatabaseReference
    private lateinit var databasePerson: DatabaseReference
    lateinit var builder : Dialog
    var SpinnerListPre: ArrayList<String> = arrayListOf("")
    var SpinnerListUn: ArrayList<String> = arrayListOf("")
    lateinit var adapterp: ArrayAdapter<String>
    lateinit var adapteru: ArrayAdapter<String>
    private lateinit var progrsDialog: ProgressDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddingNewPersonBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databasePreAttach = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://policeapp-95039-default-rtdb.firebaseio.com/").child("positions")

        databaseUnit = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://policeapp-95039-default-rtdb.firebaseio.com/").child("unit")

        databasePerson = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://policeapp-95039-default-rtdb.firebaseio.com/").child("person")

        progrsDialog = ProgressDialog(this@AddingNewPerson)

        databaseUnit.addValueEventListener(object : ValueEventListener {
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

        databasePreAttach.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                SpinnerListPre.clear()
                // TODO Auto-generated method stub
                for (ds in snapshot.children) {
                    val data = ds.value.toString()
                    SpinnerListPre.add(data)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                SpinnerListPre.clear()
            }
        })

        adapterp = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, SpinnerListPre
        )
        adapteru = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_item, SpinnerListUn
        )
        binding.spinnerUnit.adapter = adapteru
        binding.spinnerPreAttach.adapter = adapterp

        binding.addingUnit.setOnClickListener {
            builder = Dialog(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)

            builder.setContentView(dialogLayout)
            builder.setCancelable(false)
            val heading = dialogLayout.findViewById<TextView>(R.id.headingDialog)
            val text = dialogLayout.findViewById<EditText>(R.id.txt_input)
            val savebutton = dialogLayout.findViewById<Button>(R.id.btn_okay)
            val cancel = dialogLayout.findViewById<Button>(R.id.btn_cancel)
            builder.show()
            savebutton.setOnClickListener {
                progrsDialog.setMessage("adding")
                progrsDialog.show()
                val key = databaseUnit.push().key
                if(!text.text.isEmpty()) {
                    if (key != null) {
                        databaseUnit.child(key).setValue(text.text.toString())
                        SpinnerListUn.clear()
                        progrsDialog.dismiss()
                        Toast.makeText(this, text.text.toString()+" successfully added", Toast.LENGTH_SHORT).show()
                        builder.dismiss()
                    }else {
                        Toast.makeText(this, "can not add something went wrong", Toast.LENGTH_SHORT).show()
                        progrsDialog.dismiss()
                    }
                }else{
                    Toast.makeText(this, "please enter the new unit", Toast.LENGTH_SHORT).show()
                    progrsDialog.dismiss()
                }
            }
            cancel.setOnClickListener {
                builder.dismiss()
            }

        }

        binding.addingPreAttach.setOnClickListener {
            builder = Dialog(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.edit_text_layout,null)

            builder.setContentView(dialogLayout)
            builder.setCancelable(false)
            val heading = dialogLayout.findViewById<TextView>(R.id.headingDialog)
            val text = dialogLayout.findViewById<EditText>(R.id.txt_input)
            val savebutton = dialogLayout.findViewById<Button>(R.id.btn_okay)
            val cancel = dialogLayout.findViewById<Button>(R.id.btn_cancel)
            heading.text = "Add positon"
            builder.show()
            savebutton.setOnClickListener {
                progrsDialog.setMessage("adding")
                progrsDialog.show()
                val key = databasePreAttach.push().key
                if(!text.text.isEmpty()) {
                    if (key != null) {
                        databasePreAttach.child(key).setValue(text.text.toString())
                        SpinnerListPre.clear()
                        Toast.makeText(this, text.text.toString()+" successfully added", Toast.LENGTH_SHORT).show()
                        progrsDialog.dismiss()
                        builder.dismiss()
                    }else {
                        Toast.makeText(this, "can not add something went wrong", Toast.LENGTH_SHORT).show()
                        progrsDialog.dismiss()
                    }
                }else{
                    Toast.makeText(this, "please enter the new positions", Toast.LENGTH_SHORT).show()
                    progrsDialog.dismiss()
                }
            }
            cancel.setOnClickListener {
                builder.dismiss()
            }

        }

        binding.buttonSave.setOnClickListener {
            builder = Dialog(this)
            val inflater = layoutInflater
            val dialogLayout = inflater.inflate(R.layout.view_all,null)

            builder.setContentView(dialogLayout)
            builder.setCancelable(false)
            val heading = dialogLayout.findViewById<TextView>(R.id.headerShow)
            val rankShow = dialogLayout.findViewById<TextView>(R.id.Rankshow)
            rankShow.text = binding.rankNo.text.toString()
            val nameShow = dialogLayout.findViewById<TextView>(R.id.nameShow)
            nameShow.text = binding.name.text.toString()
            val positionShow = dialogLayout.findViewById<TextView>(R.id.positonShow)
            positionShow.text = binding.spinnerPreAttach.getSelectedItem().toString()
            val unitShow = dialogLayout.findViewById<TextView>(R.id.UnitShow)
            unitShow.text = binding.spinnerUnit.getSelectedItem().toString()
            val PhoneShow = dialogLayout.findViewById<TextView>(R.id.phoneShow)
            PhoneShow.text = binding.PhoneNo.text.toString()
            val savebutton = dialogLayout.findViewById<Button>(R.id.btn_okay)
            val cancel = dialogLayout.findViewById<Button>(R.id.btn_cancel)
            heading.text = "Add positon"
            builder.show()
            savebutton.setOnClickListener {
            progrsDialog.setMessage("adding new person")
            progrsDialog.show()
            val person = person(binding.rankNo.text.toString(),binding.name.text.toString(),binding.spinnerPreAttach.getSelectedItem().toString(),binding.spinnerUnit.getSelectedItem().toString(),binding.PhoneNo.text.toString(),binding.Remarkss.text.toString())
            databasePerson.child(binding.rankNo.text.toString()).get().addOnSuccessListener() {
                if(it.value == null){
                    databasePerson.child(binding.rankNo.text.toString()).setValue(person).addOnSuccessListener {
                        progrsDialog.dismiss()
                        Toast.makeText(this, "successfully added Rank " + binding.rankNo.text.toString() , Toast.LENGTH_SHORT).show()
                        builder.dismiss()
                        finish()
                    }.addOnFailureListener{
                        Toast.makeText(this, "network problem", Toast.LENGTH_SHORT).show()
                        progrsDialog.dismiss()
                    }
                }else{
                    progrsDialog.dismiss()
                    Toast.makeText(this, "the rank " + binding.rankNo.text.toString() + " is already there" , Toast.LENGTH_SHORT).show()
                }
            }.addOnFailureListener {
                progrsDialog.dismiss()
                Toast.makeText(this, "can not add something went wrong", Toast.LENGTH_SHORT).show()
            }
            }
            cancel.setOnClickListener {
                builder.dismiss()
            }
        }
    }

}