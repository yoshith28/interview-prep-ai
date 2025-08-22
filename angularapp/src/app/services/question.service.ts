
import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class QuestionService {
  private apiUrl = 'http://localhost:8080';

  constructor(private http: HttpClient) { }

  getQuestions(topic: string): Observable<any[]> {
    return this.http.get<any[]>(`${this.apiUrl}/questions`, { params: { topic } });
  }

  submitAnswer(question: string, answer: string): Observable<string> {
    return this.http.post(`${this.apiUrl}/answer`, { question, answer }, { responseType: 'text' });
  }
}
