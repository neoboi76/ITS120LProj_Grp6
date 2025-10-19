import { ComponentFixture, TestBed } from '@angular/core/testing';

import { HistoryPageComponent } from './history';

describe('History', () => {
  let component: HistoryPageComponent ;
  let fixture: ComponentFixture<HistoryPageComponent >;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [HistoryPageComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(HistoryPageComponent );
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
