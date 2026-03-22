import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;

public class Main {

    public static void main(String[] args) throws Exception {
        ObjectMapper mapper = new ObjectMapper();

        InputStream inputStream = Main.class
                .getClassLoader()
                .getResourceAsStream("students.json");

        if (inputStream == null) {
            throw new RuntimeException("Файл students.json не найден в resources!");
        }

        List<Student> students = mapper.readValue(
                inputStream,
                new TypeReference<List<Student>>() {}
        );

        students.forEach(System.out::println);

        Optional<Integer> optionalYear = students.stream()
                .flatMap(student -> student.getBooks().stream())
                .distinct()
                .filter(book -> book.getYear() > 2000)
                .sorted((b1, b2) -> Integer.compare(b1.getPages(), b2.getPages()))
                .limit(3)
                .map(Book::getYear)
                .findFirst();

        System.out.println(optionalYear.orElse(null) != null
                ? "Год найденной книги: " + optionalYear.get()
                : "Книга после 2000 года не найдена");
    }
}
