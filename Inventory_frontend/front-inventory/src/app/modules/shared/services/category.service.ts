import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})

export class CategoryService {
  private readonly base_url = 'http://localhost:8081/api/v1'; // Ahora es una propiedad constante

  constructor(private http: HttpClient) { }

  /**
   * Get all categories
   */
  getCategories(){
    const endpoint = `${this.base_url}/categories`;
    return this.http.get(endpoint);
  }

  /**
   * Save category
   */
  saveCategory(body: any){
    const endpoint =  `${this.base_url}/categories`;
    return this.http.post(endpoint, body);
  }
}
