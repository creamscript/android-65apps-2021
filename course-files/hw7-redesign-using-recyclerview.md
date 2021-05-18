# Переделать экран списка контактов на RecyclerView

Добавить поиск по именам. Лучше использовать SearchView. Каждый новый ввод символа в SearchView - должен приводить к пере-запросу данных списка контактов из репозитория. При пустом вводе - выводится весь список контактов

Отслеживание изменений через DiffUtils. Желательно производить diff на отдельном потоке, для этого можно воспользоваться готовой реализацией адаптера - ListAdapter

Написать свою реализацию разделителей между элементами, через ItemDecorator

https://developer.android.com/reference/androidx/recyclerview/widget/ListAdapter

https://www.tutorialspoint.com/how-to-use-searchview-in-toolbar-android
