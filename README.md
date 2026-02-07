# Social Media Networking Platform (OOP Java Project)

This project implements a **SocialNetwork** with key entities:
- `Profile`
- `Post`

## Package structure
- `domain` - core entities, abstractions, builder/factory patterns
- `dto` - request DTOs
- `repository` - interfaces (DIP)
- `repository.jdbc` - JDBC implementations using `PreparedStatement`
- `service` - business logic, in-memory data pool operations
- `controller` - REST API + exception handling
- `config` - DB config
- `util` - reusable generic utilities

## OOP and language feature checklist
1. **Entities and methods**: constructors, fields, getters/setters, `toString`, `equals`, `hashCode`.
2. **Abstraction/Encapsulation/Inheritance/Polymorphism**:
   - Abstraction: `EntityFactory<T>`, repositories/services interfaces.
   - Encapsulation: private fields + validation in services.
   - Inheritance: `Profile extends Account`.
   - Polymorphism: `List<Account>` generated from `Profile` instances in sorted endpoint.
3. **Data Pool**: `InMemoryDataPool<T>` with search/filter/sort using lambdas.
4. **Custom Exceptions**: `InvalidInputException`, `NotFoundException`.
5. **Database**: PostgreSQL tables `profiles`, `posts`; CRUD with JDBC + `PreparedStatement`.
6. **REST API**: JSON CRUD endpoints under `/api/profiles` and `/api/posts`.
7. **SOLID**:
   - DIP (mandatory): services depend on `ProfileRepository` / `PostRepository` interfaces, not JDBC classes.
   - SRP: controller/service/repository responsibilities are separated.
   - **Before/After short note**:
     - Before: service directly wrote SQL and handled HTTP-shaped concerns.
     - After: SQL moved to repositories; controllers map DTOs; services hold business rules. Easier to change DB implementation.
8. **Language features**:
   - Generics: `InMemoryDataPool<T>`, `EntityFactory<T>`, `Validator<T>`.
   - Lambda expressions: filtering and sorting in service/data pool.
   - Default + static interface methods: `EntityFactory#createAndValidate` and `EntityFactory#normalize`.
   - Reflection: `ReflectionInspector` + `/api/profiles/{id}/metadata` endpoint.
9. **Design Patterns**:
   - Builder: `Profile.Builder`.
   - Factory: `PostFactory`.

## Run
1. Create PostgreSQL DB `social_network`.
2. Run schema in `src/main/resources/schema.sql`.
3. Configure credentials in `application.properties`.
4. Start app (recommended with Maven Wrapper, no global Maven install needed):
   ```bash
   # Linux / macOS
   ./mvnw spring-boot:run

   # Windows PowerShell
   .\mvnw.cmd spring-boot:run
   ```

   Alternative (if Maven is installed globally):
   ```bash
   mvn spring-boot:run
   ```

## Example API calls
```bash
curl -X POST http://localhost:8080/api/profiles \
  -H "Content-Type: application/json" \
  -d '{"username":"alice","bio":"CS student","interests":"java,oops"}'

curl http://localhost:8080/api/profiles/search?username=ali

curl http://localhost:8080/api/profiles/1/metadata

curl -X POST http://localhost:8080/api/posts \
  -H "Content-Type: application/json" \
  -d '{"profileId":1,"content":"My first post"}'

curl http://localhost:8080/api/posts/sorted/newest
```


## Frontend
A simple web UI is available at `http://localhost:8080/` after running the app.
It supports:
- Creating profiles
- Creating posts
- Listing profiles and newest posts
- Searching profiles by username
