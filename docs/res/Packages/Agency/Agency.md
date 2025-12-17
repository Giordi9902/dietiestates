classDiagram
direction BT
class AdminController {
  - AdminService adminService
  + promoteToAdmin(Integer) ResponseEntity~Void~
  + getAgentsByAgency(Pageable) ResponseEntity~Page~AgentResponse~~
  + deleteAgent(Integer) ResponseEntity~Void~
  + createAgent(CreateAgentRequest) ResponseEntity~Void~
}
class AdminService {
  - AgencyRepository agencyRepository
  - RoleRepository roleRepository
  - UserRepository userRepository
  - EmailService emailService
  - AgentCreationService agentCreationService
  + getAgentsByAgency(Pageable) Page~AgentResponse~
  + promote(Integer) void
  + deleteAgent(Integer) void
  - findAdminAgency() Agency
  + createAgent(CreateAgentRequest) void
  - getRoleByName(String) Role
}
class Agency {
  - String email
  - List~User~ agents
  - Integer id
  - String phoneNumber
  - String name
  + getId() Integer
  + getName() String
  + builder() AgencyBuilder
  + getPhoneNumber() String
  + getEmail() String
  + getAgents() List~User~
  + setId(Integer) void
  + setName(String) void
  + setPhoneNumber(String) void
  + setAgents(List~User~) void
  + setEmail(String) void
}
class AgencyController {
  - AgencyService agencyService
  + getMyAgencyDetails(Authentication) ResponseEntity~AgencyDetailsResponse~
}
class AgencyDetailsResponse {
  - String name
  - String phoneNumber
  - Integer id
  - String email
  + toString() String
  + equals(Object) boolean
  + builder() AgencyDetailsResponseBuilder
  # canEqual(Object) boolean
  + hashCode() int
  + getId() Integer
  + getName() String
  + setName(String) void
  + getPhoneNumber() String
  + getEmail() String
  + setEmail(String) void
  + setId(Integer) void
  + setPhoneNumber(String) void
}
class AgencyRepository {
<<Interface>>

}
class AgencyService {
  - AgencyRepository agencyRepository
  + getMyAgencyDetails(Authentication) AgencyDetailsResponse
}
class AgentCreationService {
  - OtpService otpService
  - EmailService emailService
  - RoleRepository roleRepository
  - UserRepository userRepository
  - PasswordEncoder passwordEncoder
  - assignRoles(User, List~Role~) void
  - getRoleByName(String) Role
  + createAgent(CreateAgentRequest, Agency) void
  - notifyAgentRoleAdded(User) void
  - createAgentEntity(CreateAgentRequest) User
  - initializeRolesIfNeeded(User) void
  - createAgentFromScratch(CreateAgentRequest, Agency) void
  - addAgentRoleToExistingUser(User, Agency) void
}
class AgentResponse {
  - List~Role~ roles
  - String email
  - String username
  - String lastName
  - Integer agencyId
  - String agencyName
  - Integer id
  - String firstName
  + setId(Integer) void
  + getId() Integer
  + setEmail(String) void
  + setRoles(List~Role~) void
  + setUsername(String) void
  + getEmail() String
  + setFirstName(String) void
  + getUsername() String
  + getFirstName() String
  + getLastName() String
  + getAgencyId() Integer
  + getAgencyName() String
  + setAgencyId(Integer) void
  + getRoles() List~Role~
  + builder() AgentResponseBuilder
  + setLastName(String) void
  + setAgencyName(String) void
}
class CreateAgentRequest {
  - String firstname
  - String lastname
  - String email
  + getFirstname() String
  + getLastname() String
  + getEmail() String
  + setFirstname(String) void
  + setLastname(String) void
  + setEmail(String) void
  + builder() CreateAgentRequestBuilder
}

AdminController "1" *--> "adminService 1" AdminService 
AdminService "1" *--> "agencyRepository 1" AgencyRepository 
AdminService "1" *--> "agentCreationService 1" AgentCreationService 
AgencyController "1" *--> "agencyService 1" AgencyService 
AgencyService "1" *--> "agencyRepository 1" AgencyRepository 
