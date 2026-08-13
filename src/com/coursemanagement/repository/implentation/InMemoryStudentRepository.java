package com.coursemanagement.repository.implentation;
import com.coursemanagement.model.Student;
import com.coursemanagement.repository.StudentRepository;

import java.util.*;

public class InMemoryStudentRepository implements StudentRepository {
    private final Map<Long, Student> students = new HashMap<>();

    private Long nextId = 1L;

    @Override
    public Student save(Student student) {

        if (student.getId() == null) {
            student.setId(nextId++);
        }

        students.put(student.getId(), student);

        return student;
    }

    @Override
    public Optional<Student> findById(Long id) {
        return Optional.ofNullable(students.get(id));
    }

    @Override
    public Optional<Student> findByEmail(String email) {

        for (Student student : students.values()) {

            if (student.getEmail().equals(email)) {
                return Optional.of(student);
            }

        }

        return Optional.empty();
    }

    @Override
    public List<Student> findAll() {
        return new ArrayList<>(students.values());
    }

    @Override
    public boolean existsByEmail(String email) {

        for (Student student : students.values()) {

            if (student.getEmail().equals(email)) {
                return true;
            }

        }

        return false;
    }

    @Override
    public void deleteById(Long id) {
        students.remove(id);
    }
}
