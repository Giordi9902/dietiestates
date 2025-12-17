classDiagram
direction BT
class AuthProvider {
<<enumeration>>
  +  local
  +  github
  +  google
  + values() AuthProvider[]
  + valueOf(String) AuthProvider
}
class AuthenticationController {
  - AuthenticationService authenticationService
  + register(RegistrationRequest) ResponseEntity~Void~
  + refresh(RefreshTokenRequest) ResponseEntity~AuthenticationResponse~
  + authenticate(AuthenticationRequest) ResponseEntity~AuthenticationResponse~
  + confirm(String) void
}
class AuthenticationRequest {
  - String email
  - String password
  + getEmail() String
  + getPassword() String
  + setEmail(String) void
  + setPassword(String) void
  + builder() AuthenticationRequestBuilder
}
class AuthenticationResponse {
  - String refresh_token
  - String jwt_token
  + getJwt_token() String
  + getRefresh_token() String
  + setJwt_token(String) void
  + setRefresh_token(String) void
  + builder() AuthenticationResponseBuilder
}
class AuthenticationService {
  - RefreshTokenService refreshTokenService
  - long refresh_expiration
  - AuthenticationManager authenticationManager
  - PasswordEncoder passwordEncoder
  - OtpService otpService
  - UserRepository userRepository
  - JwtService jwtService
  - RoleRepository roleRepository
  + authenticate(AuthenticationRequest) AuthenticationResponse
  + refreshToken(RefreshTokenRequest) AuthenticationResponse
  + activateAccount(String) void
  - createNewTokens(User, String) AuthenticationResponse
  - createAndSaveRefreshToken(User) String
  + register(RegistrationRequest) void
  - validateRefreshToken(String) RefreshToken
}
class ChangePasswordRequest {
  - String newPassword
  - String oldPassword
  + getOldPassword() String
  + getNewPassword() String
  + setOldPassword(String) void
  + setNewPassword(String) void
  + equals(Object) boolean
  # canEqual(Object) boolean
  + hashCode() int
  + toString() String
}
class OtpService {
  - OtpTokenRepository otpTokenRepository
  - EmailService emailService
  + generateAndSaveActivationToken(User) String
  + validateActivationToken(String) OtpToken
  + sendValidationEmail(User) void
  + saveValidatedToken(OtpToken) void
  - generateActivationToken(int) String
}
class OtpToken {
  - User user
  - String token
  - LocalDateTime expiresAt
  - LocalDateTime validatedAt
  - Integer id
  - LocalDateTime createdAt
  + getValidatedAt() LocalDateTime
  + getId() Integer
  + getToken() String
  + getCreatedAt() LocalDateTime
  + getExpiresAt() LocalDateTime
  + getUser() User
  + setId(Integer) void
  + setUser(User) void
  + setToken(String) void
  + builder() OtpTokenBuilder
  + setValidatedAt(LocalDateTime) void
  + setCreatedAt(LocalDateTime) void
  + setExpiresAt(LocalDateTime) void
}
class OtpTokenRepository {
<<Interface>>
  + findOtpTokenByToken(String) Optional~OtpToken~
}
class RefreshToken {
  - Instant expiryDate
  - String token
  - Instant createdAt
  - Long id
  - String username
  - boolean revoked
  + getCreatedAt() Instant
  + getId() Long
  + getToken() String
  + setRevoked(boolean) void
  + getUsername() String
  + getExpiryDate() Instant
  + isRevoked() boolean
  + setExpiryDate(Instant) void
  + setId(Long) void
  + builder() RefreshTokenBuilder
  + setCreatedAt(Instant) void
  + setToken(String) void
  + setUsername(String) void
}
class RefreshTokenRepository {
<<Interface>>
  + deleteAllByUsername(String) void
  + findByToken(String) Optional~RefreshToken~
}
class RefreshTokenRequest {
  - String refresh_token
  + getRefresh_token() String
  + setRefresh_token(String) void
}
class RefreshTokenService {
  - RefreshTokenRepository repository
  + createAndSave(String, String, Instant) RefreshToken
  + findByToken(String) Optional~RefreshToken~
  + revokeToken(String) void
}
class RegistrationRequest {
  - String lastname
  - String password
  - String email
  - String firstname
  + getFirstname() String
  + getLastname() String
  + getEmail() String
  + getPassword() String
  + setFirstname(String) void
  + setLastname(String) void
  + setEmail(String) void
  + setPassword(String) void
  + builder() RegistrationRequestBuilder
}
class Role {
  - LocalDateTime lastModifiedDate
  - Integer id
  - String name
  - List~User~ users
  - LocalDateTime createdDate
  + getId() Integer
  + getName() String
  + getUsers() List~User~
  + getCreatedDate() LocalDateTime
  + getLastModifiedDate() LocalDateTime
  + setId(Integer) void
  + builder() RoleBuilder
  + setName(String) void
  + setUsers(List~User~) void
  + setLastModifiedDate(LocalDateTime) void
  + setCreatedDate(LocalDateTime) void
}
class RoleRepository {
<<Interface>>
  + findByName(String) Optional~Role~
}
class UpdateProfileRequest {
  - String firstname
  - String email
  - LocalDate birthdate
  - String lastname
  + equals(Object) boolean
  # canEqual(Object) boolean
  + setLastname(String) void
  + hashCode() int
  + setEmail(String) void
  + toString() String
  + setBirthdate(LocalDate) void
  + getFirstname() String
  + getLastname() String
  + getEmail() String
  + getBirthdate() LocalDate
  + setFirstname(String) void
}
class User {
  - boolean enabled
  - boolean accountLocked
  - String email
  - LocalDateTime createdDate
  - LocalDateTime lastModifiedDate
  - String lastname
  - String password
  - String firstname
  - Agency agency
  - Integer id
  - AuthProvider provider
  - LocalDate dateOfBirth
  - List~Role~ roles
  + getFullName() String
  + setPassword(String) void
  + getId() Integer
  + isAccountNonExpired() boolean
  + isAccountLocked() boolean
  + setDateOfBirth(LocalDate) void
  + getFirstname() String
  + getLastname() String
  + getLastModifiedDate() LocalDateTime
  + setEmail(String) void
  + setAccountLocked(boolean) void
  + setFirstname(String) void
  + setLastname(String) void
  + getEmail() String
  + setAgency(Agency) void
  + getDateOfBirth() LocalDate
  + getPassword() String?
  + getAgency() Agency
  + getProvider() AuthProvider
  + getCreatedDate() LocalDateTime
  + setId(Integer) void
  + setCreatedDate(LocalDateTime) void
  + setLastModifiedDate(LocalDateTime) void
  + setRoles(List~Role~) void
  + builder() UserBuilder
  + setProvider(AuthProvider) void
  + setEnabled(boolean) void
  + getAuthorities() Collection~GrantedAuthority~
  + isEnabled() boolean
  + isCredentialsNonExpired() boolean
  + getUsername() String
  + isAccountNonLocked() boolean
  + getRoles() List~Role~
  + getName() String
}
class UserController {
  - UserService userService
  + updateProfile(User, UpdateProfileRequest) ResponseEntity~Void~
  + changeMyPassword(User, ChangePasswordRequest) ResponseEntity~String~
  + profileDetails(User) ResponseEntity~UserDetailsResponse~
}
class UserDetailsResponse {
  - LocalDate dateOfBirth
  - String firstname
  - String lastname
  - String email
  + equals(Object) boolean
  # canEqual(Object) boolean
  + setEmail(String) void
  + setDateOfBirth(LocalDate) void
  + hashCode() int
  + toString() String
  + getFirstname() String
  + getLastname() String
  + getEmail() String
  + getDateOfBirth() LocalDate
  + setFirstname(String) void
  + setLastname(String) void
}
class UserRepository {
<<Interface>>
  + findAllByAgencyId(Integer, Pageable) Page~User~
  + revokeAgent(Integer) void
  + findByEmail(String) Optional~User~
}
class UserService {
  - UserRepository userRepository
  - PasswordEncoder passwordEncoder
  - EmailService emailService
  + updatePassword(User, ChangePasswordRequest) void
  + updateProfile(User, UpdateProfileRequest) void
  + getUserDetails(User) UserDetailsResponse
}

AuthenticationController "1" *--> "authenticationService 1" AuthenticationService 
AuthenticationService "1" *--> "otpService 1" OtpService 
AuthenticationService "1" *--> "refreshTokenService 1" RefreshTokenService 
AuthenticationService "1" *--> "roleRepository 1" RoleRepository 
AuthenticationService "1" *--> "userRepository 1" UserRepository 
OtpService "1" *--> "otpTokenRepository 1" OtpTokenRepository 
OtpToken "1" *--> "user 1" User 
RefreshTokenService "1" *--> "repository 1" RefreshTokenRepository 
Role "1" *--> "users *" User 
User "1" *--> "provider 1" AuthProvider 
User "1" *--> "roles *" Role 
UserController "1" *--> "userService 1" UserService 
UserService  ..>  UserDetailsResponse : «create»
UserService "1" *--> "userRepository 1" UserRepository 
