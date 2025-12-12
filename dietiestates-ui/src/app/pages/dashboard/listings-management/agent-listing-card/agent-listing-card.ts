import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ListingSummaryResponse } from '../../../../services/models/listing-summary-response';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-agent-listing-card',
  imports: [RouterLink],
  templateUrl: './agent-listing-card.html',
  styleUrl: './agent-listing-card.css',
})
export class AgentListingCard {
  @Input() listing!: ListingSummaryResponse;
  @Output() delete = new EventEmitter<number>();

  get isRent(): boolean {
    return (this.listing.listingOperation ?? '').toUpperCase() === 'RENT';
  }

  get isSale(): boolean {
    return (this.listing.listingOperation ?? '').toUpperCase() === 'SALE';
  }

  onDelete(): void {
    const id = this.listing.listing_id;
    if (id != null) this.delete.emit(id);
  }
}
