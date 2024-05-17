# <div style="text-align:center;"> **Учебный проект _"Телеграмм-банк"_ в рамках учебного курса _"GPB IT FACTORY"_.** :shipit: </div>

В данном репозитории представлен исходный код разрабатываемого проекта "Телеграмм-банк".
<hr>

## ***Содержание***

<div style="font-size: 16px">

- [Архитектура](#div-styletext-aligncenterархитектураdiv)
  - [FRONTEND](#frontend-telegram-bot-на-java)
  - [MIDDLE](#middle-java-сервис)
  - [BACKEND](#backend-java-сервис)
- [Диаграммы](#div-styletext-aligncenter-uml-диаграммы)
  - [Диаграмма компонентов](#диаграмма-компонентов)
  - [Диаграмма последовательности](#диаграмма-последовательности)
</div><hr>


## ***Архитектура***
Представленный проект состоит из 3 ключевых компонентов, подробнее рассмотренных ниже.

### FRONTEND (Telegram-bot на Java)

В качестве пользовательского интерфейса для взаимодействия с приложением используется телеграмм-бот.

### MIDDLE (Java-сервис)

Данный компонент занимается обработкой пользовательских запросов, валидацией, выполняет бизнес-логику, а также направляет запросы в BACKEND-компонент.

### BACKEND (Java-сервис)

Данный компонент занимается основными операциями, такими как: обработка транзакций, хранение данных пользователя и т.п.
<hr>

## ***UML-диаграммы***
### **Диаграмма компонентов**
![diagram_comp](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/gpb-it-factory/gudkov-telegram-bot/trunk/component_diagram.puml)

### **Описание диаграммы**
Передача сообщений между клиентом и сервисом происходит с помощью используемого в Telegram протокола передачи сообщений MTProto.
Внутри сервиса взаимодействие между компонентами осуществляется по протоколу HTTP.


### **Диаграмма последовательности**
![diagram_seq](http://www.plantuml.com/plantuml/proxy?src=https://raw.githubusercontent.com/gpb-it-factory/gudkov-telegram-bot/trunk/sequence_diagram.puml)
### **Описание диаграммы**
На данной диаграмме изображен абстрактный сценарий работы клиента с сервисом. Клиент отправляет команду, сервис занимается ее обработкой и возвращает ответ.
<hr>

> ### COMING SOON
> Данный файл будет расширяться в процессе разработки проекта. :frog:

