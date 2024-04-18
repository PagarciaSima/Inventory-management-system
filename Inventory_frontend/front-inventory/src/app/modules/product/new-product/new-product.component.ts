import { Component, OnInit, inject } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { ProductService } from '../../shared/services/product.service';
import { MAT_DIALOG_DATA, MatDialogRef } from '@angular/material/dialog';
import { CategoryService } from '../../shared/services/category.service';

@Component({
  selector: 'app-new-product',
  templateUrl: './new-product.component.html',
  styleUrls: ['./new-product.component.css']
})

export class NewProductComponent implements OnInit{
 
  public productForm!: FormGroup;
  public estadoFormulario: string = "Agregar Producto";

  public categories: Category [] = [];
  private selectedFile: any;
  nameImg: string = "";

  private formBuilder = inject(FormBuilder);
  private productService = inject(ProductService);
  private categoryService = inject(CategoryService);
  private dialogRef = inject(MatDialogRef);
  private data = inject(MAT_DIALOG_DATA);

  ngOnInit(): void {
    this.productForm = this.formBuilder.group({
      name: ['', Validators.required],
      price: ['', Validators.required],
      quantity: ['', Validators.required],
      category: ['', Validators.required],
      picture: ['', Validators.required]
    });

    this.getCategories();

    if (this.data != null) {
      this.updateForm(this.data);
      this.estadoFormulario = "Actualizar producto";
    }
  }

  onSave(){
    let data = {
      name: this.productForm.get('name')?.value,
      price: this.productForm.get('price')?.value,
      quantity: this.productForm.get('quantity')?.value,
      category: this.productForm.get('category')?.value,
      picture: this.selectedFile
    }

    const uploadImageData = new FormData();
    uploadImageData.append('picture', data.picture, data.picture.name);
    uploadImageData.append('name', data.name);
    uploadImageData.append('price', data.price);
    uploadImageData.append('quantity', data.quantity);
    uploadImageData.append('price', data.price);
    uploadImageData.append('categoryId', data.category);

    if(this.data != null){
      this.productService.updateProduct(uploadImageData, this.data.id)
        .subscribe({
          next: (data: any) => {
            this.dialogRef.close(1);
          },
          error: (error: any) => {
            this.dialogRef.close(2);
          }
        })
    } else{
      this.productService.saveProduct(uploadImageData)
        .subscribe({
          next: (data: any) => {
            this.dialogRef.close(1);
          },
          error: (error: any) => {
            this.dialogRef.close(2);
          }
        })
    }
  }

  onCancel(){
    this.dialogRef.close(3);
  }

  getCategories(){
    this.categoryService.getCategories()
      .subscribe({
        next: (data: any) => {
          this.categories = data.categoryResponse.category;
        },
        error: (error: any) => {
          console.log("Error al consultar categorías");
        }
      });
  }

  onFileChange(event: any){
    this.selectedFile = event.target.files[0];
    console.log(this.selectedFile);
    this.nameImg = this.selectedFile.name;
  }

  updateForm(data: any){
    this.productForm = this.formBuilder.group({
      name: [data.name, Validators.required],
      price: [data.price, Validators.required],
      quantity: [data.quantity, Validators.required],
      category: [data.category.id, Validators.required],
      picture: ['', Validators.required]
    });
  }
}

export interface Category{
  description: string;
  id: number;
  name: string;
}
