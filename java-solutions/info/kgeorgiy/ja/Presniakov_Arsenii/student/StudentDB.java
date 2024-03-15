package info.kgeorgiy.ja.Presniakov_Arsenii.student;

import info.kgeorgiy.java.advanced.student.GroupName;
import info.kgeorgiy.java.advanced.student.Student;
import info.kgeorgiy.java.advanced.student.StudentQuery;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class StudentDB implements StudentQuery {
    @Override
    public List<String> getFirstNames(List<Student> students) {
        return getType(students, Student::getFirstName);
    }

    @Override
    public List<String> getLastNames(List<Student> students) {
        return getType(students, Student::getLastName);
    }

    @Override
    public List<GroupName> getGroups(List<Student> students) {
        return getType(students, Student::getGroup);
    }

    @Override
    public List<String> getFullNames(List<Student> students) {
        return getType(students, s->(s.getFirstName() + " " + s.getLastName()));
    }

    @Override
    public Set<String> getDistinctFirstNames(List<Student> students) {
        return students.
                stream().
                map(Student::getFirstName).
                collect(Collectors.toCollection(TreeSet::new));
    }

    @Override
    public String getMaxStudentFirstName(List<Student> students) {
        return students.stream().max(Student::compareTo).map(Student::getFirstName).orElse("");
    }

    @Override
    public List<Student> sortStudentsById(Collection<Student> students) {
        return sortBy(students, Comparator.comparingInt(Student::getId));
    }

    @Override
    public List<Student> sortStudentsByName(Collection<Student> students) {
        return sortBy(students, mainComparator);
    }

    @Override
    public List<Student> findStudentsByFirstName(Collection<Student> students, String name) {
        return filterBy(students, each -> name.equals(each.getFirstName()));
    }

    @Override
    public List<Student> findStudentsByLastName(Collection<Student> students, String name) {
        return filterBy(students, each -> name.equals(each.getLastName()));
    }

    @Override
    public List<Student> findStudentsByGroup(Collection<Student> students, GroupName group) {
        return filterBy(students, each -> group.equals(each.getGroup()));
    }

    @Override
    public Map<String, String> findStudentNamesByGroup(Collection<Student> students, GroupName group) {
        return students.stream().
                filter(each -> group.equals(each.getGroup())).
                collect(Collectors.toMap(
                        Student::getLastName,
                        Student::getFirstName,
                        BinaryOperator.minBy(String::compareTo)
                ));
    }

    private <E> List<E> getType(List<Student> students, Function<Student, E> supplier) {
        return students.stream().map(supplier).toList();
    }

    private List<Student> sortBy(Collection<Student> students, Comparator<Student> comparator) {
        return students.stream().sorted(comparator).toList();
    }
    private List<Student> filterBy(Collection<Student> students, Predicate<Student> pred) {
        return students.stream().filter(pred).sorted(mainComparator).toList();
    }

    static private final Comparator<Student> mainComparator =
            Comparator.
            comparing(Student::getLastName).
                    thenComparing(Student::getFirstName).
                    thenComparing(Student::getId, Comparator.reverseOrder());
}
