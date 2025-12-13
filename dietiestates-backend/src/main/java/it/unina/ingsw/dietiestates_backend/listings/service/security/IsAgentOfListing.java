package it.unina.ingsw.dietiestates_backend.listings.service.security;

import org.springframework.security.access.prepost.PreAuthorize;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@PreAuthorize("@listingSecurity.hasBeenListedByAgent(#id)")
public @interface IsAgentOfListing {
}