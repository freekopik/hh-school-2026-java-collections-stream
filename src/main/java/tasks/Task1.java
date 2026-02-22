package tasks;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

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
    Set<Person> persons = personService.findPersons(personIds);

    /*
      Тут мы могли бы решить через два for. Псевдокод:
        ans = []
        for personId in personIds:
          for person in persons:
            if personId == person.id:
              ans.append(person)
      
      Сложность - O(n * m) из-за вложенности. 
    */

    // По-моему, здесь Stream не даёт выигрыша
    Map<Integer, Person> idToPerson = new HashMap<>();
    for (Person p : persons) { // O(n)
        idToPerson.put(p.id(), p);
    }

    return personIds.stream()
        .map(idToPerson::get) // O(m)
        .toList(); // O(m)
    
    // O(n) + O(m) + O(m) = O(n + 2m) = O(n + m)
  }
}
