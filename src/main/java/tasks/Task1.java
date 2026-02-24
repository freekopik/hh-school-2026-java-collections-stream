package tasks;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import common.Person;
import common.PersonService;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */

public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  public List<Person> findOrderedPersons(List<Integer> personIds) {
    /*
      Тут мы могли бы решить через два for. Псевдокод:
        ans = []
        for personId in personIds:
          for person in persons:
            if personId == person.id:
              ans.append(person)
      
      Сложность - O(n * m) из-за вложенности. 
    */

    Map<Integer, Person> idToPerson = personService.findPersons(personIds).stream() // O(n)
        .collect(Collectors.toMap(
            Person::id,
            p -> p
        ));

    return personIds.stream()
        .map(idToPerson::get) // O(m)
        .toList(); // O(m)
    
    // O(n) + O(m) + O(m) = O(n + 2m) = O(n + m)
  }
}
