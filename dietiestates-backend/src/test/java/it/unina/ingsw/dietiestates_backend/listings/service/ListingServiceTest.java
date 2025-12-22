package it.unina.ingsw.dietiestates_backend.listings.service;

import it.unina.ingsw.dietiestates_backend.auth.model.User;
import it.unina.ingsw.dietiestates_backend.auth.repository.UserRepository;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.dto.creational.CreateListingRequest;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.EnergyClass;
import it.unina.ingsw.dietiestates_backend.listings.model.enums.ListingOperation;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingRepository;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingSearchRepositoryImpl;
import it.unina.ingsw.dietiestates_backend.listings.service.mapper.ListingMapper;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.locationtech.jts.geom.GeometryFactory;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingServiceTest {
	@Mock
	private ListingRepository listingRepository;
	@Mock private ListingImageService listingImageService;
	@Mock private UserRepository userRepository;
	@Mock private ListingMapper listingMapper;
	@Mock private GeometryFactory geometryFactory;
	@Mock private ListingSearchRepositoryImpl listingSearchRepository;

	@InjectMocks
	private ListingService listingService;
	private CreateListingRequest validRequest;
	private User mockAgent;
	private Listing mockListing;

	@BeforeEach
	void setup() {
		validRequest = CreateListingRequest.builder()
				.title("Appartamento").price(new BigDecimal("100000.00")).area(50.0).lat(40.8).lng(14.2)
				.listingOperation(ListingOperation.SALE).energyClass(EnergyClass.G).build();
		mockAgent = User.builder().id(1).email("agent@example.com").build();
		mockListing = Listing.builder().title(validRequest.getTitle()).build();
	}


	@AfterEach
	void tearDown() {
		SecurityContextHolder.clearContext();
	}

	private void setupSecurityContext(User agentUser, boolean userFoundInDb) {
		// 1. Mock Authentication e SecurityContext
		Authentication authentication = mock(Authentication.class);
		SecurityContext securityContext = mock(SecurityContext.class);

		// 2. Configura il SecurityContextHolder
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);

		// 3. Configura l'Authentication per restituire l'utente (UserDetails)
		when(authentication.getPrincipal()).thenReturn(agentUser);

		// 4. Configura il fetchAgent (interazione con UserRepository)
		if (userFoundInDb) {
			when(userRepository.findByEmail(agentUser.getEmail())).thenReturn(Optional.of(agentUser));
		} else {
			when(userRepository.findByEmail(agentUser.getEmail())).thenReturn(Optional.empty());
		}
	}

	@Nested
	@DisplayName("Create listing testing")
	class CreateListingTest {
		@Test
		@DisplayName("Successo Totale - Crea Listing con dati validi e Immagini")
		void createListing_validRequestAndImages_success() {
			// GIVEN
			List<MultipartFile> images = List.of(mock(MultipartFile.class));
			setupSecurityContext(mockAgent, true);
			// Simula il mapping DTO -> Entity
			when(listingMapper.toEntity(validRequest)).thenReturn(mockListing);
			// Simula il salvataggio iniziale e la generazione dell'ID
			when(listingRepository.save(mockListing)).thenReturn(Listing.builder().id(10).build());
			// WHEN
			listingService.createListing(validRequest, images);
			// THEN
			// 1. Verifica che l'agente sia stato associato
			assertEquals(mockAgent, mockListing.getListedBy());
			// 2. Verifica che il listing sia stato salvato
			verify(listingRepository, times(1)).save(mockListing);
			// 3. Verifica che l'upload delle immagini sia stato chiamato
			// Si deve verificare che il metodo privato 'uploadImages' sia stato eseguito,
			// il che implica una o più chiamate a listingImageService.saveImage.
			verify(listingImageService, times(images.size())).saveImage(any(MultipartFile.class), eq(mockListing));
		}

		@Test
		@DisplayName("Lancia Eccezione se l'utente autenticato non viene trovato nel DB")
		void createListing_authenticatedUserNotFound_throwsException() {
			// GIVEN
			List<MultipartFile> images = List.of(mock(MultipartFile.class));
			setupSecurityContext(mockAgent, false); // L'utente non si trova nel DB
			// WHEN & THEN
			assertThrows(IllegalStateException.class,
					() -> listingService.createListing(validRequest, images));
			// VERIFY
			verify(listingRepository, never()).save(any(Listing.class));
		}

		@Test
		@DisplayName("Lancia Eccezione se la lista di immagini è nulla")
		void createListing_nullImages_throwsException() {
			// GIVEN
			setupSecurityContext(mockAgent, true);
			when(listingMapper.toEntity(validRequest)).thenReturn(mockListing);
			when(listingRepository.save(mockListing)).thenReturn(Listing.builder().id(10).build());
			// WHEN & THEN
			assertThrows(IllegalArgumentException.class,
					() -> listingService.createListing(validRequest, null));
		}

		@Test
		@DisplayName("Lancia Eccezione se la lista di immagini è vuota")
		void createListing_emptyImages_throwsException() {
			// GIVEN
			setupSecurityContext(mockAgent, true);
			when(listingMapper.toEntity(validRequest)).thenReturn(mockListing);
			when(listingRepository.save(mockListing)).thenReturn(Listing.builder().id(10).build());
			// WHEN & THEN
			assertThrows(IllegalArgumentException.class,
					() -> listingService.createListing(validRequest, List.of()));
		}

		@Test
		@DisplayName("Rollback della transazione se il salvataggio dell'immagine fallisce")
		void createListing_imageUploadFails_shouldRollback() {
			// GIVEN
			List<MultipartFile> images = List.of(mock(MultipartFile.class));
			setupSecurityContext(mockAgent, true);
			when(listingMapper.toEntity(validRequest)).thenReturn(mockListing);
			when(listingRepository.save(mockListing)).thenReturn(Listing.builder().id(10).build());
			doThrow(new RuntimeException("Image save failed")).when(listingImageService).saveImage(any(MultipartFile.class), any(Listing.class));

			// WHEN & THEN
			assertThrows(RuntimeException.class,
					() -> listingService.createListing(validRequest, images));

			// VERIFY
			verify(listingRepository, times(1)).save(mockListing);
			verify(listingImageService, times(1)).saveImage(any(MultipartFile.class), any(Listing.class));
		}

		@Test
		@DisplayName("Lancia Eccezione se il DTO di richiesta è nullo")
		void createListing_nullRequest_throwsException() {
			// GIVEN
			List<MultipartFile> images = List.of(mock(MultipartFile.class));

			// WHEN & THEN
			assertThrows(NullPointerException.class,
					() -> listingService.createListing(null, images));

			// VERIFY
			verifyNoInteractions(userRepository, listingRepository, listingMapper, listingImageService);
		}

		@Test
		@DisplayName("Lancia Eccezione se non c'è utente autenticato")
		void createListing_noAuthenticatedUser_throwsException() {
			// GIVEN
			List<MultipartFile> images = List.of(mock(MultipartFile.class));
			// SecurityContext is cleared by tearDown(), so no user is authenticated

			// WHEN & THEN
			assertThrows(IllegalStateException.class,
					() -> listingService.createListing(validRequest, images));

			// VERIFY
			verify(listingRepository, never()).save(any(Listing.class));
			verify(listingImageService, never()).saveImage(any(), any());
		}
	}
}