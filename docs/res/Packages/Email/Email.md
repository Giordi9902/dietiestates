classDiagram
direction BT
class EmailService {
  - ObjectProvider~JavaMailSender~ mailSenderProvider
  - SpringTemplateEngine templateEngine
  - String loginUrl
  - String activationUrl
  ~ sendUsingTemplate(String, String, String, Map~String, Object~) void
  + sendAgentCreatedEmail(User, String, String) void
  + sendAgentRoleAdded(User) void
  + sendPasswordChangedNotification(User) void
  + sendActivationEmail(User, String) void
  + sendAgentPromotedEmail(User) void
}
class EmailTemplateName {
<<enumeration>>
  +  AGENT_ROLE_ADDED
  - String templateName
  +  PASSWORD_CHANGED
  +  ACTIVATE_ACCOUNT
  +  AGENT_PROMOTED
  +  AGENT_CREATED
  + valueOf(String) EmailTemplateName
  + getTemplateName() String
  + values() EmailTemplateName[]
}

