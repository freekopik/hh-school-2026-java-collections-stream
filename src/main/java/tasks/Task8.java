package tasks;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import common.Person;
import common.PersonService;
import common.PersonWithResumes;
import common.Resume;

/*
  Еще один вариант задачи обогащения
  На вход имеем коллекцию персон
  Сервис умеет по personId искать их резюме (у каждой персоны может быть несколько резюме)
  На выходе хотим получить объекты с персоной и ее списком резюме
 */
public class Task8 {
  private final PersonService personService;

  public Task8(PersonService personService) {
    this.personService = personService;
  }

  public Set<PersonWithResumes> enrichPersonsWithResumes(Collection<Person> persons) {
    /*
      У этой задачи два решения. 

      1. Просто перебираем каждую персону и в findResumes кидаем только один id. 
      В таком случае мы сделаем столько поисковых запросов, сколько у нас персон:

      public Set<PersonWithResumes> enrichPersonsWithResumes(Collection<Person> persons) {
        return persons.stream()
        .map(p -> new PersonWithResumes(p, personService.findResumes(p.id())))
        .collect(Collectors.toSet()); 

      2. Один раз читаем все резюме пачкой и кидаем в мапу все уникальные резюме для каждого пользователя. 
      Запрос только один, поэтому накладных сетевых нагрузок меньше. 

      Я реализовал второй подход.
    */
   
    Set<Integer> personIds = persons.stream()
        .map(Person::id)
        .collect(Collectors.toSet());
    

    Set<Resume> allResumes = personService.findResumes(personIds);

    Map<Integer, Set<Resume>> groupedResumes = allResumes.stream()
        .collect(Collectors.groupingBy(Resume::personId, Collectors.toSet()));
    
    return persons.stream()
        .map(p -> new PersonWithResumes(p, groupedResumes.getOrDefault(p.id(), Set.of())))
        .collect(Collectors.toSet());
  }
}
