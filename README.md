# WeatherServiceAdapter
Описание проекта.

В адаптере создано 4 маршрута. 
- Первый маршрут принимает REST запрос от сервиса А, валидирует входящее сообщение, возвращает статус 200 в случае валидного сообщения и статус 400 в случае невалидного сообщения.
- Второй маршрут фильтрует сообщения по полю "lng" и помещает в Headers значения долготы и широты.
- Третий маршрут берет из входящего сообщения заголовки (Headers). составляет из них строку запроса и обращается во внешний сервис погоды. Оттуда получает JSON {value: int} со значением температуры, собирает из двух сообщений одно и выводит в начало четвертого маршрута.
- Четвертый маршрут отправляет полученное сообщение во внешний сервис В в виде POST запроса.

Написаны тесты для первых двух маршрутов, протестирована связь с внешним сервисом погоды через MockRestServiceServer. Частично написан тест для третьего маршрута, однако успешно доделать и пройти его не удается. Camel routes не использует MockRestServiceServer, попытки выполнить тест с использованием MockEndpoints и метода isMockEndpointsAndSkip() не привели к успеху.

Тесты для первых двух маршрутов выполняются при комментировании тел третьего и четвертого маршрутов. В чем причина этого, я понять не смог. 

Пути для точки входа сервиса А, внешнего сервиса погоды и выгрузки в сервис В могут изменены в файле application.yml, что позволяет гибко конфигурировать адаптер в момент развертывания сервиса.


