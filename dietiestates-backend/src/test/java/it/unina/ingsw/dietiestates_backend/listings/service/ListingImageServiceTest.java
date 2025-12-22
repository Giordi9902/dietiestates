package it.unina.ingsw.dietiestates_backend.listings.service;

import it.unina.ingsw.dietiestates_backend.file.S3ImageStorageService;
import it.unina.ingsw.dietiestates_backend.listings.model.Listing;
import it.unina.ingsw.dietiestates_backend.listings.model.ListingImage;
import it.unina.ingsw.dietiestates_backend.listings.repository.ListingImageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListingImageServiceTest {

    @Mock
    private S3ImageStorageService s3ImageStorageService;

    @Mock
    private ListingImageRepository listingImageRepository;

    @InjectMocks
    private ListingImageService listingImageService;

    private MultipartFile mockFile;
    private Listing mockListing;

    @BeforeEach
    void setUp() {
        mockFile = mock(MultipartFile.class);
        mockListing = mock(Listing.class);
    }
	@Nested
	@DisplayName("Save image testing")
	class SaveImage {
		@Test
		@DisplayName("Salva immagine per un listing senza immagini esistenti")
		void saveImage_firstImageForListing_success() {
			// GIVEN
			when(mockListing.getId()).thenReturn(1);
			String imageUrl = "http://example.com/image.jpg";
			when(mockListing.getImages()).thenReturn(new ArrayList<>());
			when(s3ImageStorageService.storeImage(mockFile, mockListing.getId())).thenReturn(imageUrl);
			when(listingImageRepository.findMaxSortOrderByListingId(mockListing.getId())).thenReturn(null);
			when(listingImageRepository.save(any(ListingImage.class))).thenAnswer(invocation -> invocation.getArgument(0));

			// WHEN
			ListingImage savedImage = listingImageService.saveImage(mockFile, mockListing);

			// THEN
			ArgumentCaptor<ListingImage> imageCaptor = ArgumentCaptor.forClass(ListingImage.class);
			verify(listingImageRepository).save(imageCaptor.capture());
			ListingImage capturedImage = imageCaptor.getValue();

			assertEquals(imageUrl, savedImage.getUrl());
			assertEquals(1, capturedImage.getSortOrder());
			assertEquals(mockListing, capturedImage.getListing());

			verify(s3ImageStorageService, times(1)).storeImage(mockFile, mockListing.getId());
			verify(listingImageRepository, times(1)).findMaxSortOrderByListingId(mockListing.getId());
		}

		@Test
		@DisplayName("Salva immagine per un listing con immagini esistenti")
		void saveImage_withExistingImages_success() {
			// GIVEN
			when(mockListing.getId()).thenReturn(1);
			String imageUrl = "http://example.com/new_image.jpg";
			int maxSortOrder = 3;
			when(mockListing.getImages()).thenReturn(new ArrayList<>());
			when(s3ImageStorageService.storeImage(mockFile, mockListing.getId())).thenReturn(imageUrl);
			when(listingImageRepository.findMaxSortOrderByListingId(mockListing.getId())).thenReturn(maxSortOrder);
			when(listingImageRepository.save(any(ListingImage.class))).thenAnswer(invocation -> invocation.getArgument(0));
			// WHEN
			ListingImage savedImage = listingImageService.saveImage(mockFile, mockListing);
			// THEN
			ArgumentCaptor<ListingImage> imageCaptor = ArgumentCaptor.forClass(ListingImage.class);
			verify(listingImageRepository).save(imageCaptor.capture());
			ListingImage capturedImage = imageCaptor.getValue();
			assertEquals(imageUrl, savedImage.getUrl());
			assertEquals(maxSortOrder + 1, capturedImage.getSortOrder());
			assertEquals(mockListing, capturedImage.getListing());
			verify(s3ImageStorageService, times(1)).storeImage(mockFile, mockListing.getId());
			verify(listingImageRepository, times(1)).findMaxSortOrderByListingId(mockListing.getId());
		}

		@Test
		@DisplayName("Lancia eccezione se il caricamento su S3 fallisce")
		void saveImage_s3UploadFails_throwsException() {
			// GIVEN
			when(mockListing.getId()).thenReturn(1);
			when(s3ImageStorageService.storeImage(mockFile, mockListing.getId())).thenThrow(new RuntimeException("S3 upload failed"));
			// WHEN & THEN
			assertThrows(RuntimeException.class, () -> listingImageService.saveImage(mockFile, mockListing));
			// THEN
			verify(listingImageRepository, never()).findMaxSortOrderByListingId(anyInt());
			verify(listingImageRepository, never()).save(any(ListingImage.class));
		}

		@Test
		@DisplayName("Lancia eccezione se il listing è nullo")
		void saveImage_nullListing_throwsException() {
			// WHEN & THEN
			assertThrows(NullPointerException.class, () -> listingImageService.saveImage(mockFile, null));

			// THEN
			verifyNoInteractions(s3ImageStorageService);
			verifyNoInteractions(listingImageRepository);
		}

		@Test
		@DisplayName("Lancia eccezione se il file è nullo")
		void saveImage_nullFile_throwsException() {
			// GIVEN
			when(mockListing.getId()).thenReturn(1);
			when(s3ImageStorageService.storeImage(null, mockListing.getId())).thenThrow(new IllegalArgumentException("File must not be null"));

			// WHEN & THEN
			assertThrows(IllegalArgumentException.class, () -> listingImageService.saveImage(null, mockListing));

			// THEN
			verifyNoInteractions(listingImageRepository);
			verify(s3ImageStorageService, times(1)).storeImage(null, mockListing.getId());
		}
	}
}