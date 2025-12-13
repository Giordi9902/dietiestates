import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ListingSummaryResponse } from '../../services/models/listing-summary-response';
import { CommonModule } from '@angular/common';
import { RouterModule } from '@angular/router';

@Component({
  selector: 'app-listing-card',
  standalone: true,
  imports: [CommonModule, RouterModule],
  templateUrl: './listing-card.html',
  styleUrls: ['./listing-card.css'],
})
export class ListingCard {
  @Input() listing!: ListingSummaryResponse;
}
