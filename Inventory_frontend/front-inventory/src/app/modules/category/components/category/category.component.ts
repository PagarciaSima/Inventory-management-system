import { Component, OnInit, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { CategoryService } from 'src/app/modules/shared/services/category.service';
import { NewCategoryComponent } from '../new-category/new-category.component';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  constructor() {} 

  private categoryService = inject(CategoryService);
  private dialog = inject(MatDialog);

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

  openCategoryDialog(){
    const dialogRef = this.dialog.open( NewCategoryComponent, {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      // result es de tipo 'any'
    });
  }
}


export interface CategoryElement{
    description: string;
    id: number;
    name: string;
}