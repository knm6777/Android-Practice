package com.example.roomexample

import androidx.lifecycle.LiveData
import androidx.room.*

data class StudentWithEnrollments(  // 1:N 관계
    @Embedded val student: Student,
    @Relation(
        parentColumn = "student_id",
        entityColumn = "sid"
    )
    val enrollments: List<Enrollment>
)

@Dao
interface MyDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)  // INSERT, key 충돌이 나면 새 데이터로 교체
    suspend fun insertStudent(student: Student)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertClass(classInfo: ClassInfo)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEnrollment(enrollment: Enrollment)

    @Query("SELECT * FROM student_table")
    fun getAllStudents(): LiveData<List<Student>>        // LiveData<> 사용

    /*
    @Query("SELECT * FROM enrollment")
    suspend fun getAllEnrollment(): List<Enrollment>
    // fun getAllEnrollment(): LiveData<List<Enrollment>>        // LiveData<> 사용

    //@Query("SELECT * FROM enrollment WHERE sid = :id")
    //suspend fun getEnrollmentById(id: Int): List<Enrollment>
*/
    @Query("SELECT * FROM student_table WHERE name = :sname")   // 메소드 인자를 SQL문에서 :을 붙여 사용
    suspend fun getStudentByName(sname: String): List<Student>

    @Query("SELECT * FROM student_table WHERE student_id = :id")   // 메소드 인자를 SQL문에서 :을 붙여 사용
    suspend fun getStudentById(id: Int): List<Student>

    @Delete
    suspend fun deleteStudent(student: Student) // primary key is used to find the student

    @Delete
    suspend fun deleteEnrollment(enrollment: Enrollment)

    @Transaction
    @Query("SELECT * FROM student_table WHERE student_id = :id")
    suspend fun getStudentsWithEnrollment(id: Int): List<StudentWithEnrollments>

    @Query("SELECT * FROM class_table WHERE id = :id")
    suspend fun getClassInfo(id: Int): List<ClassInfo>
}