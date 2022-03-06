package com.example.demo.student;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class StudentRepositoryTest {

    @Autowired
    private StudentRepository underTest;

    @Test
    void isShouldCheckIfStudentEmailExists() {
        // given
        String email = "mwitkowski@gmail.com"; //crtl+alt+v to extract constant (or method's result) to a variable
        Student student = new Student("Mikolaj", email, Gender.MALE);
        underTest.save(student);

        //when
        boolean exists = underTest.selectExistsEmail(email);

        //then
        assertThat(exists).isTrue();
    }
}