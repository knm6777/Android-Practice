package com.example.roomexample

import android.os.Bundle
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.example.roomexample.databinding.ActivityMainBinding
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var myDao: MyDAO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        myDao = MyDatabase.getDatabase(this).getMyDao()
        runBlocking {
            with(myDao) {
                insertStudent(Student(1, "james"))
                insertStudent(Student(2, "john"))
                insertClass(ClassInfo(1, "c-lang", "Mon 9:00", "E301", 1))
                insertClass(ClassInfo(2, "android prog", "Tue 9:00", "E302", 1))
                insertClass(ClassInfo(3, "os", "Fri 9:00", "E303", 1))
                insertEnrollment(Enrollment(1, 1))
                insertEnrollment(Enrollment(1, 2))
            }
        }


        // 빈 데이터 리스트 생성.
        val items: ArrayList<String> = ArrayList()

        val allStudents = myDao.getAllStudents()
        allStudents.observe(this) {
            items.clear()
            for ((id, name) in it) {
                val str = StringBuilder().apply {
                    append(id)
                    append("-")
                    append(name)
                }.toString()
                items.add(str)
            }
            val adapter: ArrayAdapter<*> =
                ArrayAdapter<Any?>(this, android.R.layout.simple_list_item_1, items as List<Any?>)
            binding.list.adapter = adapter
        }

        // list 아이템 누르면 query 출력
        // 클릭된 아이디 저장해둘 변수
        var curId = -1  //초기화

        binding.list.onItemClickListener = OnItemClickListener { adapterView, view, position, id ->
            val selectedItem = adapterView.getItemAtPosition(position) as String
            val selItem = selectedItem.split("-")
            val selId = selItem[0].toInt()
            // 현재 클릭된 아이디 알아두기
            curId = selId

            runBlocking {
                val results = myDao.getStudentsWithEnrollment(selId)
                if (results.isNotEmpty()) {
                    val str = StringBuilder().apply {
                        append(results[0].student.id)
                        append("-")
                        append(results[0].student.name)
                        append(":")
                        for (c in results[0].enrollments) {
                            append(c.cid)
                            val clsResult = myDao.getClassInfo(c.cid)
                            if (clsResult.isNotEmpty())
                                append("(${clsResult[0].name})")
                            append(",")
                        }
                    }
                    binding.textQueryStudent.text = str
                } else {
                    binding.textQueryStudent.text = ""
                }
            }
        }


        // class_table에서 임의의 과목과 학생 id enrollment 에 추가
        binding.enroll.setOnClickListener {
            runBlocking {
                myDao.insertEnrollment(Enrollment(curId, 1))
                myDao.insertEnrollment(Enrollment(curId, 3))
            }
        }

        // student 삭제
        var i = 0
        binding.delete.setOnClickListener {
            i = 0
            runBlocking {
                val result = myDao.getStudentsWithEnrollment(curId)
                for(i in result){
                    for(j in i.enrollments)
                        myDao.deleteEnrollment(j)
                    myDao.deleteStudent(i.student)
                }
            }
        }


        // student 추가
        binding.addStudent.setOnClickListener {
            val id = binding.editStudentId.text.toString().toInt()
            val name = binding.editStudentName.text.toString()
            if (id > 0 && name.isNotEmpty()) {
                runBlocking {
                    myDao.insertStudent(Student(id, name))
                }
            }
        }
    }
}