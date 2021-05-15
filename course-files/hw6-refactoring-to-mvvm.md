# Рефакторинг проекта по паттерну проектирования MVVM либо MVP

Необходимо произвести рефакторинг существующего кода приложения с использованием на выбор:
1. MVVM - использовать компоненты от Google: ViewModel + LiveData
2. MVP - в качестве библиотеки использовать Moxy

В рамках рефакторинга необходимо будет удалить существующий сервис загрузки контактов, и всю работу по загрузке контактов из сервиса вынести в репозиторий, который будет использоваться в ViewModel/Presenter соответственно выбранного паттерна проектирования.

https://developer.android.com/topic/libraries/architecture/viewmodel

https://github.com/Arello-Mobile/Moxy

https://developer.android.com/topic/libraries/architecture/livedata