classDiagram
direction BT
class EnergyClass {
<<enumeration>>
  +  B
  +  G
  +  A4
  +  A3
  +  A2
  +  E
  +  D
  +  A1
  +  C
  +  F
}
class Listing {
  - Double area
  - Integer bathrooms
  - User listedBy
  - String title
  - String description
  - Point coordinate
  - Integer id
  - EnergyClass energyClass
  - Boolean hasBalcony
  - LocalDateTime createdDate
  - ListingOperation listingOperation
  - String address
  - Boolean hasElevator
  - List~ListingImage~ images
  - Integer floor
  - Boolean hasParking
  - LocalDateTime lastModifiedDate
  - BigDecimal price
  - Boolean hasGarden
  - Integer rooms
}
class ListingImage {
  - Integer sortOrder
  - Integer id
  - String url
  - LocalDateTime createdAt
  - Listing listing
}
class ListingOperation {
<<enumeration>>
  +  SALE
  +  RENT
}

Listing "1" *--> "energyClass 1" EnergyClass 
Listing "1" *--> "images *" ListingImage 
Listing "1" *--> "listingOperation 1" ListingOperation 
ListingImage "1" *--> "listing 1" Listing 
