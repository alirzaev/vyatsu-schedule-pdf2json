# pdf2json-конвертер для VyatSU schedule

[![Build Status](https://travis-ci.org/alirzaev/vyatsu-schedule-pdf2json.svg?branch=master)](https://travis-ci.org/alirzaev/vyatsu-schedule-pdf2json)

Данный сервер предоставляет REST API для перевода PDF-файлов с расписанием в JSON.

Разработано для [Вятского государственного университета](https://www.vyatsu.ru).

## Для разработчиков

### Необходимые переменные окружения

`PORT` - порт, который сервер будет слушать, по умолчанию `80`.

### Сборка самодостоточного JAR-архива

`mvn -DskipTests package`

### Запуск

- Без предварительной сборки JAR-архива:

  `mvn -DskipTests compile exec:java`

- И если архив уже собран:

  `java -jar target/pdf2json-jar-with-dependencies.jar`

### Docker

1. Собираем образ

   ```
   docker build -t imagename .
   ```

2. Запускаем

   ```
   docker run --name somename -d -p 8080:80 imagename
   ```

### API

`/api/v2/convert` - перевести PDF-файл с расписанием в JSON

Параметры строки запроса (query string):

- `url` - URL PDF-файла

#### Пример

Запрос:

```http
GET /api/v2/convert?url=https://www.vyatsu.ru/reports/schedule/Group/10820_1.pdf HTTP/1.1
```

<details>
<summary>Ответ:</summary>

```http
HTTP/1.1 200 OK
Connection: keep-alive
Content-Encoding: gzip
Content-Type: application/json
Date: Wed, 05 Dec 2018 12:54:18 GMT
Server: nginx
Transfer-Encoding: chunked
Vary: Accept-Encoding

{
  "meta": {
    "status": 200,
    "success": "CONVERTED"
  },
  "data": {
    "schedule": [
      [
        [
          "Теория автоматов Лабораторная работа Мельцов В.Ю. 1-113",
          "Теория автоматов Лабораторная работа Мельцов В.Ю. 1-113",
          "Теория автоматов Лекция Мельцов В.Ю. 1-236",
          "",
          "",
          "",
          ""
        ],
        [
          "",
          "",
          "Вычислительная математика Лекция Исупов К.С. 1-128",
          "Физика Лекция Будин А.Г. 2-209",
          "",
          "",
          ""
        ],
        [
          "",
          "Электротехника и электроника Лекция Куваев А.С. 2-408",
          "Электротехника и электроника Лабораторная работа Куваев А.С. 2-308",
          "Электротехника и электроника Лабораторная работа Куваев А.С. 2-308",
          "",
          "",
          ""
        ],
        [
          "",
          "",
          "Элективные дисциплины (модули) по физической культуре и спорту Практическое занятие Преподаватель К.Ф. 9-120",
          "",
          "Математика Лекция Ситникова И.В. 1-242",
          "Математика Лабораторная работа Ситникова И.В. 1-227",
          ""
        ],
        [
          "",
          "Вычислительная математика Лабораторная работа Исупов К.С. 1-514",
          "Вычислительная математика Лабораторная работа Исупов К.С. 1-514",
          "",
          "",
          "",
          ""
        ],
        [
          "п ",
          "",
          "Компьютерная графика Лекция Вожегов Д.В. 1-242",
          "",
          "",
          "",
          ""
        ]
      ],
      [
        [
          "Физика Практическое занятие Будин А.Г. 2-404",
          "Компьютерная графика Лабораторная работа Клюкин В.Л. 1-116",
          "Компьютерная графика Лабораторная работа Клюкин В.Л. 1-116",
          "Теория автоматов Лекция Мельцов В.Ю. 1-236",
          "",
          "",
          ""
        ],
        [
          "",
          "Электротехника и электроника Лабораторная работа Куваев А.С. 2-307",
          "Электротехника и электроника Лабораторная работа Куваев А.С. 2-307",
          "Теория автоматов Практическое занятие Мельцов В.Ю. 1-239",
          "",
          "",
          ""
        ],
        [
          "",
          "Иностранный язык Лабораторная работа Дубовцева Л.В. 1-420",
          "Математика Лабораторная работа Ситникова И.В. 1-530",
          "",
          "",
          "",
          ""
        ],
        [
          "",
          "",
          "Элективные дисциплины (модули) по физической культуре и спорту Практическое занятие Преподаватель К.Ф. 9-120",
          "",
          "Математика Лекция Ситникова И.В. 2-409",
          "",
          ""
        ],
        [
          "",
          "Электротехника и электроника Лекция Куваев А.С. 2-408",
          "Вычислительная математика Лекция Исупов К.С. 1-242",
          "Вычислительная математика Практическое занятие Исупов К.С. 1-111",
          "",
          "",
          ""
        ],
        [
          "",
          "",
          "Компьютерная графика Лекция Вожегов Д.В. 1-242",
          "",
          "",
          "",
          ""
        ]
      ]
    ]
  }
}
```

</details>

#### Возможные ошибки

- Ошибка при разборе PDF-файла (битый файл)
  ```http
  HTTP/1.1 200 OK
  Connection: keep-alive
  Content-Encoding: gzip
  Content-Type: application/json
  Date: Wed, 05 Dec 2018 12:54:18 GMT
  Server: nginx
  Transfer-Encoding: chunked
  Vary: Accept-Encoding

  {
    "meta": {
      "status": 422,
      "error": "PDF_PARSE_ERROR"
    }
  }
  ```

- Данные из файла извлечены, но при проверке оказалось, что количество строк
  в расписанни не соответствует контрольному значению (14x7).
  ```http
  HTTP/1.1 200 OK
  Connection: keep-alive
  Content-Encoding: gzip
  Content-Type: application/json
  Date: Wed, 05 Dec 2018 12:54:18 GMT
  Server: nginx
  Transfer-Encoding: chunked
  Vary: Accept-Encoding

  {
    "meta": {
      "status": 422,
      "error": "INVALID_ROW_COUNT"
    }
  }
  ```

- Неизвестная ошибка
  ```http
  HTTP/1.1 200 OK
  Connection: keep-alive
  Content-Encoding: gzip
  Content-Type: application/json
  Date: Wed, 05 Dec 2018 12:54:18 GMT
  Server: nginx
  Transfer-Encoding: chunked
  Vary: Accept-Encoding

  {
    "meta": {
      "status": 500,
      "error": "INTERNAL_SERVER_ERROR"
    }
  }
  ```

- Не предоставлен URL для PDF-файла
  ```http
  HTTP/1.1 200 OK
  Connection: keep-alive
  Content-Encoding: gzip
  Content-Type: application/json
  Date: Wed, 05 Dec 2018 12:54:18 GMT
  Server: nginx
  Transfer-Encoding: chunked
  Vary: Accept-Encoding

  {
    "meta": {
      "status": 500,
      "error": "NO_URL"
    }
  }
  ```
