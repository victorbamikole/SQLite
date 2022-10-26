package com.globomed.learn

import android.app.Activity
import android.app.DatePickerDialog
import android.content.ContentValues
import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_add.*
import java.text.SimpleDateFormat
import java.util.*
import com.globomed.learn.GloboMedDbContract as GloboMedDbContract1
import com.globomed.learn.GloboMedDbContract.EmployeeEntry as EmployeeEntry1

class AddEmployeeActivity : Activity() {

    private val myCalendar = Calendar.getInstance()
    private lateinit var dataBaseHelper: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add)

        dataBaseHelper = DatabaseHelper(this)

        // on clicking ok on the calender dialog
        val date = DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
            myCalendar.set(Calendar.YEAR, year)
            myCalendar.set(Calendar.MONTH, monthOfYear)
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)

            findViewById<EditText>(R.id.etDOB).setText(getFormattedDate(myCalendar.timeInMillis))
        }

        findViewById<EditText>(R.id.etDOB).setOnClickListener {
            setUpCalender(date)
        }

        findViewById<Button>(R.id.bCancel).setOnClickListener {
            finish()
        }
        findViewById<Button>(R.id.bSave).setOnClickListener {
            saveEmployee()
        }
    }

    private fun saveEmployee() {
        var isValid = true

        val etEmpName:EditText = findViewById<EditText>(R.id.etEmpName)
        val etDesignation:EditText = findViewById<EditText>(R.id.etDesignation)
        val etDOB:EditText = findViewById<EditText>(R.id.etDOB)

        etEmpName.error = if (etEmpName.text.toString().isEmpty()){
            isValid = false
            "required"
        }else{
            null
        }

        etDesignation.error = if (etDesignation.text.toString().isEmpty()){
            isValid = false
            "required"
        }else{
            null
        }

        if (isValid){
            val name:String = etEmpName.text.toString()
            val designation: String = etDesignation.text.toString()
            val dob: Long = myCalendar.timeInMillis
            val isSurgeon = if (sSurgeon.isChecked) 1 else 0

            val db: SQLiteDatabase = dataBaseHelper.writableDatabase

            val values = ContentValues()
            values.put(EmployeeEntry1.COLUMN_NAME, name)
            values.put(EmployeeEntry1.COLUMN_DESIGNATION, designation)
            values.put(EmployeeEntry1.COLUMN_DOB, dob)
            values.put(EmployeeEntry1.COLUMN_SURGEON, isSurgeon)

            val result: Long = db.insert(EmployeeEntry1.TABLE_NAME, null, values)

            setResult(RESULT_OK, Intent())

            Toast.makeText(applicationContext, "Employee Added", Toast.LENGTH_SHORT).show()

        }

        finish()

    }

    private fun setUpCalender(date: DatePickerDialog.OnDateSetListener) {

        DatePickerDialog(
            this, date, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
            myCalendar.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun getFormattedDate(dobInMilis: Long?): String {

        return dobInMilis?.let {
            val sdf = SimpleDateFormat("d MMM, yyyy", Locale.getDefault())
            sdf.format(dobInMilis)
        } ?: "Not Found"
    }
}
