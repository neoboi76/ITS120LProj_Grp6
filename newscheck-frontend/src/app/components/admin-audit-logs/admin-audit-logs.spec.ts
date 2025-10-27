import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminAuditLogsComponent } from './admin-audit-logs';

describe('AdminAuditLogs', () => {
  let component: AdminAuditLogsComponent;
  let fixture: ComponentFixture<AdminAuditLogsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminAuditLogsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminAuditLogsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
