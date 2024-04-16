import { Component, OnInit, inject } from '@angular/core';
import { MatDialog } from '@angular/material/dialog';
import { MatTableDataSource } from '@angular/material/table';
import { CategoryService } from 'src/app/modules/shared/services/category.service';
import { NewCategoryComponent } from '../new-category/new-category.component';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { ConfirmComponent } from 'src/app/modules/shared/components/confirm/confirm.component';

@Component({
  selector: 'app-category',
  templateUrl: './category.component.html',
  styleUrls: ['./category.component.css']
})
export class CategoryComponent implements OnInit {

  constructor() {} 

  private categoryService = inject(CategoryService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);

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
      if(result == 1){
        this.openSnackBar("Categoría agregada", "Éxito");
        this.getCategories();
      } else if (result == 2){
        this.openSnackBar("Se ha producido un error al guardar la categoría", "Error");
      }
    });
  }

  edit(id: number, name: string, description: string){
    const dialogRef = this.dialog.open( NewCategoryComponent, {
      width: '450px',
      data: {id: id, name: name, description: description}
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      if(result == 1){
        this.openSnackBar("Categoría Actualizada", "Éxito");
        this.getCategories();
      } else if (result == 2){
        this.openSnackBar("Se ha producido un error al actualizar la categoría", "Error");
      }
    });
  }

  delete(id: any){
    const dialogRef = this.dialog.open( ConfirmComponent, {
      data: {id: id}
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      if(result == 1){
        this.openSnackBar("Categoría Eliminada", "Éxito");
        this.getCategories();
      } else if (result == 2){
        this.openSnackBar("Se ha producido un error al eliminar la categoría", "Error");
      }
    });
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 4000
    });
  }
}


export interface CategoryElement{
    description: string;
    id: number;
    name: string;
}