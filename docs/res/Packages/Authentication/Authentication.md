classDiagram
direction BT
class AuthProvider {
<<enumeration>>
  +  local
  +  google
  +  github
}
class OtpToken {
  - LocalDateTime expiresAt
  - LocalDateTime validatedAt
  - User user
  - LocalDateTime createdAt
  - Integer id
  - String token
}
class Role {
  - String name
  - LocalDateTime lastModifiedDate
  - List~User~ users
  - LocalDateTime createdDate
  - Integer id
}
class User {
  - boolean enabled
  - List~Role~ roles
  - String password
  - Agency agency
  - String email
  - LocalDateTime lastModifiedDate
  - String lastname
  - Integer id
  - String firstname
  - LocalDateTime createdDate
  - AuthProvider provider
  - LocalDate dateOfBirth
  - boolean accountLocked
}

OtpToken "1" *--> "user 1" User 
User "1" *--> "provider 1" AuthProvider 
User "1" *--> "roles *" Role 
