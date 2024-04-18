import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const base_url = "http://localhost:8081/api/v1";

@Injectable({
  providedIn: 'root'
})
export class ProductService {

  constructor(private http: HttpClient) { }

  /**
   * Get all products
   */
  getProducts(){
    const endpoint = `${base_url}/products`;
    return this.http.get(endpoint);
  }

  /**
   * Save product
   */
  saveProduct(body: any){
    const endpoint = `${base_url}/products`;
    return this.http.post(endpoint, body);
  }

  /**
   * Update product
   */
  updateProduct(body: any, id: any){
    const endpoint = `${base_url}/products/${id}`;
    return this.http.put(endpoint, body);
  }

  /**
   * Delete product
   */
  deleteProduct(id: any){
    const endpoint = `${base_url}/products/${id}`;
    return this.http.delete(endpoint);
  }

  /**
   *  Get product by name
   */
  getProductByName(name: any){
    const endpoint = `${base_url}/products/filter/${name}`;
    return this.http.get(endpoint);
  }

}
