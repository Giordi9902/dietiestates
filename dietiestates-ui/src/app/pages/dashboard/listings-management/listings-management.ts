import { Component, inject } from '@angular/core';
import { ListingsService } from '../../../services/services/listings.service';
import { ListingSummaryResponse } from '../../../services/models/listing-summary-response';
import { AgentListingCard } from './agent-listing-card/agent-listing-card';
import { RouterLink } from '@angular/router';
import { CommonModule } from '@angular/common';

@Component({
  selector: 'app-listings-management',
  imports: [AgentListingCard, RouterLink, CommonModule],
  templateUrl: './listings-management.html',
  styleUrl: './listings-management.css',
})
export class ListingsManagement {
  private listingsService = inject(ListingsService);

  items: ListingSummaryResponse[] = [];
  loading = true;
  error: string | null = null;

  page = 0;
  size = 12;
  totalPages = 0;
  confirmId: number | null = null;

  constructor() {
    this.load();
  }

  load(page: number = this.page): void {
    this.loading = true;
    this.error = null;
    this.listingsService.getAgentsListings({ page, size: this.size }).subscribe({
      next: (resp) => {
        this.items = resp.content ?? [];
        this.page = resp.number ?? 0;
        this.size = resp.size ?? this.size;
        this.totalPages = resp.totalPages ?? 0;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossibile caricare i tuoi annunci.';
        this.loading = false;
      },
    });
  }

  openDeleteConfirm(id: number): void {
    this.confirmId = id;
  }

  cancelDelete(): void {
    this.confirmId = null;
  }

  confirmDelete(): void {
    if (this.confirmId == null) return;
    const id = this.confirmId;
    this.listingsService.delete({ id }).subscribe({
      next: () => {
        this.confirmId = null;
        this.load(this.page);
      },
      error: () => {
        this.error = 'Eliminazione non riuscita.';
        this.confirmId = null;
      },
    });
  }
}
