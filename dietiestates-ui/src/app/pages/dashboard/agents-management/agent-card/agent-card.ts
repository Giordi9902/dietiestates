import { Component, EventEmitter, Input, Output } from '@angular/core';
import { AgentResponse } from '../../../../services/models/agent-response';

@Component({
  selector: 'app-agent-card',
  standalone: true,
  templateUrl: './agent-card.html',
  styleUrl: './agent-card.css',
})
export class AgentCard {
  @Input() agent!: AgentResponse;
  @Input() currentUserEmail?: string;
  @Output() promote = new EventEmitter<number>();
  @Output() remove = new EventEmitter<number>();

  hasRoleAdmin(): boolean {
    return this.agent.roles?.some((r) => r.name === 'ROLE_ADMIN') ?? false;
  }

  onPromote() {
    if (this.agent?.id != null) this.promote.emit(this.agent.id);
  }

  onRemove() {
    if (this.agent?.id != null) this.remove.emit(this.agent.id);
  }
}
