# Spring Boot Starter JDBC DAO
> A Spring Boot starter to create simple DAOs using JDBC Template. 
>
> The intention of this library is to be a simple implementation of DAO classes, when using big ORM frameworks is 
> overkill, and/or you want to have more control in the sql statements you want to execute, and don't want to write 
> the same things every time you want to query an object from the database.

## How to install

#### Requirements

- [Maven](https://maven.apache.org/download.cgi)
- [Spring Boot 2.2.x](https://spring.io/projects/spring-boot)

#### Installation

```bash
git clone https://github.com/ndiaz42/spring-boot-starter-jdbc-dao.git
cd spring-boot-starter-jdbc-dao
mvn clean install
```

## Usage

#### Adding the dependency
Add this dependecy to your project's `pom.xml`
```xml
<dependency>
    <groupId>com.github.ndiaz42</groupId>
    <artifactId>jdbc-dao-spring-boot-starter</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```

#### Creating your DAOs

Suppose you want to perform some CRUD operations in your "User" table, so you have an `User` class and want to create
a DAO for it.  

##### Extend `JdbcEntityDao`
```java
@Repository
public class UserDao extends JdbcEntityDao<User> {
  public User findUserById(Long id) {
    String sql = "select * from USER where ID = :id"; 
    return queryForObject(sql, new MapSqlParameterSource("id", id));
  }
}
```

##### Define a `RowMapper`
Define a `RowMapper<User>` and anotate it with `@Component` so your DAO can detect it. 

```java
@Component
public class UserRowMapper implements RowMapper<User> {
  @Override
  public User mapRow(ResultSet rs, int row) throws SQLException {
    User user = new User();
    user.setId(rs.getLong("ID"));
    user.getName(rs.getString("NAME"));
    // etc
    return user;
  }
}
``` 
If you don't define a `RowMapper`, Spring's `BeanPropertyRowMapper` will be used by default.

##### Define a `RowUnmapper`

A `RowUnmapper<User>`, used primary on create and update statements. These are the parameters used by the sql query.

If you don't define a `RowUnmapper`, Spring's `BeanPropertySqlParameterSource` will be used by default.
```java
@Component
public class UserRowUnmapper implements RowUnmapper<User> {
  @Override
  public AbstractSqlParameterSource getSqlParameters(final User user) {
    MapSqlParameterSoruce params = new MapSqlParameterSource();
    params.addValue("id", user.getId());
    params.addValue("name", user.getName());
    // etc
    return params;
  }
}
```

#### Loading SQL from a file

If you want to have your SQL queries in a single place, you can create a file in `src/main/resources/sql/User.yml`.
See [Configuration](#Configuration) to enable this feature.
```yaml
findAll: select * from USER
findById: select * from USER where ID = :id
```
```java
@Repository
public class UserDao extends JdbcEntityDao<User> {
  public User findUserById(Long id) {
    String sql = getSql("User", "findById");
    return queryForObject(sql, new MapSqlParameterSource("id", id));
  }
}
```

#### Pagination
You can paginate the results using known Spring Data's `Page` and `Pageable` classes:
```java
@Repository
public class UserDao extends JdbcEntityDao<User> {
  public Page<User> findAll(Pageable pageable) {
    String sql = "select * from USER";
    return queryForPage(sql, pageable);
  }
}
```

## Configuration

Add the following properties to your `application.properties` or `application.yml` file if you want to enable sql file
loading.

```yaml
dao:
  enable-sql-file: [default false]
  path: [default "classpath*:/sql"]
```