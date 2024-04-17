import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

const base_url = "http://localhost:8080/api/v1";

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
    this.http.get(endpoint);
  }
}
