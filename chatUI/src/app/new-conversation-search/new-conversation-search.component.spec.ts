import { ComponentFixture, TestBed } from '@angular/core/testing';

import { NewConversationSearchComponent } from './new-conversation-search.component';

describe('NewConversationSearchComponent', () => {
  let component: NewConversationSearchComponent;
  let fixture: ComponentFixture<NewConversationSearchComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ NewConversationSearchComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(NewConversationSearchComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
