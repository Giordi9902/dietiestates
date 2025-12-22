classDiagram
direction TB
class AdminController {
  - AdminService adminService
  + getAgentsByAgency(Pageable) ResponseEntity~Page~AgentResponse~~
  + createAgent(CreateAgentRequest) ResponseEntity~Void~
  + deleteAgent(Integer) ResponseEntity~Void~
  + promoteToAdmin(Integer) ResponseEntity~Void~
}
class AdminService {
  - RoleRepository roleRepository
  - AgencyRepository agencyRepository
  - UserRepository userRepository
  - AgentCreationService agentCreationService
  - EmailService emailService
  + getAgentsByAgency(Pageable) Page~AgentResponse~
  + deleteAgent(Integer) void
  - findAdminAgency() Agency
  + promote(Integer) void
  + createAgent(CreateAgentRequest) void
  - getRoleByName(String) Role
}
class Agency {
  - String name
  - String email
  - String phoneNumber
  - List~User~ agents
  - Integer id
  + getEmail() String
  + getId() Integer
  + getName() String
  + builder() AgencyBuilder
  + getPhoneNumber() String
  + getAgents() List~User~
  + setAgents(List~User~) void
  + setId(Integer) void
  + setName(String) void
  + setPhoneNumber(String) void
  + setEmail(String) void
}
class AgencyController {
  - AgencyService agencyService
  + getMyAgencyDetails(Authentication) ResponseEntity~AgencyDetailsResponse~
}
class AgencyRepository {
<<Interface>>

}
class AgencyService {
  - AgencyRepository agencyRepository
  + getMyAgencyDetails(Authentication) AgencyDetailsResponse
}
class AgentCreationService {
  - UserRepository userRepository
  - EmailService emailService
  - PasswordEncoder passwordEncoder
  - RoleRepository roleRepository
  - OtpService otpService
  - createAgentFromScratch(CreateAgentRequest, Agency) void
  - addAgentRoleToExistingUser(User, Agency) void
  - getRoleByName(String) Role
  - createAgentEntity(CreateAgentRequest) User
  - assignRoles(User, List~Role~) void
  + createAgent(CreateAgentRequest, Agency) void
  - notifyAgentRoleAdded(User) void
  - initializeRolesIfNeeded(User) void
}

AdminController "1" *--> "adminService 1" AdminService 
AdminService "1" *--> "agencyRepository 1" AgencyRepository 
AdminService "1" *--> "agentCreationService 1" AgentCreationService 
AgencyController "1" *--> "agencyService 1" AgencyService 
AgencyService "1" *--> "agencyRepository 1" AgencyRepository 
