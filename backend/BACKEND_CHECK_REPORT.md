# Backend Project Check Report
**Date:** Generated on review  
**Project:** Quiz Web App Backend

## ✅ **Overall Status**
The backend project has a solid structure with proper separation of concerns (controllers, services, repositories, mappers, DTOs). However, several issues and improvements are identified.

---

## 🔴 **Critical Issues**

### 1. **POM.xml Configuration Issues**
- **Java Version 25**: Line 30 specifies `java.version>25</java.version>` - Java 25 doesn't exist yet. Should be Java 17 or 21.
- **Spring Boot 4.0.0**: Line 8 uses Spring Boot 4.0.0 which may not be released. Current stable is 3.x.
- **Test Dependencies**: Lines 58-70 have suspicious test dependencies:
  - `spring-boot-starter-data-jpa-test` (should be `spring-boot-starter-test`)
  - `spring-boot-starter-validation-test` (should be `spring-boot-starter-test`)
  - `spring-boot-starter-webmvc-test` (should be `spring-boot-starter-test`)

### 2. **LazyInitializationException Risk**
**Location:** `AttemptServiceImpl.submitAttempt()` (Line 55)
```java
int totalQuestions = attempt.getQuiz().getQuestions().size();
```
**Issue:** Accessing `getQuestions()` on a lazy-loaded entity outside a transaction context can cause `LazyInitializationException`.

**Fix:** Fetch the quiz with questions eagerly or use `@EntityGraph`:
```java
@Query("SELECT q FROM Quiz q LEFT JOIN FETCH q.questions WHERE q.id = :id")
Optional<Quiz> findByIdWithQuestions(@Param("id") Long id);
```

### 3. **Potential Null Pointer Exceptions**
**Location:** `AttemptMapper.toDTO()` (Lines 14-16)
```java
.userId(attempt.getUser().getId())
.quizId(attempt.getQuiz().getId())
```
**Issue:** If lazy loading fails or entities are null, this will throw NPE.

**Fix:** Add null checks or ensure entities are properly loaded.

---

## ⚠️ **Important Issues**

### 4. **Missing Exception Handling**
- No custom exception classes in `exceptions/` package
- All errors throw generic `RuntimeException` with string messages
- No global exception handler (`@ControllerAdvice`)
- No proper HTTP status code mapping

**Recommendation:** Create:
- `ResourceNotFoundException` (404)
- `BadRequestException` (400)
- `GlobalExceptionHandler` with `@ControllerAdvice`

### 5. **Missing Input Validation**
**Location:** All DTOs and Controllers
- No `@Valid` annotations on `@RequestBody` parameters
- No validation annotations (`@NotNull`, `@NotEmpty`, `@Size`, etc.) on DTOs
- No validation for:
  - Empty quiz titles
  - Negative time limits
  - Empty question lists
  - Invalid question types

**Example Fix:**
```java
@PostMapping
public ResponseEntity<QuizDto> createQuiz(
        @Valid @RequestBody CreateQuizRequest request,
        @RequestHeader("User-Id") Long userId) {
    // ...
}
```

### 6. **Security Concerns**
**Location:** All Controllers
- `User-Id` header is used for authentication but:
  - No validation that user exists
  - No authentication/authorization mechanism
  - No token validation
  - Anyone can send any `User-Id` header

**Recommendation:** Implement proper authentication (JWT, Spring Security).

### 7. **Missing Transaction Management**
**Location:** `QuizServiceImpl.getAllQuizzes()` and `getQuizById()`
- Methods that read data should also be `@Transactional(readOnly = true)` to:
  - Optimize performance
  - Ensure proper session management
  - Prevent lazy loading issues

---

## 💡 **Code Quality Issues**

### 8. **Inconsistent Error Messages**
- Some methods throw "User not found", others "Quiz not found"
- Consider using consistent exception types with proper error codes

### 9. **Missing Validation in Business Logic**
**Location:** `AttemptServiceImpl.submitAttempt()`
- No validation that:
  - Selected option belongs to the question
  - All questions are answered
  - Attempt belongs to the quiz
  - Question belongs to the quiz

### 10. **Missing Repository Methods**
- `AttemptRepository.findByUserId()` - exists ✅
- `AttemptRepository.findByQuizId()` - exists ✅
- But no method to find attempts by user AND quiz
- No method to check if user has already attempted a quiz

### 11. **Potential Data Integrity Issues**
**Location:** `AttemptServiceImpl.submitAttempt()`
- No validation that `selectedOption` belongs to the `question`
- User could submit answers for questions not in the quiz
- No check if attempt's quiz matches the questions being answered

---

## 📋 **Recommendations**

### High Priority
1. ✅ Fix POM.xml (Java version, Spring Boot version, test dependencies)
2. ✅ Add proper exception handling with custom exceptions
3. ✅ Add input validation with `@Valid` and validation annotations
4. ✅ Fix lazy loading issues in `submitAttempt()`
5. ✅ Add transaction management to read methods

### Medium Priority
6. ✅ Implement proper authentication/authorization
7. ✅ Add validation in business logic (option belongs to question, etc.)
8. ✅ Add null checks in mappers
9. ✅ Add logging for debugging

### Low Priority
10. ✅ Add API documentation (Swagger/OpenAPI)
11. ✅ Add unit tests
12. ✅ Add integration tests
13. ✅ Consider using DTOs for responses instead of exposing entities

---

## 📊 **Code Structure Analysis**

### ✅ **Good Practices Found**
- Proper use of DTOs for request/response
- Separation of concerns (Controller → Service → Repository)
- Use of Lombok for boilerplate reduction
- Proper JPA entity relationships
- Use of `@Builder` pattern
- Proper use of `@Transactional` in write operations

### 📁 **Package Structure**
```
✅ Controllers - REST endpoints
✅ Services - Business logic
✅ Repositories - Data access
✅ DTOs - Data transfer objects
✅ Entities - JPA entities
✅ Mappers - Entity-DTO conversion
⚠️ Exceptions - Empty (needs implementation)
```

---

## 🔍 **Specific Code Issues**

### Issue in `AttemptServiceImpl.submitAttempt()`
```java
// Line 55 - Potential LazyInitializationException
int totalQuestions = attempt.getQuiz().getQuestions().size();

// Line 57-74 - No validation that:
// - selectedOption belongs to question
// - question belongs to quiz
// - all questions are answered
```

### Issue in `QuizServiceImpl.createQuiz()`
```java
// No validation that:
// - title is not empty
// - timeLimit is positive
// - questions list is not empty
// - each question has at least one option
// - at least one option is marked as correct per question
```

---

## ✅ **Summary**

**Total Issues Found:** 11  
**Critical:** 3  
**Important:** 4  
**Code Quality:** 4  

**Overall Assessment:** The codebase has a good foundation but needs improvements in:
- Configuration (POM.xml)
- Exception handling
- Input validation
- Security
- Lazy loading management

**Next Steps:**
1. Fix POM.xml immediately
2. Add exception handling package
3. Add validation annotations
4. Fix lazy loading issues
5. Implement proper authentication

