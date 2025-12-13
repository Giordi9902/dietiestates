package it.unina.ingsw.dietiestates_backend.email;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

import java.util.HashMap;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class EmailService {

	private final ObjectProvider<JavaMailSender> mailSenderProvider;
	private final SpringTemplateEngine templateEngine;
	@Value("${application.mailing.frontend.login-url}")
	private String loginUrl;
	@Value("${application.mailing.frontend.activation-url}")
	private String activationUrl;

	@Async
	void sendUsingTemplate(String to, String subject, String templateName, Map<String,Object> variables) throws MessagingException {
		JavaMailSender mailSender = mailSenderProvider.getObject();
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(
				mimeMessage,
				MimeMessageHelper.MULTIPART_MODE_MIXED,
				UTF_8.name()
		);

		Context context = new Context();
		if(variables!=null){
			context.setVariables(variables);
		}
		helper.setFrom("contact@dietiestates.com");
		helper.setTo(to);
		helper.setSubject(subject);
		String template = templateEngine.process(templateName, context);
		helper.setText(template, true);
		mailSender.send(mimeMessage);
	}

	public void sendActivationEmail(User user, String activationCode) throws MessagingException{
		Map<String,Object> properties = new HashMap<>();
		properties.put("username", user.getUsername());
		properties.put("activationCode", activationCode);
		properties.put("confirmationUrl",activationUrl);
		sendUsingTemplate(user.getEmail(), "Activate your DietiEstates account", EmailTemplateName.ACTIVATE_ACCOUNT.getTemplateName(), properties);
	}

	public void sendAgentCreatedEmail(User user, String randomPassword, String otp) throws MessagingException {
		Map<String,Object> properties = new HashMap<>();
		properties.put("username", user.getUsername());
		properties.put("password", randomPassword);
		properties.put("activationCode",otp);
		properties.put("confirmationUrl",activationUrl);
		sendUsingTemplate(user.getEmail(), "Your Agent Account has been Created", EmailTemplateName.AGENT_CREATED.getTemplateName(), properties);
	}

	public void sendAgentPromotedEmail(User user) throws MessagingException {
		Map<String,Object> properties = new HashMap<>();
		properties.put("username", user.getUsername());
		properties.put("loginUrl",loginUrl);
		sendUsingTemplate(user.getEmail(), "You have been promoted to Admin",
				EmailTemplateName.AGENT_PROMOTED.getTemplateName(), properties);
	}

	public void sendPasswordChangedNotification(User user) throws MessagingException {
		Map<String,Object> properties = new HashMap<>();
		properties.put("username", user.getUsername());
		sendUsingTemplate(user.getEmail(), "Your Password Has Been Changed", EmailTemplateName.PASSWORD_CHANGED.getTemplateName(), properties);
	}

	public void sendAgentRoleAdded(User newAgent) throws MessagingException {
		Map<String,Object> properties = new HashMap<>();
		properties.put("username",newAgent.getUsername());
		properties.put("agency", newAgent.getAgency() != null ? newAgent.getAgency().getName() : "your agency");
		properties.put("loginUrl", loginUrl);
		sendUsingTemplate(newAgent.getEmail(), "You have been granted Agent permissions",
				EmailTemplateName.AGENT_ROLE_ADDED.getTemplateName(),properties);
	}
}