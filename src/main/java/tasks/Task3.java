package tasks;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;

import common.Person;

/*
Задача 3
Отсортировать коллекцию сначала по фамилии, по имени (при равной фамилии), и по дате создания (при равных фамилии и имени)
 */
public class Task3 {

  public static List<Person> sort(Collection<Person> persons) {
    return persons.stream()
        .sorted(Comparator.comparing(Person::secondName, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Person::firstName, Comparator.nullsLast(Comparator.naturalOrder()))
                .thenComparing(Person::createdAt, Comparator.nullsLast(Comparator.naturalOrder())))
        .toList();
  }
}
