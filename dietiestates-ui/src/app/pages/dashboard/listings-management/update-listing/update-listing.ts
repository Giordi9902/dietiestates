import { Component, inject, OnInit, AfterViewInit, ChangeDetectorRef } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';
import { FormBuilder, FormGroup, ReactiveFormsModule, Validators } from '@angular/forms';
import { CommonModule, CurrencyPipe } from '@angular/common';
import { ListingsService } from '../../../../services/services/listings.service';
import { ListingDetailsResponse } from '../../../../services/models/listing-details-response';
import { UpdateListingRequest } from '../../../../services/models/update-listing-request';
import { HttpClient } from '@angular/common/http';
import * as L from 'leaflet';

@Component({
  selector: 'app-update-listing',
  standalone: true,
  imports: [CommonModule, ReactiveFormsModule, CurrencyPipe],
  templateUrl: './update-listing.html',
  styleUrl: './update-listing.css',
})
export class UpdateListing implements OnInit, AfterViewInit {
  private route = inject(ActivatedRoute);
  private router = inject(Router);
  private listingsService = inject(ListingsService);
  private fb = inject(FormBuilder);
  private http = inject(HttpClient);
  private cdr = inject(ChangeDetectorRef);

  listingId: number | null = null;
  listingForm!: FormGroup;
  loading = true;
  error: string | null = null;

  currentImages: string[] = [];
  imagesToRemove: string[] = [];
  newImages: File[] = [];
  newImagePreviews: string[] = [];
  energyClasses = ['A4', 'A3', 'A2', 'A1', 'B', 'C', 'D', 'E', 'F', 'G'];

  showModal = false;
  modalTitle = '';
  modalMessage = '';
  pendingAction: 'save' | 'cancel' | null = null;

  private map: L.Map | null = null;
  private marker: L.Marker | null = null;
  private initialLatLng: L.LatLng | null = null;
  private mapInitialized = false;

  // Workaround for broken Leaflet icon in Angular
  private defaultIcon = L.icon({
    iconRetinaUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon-2x.png',
    iconUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-icon.png',
    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
    iconSize: [25, 41],
    iconAnchor: [12, 41],
    popupAnchor: [1, -34],
    shadowSize: [41, 41],
  });

  ngOnInit(): void {
    const id = this.route.snapshot.paramMap.get('id');
    if (!id) {
      this.error = 'ID annuncio non valido';
      this.loading = false;
      return;
    }
    this.listingId = parseInt(id, 10);
    this.initForm();
    this.loadListing();
  }

  ngAfterViewInit(): void {
    this.initMap();
  }

  private initForm(): void {
    this.listingForm = this.fb.group({
      title: ['', Validators.required],
      description: ['', Validators.required],
      price: [null, Validators.required],
      address: ['', Validators.required],
      area: [null, Validators.required],
      rooms: [null, Validators.required],
      bathrooms: [null, Validators.required],
      listingOperation: ['SALE', Validators.required],
      energyClass: [null],
      floor: [null],
      hasElevator: [false],
      hasParking: [false],
      hasGarden: [false],
      hasBalcony: [false],
      lat: [null],
      lng: [null],
    });
  }

  private initMap(): void {
    if (document.getElementById('map-update') && !this.map) {
      this.map = L.map('map-update').setView([40.8518, 14.2681], 13); // Default to Naples
      L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png').addTo(this.map);
      this.map.on('click', this.onMapClick.bind(this));
      this.mapInitialized = true;
      this.setInitialMapView(); // Attempt to set view now that map is ready
    }
  }

  private loadListing(): void {
    this.listingsService.getListingById({ id: this.listingId! }).subscribe({
      next: (data) => {
        this.listingForm.patchValue(data);
        this.currentImages = data.imageUrls || [];
        this.loading = false;
        this.cdr.detectChanges();
        this.initMap();

        if (data.lat && data.lng) {
          this.initialLatLng = L.latLng(data.lat, data.lng);
          this.setInitialMapView(); // Attempt to set view now that data is ready
        }
      },
      error: (err) => {
        this.error = 'Caricamento dati annuncio non riuscito.';
        this.loading = false;
      },
    });
  }

  private setInitialMapView(): void {
    if (this.mapInitialized && this.initialLatLng && this.map) {
      this.updateMarker(this.initialLatLng);
      this.map.setView(this.initialLatLng, 15);
      setTimeout(() => this.map?.invalidateSize(), 200); // Recalculate size
      this.initialLatLng = null; // Don't run this again
    }
  }

  onMapClick(e: L.LeafletMouseEvent): void {
    this.updateMarker(e.latlng);
    this.reverseGeocode(e.latlng);
  }

  updateMarker(latlng: L.LatLng): void {
    this.listingForm.patchValue({ lat: latlng.lat, lng: latlng.lng });
    if (this.marker) {
      this.marker.setLatLng(latlng);
    } else {
      this.marker = L.marker(latlng, { icon: this.defaultIcon }).addTo(this.map!);
    }
    this.map?.panTo(latlng);
  }

  searchAddress(): void {
    const address = this.listingForm.get('address')?.value;
    if (!address) return;

    this.http
      .get<any[]>(`https://nominatim.openstreetmap.org/search?q=${address}&format=json`)
      .subscribe((results) => {
        if (results && results.length > 0) {
          const { lat, lon, display_name } = results[0];
          const latlng = L.latLng(parseFloat(lat), parseFloat(lon));
          this.updateMarker(latlng);
          this.listingForm.patchValue({ address: display_name });
          this.map?.setView(latlng, 15);
        }
      });
  }

  private reverseGeocode(latlng: L.LatLng): void {
    this.http
      .get<any>(
        `https://nominatim.openstreetmap.org/reverse?lat=${latlng.lat}&lon=${latlng.lng}&format=json`
      )
      .subscribe((data) => {
        if (data && data.display_name) {
          this.listingForm.patchValue({ address: data.display_name });
          this.cdr.detectChanges();
        }
      });
  }

  onFileChange(event: any): void {
    const files = event.target.files;
    if (files) {
      for (const file of files) {
        this.newImages.push(file);
        const reader = new FileReader();
        reader.onload = (e: any) => {
          this.newImagePreviews.push(e.target.result);
        };
        reader.readAsDataURL(file);
      }
    }
  }

  removeCurrentImage(imageUrl: string): void {
    this.currentImages = this.currentImages.filter((url) => url !== imageUrl);
    this.imagesToRemove.push(imageUrl);
  }

  removeNewImage(index: number): void {
    this.newImages.splice(index, 1);
    this.newImagePreviews.splice(index, 1);
  }

  onCancel(): void {
    this.modalTitle = 'Annulla Modifiche';
    this.modalMessage = 'Sei sicuro di voler annullare le modifiche?';
    this.pendingAction = 'cancel';
    this.showModal = true;
  }

  onSubmit(): void {
    if (this.listingForm.invalid) {
      this.error = 'Per favore, compila tutti i campi obbligatori.';
      return;
    }

    this.modalTitle = 'Salva Modifiche';
    this.modalMessage = 'Sei sicuro di voler salvare le modifiche?';
    this.pendingAction = 'save';
    this.showModal = true;
  }

  confirmAction(): void {
    this.showModal = false;
    if (this.pendingAction === 'cancel') {
      this.router.navigate(['/dashboard/listings']);
    } else if (this.pendingAction === 'save') {
      this.saveListing();
    }
    this.pendingAction = null;
  }

  closeModal(): void {
    this.showModal = false;
    this.pendingAction = null;
  }

  private saveListing(): void {
    this.loading = true;
    this.error = null;

    const formValue = this.listingForm.value;
    const updateRequest: UpdateListingRequest = {
      ...formValue,
      imagesToRemove: this.imagesToRemove,
    } as any;

    this.listingsService
      .update({
        id: this.listingId!,
        body: {
          request: updateRequest,
          newImages: this.newImages,
        },
      })
      .subscribe({
        next: () => {
          this.router.navigate(['/dashboard/listings']);
        },
        error: (err) => {
          this.error = err.error?.message || err.message || 'Aggiornamento non riuscito.';
          this.loading = false;
        },
      });
  }
}
