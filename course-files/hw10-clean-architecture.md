# Произвести рефакторинг приложения с учетом Clean Architecture

- Разбить приложение на слои - gradle модули:
- Entity и Interactors/UseCases - чисто java модули: apply plugin: 'java'
- Адаптеры и интерфейсы(Presenter/ViewModel, Репозитории) и платформенный слой(Детали реализации) - android модули: apply plugin: 'com.android.library'
- Конфигурация(DI) - модуль android приложения: apply plugin: 'com.android.application'

- Следовать правилу зависимостей