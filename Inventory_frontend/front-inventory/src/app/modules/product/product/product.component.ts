import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ProductService } from '../../shared/services/product.service';
import { MatDialog } from '@angular/material/dialog';
import { MatSnackBar, MatSnackBarRef, SimpleSnackBar } from '@angular/material/snack-bar';
import { NewProductComponent } from '../new-product/new-product.component';
import { ConfirmComponent } from '../../shared/components/confirm/confirm.component';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit{

  private productService : ProductService = inject(ProductService);
  private dialog = inject(MatDialog);
  private snackBar = inject(MatSnackBar);
  
  constructor() {}

  ngOnInit(): void {
    this.getProducts();
  }

  displayedColumns: string[] = ['id', 'name', 'price', 'quantity', 'category', 'picture', 'actions'];
  datasource = new MatTableDataSource<ProductElement>();

  @ViewChild(MatPaginator)
  paginator!: MatPaginator;

  getProducts(){
    this.productService.getProducts() 
      .subscribe({
        next: (data: any) => {
          console.log("Respuesta get productos: ", data);
          this.processProductResponse(data);
        }, 
        error: (error: any) => {
          console.log("Error get productos: ", error);
        }
      });
  }

  processProductResponse(response: any){
    const dateProduct: ProductElement[] = [];
    if(response.metadata[0].code === "200") {
      let listProducts = response.productResponse.products;

      listProducts.forEach((element: ProductElement) => {
        element.picture = 'data:image/jpeg;base64,' + element.picture;
        dateProduct.push(element);
      });

      this.datasource = new MatTableDataSource<ProductElement>(dateProduct);
      this.datasource.paginator = this.paginator;
    }
  }

  openProductDialog(){
    const dialogRef = this.dialog.open( NewProductComponent, {
      width: '450px'
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      if(result == 1){
        this.openSnackBar("Producto agregado", "Éxito");
        this.getProducts();
      } else if (result == 2){
        this.openSnackBar("Se ha producido un error al guardar el producto", "Error");
      }
    });
  }

  openSnackBar(message: string, action: string) : MatSnackBarRef<SimpleSnackBar>{
    return this.snackBar.open(message, action, {
      duration: 4000
    });
  }

  edit(id: number, name: string, price: number, quantity: number, category: any){
    const dialogRef = this.dialog.open( NewProductComponent, {
      width: '450px',
      data: {id: id, name: name, price: price, quantity: quantity, category: category}
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      if(result == 1){
        this.openSnackBar("Producto actualizado", "Éxito");
        this.getProducts();
      } else if (result == 2){
        this.openSnackBar("Se ha producido un error al actualizar el producto", "Error");
      }
    });
  }

  delete(id: number){
    const dialogRef = this.dialog.open( ConfirmComponent, {
      width: '450px',
      data: {id: id, module: "product"}
    });

    dialogRef.afterClosed().subscribe((result: any) => {
      if(result == 1){
        this.openSnackBar("Producto eliminado", "Éxito");
        this.getProducts();
      } else if (result == 2){
        this.openSnackBar("Se ha producido un error al eliminar el producto", "Error");
      }
    });
  }

  buscar(nombre: any){
    if (nombre.length === 0){
      return this.getProducts();
    } 

    this.productService.getProductByName(nombre)
    .subscribe({
      next: (resp: any) => {
        this.processProductResponse(resp);
      }
    });
  }

}

export interface ProductElement {
  id: number;
  name: string;
  price: number;
  quantity: number;
  category: any;
  picture: any;
}
