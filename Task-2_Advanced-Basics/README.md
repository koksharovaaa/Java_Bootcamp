Java Bootcamp: П: Продолжение изучения основ

Консольное приложение для управления списком питомцев. Демонстрирует работу с ООП, интерфейсами, Stream API и многопоточностью.

## Стек
- Java 18
- Gradle (Kotlin DSL)

## Запуск
cd T02/src/exerciseN
./gradlew run

## Составляющие проекта
- Иерархия Animal: Dog, Cat, Hamster, GuineaPig
- Интерфейсы Herbivore, Omnivore
- Расчёт порции корма
- Увеличение возраста через Stream API
- Асинхронные прогулки (потоки, TimeUnit)
- Собственный итератор