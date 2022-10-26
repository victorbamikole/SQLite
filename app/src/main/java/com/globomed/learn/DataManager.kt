package com.globomed.learn

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import com.globomed.learn.GloboMedDbContract.EmployeeEntry

object DataManager {

    fun fetchAllEmployees(databaseHelper: DatabaseHelper): ArrayList<Employee> {

        val employees = ArrayList<Employee>()
        var db: SQLiteDatabase = databaseHelper.readableDatabase

        val columns: Array<String> = arrayOf(
            EmployeeEntry.COLUMN_ID,
            EmployeeEntry.COLUMN_NAME,
            EmployeeEntry.COLUMN_DOB,
            EmployeeEntry.COLUMN_DESIGNATION,
            EmployeeEntry.COLUMN_SURGEON
        )

        val cursor = db.query(
            EmployeeEntry.TABLE_NAME, columns, null, null, null, null,
            null,
        )

        val idPos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_ID)
        val namePos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_NAME)
        val dobPos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_DOB)
        val designationPos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_DESIGNATION)
        val surgeonPos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_SURGEON)

        while (cursor.moveToNext()) {
            val id = cursor.getString(idPos)
            val name: String = cursor.getString(namePos)
            val dob: Long = cursor.getLong(dobPos)
            val designation: String = cursor.getString(designationPos)
            val surgeon: Int = cursor.getInt(surgeonPos)

            employees.add(Employee(id, name, dob, designation, surgeon))
        }

        cursor.close()

        return employees

    }

    fun fetchEmployee(databaseHelper: DatabaseHelper, empId: String): Employee? {
        val db: SQLiteDatabase = databaseHelper.readableDatabase
        var employee: Employee? = null

        val columns: Array<String> = arrayOf(
            EmployeeEntry.COLUMN_NAME,
            EmployeeEntry.COLUMN_DOB,
            EmployeeEntry.COLUMN_DESIGNATION,
            EmployeeEntry.COLUMN_SURGEON
        )

        val selection: String = EmployeeEntry.COLUMN_ID + " LIKE ? "

        val selectionArgs: Array<String> = arrayOf(empId)

        val cursor = db.query(
            EmployeeEntry.TABLE_NAME, columns, selection, selectionArgs, null, null,
            null,
        )

        val namePos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_NAME)
        val dobPos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_DOB)
        val designationPos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_DESIGNATION)
        val surgeonPos: Int = cursor.getColumnIndex(EmployeeEntry.COLUMN_SURGEON)

        while (cursor.moveToNext()) {
            val name: String = cursor.getString(namePos)
            val dob: Long = cursor.getLong(dobPos)
            val designation: String = cursor.getString(designationPos)
            val surgeon: Int = cursor.getInt(surgeonPos)

            employee = Employee(empId, name, dob, designation, surgeon)
        }

        cursor.close()

        return employee

    }

    fun updateEmployee(databaseHelper: DatabaseHelper, employee: Employee) {
        var db: SQLiteDatabase = databaseHelper.writableDatabase

        val values = ContentValues()
        values.put(EmployeeEntry.COLUMN_NAME, employee.name)
        values.put(EmployeeEntry.COLUMN_DESIGNATION, employee.designation)
        values.put(EmployeeEntry.COLUMN_DOB, employee.dob)
        values.put(EmployeeEntry.COLUMN_SURGEON, employee.isSurgeon)

        val selection: String = EmployeeEntry.COLUMN_ID + " LIKE ? "

        val selectionArgs: Array<String> = arrayOf(employee.id)

        db.update(EmployeeEntry.TABLE_NAME, values, selection, selectionArgs)


    }

    fun deleteEmployee(databaseHelper: DatabaseHelper, empId: String): Int {
        var db: SQLiteDatabase = databaseHelper.writableDatabase


        val selection: String = EmployeeEntry.COLUMN_ID + " LIKE ? "

        val selectionArgs: Array<String> = arrayOf(empId)

        return db.delete(EmployeeEntry.TABLE_NAME, selection, selectionArgs)
    }

    fun deleteAllEmployee(databaseHelper: DatabaseHelper): Int {
        var db: SQLiteDatabase = databaseHelper.writableDatabase
        return db.delete(EmployeeEntry.TABLE_NAME, "1", null)
    }

}