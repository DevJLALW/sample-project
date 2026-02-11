# Sample App - OpenRewrite Testing Project

A sample Maven project configured for Java 21 to demonstrate and test OpenRewrite recipes for codebase analysis and refactoring.

## Project Structure

```
sample-project/
├── pom.xml
└── src/
    ├── main/
    │   ├── java/com/example/app/
    │   │   ├── Application.java       (Main class - demonstrates legacy patterns)
    │   │   ├── UserService.java       (Service class - business logic)
    │   │   ├── User.java              (Entity class)
    │   │   └── DataUtil.java          (Utility class)
    │   └── resources/
    └── test/
        └── java/com/example/app/
            └── UserServiceTest.java   (Test cases)
```

## Configuration

- **Java Version**: 21
- **Build Tool**: Maven 3.x
- **Packaging**: JAR

## Dependencies

### Spring Framework
- spring-core (6.0.11)
- spring-context (6.0.11)

### Logging
- slf4j-api (2.0.7)
- log4j-api (2.20.0)
- log4j-core (2.20.0)
- log4j-slf4j2-impl (2.20.0)

### Utilities
- commons-lang3 (3.13.0)
- gson (2.10.1)

### Testing
- junit (4.13.2)

## Code Patterns for OpenRewrite Analysis

This project includes several patterns that OpenRewrite recipes can analyze and potentially modernize:

### 1. **Legacy Date API**
- `Application.java`: Uses `java.util.Date` (legacy)
- OpenRewrite Recipe: `org.openrewrite.java.time.JavaUtilDateToInstant`

### 2. **Raw Types and Generics**
- `DataUtil.java`: Uses raw types and unchecked conversions
- OpenRewrite Recipe: `org.openrewrite.java.Java17ToJava21`

### 3. **String Construction**
- `Application.java`: Uses `StringBuffer` (legacy)
- OpenRewrite Recipe: `org.openrewrite.java.lang.UseStringBuilderInsteadOfStringBuffer`

### 4. **Legacy Test Framework**
- `UserServiceTest.java`: Uses JUnit 4
- OpenRewrite Recipe: `org.openrewrite.java.testing.junit5.JUnit4to5Migration`

### 5. **Synchronized Methods**
- `DataUtil.java`: Legacy synchronized method pattern
- OpenRewrite Recipe: Modern concurrency patterns

### 6. **Logging Patterns**
- Multiple classes use Log4j with SLF4J
- OpenRewrite Recipe: Log level optimization, deprecated API migration

## Building the Project

```bash
mvn clean install
```

## Running the Application

```bash
mvn exec:java -Dexec.mainClass="com.example.app.Application"
```

## Running Tests

```bash
mvn test
```

## Using with OpenRewrite

### Maven Plugin Configuration

Add to your `pom.xml`:

```xml
<plugin>
    <groupId>org.openrewrite.maven</groupId>
    <artifactId>rewrite-maven-plugin</artifactId>
    <version>5.34.0</version>
    <configuration>
        <activeRecipes>
            <recipe>org.openrewrite.java.Java17ToJava21</recipe>
            <recipe>org.openrewrite.java.spring.boot3.Boot2to3Migration</recipe>
        </activeRecipes>
    </configuration>
</plugin>
```

### View Suggested Refactorings

```bash
mvn rewrite:dryRun
```

### Apply Refactorings

```bash
mvn rewrite:run
```

## Code Flow Analysis

The project demonstrates a simple but realistic code flow:

1. **Entry Point**: `Application.main()` - Initializes the application and demonstrates legacy patterns
2. **Service Layer**: `UserService` - Contains business logic with logging
3. **Entity**: `User` - Data model
4. **Utilities**: `DataUtil` - Provides utility functions for data manipulation
5. **Testing**: `UserServiceTest` - Validates service functionality

## Topics for Analysis

- **Import optimization**: Unused imports, redundant imports
- **Generics**: Raw type usage and unchecked casts
- **Code patterns**: Deprecated APIs and modern equivalents
- **Logging**: Consistent logging patterns, efficiency
- **Testing**: JUnit version compatibility
- **Spring compatibility**: Spring Framework version upgrades
- **Java language features**: Using modern Java 21 features

---

This project is ideal for testing and demonstrating OpenRewrite recipes without breaking existing functionality.
