import { Component, inject } from '@angular/core';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from '../../services/category.service';
import { ProductService } from '../../services/product.service';

@Component({
  selector: 'app-confirm',
  templateUrl: './confirm.component.html',
  styleUrls: ['./confirm.component.css']
})
export class ConfirmComponent {

  public dialogRef = inject(MatDialogRef);
  public data = inject(MAT_DIALOG_DATA);
  private categoryService = inject(CategoryService);
  private productService = inject(ProductService);

  
  onNoClick(){
    this.dialogRef.close(3);
  }

  delete(){
    if(this.data != null){

      if (this.data.module === 'category'){
        this.categoryService.deleteCategory(this.data.id)
          .subscribe({
            next: (data: any) => {
              this.dialogRef.close(1);
            },
            error: (error: any) => {
              this.dialogRef.close(2);
            }
          });
      } else if (this.data.module === 'product'){
        this.productService.deleteProduct(this.data.id)
          .subscribe({
            next: (data: any) => {
              this.dialogRef.close(1);
            },
            error: (error: any) => {
              this.dialogRef.close(2);
            }
          });
      }
      
    } else{
      this.dialogRef.close(2);
    }
  }
}
