import { Component, OnInit, AfterViewInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, FormsModule, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ListingsService } from '../../services/services/listings.service';
import { ListingSummaryResponse } from '../../services/models/listing-summary-response';
import { ListingSearchRequest } from '../../services/models/listing-search-request';
import { ListingCard } from '../../shared/listing-card/listing-card';
import * as L from 'leaflet';
import { HttpClient, HttpClientModule } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, FormsModule, ListingCard],
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search implements OnInit, AfterViewInit {
  private fb = inject(FormBuilder);
  private listingsService = inject(ListingsService);
  private router = inject(Router);
  private http = inject(HttpClient);

  searchForm!: FormGroup;
  listings: ListingSummaryResponse[] = [];
  loading = false;
  error: string | null = null;
  map: L.Map | null = null;
  marker: L.Marker | null = null;
  circle: L.Circle | null = null;

  showAdvancedFilters = false;
  currentPage = 0;
  pageSize = 10;
  totalPages = 0;
  totalElements = 0;

  private defaultIcon!: L.Icon;
  private highlightIcon!: L.Icon;

  energyClasses = ['A4', 'A3', 'A2', 'A1', 'B', 'C', 'D', 'E', 'F', 'G'];

  ngOnInit(): void {
    this.defaultIcon = new L.Icon({
      iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
      shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    this.highlightIcon = new L.Icon({
      iconUrl:
        'https://raw.githubusercontent.com/pointhi/leaflet-color-markers/master/img/marker-icon-2x-red.png',
      shadowUrl: 'https://cdnjs.cloudflare.com/ajax/libs/leaflet/0.7.7/images/marker-shadow.png',
      iconSize: [25, 41],
      iconAnchor: [12, 41],
      popupAnchor: [1, -34],
      shadowSize: [41, 41],
    });

    this.searchForm = this.fb.group({
      operation: ['SALE'],
      address: [null],
      lat: [null],
      lng: [null],
      radiusKm: [5],
      minPrice: [null],
      maxPrice: [null],
      minArea: [null],
      maxArea: [null],
      minRooms: [null],
      maxRooms: [null],
      energyClass: [null],
    });
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initMap(): void {
    this.map = L.map('map').setView([40.8518, 14.2681], 13); // Default to Naples

    L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
      attribution: '© OpenStreetMap contributors',
    }).addTo(this.map);

    this.map.on('click', (e: L.LeafletMouseEvent) => {
      this.updateMarkerAndCircle(e.latlng);
    });
  }

  updateMarkerAndCircle(latlng: L.LatLng): void {
    this.searchForm.patchValue({ lat: latlng.lat, lng: latlng.lng });

    if (!this.marker) {
      this.marker = L.marker(latlng, { icon: this.defaultIcon }).addTo(this.map!);
    } else {
      this.marker.setLatLng(latlng);
    }

    this.updateCircle();
  }

  updateCircle(): void {
    const latlng = this.marker?.getLatLng();
    if (!latlng) return;

    const radius = this.searchForm.value.radiusKm * 1000;

    if (!this.circle) {
      this.circle = L.circle(latlng, { radius }).addTo(this.map!);
    } else {
      this.circle.setLatLng(latlng);
      this.circle.setRadius(radius);
    }
    this.map?.fitBounds(this.circle.getBounds());
  }

  toggleAdvancedFilters(): void {
    this.showAdvancedFilters = !this.showAdvancedFilters;
    setTimeout(() => {
      this.map?.invalidateSize();
    }, 0);
  }

  onSearch(resetPage = true): void {
    if (resetPage) {
      this.currentPage = 0;
    }

    if (!this.searchForm.value.lat || !this.searchForm.value.lng) {
      this.error = 'Seleziona una posizione sulla mappa o cerca un indirizzo per procedere.';
      return;
    }

    this.loading = true;
    this.error = null;
    const searchParams: ListingSearchRequest = this.searchForm.value;

    Object.keys(searchParams).forEach(
      (key) => (searchParams as any)[key] == null && delete (searchParams as any)[key]
    );

    this.listingsService
      .search({
        body: searchParams,
        page: this.currentPage,
        size: this.pageSize,
      })
      .subscribe({
        next: (response) => {
          this.listings = response.content ?? [];
          this.totalPages = response.totalPages ?? 0;
          this.totalElements = response.totalElements ?? 0;
          this.updateResultMarkers();
          this.loading = false;
        },
        error: (err) => {
          this.error = err.error?.message || err.message || 'Ricerca non riuscita.';
          this.loading = false;
        },
      });
  }

  changePage(page: number): void {
    if (page >= 0 && page < this.totalPages) {
      this.currentPage = page;
      this.onSearch(false);
    }
  }

  get pages(): number[] {
    return Array.from({ length: this.totalPages }, (_, i) => i);
  }

  private resultMarkers = new Map<number, L.Marker>();

  private updateResultMarkers(): void {
    this.resultMarkers.forEach((marker) => marker.removeFrom(this.map!));
    this.resultMarkers.clear();

    this.listings.forEach((listing) => {
      if (listing.latitude && listing.longitude && listing.listing_id) {
        const marker = L.marker([listing.latitude, listing.longitude], {
          icon: this.defaultIcon,
        })
          .addTo(this.map!)
          .bindPopup(`<b>${listing.title}</b><br>${listing.price} €`);
        this.resultMarkers.set(listing.listing_id, marker);
      }
    });
  }

  onRadiusChange(): void {
    this.updateCircle();
  }

  searchByAddress(): void {
    const address = this.searchForm.value.address;
    if (!address) {
      return;
    }

    this.http
      .get<any[]>(`https://nominatim.openstreetmap.org/search?q=${address}&format=json&limit=1`)
      .pipe(
        catchError((err) => {
          this.error = 'Could not geocode address. Please try again.';
          return throwError(() => err);
        })
      )
      .subscribe((data) => {
        if (data.length > 0) {
          const latlng = new L.LatLng(data[0].lat, data[0].lon);
          this.updateMarkerAndCircle(latlng);
          this.map?.setView(latlng, 15);
        } else {
          this.error = 'Address not found.';
        }
      });
  }

  highlightMarker(listing: ListingSummaryResponse, isHighlighted: boolean): void {
    if (!listing.listing_id) return;
    const marker = this.resultMarkers.get(listing.listing_id);
    if (marker) {
      marker.setIcon(isHighlighted ? this.highlightIcon : this.defaultIcon);
      if (isHighlighted) {
        marker.setZIndexOffset(1000);
        marker.openPopup();
      } else {
        marker.setZIndexOffset(0);
        marker.closePopup();
      }
    }
  }
}
