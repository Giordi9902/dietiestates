import { Component, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router } from '@angular/router';
import { AdminService } from '../../../services/services/admin.service';
import { UserService } from '../../../services/user/user';
import { AgentResponse } from '../../../services/models/agent-response';
import { AgentCard } from './agent-card/agent-card';

@Component({
  selector: 'app-agents-management',
  imports: [CommonModule, AgentCard],
  templateUrl: './agents-management.html',
  styleUrl: './agents-management.css',
})
export class AgentsManagement {
  private adminService = inject(AdminService);
  private userService = inject(UserService);
  private router = inject(Router);

  agents: AgentResponse[] = [];
  loading = false;
  error: string | null = null;
  currentUserEmail: string | null = null;

  isModalVisible = false;
  agentIdToDelete: number | null = null;

  isPromoteModalVisible = false;
  agentIdToPromote: number | null = null;

  ngOnInit() {
    this.currentUserEmail = this.userService.email;
    this.loadAgents();
  }

  loadAgents() {
    this.loading = true;
    this.error = null;
    this.adminService.getAgentsByAgency({ pageable: { page: 0, size: 20 } as any }).subscribe({
      next: (page) => {
        this.agents = page.content ?? [];
        this.loading = false;
      },
      error: (err) => {
        this.error = err.error?.message || err.message || 'Caricamento agenti non riuscito.';
        this.loading = false;
      },
    });
  }

  onPromote(id: number) {
    this.agentIdToPromote = id;
    this.isPromoteModalVisible = true;
  }

  confirmPromote() {
    if (this.agentIdToPromote === null) return;
    this.adminService.promoteToAdmin({ agentId: this.agentIdToPromote }).subscribe({
      next: () => {
        this.loadAgents();
        this.cancelPromote();
      },
      error: (err) => {
        this.error = err.error?.message || err.message || 'Promozione non riuscita.';
        this.cancelPromote();
      },
    });
  }

  cancelPromote() {
    this.isPromoteModalVisible = false;
    this.agentIdToPromote = null;
  }

  onRemove(id: number) {
    this.agentIdToDelete = id;
    this.isModalVisible = true;
  }

  confirmRemove() {
    if (this.agentIdToDelete === null) return;

    this.adminService.deleteAgent({ agentId: this.agentIdToDelete }).subscribe({
      next: () => {
        this.loadAgents();
        this.cancelRemove();
      },
      error: (err) => {
        this.error = err.error?.message || err.message || 'Eliminazione non riuscita.';
        this.cancelRemove();
      },
    });
  }

  cancelRemove() {
    this.isModalVisible = false;
    this.agentIdToDelete = null;
  }

  goToCreate() {
    this.router.navigate(['/dashboard/agents-new']);
  }
}
