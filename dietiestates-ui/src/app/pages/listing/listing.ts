import { Component, inject, OnInit, AfterViewInit } from '@angular/core';
import { ActivatedRoute, RouterModule } from '@angular/router';
import { CommonModule } from '@angular/common';
import { ListingsService } from '../../services/services/listings.service';
import { ListingDetailsResponse } from '../../services/models/listing-details-response';

@Component({
  selector: 'app-listing',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './listing.html',
  styleUrl: './listing.css',
})
export class Listing implements OnInit, AfterViewInit {
  private route = inject(ActivatedRoute);
  private listingsService = inject(ListingsService);

  listing: ListingDetailsResponse | null = null;
  loading = true;
  error: string | null = null;
  selectedImageUrl: string | null = null;

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.fetchListing(parseInt(id, 10));
    } else {
      this.error = 'Nessun annuncio specificato.';
      this.loading = false;
    }
  }

  ngAfterViewInit(): void {
    // Inizializza mappa dopo che il view è pronto
    setTimeout(() => this.initMap(), 100);
  }

  fetchListing(id: number): void {
    this.loading = true;
    this.listingsService.getListingById({ id }).subscribe({
      next: (data) => {
        this.listing = data;
        if (data.imageUrls && data.imageUrls.length > 0) {
          this.selectedImageUrl = data.imageUrls[0];
        }
        this.loading = false;
        // Inizializza mappa dopo aver ricevuto i dati
        setTimeout(() => this.initMap(), 100);
      },
      error: (err) => {
        this.error = err.error?.message || err.message || 'Caricamento annuncio non riuscito.';
        this.loading = false;
      },
    });
  }

  selectImage(url: string): void {
    this.selectedImageUrl = url;
  }

  map?: any;
  marker?: any;

  initMap(): void {
    if (!this.listing || typeof window === 'undefined') return;
    try {
      const L = (window as any).L;
      if (!L) return;
      // Fix marker icon URLs when using Leaflet via CDN
      const defaultIcon = new L.Icon({
        iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
        iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
        shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41],
      });
      this.map = L.map('map').setView([this.listing.lat, this.listing.lng], 15);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap',
      }).addTo(this.map);

      this.marker = L.marker([this.listing.lat, this.listing.lng], {
        icon: defaultIcon,
      }).addTo(this.map);
    } catch {
      // map not available
    }
  }
}
