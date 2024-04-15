import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewCategoryComponent } from './new-category.component';

describe('NewCategoryComponent', () => {
  let component: NewCategoryComponent;
  let fixture: ComponentFixture<NewCategoryComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [NewCategoryComponent]
    });
    fixture = TestBed.createComponent(NewCategoryComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
