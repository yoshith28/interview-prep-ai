import { Component } from '@angular/core';
import { FormBuilder, FormGroup } from '@angular/forms';

@Component({
  selector: 'app-dashboard',
  templateUrl: './dashboard.component.html',
  styleUrls: ['./dashboard.component.css']
})
export class DashboardComponent {
  sessions: any[] = [];
  filteredSessions: any[] = [];
  filterForm: FormGroup;

  constructor(private fb: FormBuilder) {
    this.filterForm = this.fb.group({
      topic: [''],
      date: ['']
    });
    this.sessions = this.getMockSessions();
    this.filteredSessions = this.sessions;
  }

  getMockSessions() {
    return [
      {
        topic: 'Java',
        date: '2025-08-17',
        questions: [
          'What is polymorphism in Java?',
          'Explain the concept of JVM.'
        ],
        answers: [
          'Polymorphism allows objects to be treated as instances of their parent class rather than their actual class.',
          'JVM is the Java Virtual Machine that runs Java bytecode.'
        ],
        feedback: [
          'Good explanation, but add more about method overriding.',
          'Correct, but mention platform independence.'
        ]
      },
      {
        topic: 'DBMS',
        date: '2025-08-16',
        questions: [
          'What is normalization in DBMS?',
          'Explain ACID properties.'
        ],
        answers: [
          'Normalization is the process of organizing data to reduce redundancy.',
          'ACID stands for Atomicity, Consistency, Isolation, Durability.'
        ],
        feedback: [
          'Good, mention normal forms.',
          'Correct, but elaborate on each property.'
        ]
      }
    ];
  }

  onFilter() {
    const { topic, date } = this.filterForm.value;
    this.filteredSessions = this.sessions.filter(session => {
      const topicMatch = !topic || session.topic === topic;
      const dateMatch = !date || session.date === date;
      return topicMatch && dateMatch;
    });
  }

  onReset() {
    this.filterForm.reset();
    this.filteredSessions = this.sessions;
  }
}
