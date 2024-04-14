import { Component, OnInit, inject } from '@angular/core';
import { CategoryService } from 'src/app/modules/shared/services/category.service';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  private categoryService = inject(CategoryService);

  ngOnInit(): void {
    this.getCategories();
  }

  getCategories(): void {
    this.categoryService.getCategories()
      .subscribe({
        next: (data: any) => {
          console.log("Respuesta categories: ", data);
        },
        error: (error: any) => {
          console.log("Error: ", error);
        }
      });
  }
}
