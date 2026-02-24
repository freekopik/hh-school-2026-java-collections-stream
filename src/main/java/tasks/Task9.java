package tasks;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import common.Person;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  private Stream<Person> getCleanedStream(List<Person> persons) { 
      return persons.stream().skip(1); // безопасно пропускаем фальшивку без лишних проверок
  }

  public List<String> getNames(List<Person> persons) {
      return getCleanedStream(persons)
              .map(Person::firstName)
              .collect(Collectors.toList());
  }

  public Set<String> getDifferentNames(List<Person> persons) { 
      /*
        .distinct() не нужен, поскольку toSet() сделает то же самое.
      */
      return getNames(persons).stream()
              .collect(Collectors.toSet());
  }
  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    /* 
      Здесь лучше подходит .joining().
      Обычная конкатенация добавит лишний пробел, если фамилия или имя будет null. 
    */
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
            .filter(Objects::nonNull) 
            .collect(Collectors.joining(" ")); 
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    return persons.stream() // здесь просто переписал for-loop на stream
        .collect(Collectors.toMap(
          Person::id,
          p -> convertPersonToString(p),
          (oldValue, newValue) -> oldValue
        ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    /*
      Исходный алгоритм имеет сложность O(n * m) из-за вложенного цикла.
      Множества дают нам сложность O(n + m), так как поиск одного элемента занимает O(1).
    */
    Set<Person> set1 = new HashSet<>(persons1);
    return persons2.stream().anyMatch(set1::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Collection<Integer> numbers) {
    /*
      Изменил сигнатуру метода. 
      Интуитивно кажется, что передавать Stream<Integer> как аргумент - плохая идея. 
      Потому что Stream - это как бы одноразовый, ленивый поток. 
      Переписал так, чтобы внешняя переменная не попадала в .stream().
    */
    return numbers.stream()
        .filter(n -> n % 2 == 0)
        .count(); // пользуемся встроенным методом вместо магии
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().toList(); // просто toList()
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);

    /*
      HashSet основан на HashMap, где элементы сета - ключи, а значения - заглушки. 
      Индекс бакета обычно равен самому числу по формуле hash & (capacity - 1). 
      Ближайший Capacity вычисляется на основе Size и loadFactor и является степенью двойки.
      Например, для 10000 элементов из ДЗ это 2^14 = 16384. 
      Как только произойдёт коллизия (один индекс бакета для разных чисел), псевдопорядок нарушится. 
    */

    assert snapshot.toString().equals(set.toString());
  }
}
