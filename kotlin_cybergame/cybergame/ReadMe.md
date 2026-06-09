# Cybergame Backend

Backend-сервис веб-игры на **Kotlin + Spring Boot 3**. Приложение работает с PostgreSQL через хранимые функции и процедуры (`user_*`, `task_*`, `achievements_*`).

Базовый адрес после запуска: `http://localhost:8080`

---

## Требования

- **Java 17** или новее
- **PostgreSQL** с базой `game_db` и пользователем с правами на неё
- Инициализированная схема БД (таблицы и процедуры из SQL-скриптов проекта БД)

---

## Настройка подключения к базе данных

### 1. `application.properties`

Файл: `src/main/resources/application.properties`

Укажите параметры вашей PostgreSQL:

```properties
spring.datasource.url=jdbc:postgresql://127.0.0.1:5433/game_db
spring.datasource.username=game_user
spring.datasource.password=game_pass
spring.datasource.driver-class-name=org.postgresql.Driver
```

| Параметр | Описание |
|----------|----------|
| `spring.datasource.url` | JDBC-URL. Формат: `jdbc:postgresql://<хост>:<порт>/<имя_базы>` |
| `spring.datasource.username` | Имя пользователя PostgreSQL |
| `spring.datasource.password` | Пароль пользователя |
| `spring.datasource.driver-class-name` | Драйвер PostgreSQL (менять не нужно) |

Пример для локальной БД на стандартном порту:

```properties
spring.datasource.url=jdbc:postgresql://127.0.0.1:5432/game_db
spring.datasource.username=postgres
spring.datasource.password=your_password
spring.datasource.driver-class-name=org.postgresql.Driver
```

### 2. `DBParams.kt`

> **Важно:** DAO-слой подключается к БД через `DBConnection`, который читает константы из `src/main/kotlin/constants/DBParams.kt`. Значения там должны **совпадать** с `application.properties`.

```kotlin
object DBParams {
    const val URL: String = "jdbc:postgresql://127.0.0.1:5433/game_db"
    const val NAME: String = "game_user"
    const val PASSWORD: String = "game_pass"
}
```

Обновите `URL`, `NAME` и `PASSWORD` под вашу среду.

---

## Запуск приложения

Перейдите в корень проекта (`cybergame/`):

```bash
cd path/to/backend/kotlin_cybergame/cybergame
```

### Вариант 1 — Gradle (рекомендуется)

```bash
./gradlew bootRun
```

### Вариант 2 — через application plugin

```bash
./gradlew run
```

### Вариант 3 — IDE

Запустите класс `com.hse.cyber.MainApplication` (файл `MainApplication.kt`).

### Проверка

После успешного старта сервер слушает порт **8080**. Пример:

```bash
curl http://localhost:8080/user/get/all
```

---

## Формат ответов

Все ручки возвращают `ResponseEntity<Result<T>>`:

| Поле | Описание |
|------|----------|
| HTTP-статус | Код результата операции |
| Тело | Kotlin `Result` с данными или ошибкой |

Пример успешного ответа (поле `value` может отличаться в зависимости от сериализации Jackson):

```json
{
  "value": { ... }
}
```

Пример ошибки:

```json
{
  "value": null,
  "isFailure": true
}
```

---

## API: Пользователи (`/user`)

### Валидация полей при регистрации и авторизации

Все строковые поля (`name`, `login`, `password`, `secretWord`) должны быть **длиннее 3 символов**. Иначе — `400 Bad Request` (`InvalidFormatException`).

---

### `GET /user/auth` — авторизация

**Тело запроса** (`Content-Type: application/json`):

```json
{
  "login": "player1",
  "password": "pass1234"
}
```

| Поле | Тип | Описание |
|------|-----|----------|
| `login` | string | Логин пользователя |
| `password` | string | Пароль |

**Успех — `200 OK`**, тело: `Result<User>`:

```json
{
  "userId": 1,
  "name": "Игрок1",
  "login": "player1",
  "secretWord": "кот",
  "isAdmin": false
}
```

**Ошибки:**

| Код | Причина |
|-----|---------|
| `400` | Неверный формат полей |
| `404` | Пользователь не найден |
| `500` | Ошибка БД |
| `503` | Прочая ошибка сервиса |

---

### `POST /user/register` — регистрация

**Тело запроса:**

```json
{
  "name": "Игрок1",
  "login": "player1",
  "password": "pass1234",
  "secretWord": "кот"
}
```

| Поле | Тип | Описание |
|------|-----|----------|
| `name` | string | Имя игрока |
| `login` | string | Логин (уникальный) |
| `password` | string | Пароль |
| `secretWord` | string | Секретное слово |

**Успех — `200 OK`**, тело: `Result<Long>` — id нового пользователя.

**Ошибки:** `400`, `404`, `500`, `503` (см. `/user/auth`).

---

### `GET /user/get/id/{userId}` — пользователь по id

**Параметры пути:**

| Параметр | Тип | Описание |
|----------|-----|----------|
| `userId` | long | Id пользователя |

**Пример:** `GET /user/get/id/1`

**Успех — `200 OK`**, тело: `Result<User>` (структура как в `/user/auth`).

> Если пользователь не найден, исключение пробрасывается без обработки в контроллере.

---

### `GET /user/get/all` — все пользователи

**Параметры:** нет.

**Успех — `200 OK`**, тело: `Result<List<User>>`.

---

## API: Задачи (`/task`)

---

### `GET /task/get` — все задачи

**Параметры:** нет.

**Успех — `200 OK`**, тело: `Result<List<Task>>`:

```json
[
  {
    "id": 1,
    "header": "Заголовок",
    "hint": "подсказка",
    "description": "Описание задачи",
    "answer": "FLAG{hello}",
    "points": 100
  }
]
```

> Ответ содержит правильные ответы — предназначено для админки.

---

### `GET /task/get/solved/{userId}` — решённые задачи пользователя

**Параметры пути:**

| Параметр | Тип | Описание |
|----------|-----|----------|
| `userId` | long | Id пользователя |

**Пример:** `GET /task/get/solved/1`

**Успех — `200 OK`**, тело: `Result<List<Task>>` — список решённых задач (может быть пустым `[]`).

**Ошибки:** `404`, если пользователь не найден в БД.

---

### `POST /task/solve` — отправить ответ на задачу

**Тело запроса:**

```json
{
  "userId": 1,
  "taskId": 3,
  "answer": "FLAG{hello}"
}
```

| Поле | Тип | Описание |
|------|-----|----------|
| `userId` | long | Id игрока |
| `taskId` | long | Id задачи |
| `answer` | string | Ответ игрока |

**Успех — `200 OK`**, тело: `Result<Unit>`.

**Ошибки:**

| Код | Причина |
|-----|---------|
| `400` | Неверный ответ (`WrongTaskAnswerException`) |
| `404` | Задача или пользователь не найдены |
| `409` | Задача уже решена (`TaskAlreadySolvedException`) |
| `500` | Ошибка БД |
| `503` | Прочая ошибка сервиса |

---

### `POST /task/create` — создать задачу

**Тело запроса:**

```json
{
  "header": "Заголовок",
  "description": "Описание",
  "answer": "FLAG{hello}",
  "points": 100,
  "hint": "подсказка"
}
```

| Поле | Тип | Обязательное | Описание |
|------|-----|--------------|----------|
| `header` | string | да | Заголовок |
| `description` | string | да | Условие задачи |
| `answer` | string | да | Правильный ответ |
| `points` | int | да | Баллы за решение |
| `hint` | string \| null | нет | Подсказка (пустая строка, если null) |

**Успех — `200 OK`**, тело: `Result<Long>` — id новой задачи.

---

### `DELETE /task/delete/{taskId}` — удалить задачу

**Параметры пути:**

| Параметр | Тип | Описание |
|----------|-----|----------|
| `taskId` | long | Id задачи |

**Пример:** `DELETE /task/delete/1`

**Успех — `200 OK`**, тело: `Result<Unit>`.

> Удаляет задачу из каталога и очищает прогресс у всех пользователей.

---

## API: Достижения (`/achievement`)

---

### `GET /achievement/get` — достижения пользователя

**Тело запроса** (`Content-Type: application/json`):

```json
1
```

Передаётся **число** — id пользователя (Long).

**Успех — `200 OK`**, тело: `Result<List<Achievement>>`:

```json
[
  {
    "id": 1,
    "header": "Первый флаг"
  }
]
```

---

### `POST /achievement/grant` — выдать достижение

**Тело запроса:**

```json
{
  "achievementId": 1,
  "userId": 1
}
```

| Поле | Тип | Описание |
|------|-----|----------|
| `achievementId` | long | Id достижения |
| `userId` | long | Id пользователя |

**Успех — `200 OK`**, тело: `Result<Unit>`.

---

---

## Структура проекта

```
src/main/kotlin/
├── controller/     # REST-ручки
├── dao/            # Работа с PostgreSQL
├── model/          # Модели данных
├── constants/      # SQL-запросы и имена полей БД
├── config/         # Подключение к БД
└── MainApplication.kt
```
