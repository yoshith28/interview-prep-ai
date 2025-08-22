import { Component } from '@angular/core';
import { FormBuilder, FormGroup, Validators, FormArray } from '@angular/forms';
import { QuestionService } from '../../services/question.service';

@Component({
  selector: 'app-interview',
  templateUrl: './interview.component.html',
  styleUrls: ['./interview.component.css']
})
export class InterviewComponent {
  topics = ['Java', 'Spring','DBMS','Angular','JDBC','Microservices','Gen Ai'];
  selectedTopic: string | null = null;
  questions: string[] = [];
  feedbacks: any[] = [];
  loadingQuestions = false;
  submitting = false;
  feedbackReady = false;
  showCorrectAnswerIndex: number | null = null;
  showDetailsIndex: number | null = null;

  answerForm: FormGroup;

  constructor(private fb: FormBuilder, private questionService: QuestionService) {
    this.answerForm = this.fb.group({ answers: this.fb.array([]) });
  }

  get answersArray() {
    return this.answerForm.get('answers') as FormArray;
  }

  selectTopic(topic: string) {
    this.selectedTopic = topic;
    this.loadingQuestions = true;
    this.feedbackReady = false;
    this.questionService.getQuestions(topic).subscribe({
      next: (questions) => {
        this.questions = questions.map((q: any) => q.question);
        this.feedbacks = Array(this.questions.length).fill({ summary: '', correctAnswer: '', score: '', details: '' });
        this.answerForm.setControl('answers', this.fb.array(this.questions.map(() => this.fb.control('', Validators.required))));
        this.loadingQuestions = false;
      },
      error: () => {
        this.questions = [];
        this.loadingQuestions = false;
      }
    });
  }

  async submitAnswers() {
    if (this.answerForm.invalid) {
      this.answersArray.markAllAsTouched();
      return;
    }
    this.submitting = true;
    const answerObservables = this.questions.map((question, i) =>
      this.questionService.submitAnswer(question, this.answersArray.at(i).value)
    );
    try {
      const feedbacks: any[] = await Promise.all(answerObservables.map(obs => obs.toPromise()));
      this.feedbacks = feedbacks.map(fb => {
        let clean = fb;
        if (typeof clean === 'string') {
          // Remove assignment like 'clean =' or 'Feedback:'
          clean = clean.replace(/^\s*(clean\s*=|Feedback:)\s*/i, '');
          // Remove code block markers (```json, ```)
          clean = clean.replace(/```json|```/gi, '');
          // Remove any leading/trailing whitespace and newlines
          clean = clean.trim();
        }
        try {
          return JSON.parse(clean);
        } catch {
          return { summary: clean, correctAnswer: '', score: '', details: '' };
        }
      });
      this.feedbackReady = true;
      this.submitting = false;
    } catch {
      this.feedbacks = this.questions.map(() => ({ summary: 'Error getting feedback.', correctAnswer: '', score: '', details: '' }));
      this.feedbackReady = true;
      this.submitting = false;
    }
  }

  resetInterview() {
    this.selectedTopic = null;
    this.questions = [];
    this.feedbacks = [];
    this.answerForm.setControl('answers', this.fb.array([]));
    this.feedbackReady = false;
    this.showCorrectAnswerIndex = null;
    this.showDetailsIndex = null;
  }

  openCorrectAnswer(i: number) {
    this.showCorrectAnswerIndex = i;
  }

  closeCorrectAnswer() {
    this.showCorrectAnswerIndex = null;
  }

  openDetails(i: number) {
    this.showDetailsIndex = i;
  }

  closeDetails() {
    this.showDetailsIndex = null;
  }
}
