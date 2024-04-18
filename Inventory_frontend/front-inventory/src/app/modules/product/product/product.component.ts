import { Component, OnInit, ViewChild, inject } from '@angular/core';
import { MatTableDataSource } from '@angular/material/table';
import { MatPaginator } from '@angular/material/paginator';
import { ProductService } from '../../shared/services/product.service';

@Component({
  selector: 'app-product',
  templateUrl: './product.component.html',
  styleUrls: ['./product.component.css']
})
export class ProductComponent implements OnInit{

  private productService : ProductService = inject(ProductService);

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
        element.category = element.category.name;
        element.picture = 'data:image/jpeg;base64,' + element.picture;
        dateProduct.push(element);
      });

      this.datasource = new MatTableDataSource<ProductElement>(dateProduct);
      this.datasource.paginator = this.paginator;
    }
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
