import { Component, OnInit, inject } from '@angular/core';
import { Chart } from 'chart.js';
import { ProductElement } from 'src/app/modules/product/product/product.component';
import { ProductService } from 'src/app/modules/shared/services/product.service';

@Component({
  selector: 'app-home',
  templateUrl: './home.component.html',
  styleUrls: ['./home.component.css']
})
export class HomeComponent implements OnInit{

  chartBar: any;
  chartDoughnut: any;

  private productService: ProductService = inject(ProductService);
  ngOnInit(): void {
    this.getProducts();
  }

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
    const nameProduct: string[] = [];
    const quantity: number[] = [];

    if(response.metadata[0].code === "200") {
      let listProducts = response.productResponse.products;

      listProducts.forEach((element: ProductElement) => {
        nameProduct.push(element.name);
        quantity.push(element.quantity);

      });

      // Gráfico barras
      this.chartBar = new Chart('canvas-bar', {
        type: 'bar',
        data: {
          labels: nameProduct,
          datasets: [
            {label: 'Productos', data: quantity}
          ]
        }
      });

      // Gráfico doughnut
      this.chartDoughnut = new Chart('canvas-doughnut', {
        type: 'doughnut',
        data: {
          labels: nameProduct,
          datasets: [
            {label: 'Productos', data: quantity}
          ]
        }
      });

    }
  }
}
