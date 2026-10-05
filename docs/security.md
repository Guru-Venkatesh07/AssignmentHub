# Security Architecture & Defense-in-Depth Specification

## 1. Password Hashing with BCrypt

- **Algorithm**: BCrypt (Key derivation function based on the Blowfish cipher).
- **Work Factor**: 10 rounds ($2^{10} = 1024$ iterations).
- **Salt Generation**: Unique cryptographic 128-bit salt generated per password via `BCrypt.gensalt(10)`.
- **Zero Plaintext**: Passwords are never logged, printed, or persisted in cleartext.

```java
// Password Hashing Implementation
public static String hashPassword(String plainPassword) {
    return BCrypt.hashpw(plainPassword, BCrypt.gensalt(10));
}

// Constant-Time Password Verification
public static boolean verifyPassword(String plainPassword, String hashedPassword) {
    return BCrypt.checkpw(plainPassword, hashedPassword);
}
```

---

## 2. Session Management & Fixation Protection

1. **Session Lifecycle**:
   - `session.invalidate()` is invoked on logout.
   - On successful login, the existing session is explicitly invalidated before creating a new session ID to prevent **Session Fixation attacks**.
   - Session attributes are restricted to safe primitives: `userId`, `userName`, `userEmail`, `userRole`, `registerNumber`, `department`. **No passwords or hashes are ever placed in the session.**
2. **Session Timeout**: Enforced to 30 minutes in `web.xml`.
3. **No Cache on Protected Views**: Response headers `Cache-Control: no-cache, no-store, must-revalidate` and `Pragma: no-cache` prevent browser back-button caching of sensitive user views after logout.

---

## 3. Cookie Security

- The "Remember Username" feature persists only the user's non-sensitive email/username.
- Cookie flags:
  - `HttpOnly = true`: Prevents client-side script access via `document.cookie` (mitigates cookie theft via XSS).
  - Explicit path scoping: `cookie.setPath(contextPath)`.
  - Passwords and session IDs are strictly forbidden from cookie storage.

---

## 4. SQL Injection Prevention

- **PreparedStatement**: 100% of SQL operations throughout all DAOs use parameterized `java.sql.PreparedStatement` with typed binding (`setInt`, `setString`, `setDate`).
- **No Dynamic String Concatenation**: User inputs are never directly interpolated into raw SQL statements.

---

## 5. Cross-Site Scripting (XSS) Prevention

- Input sanitization on user inputs (`ValidationUtil.sanitizeHtml`).
- Output escaping in JSP using JSTL `<c:out>` and core EL tags.
- XML special characters (`&`, `<`, `>`, `"`, `'`) are systematically escaped before XML rendering.

---

## 6. Secure Multipart File Upload Handling

1. **Whitelist Extension Validation**:
   - Only strictly permitted file extensions: `.pdf`, `.docx`, `.doc`, `.pptx`, `.ppt`, `.zip`.
   - Executable scripts (`.exe`, `.sh`, `.jsp`, `.php`, `.bat`) are immediately rejected.
2. **File Size Enforcement**:
   - Hard 10MB limit enforced on both client-side and server-side via `@MultipartConfig(maxFileSize = 10485760L)`.
3. **Safe Server-Generated Filenames**:
   - Original client filenames are sanitized to prevent **Path Traversal / Directory Traversal attacks** (`../`).
   - Saved files use generated unique names: `submission_{assignmentId}_{studentId}_{timestamp}.{ext}`.

---

## 7. Role-Based Authorization & Protected Endpoints

- Handled centrally by `AuthenticationFilter`:
  - `/student/*` $\rightarrow$ Accessible exclusively by users with role `STUDENT`.
  - `/faculty/*` $\rightarrow$ Accessible exclusively by users with role `FACULTY`.
  - Unauthorized role attempts return a clean `403 Forbidden` error without revealing internal state.
