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

  /**
   * Update category
   */
  updateCategory(body: any, id: any){
    const endpoint = `${this.base_url}/categories/${id}`;
    return this.http.put(endpoint, body);
  }

  /**
   * Delete Category
   */
  deleteCategory(id: any){
    const endpoint = `${this.base_url}/categories/${id}`;
    return this.http.delete(endpoint);
  }

  /**
   * Get by ID
   */
    getCategoryById(id: any){
      const endpoint = `${this.base_url}/categories/${id}`;
      return this.http.get(endpoint);
  }
  
  /**
   * Export categories to Excel
   */
  exportCategories() {
    const endpoint = `${this.base_url}/categories/export/excel`;
    return this.http.get(endpoint, {
      responseType: 'blob'
    });
  }
}
