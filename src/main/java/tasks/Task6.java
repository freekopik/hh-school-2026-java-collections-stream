package tasks;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import common.Area;
import common.Person;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 {

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    HashMap<Integer, String> areaIdToRegion = new HashMap<>();

    /*
      Здесь я заранее сохраняю имя региона в мапу, чтобы 
      потом не пришлось сравнивать каждый areaId с каждым Area на равенство. 
      
      Таким образом получаем сложность O(n) вместо O(n * m).
    */

    for (Area area: areas) {
      areaIdToRegion.put(area.getId(), area.getName());
    }          

    return persons.stream()
        .flatMap(person -> personAreaIds.getOrDefault(person.id(), Set.of()).stream()
          .map(areaId -> String.format("%s - %s", 
              person.firstName(), 
              areaIdToRegion.getOrDefault(areaId, "")))
        )
        .collect(Collectors.toSet());
  }
}
