package com.example.studentman

import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    private val students = mutableListOf(
        StudentModel("Nguyễn Văn An", "SV001"),
        StudentModel("Trần Thị Bảo", "SV002"),
        StudentModel("Lê Hoàng Cường", "SV003"),
        StudentModel("Phạm Thị Dung", "SV004"),
        StudentModel("Đỗ Minh Đức", "SV005"),
        StudentModel("Vũ Thị Hoa", "SV006"),
        StudentModel("Hoàng Văn Hải", "SV007"),
        StudentModel("Bùi Thị Hạnh", "SV008"),
        StudentModel("Đinh Văn Hùng", "SV009"),
        StudentModel("Nguyễn Thị Linh", "SV010"),
        StudentModel("Phạm Văn Long", "SV011"),
        StudentModel("Trần Thị Mai", "SV012"),
        StudentModel("Lê Thị Ngọc", "SV013"),
        StudentModel("Vũ Văn Nam", "SV014"),
        StudentModel("Hoàng Thị Phương", "SV015"),
        StudentModel("Đỗ Văn Quân", "SV016"),
        StudentModel("Nguyễn Thị Thu", "SV017"),
        StudentModel("Trần Văn Tài", "SV018"),
        StudentModel("Phạm Thị Tuyết", "SV019"),
        StudentModel("Lê Văn Vũ", "SV020")
    )
    private lateinit var studentAdapter: StudentAdapter
    private var lastDeletedStudent: StudentModel? = null
    private var lastDeletedPosition: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        studentAdapter = StudentAdapter(students, ::editStudent, ::deleteStudent)

        findViewById<RecyclerView>(R.id.recycler_view_students).run {
            adapter = studentAdapter
            layoutManager = LinearLayoutManager(this@MainActivity)
        }

        findViewById<Button>(R.id.btn_add_new).setOnClickListener {
            showAddStudentDialog()
        }
    }

    private fun showAddStudentDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_student_form, null)
        val nameInput = dialogView.findViewById<android.widget.EditText>(R.id.edit_text_student_name)
        val idInput = dialogView.findViewById<android.widget.EditText>(R.id.edit_text_student_id)

        AlertDialog.Builder(this)
            .setTitle("Add New Student")
            .setView(dialogView)
            .setPositiveButton("Add") { _, _ ->
                val name = nameInput.text.toString().trim()
                val id = idInput.text.toString().trim()
                if (name.isNotEmpty() && id.isNotEmpty()) {
                    students.add(StudentModel(name, id))
                    studentAdapter.notifyItemInserted(students.size - 1)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun editStudent(position: Int) {
        val student = students[position]
        val dialogView = layoutInflater.inflate(R.layout.dialog_student_form, null)
        val nameInput = dialogView.findViewById<android.widget.EditText>(R.id.edit_text_student_name)
        val idInput = dialogView.findViewById<android.widget.EditText>(R.id.edit_text_student_id)

        nameInput.setText(student.studentName)
        idInput.setText(student.studentId)

        AlertDialog.Builder(this)
            .setTitle("Edit Student")
            .setView(dialogView)
            .setPositiveButton("Save") { _, _ ->
                student.studentName = nameInput.text.toString().trim()
                student.studentId = idInput.text.toString().trim()
                studentAdapter.notifyItemChanged(position)
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    private fun deleteStudent(position: Int) {
        lastDeletedStudent = students[position]
        lastDeletedPosition = position

        students.removeAt(position)
        studentAdapter.notifyItemRemoved(position)

        Snackbar.make(findViewById(R.id.main), "Student deleted", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                students.add(lastDeletedPosition, lastDeletedStudent!!)
                studentAdapter.notifyItemInserted(lastDeletedPosition)
            }.show()
    }
}
