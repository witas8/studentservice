package com.example.demo.student;

import com.example.demo.student.exception.BadRequestException;
import com.example.demo.student.exception.StudentNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

//import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class StudentServiceTest {

    @Mock private StudentRepository studentRepository;
    private StudentService underTest;

    @BeforeEach
    void setUp() {
        //given
        underTest = new StudentService(studentRepository);
    }

    @Test
    void canGetAllStudents() {
        // when
        underTest.getAllStudents();

        // then
        // in the StudentRepository are many methods, but now I am testing getAllStudents
        // and in this method was used findAll(), that is why below is used this method
        verify(studentRepository).findAll();
    }

    @Test
    void canAddStudent() {
        // given
        Student student = new Student("Mikolaj", "mwitkowski@gmail.com", Gender.MALE);

        // when
        underTest.addStudent(student);

        // then
        //take a capture from an added object
        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        //point what method you want to verify in the Add Method (Save Method)
        verify(studentRepository).save(studentArgumentCaptor.capture());
        //take a value of the captured object that was taken during performing the Add Method
        Student capturedStudent = studentArgumentCaptor.getValue();
        assertThat(capturedStudent).isEqualTo(student);
    }

    @Test
    void willThrownWhenEmailIsTaken() {
        // given - object and true or false to force the throw situation
        Student student = new Student("Mikolaj", "mwitkowski@gmail.com", Gender.MALE);
        // student.getEmail() or anyString()
        given(studentRepository.selectExistsEmail(student.getEmail())).willReturn(true);

        // when + then
        assertThatThrownBy(() -> underTest.addStudent(student))
                .isInstanceOf(BadRequestException.class)
                .hasMessageContaining("Email " + student.getEmail() + " taken");

        // Remember that thrown new works like a break in a function so it stops the method
        // so we can test if after thrown the student for sure will not be save to the database (verify if never)
        verify(studentRepository, never()).save(any());

    }

    @Test
    void canDeleteStudent() {
        // given
        long id = 10;
        given(studentRepository.existsById(id))
                .willReturn(true);
        // when
        underTest.deleteStudent(id);

        // then
        verify(studentRepository).deleteById(id);
    }

    @Test
    void willThrowWhenDeleteStudentNotFound() {
        // given
        long id = 10;
        given(studentRepository.existsById(id))
                .willReturn(false);
        // when
        // then
        assertThatThrownBy(() -> underTest.deleteStudent(id))
                .isInstanceOf(StudentNotFoundException.class)
                .hasMessageContaining("Student with id " + id + " does not exists");

        verify(studentRepository, never()).deleteById(any());
    }
}