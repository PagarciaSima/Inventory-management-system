import { Component, OnInit } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { CategoryService } from 'src/app/modules/shared/services/category.service';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  constructor(private categoryService: CategoryService) {} 

  ngOnInit(): void {
    this.getCategories();
  }

  displayedColumns: string[] = ['id', 'name', 'description', 'actions'];
  datasource = new MatTableDataSource<CategoryElement>();

  getCategories(): void {
    this.categoryService.getCategories()
      .subscribe({
        next: (data: any) => {
          console.log("Respuesta categories: ", data);
          this.processCategoriesResponse(data);
        },
        error: (error: any) => {
          console.log("Error: ", error);
        }
      });
  }

  processCategoriesResponse(resp: any){
    const dataCategory: CategoryElement[] = [];
    if(resp.metadata[0].code === "200"){ 
      let listCategory = resp.categoryResponse.category;
      listCategory.forEach((element: CategoryElement) => { 
        dataCategory.push(element);
      });
      this.datasource = new MatTableDataSource<CategoryElement>(dataCategory); 
    }
  }
}
export interface CategoryElement{
    description: string;
    id: number;
    name: string;
}