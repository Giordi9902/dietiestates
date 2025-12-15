import { Component, inject } from '@angular/core';
import { AgencyService } from '../../../services/services/agency.service';
import { AgencyDetailsResponse } from '../../../services/models/agency-details-response';
import { RouterLink } from '@angular/router';
import { UserService } from '../../../services/user/user';

@Component({
  selector: 'app-agency-homepage',
  imports: [RouterLink],
  templateUrl: './agency-homepage.html',
  styleUrl: './agency-homepage.css',
})
export class AgencyHomepage {
  private agencyService = inject(AgencyService);
  private userService = inject(UserService);

  agency: AgencyDetailsResponse | null = null;
  loading = true;
  error: string | null = null;

  constructor() {
    this.loadAgency();
  }

  get isAdmin(): boolean {
    return this.userService.hasAnyRole(['ROLE_ADMIN', 'ROLE_ADMIN_ASSISTANT']);
  }

  private loadAgency(): void {
    this.loading = true;
    this.error = null;
    this.agencyService.getMyAgencyDetails().subscribe({
      next: (res) => {
        this.agency = res;
        this.loading = false;
      },
      error: () => {
        this.error = 'Impossibile caricare i dettagli della tua agenzia.';
        this.loading = false;
      },
    });
  }
}
