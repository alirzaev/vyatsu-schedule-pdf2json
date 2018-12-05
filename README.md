# VyatSU schedule PDF parser

This application provides RESTful API for converting PDF files with student group schedules into JSON

Designed for [Vyatka State University](www.vyatsu.ru)

## Usage

### Compile JAR with dependencies

`mvn -DskipTests package`

### Run server

- Environment variables:

  - `PORT` - port on which listen to requests, default `80`

- If you don't want to compile JAR:

  `mvn -DskipTests compile exec:java`

- And if you have already compiled JAR:

  `java -jar target/vyatsu_pdf_parser-jar-with-dependencies.jar`

### API

`/api/v1/parse_pdf` - get group schedule in JSON format from the given link (PDF file)

Query params:

- `url` - URL to the PDF file

Request example:

```http
GET /api/v1/parse_pdf?url=https://www.vyatsu.ru/reports/schedule/Group/10820_1.pdf HTTP/1.1
```

Success response example:

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
    "success": "PARSED"
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

Error response examples:

- Error uccured during PDF file parsing (invalid PDF file)
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

- PDF file was successfully parsed but total count of rows representing lessons is invalid (doesn't equal to 14x7)
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

- VyatSU server doesn't response
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
      "error": "VYATSU_RU_ERROR"
    }
  }
  ```

- Unexpected error occured during PDF file parsing
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

- Seems like you did't provide URL to PDF file
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
