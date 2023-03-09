# Сервис медицинских показаний

В классе Main, создаются несколько тестовых данных о пациентах и записываются в файл (репозиторий)

## Задание. [Тестирование сервиса медицинских показаний](https://github.com/netology-code/jd-homeworks/blob/master/mocks/task2/README.md)
реализация по [ссылке](https://github.com/andmosc/healthcare-service/tree/master/src/test/java)

### Что нужно сделать
- Написать тесты для проверки класса MedicalServiceImpl, сделав заглушку для класса PatientInfoFileRepository, который он использует
    1. Проверить вывод сообщения во время проверки давления `checkBloodPressure`
    2. Проверить вывод сообщения во время проверки температуры `checkTemperature`
    3. Проверить, что сообщения не выводятся, когда показатели в норме.