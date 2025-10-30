import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FooterComponent } from './footer';

/* 
Developed by Group 6:
      Ken Aliling
      Anicia Kaela Bonayao
      Carl Norbi Felonia
      Cedrick Miguel Kaneko
      Dino Alfred T. Timbol (Group Leader)
 */

describe('Footer', () => {
  let component: FooterComponent;
  let fixture: ComponentFixture<FooterComponent >;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      imports: [FooterComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FooterComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
