package com.globomed.learn

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton

class MainActivity : AppCompatActivity() {

    lateinit var databaseHelper: DatabaseHelper
    private val  employeeListAdapter = EmployeeListAdapter(this)
    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        databaseHelper = DatabaseHelper(this)
        recyclerView = findViewById<RecyclerView>(R.id.recyclerView)


        recyclerView.adapter = employeeListAdapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(databaseHelper))


        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener {
            val addEmployee = Intent(this, AddEmployeeActivity::class.java)
            startActivityForResult(addEmployee, 1)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK){
            employeeListAdapter.setEmployees(DataManager.fetchAllEmployees(databaseHelper))
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId){
            R.id.action_deleteAll -> {
                val builder = AlertDialog.Builder(this)
                builder.setMessage(R.string.confirm_sure)
                    .setPositiveButton(R.string.yes) {dialog, eId ->
                        val result = DataManager.deleteAllEmployee(databaseHelper)
                        Toast.makeText(applicationContext, "$result Deleted", Toast.LENGTH_SHORT).show()

                        setResult(RESULT_OK, Intent())
                        finish()

                    }.setNegativeButton(R.string.no){dialog, id ->
                        dialog.dismiss()
                    }
                val dialog = builder.create()
                dialog.setTitle("Are you sure")
                dialog.show()
                true

            }
            else -> {
                super.onOptionsItemSelected(item)
            }
        }
    }
}