import { Component, inject } from '@angular/core';
import { CurrencyPipe } from '@angular/common';
import { ListingsService } from '../../../../services/services/listings.service';
import { CreateListingRequest } from '../../../../services/models/create-listing-request';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';

@Component({
  selector: 'app-create-new-listing',
  imports: [FormsModule, CurrencyPipe],
  templateUrl: './create-new-listing.html',
  styleUrl: './create-new-listing.css',
})
export class CreateNewListing {
  private listingsService = inject(ListingsService);
  private router = inject(Router);

  model: CreateListingRequest = {
    title: '',
    description: '',
    listingOperation: 'SALE',
    price: 0,
    area: 0,
    energyClass: 'C',
    lat: 0,
    lng: 0,
    rooms: 0,
    bathrooms: 0,
    floor: 0,
    hasBalcony: false,
    hasElevator: false,
    hasGarden: false,
    hasParking: false,
    address: '',
  } as CreateListingRequest;

  images: File[] = [];
  imagePreviews: string[] = [];

  addressInput = '';
  submitting = false;
  error: string | null = null;

  ngOnInit() {
    setTimeout(() => this.initMap(), 0);
  }

  onFilesSelected(event: Event) {
    const input = event.target as HTMLInputElement;
    if (!input.files) return;
    const selected = Array.from(input.files);
    const existingKeys = new Set(this.images.map((f) => `${f.name}:${f.size}`));
    selected.forEach((f) => {
      const key = `${f.name}:${f.size}`;
      if (!existingKeys.has(key)) {
        this.images.push(f);
      }
    });
    this.imagePreviews = [];
    this.images.forEach((file) => {
      const reader = new FileReader();
      reader.onload = () => this.imagePreviews.push(String(reader.result));
      reader.readAsDataURL(file);
    });
    input.value = '';
  }

  reset() {
    this.model = {
      title: '',
      description: '',
      listingOperation: 'SALE',
      price: 0,
      area: 0,
      energyClass: 'C',
      lat: 0,
      lng: 0,
      rooms: 0,
      bathrooms: 0,
      floor: 0,
      hasBalcony: false,
      hasElevator: false,
      hasGarden: false,
      hasParking: false,
      address: '',
    } as CreateListingRequest;
    this.images = [];
    this.imagePreviews = [];
    this.addressInput = '';
    this.error = null;
    this.updateMarker();
  }

  async geocodeAddress() {
    if (!this.addressInput?.trim()) return;
    try {
      const q = encodeURIComponent(this.addressInput.trim());
      const res = await fetch(`https://nominatim.openstreetmap.org/search?format=json&q=${q}`);
      const data = await res.json();
      if (Array.isArray(data) && data.length > 0) {
        const first = data[0];
        this.model.lat = parseFloat(first.lat);
        this.model.lng = parseFloat(first.lon);
        this.model.address = first.display_name;
        this.updateMarker(true);
      } else {
        this.error = 'Indirizzo non trovato';
      }
    } catch {
      this.error = 'Errore durante la ricerca indirizzo';
    }
  }

  async reverseGeocode(lat: number, lon: number) {
    try {
      const res = await fetch(
        `https://nominatim.openstreetmap.org/reverse?format=json&lat=${lat}&lon=${lon}`
      );
      const data = await res.json();
      if (data?.display_name) {
        this.model.address = data.display_name;
        this.addressInput = data.display_name;
      }
    } catch {}
  }

  submit() {
    this.error = null;
    if (!this.images.length) {
      this.error = 'Carica almeno una foto.';
      return;
    }
    if (
      !this.model.title ||
      !this.model.description ||
      !this.model.listingOperation ||
      this.model.price == null ||
      this.model.area == null ||
      !this.model.energyClass ||
      !this.model.lat ||
      !this.model.lng
    ) {
      this.error = 'Compila tutti i campi obbligatori.';
      return;
    }

    this.submitting = true;

    console.log('Submitting listing:', {
      model: this.model,
      imagesCount: this.images.length,
      images: this.images.map((f) => ({ name: f.name, size: f.size, type: f.type })),
    });

    this.listingsService
      .create({
        body: {
          request: this.model,
          images: this.images,
        },
      })
      .subscribe({
        next: () => {
          console.log('Listing created successfully');
          this.submitting = false;
          this.router.navigate(['/dashboard/listings']);
        },
        error: (err) => {
          console.error('Error creating listing:', err);
          console.error('Error details:', {
            status: err.status,
            statusText: err.statusText,
            message: err.error?.message || err.message,
            error: err.error,
          });
          this.submitting = false;
          this.error = err.error?.message || err.message || 'Creazione annuncio non riuscita.';
        },
      });
  }

  map?: any;
  marker?: any;

  initMap() {
    try {
      const L = (window as any).L;
      if (!L) return;

      const defaultIcon = new L.Icon({
        iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
        iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
        shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41],
      });
      this.map = L.map('map').setView([this.model.lat || 40.8518, this.model.lng || 14.2681], 13);
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
        maxZoom: 19,
        attribution: '© OpenStreetMap',
      }).addTo(this.map);

      this.marker = L.marker([this.model.lat || 40.8518, this.model.lng || 14.2681], {
        draggable: true,
        icon: defaultIcon,
      }).addTo(this.map);
      this.marker.on('dragend', (e: any) => {
        const p = e.target.getLatLng();
        this.model.lat = p.lat;
        this.model.lng = p.lng;
        this.reverseGeocode(p.lat, p.lng);
      });

      this.map.on('click', (e: any) => {
        const p = e.latlng;
        this.model.lat = p.lat;
        this.model.lng = p.lng;
        this.updateMarker();
        this.reverseGeocode(p.lat, p.lng);
      });
    } catch {}
  }

  updateMarker(fly = false) {
    if (!this.map) return;
    const L = (window as any).L;
    if (!this.marker) {
      const defaultIcon = new L.Icon({
        iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
        iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
        shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
        iconSize: [25, 41],
        iconAnchor: [12, 41],
        popupAnchor: [1, -34],
        shadowSize: [41, 41],
      });
      this.marker = L.marker([this.model.lat, this.model.lng], { icon: defaultIcon }).addTo(
        this.map
      );
    } else {
      this.marker.setLatLng([this.model.lat, this.model.lng]);
    }
    if (fly) this.map.flyTo([this.model.lat, this.model.lng], 16);
  }
}
