# SeaBattleRMI

Учебное приложение "Морской бой" для изучения технологии Java RMI.  

Состоит из 4х разделов:  
1. server - здесь находятся программа запуска серверной части и все компоненты, необходимые ей для работы (включая реализацию интерфейсов RMI, работающих на стороне сервера).
2. client - здесь находятся программа запуска клиентской части и все компоненты, необходимые ей для работы (включая реализацию интерфейсов RMI, работающих на стороне клиента).
3. shared - хдесь находятся компоненты, необходимые и клиенту и серверу: реализация дата-классов, конфиги, и интерфейсы RMI.
4. resources - здесь находятся ресурсы программы: картинки для отображения состояний клеток игры, шаблоны расположения кораблей.

Расположение main-классов:
* Сервер: src/main/java/server/ServerApp.java
* Клиент: src\main\java\client\ClientApp.java

Для работы программы необходимо:
1. Запустить серверное приложение;
2. Запустить 1го клиента и нажать кнопку "Start Game";
3. Запустить 2го клиента и нажать кнопку "Start Game";  

Оба клиента являются полностью идентичными.

Используемые интерфейсы RMI:
1. Register отвечает за регистрацию клиентов на игру, реализуется на стороне сервера;
2. MessageAcceptor информирует клиента о создании игровой сессии, а также наличиях изменений на поле. Реализуется на стороне клиента.
3. GameAcceptor помогает клиенту запрашивать у сервера о текущем состоянии игры, а также отправлять заявку на ход. Реализуется на стороне сервера.
