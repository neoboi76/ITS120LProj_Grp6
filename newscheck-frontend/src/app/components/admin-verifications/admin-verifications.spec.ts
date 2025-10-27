import { ComponentFixture, TestBed } from '@angular/core/testing';

import { AdminVerificationsComponent } from './admin-verifications';

describe('AdminVerifications', () => {
  let component: AdminVerificationsComponent;
  let fixture: ComponentFixture<AdminVerificationsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [AdminVerificationsComponent]
    })
    .compileComponents();

    fixture = TestBed.createComponent(AdminVerificationsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
