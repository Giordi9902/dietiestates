classDiagram
direction BT
class CreateListingRequest {
  - Double lng
  - String title
  - Integer bathrooms
  - String description
  - EnergyClass energyClass
  - Double area
  - Boolean hasGarden
  - BigDecimal price
  - ListingOperation listingOperation
  - Integer rooms
  - Double lat
  - Boolean hasBalcony
  - String address
  - Boolean hasParking
  - Boolean hasElevator
  - Integer floor
  + getTitle() String
  + setAddress(String) void
  + setFloor(Integer) void
  + setLat(Double) void
  + setEnergyClass(EnergyClass) void
  + setRooms(Integer) void
  + getDescription() String
  + getHasElevator() Boolean
  + setListingOperation(ListingOperation) void
  + getBathrooms() Integer
  + getLng() Double
  + getRooms() Integer
  + setBathrooms(Integer) void
  + setPrice(BigDecimal) void
  + getHasGarden() Boolean
  + setHasParking(Boolean) void
  + setTitle(String) void
  + getLat() Double
  + getHasParking() Boolean
  + setDescription(String) void
  + setLng(Double) void
  + getAddress() String
  + getEnergyClass() EnergyClass
  + getListingOperation() ListingOperation
  + getFloor() Integer
  + getHasBalcony() Boolean
  + setHasBalcony(Boolean) void
  + getPrice() BigDecimal
  + getArea() Double
  + builder() CreateListingRequestBuilder
  + setArea(Double) void
  + setHasGarden(Boolean) void
  + setHasElevator(Boolean) void
}
class GeometryService {
  - GeometryFactory geometryFactory
  + createPoint(double, double) Point
}
class Listing {
  - Boolean hasElevator
  - Integer floor
  - Point coordinate
  - String title
  - BigDecimal price
  - String description
  - List~ListingImage~ images
  - LocalDateTime createdDate
  - Boolean hasGarden
  - Integer id
  - LocalDateTime lastModifiedDate
  - User listedBy
  - String address
  - Boolean hasBalcony
  - EnergyClass energyClass
  - Integer bathrooms
  - Integer rooms
  - ListingOperation listingOperation
  - Boolean hasParking
  - Double area
  + getDescription() String
  + getId() Integer
  + getListedBy() User
  + getCreatedDate() LocalDateTime
  + getEnergyClass() EnergyClass
  + setTitle(String) void
  + getRooms() Integer
  + setImages(List~ListingImage~) void
  + getHasBalcony() Boolean
  + getListingOperation() ListingOperation
  + getHasGarden() Boolean
  + builder() ListingBuilder
  + setId(Integer) void
  + setPrice(BigDecimal) void
  + getHasParking() Boolean
  + setHasParking(Boolean) void
  + setListedBy(User) void
  + getPrice() BigDecimal
  + getHasElevator() Boolean
  + setBathrooms(Integer) void
  + setFloor(Integer) void
  + setAddress(String) void
  + getTitle() String
  + setRooms(Integer) void
  + setCoordinate(Point) void
  + getLastModifiedDate() LocalDateTime
  + setArea(Double) void
  + setLastModifiedDate(LocalDateTime) void
  + getFloor() Integer
  + setDescription(String) void
  + setEnergyClass(EnergyClass) void
  + setCreatedDate(LocalDateTime) void
  + setHasGarden(Boolean) void
  + setHasBalcony(Boolean) void
  + setListingOperation(ListingOperation) void
  + getArea() Double
  + getCoordinate() Point
  + setHasElevator(Boolean) void
  + getBathrooms() Integer
  + getAddress() String
  ~ onUpdate() void
  ~ onCreate() void
  + getImages() List~ListingImage~
}
class ListingController {
  - ListingService listingService
  - Logger log
  + create(CreateListingRequest, List~MultipartFile~) ResponseEntity~String~
  + getListingById(Integer) ResponseEntity~ListingDetailsResponse~
  + search(ListingSearchRequest, int, int) ResponseEntity~Page~ListingSummaryResponse~~
  + delete(Integer) ResponseEntity~Void~
  + getAgentsListings(int, int) ResponseEntity~Page~ListingSummaryResponse~~
  + update(Integer, UpdateListingRequest, List~MultipartFile~) ResponseEntity~Void~
}
class ListingDetailsResponse {
  - String description
  - Boolean hasBalcony
  - List~String~ imageUrls
  - BigDecimal price
  - Integer floor
  - String title
  - Double lat
  - EnergyClass energyClass
  - Double area
  - Boolean hasElevator
  - String address
  - Boolean hasParking
  - Boolean hasGarden
  - Integer id
  - Double lng
  - ListingOperation listingOperation
  - Integer bathrooms
  - Integer rooms
  + getArea() Double
  + setRooms(Integer) void
  + setHasBalcony(Boolean) void
  + setLng(Double) void
  + builder() ListingDetailsResponseBuilder
  + getId() Integer
  + getTitle() String
  + setEnergyClass(EnergyClass) void
  + getHasBalcony() Boolean
  + setArea(Double) void
  + setBathrooms(Integer) void
  + setHasElevator(Boolean) void
  + setLat(Double) void
  + getDescription() String
  + getAddress() String
  + setAddress(String) void
  + getLng() Double
  + getHasGarden() Boolean
  + setListingOperation(ListingOperation) void
  + getListingOperation() ListingOperation
  + getHasParking() Boolean
  + getRooms() Integer
  + getBathrooms() Integer
  + getLat() Double
  + setImageUrls(List~String~) void
  + getPrice() BigDecimal
  + getEnergyClass() EnergyClass
  + setDescription(String) void
  + setPrice(BigDecimal) void
  + setHasGarden(Boolean) void
  + getImageUrls() List~String~
  + setHasParking(Boolean) void
  + getFloor() Integer
  + setId(Integer) void
  + setFloor(Integer) void
  - $default$imageUrls() List~String~
  + getHasElevator() Boolean
  + setTitle(String) void
}
class ListingImage {
  - LocalDateTime createdAt
  - Listing listing
  - Integer id
  - Integer sortOrder
  - String url
  + getCreatedAt() LocalDateTime
  + getId() Integer
  + setSortOrder(Integer) void
  + getUrl() String
  + getListing() Listing
  + getSortOrder() Integer
  + builder() ListingImageBuilder
  + setId(Integer) void
  + setUrl(String) void
  + setCreatedAt(LocalDateTime) void
  + setListing(Listing) void
}
class ListingImageRepository {
<<Interface>>
  + findByListingId(Integer) List~ListingImage~
  + deleteByUrl(String) void
  + findMaxSortOrderByListingId(Integer) Integer
}
class ListingImageService {
  - S3ImageStorageService s3ImageStorageService
  - ListingImageRepository listingImageRepository
  + findListingImages(Integer) List~ListingImage~
  + presign(String) String
  + deleteImageByUrl(String) void
  + saveImage(MultipartFile, Listing) ListingImage
  + deleteListingImages(Integer) void
}
class ListingMapper {
  - ListingImageService listingImageService
  - GeometryService geometryService
  - setOptionalFields(CreateListingRequest, Listing) void
  + createListingDetailResponse(Listing) ListingDetailsResponse
  + toEntity(CreateListingRequest) Listing
}
class ListingRepository {
<<Interface>>
  + findAgentIdByListingId(Integer) Optional~Integer~
  + findListingsByAgentId(Integer, Pageable) Page~ListingSummaryResponse~
}
class ListingSearchRepository {
<<Interface>>
  + searchListingsByFilters(ListingSearchRequest, Pageable) Page~ListingSummaryResponse~
}
class ListingSearchRepositoryImpl {
  - NamedParameterJdbcTemplate jdbcTemplate
  - ListingImageService listingImageService
  + searchListingsByFilters(ListingSearchRequest, Pageable) Page~ListingSummaryResponse~
}
class ListingSearchRequest {
  - Double maxArea
  - ListingOperation operation
  - Double radiusKm
  - Integer minRooms
  - EnergyClass energyClass
  - Double maxPrice
  - Double minArea
  - Double minPrice
  - Double lng
  - Integer maxRooms
  - Double lat
  + getEnergyClass() EnergyClass
  + getOperation() ListingOperation
  + equals(Object) boolean
  + setOperation(ListingOperation) void
  + setMinRooms(Integer) void
  + getMinPrice() Double
  + getMaxPrice() Double
  + setMinPrice(Double) void
  + setMaxArea(Double) void
  + getMinRooms() Integer
  + setRadiusKm(Double) void
  + getRadiusKm() Double
  + setEnergyClass(EnergyClass) void
  + setLng(Double) void
  # canEqual(Object) boolean
  + setMinArea(Double) void
  + getMaxRooms() Integer
  + getMinArea() Double
  + setMaxRooms(Integer) void
  + toString() String
  + hashCode() int
  + setMaxPrice(Double) void
  + getMaxArea() Double
  + setLat(Double) void
  + getLng() Double
  + getLat() Double
}
class ListingService {
  - ListingSearchRepositoryImpl listingSearchRepository
  - Logger log
  - ListingMapper listingMapper
  - GeometryFactory geometryFactory
  - ListingRepository listingRepository
  - ListingImageService listingImageService
  - UserRepository userRepository
  - getAgent() User
  - uploadImages(Listing, List~MultipartFile~) void
  + retrieveListingById(Integer) ListingDetailsResponse
  + getListingsByAgent(Pageable) Page~ListingSummaryResponse~
  - applyUpdateFields(Listing, UpdateListingRequest) void
  + createListing(CreateListingRequest, List~MultipartFile~) void
  + updateListingById(UpdateListingRequest, Integer, List~MultipartFile~) void
  - fetchAgent(UserDetails) User
  + searchListingsByFilters(ListingSearchRequest, Pageable) Page~ListingSummaryResponse~
  + deleteListingById(Integer) void
}
class ListingSummaryResponse {
  - Integer bathrooms
  - String energyClass
  - Double longitude
  - String thumbnailUrl
  - String title
  - String address
  - Integer rooms
  - String description
  - Double area
  - Integer listing_id
  - BigDecimal price
  - String listingOperation
  - Double latitude
  + getDescription() String
  + getListing_id() Integer
  + getTitle() String
  + getAddress() String
  + getLongitude() Double
  + getRooms() Integer
  + setListingOperation(String) void
  + setLatitude(Double) void
  + getEnergyClass() String
  + setLongitude(Double) void
  + setThumbnailUrl(String) void
  + getBathrooms() Integer
  + setTitle(String) void
  + setBathrooms(Integer) void
  + getLatitude() Double
  + setEnergyClass(String) void
  + setListing_id(Integer) void
  + getPrice() BigDecimal
  + setAddress(String) void
  + getListingOperation() String
  + getArea() Double
  + setArea(Double) void
  + setPrice(BigDecimal) void
  + setDescription(String) void
  + setRooms(Integer) void
  + getThumbnailUrl() String
}
class UpdateListingRequest {
  - Double lat
  - Double area
  - String description
  - Integer bathrooms
  - Boolean hasBalcony
  - Boolean hasElevator
  - Boolean hasGarden
  - Integer rooms
  - List~String~ imagesToRemove
  - ListingOperation operation
  - Boolean hasParking
  - BigDecimal price
  - Integer floor
  - String address
  - EnergyClass energyClass
  - String title
  - Integer id
  - Double lng
  + hashCode() int
  + setRooms(Integer) void
  + setBathrooms(Integer) void
  + getHasBalcony() Boolean
  + setDescription(String) void
  + setTitle(String) void
  + equals(Object) boolean
  + setLat(Double) void
  # canEqual(Object) boolean
  + getHasGarden() Boolean
  + setOperation(ListingOperation) void
  + setArea(Double) void
  + getBathrooms() Integer
  + setHasParking(Boolean) void
  + setImagesToRemove(List~String~) void
  + getId() Integer
  + getTitle() String
  + getArea() Double
  + getDescription() String
  + setId(Integer) void
  + getAddress() String
  + setEnergyClass(EnergyClass) void
  + setHasGarden(Boolean) void
  + toString() String
  + setLng(Double) void
  + getLat() Double
  + getLng() Double
  + getPrice() BigDecimal
  + getHasParking() Boolean
  + getHasElevator() Boolean
  + setHasBalcony(Boolean) void
  + getImagesToRemove() List~String~
  + setPrice(BigDecimal) void
  + getFloor() Integer
  + getEnergyClass() EnergyClass
  + setAddress(String) void
  + getRooms() Integer
  + getOperation() ListingOperation
  + setFloor(Integer) void
  + builder() UpdateListingRequestBuilder
  + setHasElevator(Boolean) void
}

Listing "1" *--> "images *" ListingImage 
ListingController "1" *--> "listingService 1" ListingService 
ListingImage "1" *--> "listing 1" Listing 
ListingImageService  ..>  ListingImage : «create»
ListingImageService "1" *--> "listingImageRepository 1" ListingImageRepository 
ListingMapper "1" *--> "geometryService 1" GeometryService 
ListingMapper "1" *--> "listingImageService 1" ListingImageService 
ListingSearchRepositoryImpl "1" *--> "listingImageService 1" ListingImageService 
ListingSearchRepositoryImpl  ..>  ListingSearchRepository 
ListingSearchRepositoryImpl  ..>  ListingSummaryResponse : «create»
ListingService "1" *--> "listingImageService 1" ListingImageService 
ListingService "1" *--> "listingMapper 1" ListingMapper 
ListingService "1" *--> "listingRepository 1" ListingRepository 
ListingService "1" *--> "listingSearchRepository 1" ListingSearchRepositoryImpl 
