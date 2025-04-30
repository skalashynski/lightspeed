### 🔍 Context

This project was developed as part of a technical assignment for **[Lightspeed](https://www.lightspeedhq.com)** — a global commerce platform powering merchants around the world.
Technical task: **[GitHub](https://github.com/Ecwid/new-job/blob/master/SQL-parser.md)**

# 🧠 SQL Parser

A lightweight, extensible SQL parser in Java that breaks down `SELECT` queries into structured objects (AST).  
Supports nested subqueries, joins, and common clauses like `WHERE`, `GROUP BY`, `ORDER BY`, etc.

---

## ✅ Features

- Parses `SELECT`, `FROM`, `WHERE`, `GROUP BY`, `HAVING`, `ORDER BY`, `LIMIT`, `OFFSET`
- Supports nested subqueries inside `FROM` and `JOIN`
- Handles `JOIN` clauses with and without conditions
- Parses logical expressions in `WHERE` and `HAVING` (`AND`, `OR`)
- Clean object model: `Query`, `Column`, `Table`, `Join`, `ConditionNode`, etc.
- Easy to extend for additional clauses
- Parameterized unit tests with JUnit 5

---

## 📦 Project Structure
    src/
    ├── model/                  # SQL AST classes (Query, Table, etc.) 
    ├── clause/                 # Individual clause parsers 
    ├── engine/                 # SqlParsingEngine (entry point) 
    └── test/                   # JUnit 5 parameterized tests
---

## 🚀 Usage

### Requirements

- Java 17+
- Maven

### Build

```bash
mvn clean install
```


### Example
```java
SqlParsingEngine engine = new SqlParsingEngine();

String sql = "SELECT a.name FROM author a LEFT JOIN book b ON a.id = b.author_id WHERE b.cost > 100";
Query query = engine.parse(sql);

System.out.println(query.getColumns());
System.out.println(query.getFromSources());
System.out.println(query.getJoins());
System.out.println(query.getWhere());
```

## 🧪 Running Tests
```bash
mvn test
```
The project uses `@ParameterizedTest` with JSON fixtures to verify correctness across complex SQL cases.

## 🛠️ Extending the Parser
To add new clause support:

Create a class that implements `ClauseParser`.

Register it in `SqlParsingEngine`.

Add test coverage.