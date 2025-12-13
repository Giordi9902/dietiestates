import { Component, OnInit, AfterViewInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, ReactiveFormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { ListingsService } from '../../services/services/listings.service';
import { ListingSummaryResponse } from '../../services/models/listing-summary-response';
import { ListingSearchRequest } from '../../services/models/listing-search-request';
import { ListingCard } from '../../shared/listing-card/listing-card';
import * as L from 'leaflet';

@Component({
  selector: 'app-search',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, ListingCard],
  templateUrl: './search.html',
  styleUrl: './search.css',
})
export class Search implements OnInit, AfterViewInit {
  private fb = inject(FormBuilder);
  private listingsService = inject(ListingsService);
  private router = inject(Router);

  searchForm!: FormGroup;
  listings: ListingSummaryResponse[] = [];
  loading = false;
  error: string | null = null;
  map: L.Map | null = null;
  marker: L.Marker | null = null;
  circle: L.Circle | null = null;

  private defaultIcon!: L.Icon;

  energyClasses = ['A4', 'A3', 'A2', 'A1', 'B', 'C', 'D', 'E', 'F', 'G'];

  ngOnInit(): void {
    this.defaultIcon = new L.Icon({
      iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
      iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
      shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
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
      this.updateMarkerAndCircle(e.latlng, this.defaultIcon);
    });
  }

  updateMarkerAndCircle(latlng: L.LatLng, icon: L.Icon): void {
    this.searchForm.patchValue({ lat: latlng.lat, lng: latlng.lng });

    if (!this.marker) {
      this.marker = L.marker(latlng, { icon }).addTo(this.map!);
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

  onSearch(): void {
    this.loading = true;
    this.error = null;
    const searchParams: ListingSearchRequest = this.searchForm.value;

    // Remove null and empty values
    Object.keys(searchParams).forEach(
      (key) => (searchParams as any)[key] == null && delete (searchParams as any)[key]
    );

    this.listingsService.search({ body: searchParams }).subscribe({
      next: (response) => {
        this.listings = response.content ?? [];
        this.updateResultMarkers();
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.message || err.message || 'Ricerca non riuscita.';
        this.loading = false;
      },
    });
  }

  private resultMarkers: L.Marker[] = [];

  private updateResultMarkers(): void {
    // Clear previous markers
    this.resultMarkers.forEach((marker) => marker.removeFrom(this.map!));
    this.resultMarkers = [];

    this.listings.forEach((listing) => {
      if (listing.latitude && listing.longitude) {
        const marker = L.marker([listing.latitude, listing.longitude], { icon: this.defaultIcon })
          .addTo(this.map!)
          .bindPopup(`<b>${listing.title}</b><br>${listing.price} €`);
        this.resultMarkers.push(marker);
      }
    });
  }

  onRadiusChange(): void {
    this.updateCircle();
  }
}
