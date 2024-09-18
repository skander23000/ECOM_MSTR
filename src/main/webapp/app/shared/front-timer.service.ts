import { Injectable } from '@angular/core';
import { BehaviorSubject, interval, Observable, Subject, Subscription } from 'rxjs';
import { BasketService } from '../basket.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class FrontTimerService {
  private initialTime = 30 * 60; // 30 minutes en secondes
  private remainingTime: number = this.initialTime;
  private timerSubscription!: Subscription;
  private isInitialized = false; // Vérifie si le timer est initialisé

  private timerSubject: BehaviorSubject<number>;
  private timerCompleteSubject: Subject<void> = new Subject<void>(); // Sujet pour la notification
  private showTimerError = false;

  constructor(
    private basketService: BasketService,
    private router: Router,
  ) {
    this.timerSubject = new BehaviorSubject(this.remainingTime);
  }

  // Méthode pour démarrer le minuteur, qui ne s'exécute qu'une seule fois
  startTimer(): void {
    if (this.basketService.getNumberOfTires() === 0) {
      return;
    }
    if (!this.isInitialized) {
      // Si le timer n'est pas encore initialisé
      this.isInitialized = true;
      this.remainingTime = this.initialTime;
      this.timerSubscription = interval(1000).subscribe(() => {
        this.remainingTime--;
        if (this.remainingTime <= 0) {
          this.remainingTime = 0;
          this.timerSubscription.unsubscribe();
          this.onTimerComplete();
        }

        this.timerSubject.next(this.remainingTime); // Met à jour le temps restant
      });
    }
  }

  setShowTimerError(value: boolean): void {
    this.showTimerError = value;
  }

  getShowTimerError(): boolean {
    return this.showTimerError;
  }

  getIsInitialized(): boolean {
    return this.isInitialized;
  }

  // Récupère l'état du minuteur sous forme d'Observable
  getTimerState(): Observable<number> {
    return this.timerSubject.asObservable();
  }

  addActivity(): void {
    if (!this.isInitialized) {
      this.startTimer();
    } else {
      this.resetTimer();
    }
  }

  // Reset le minuteur à son état initial
  setTimer(tmps: number): void {
    this.remainingTime = tmps;
    this.timerSubject.next(this.remainingTime);
  }

  // Reset le minuteur à son état initial
  resetTimer(): void {
    this.remainingTime = this.initialTime;
    this.timerSubject.next(this.remainingTime);
  }

  // Observable pour s'abonner à la fin du minuteur
  getTimerComplete(): Observable<void> {
    return this.timerCompleteSubject.asObservable();
  }

  stopTimer(): void {
    this.isInitialized = false; // Réinitialise l'état du minuteur
  }

  // Fonction appelée lorsque le minuteur atteint zéro
  private onTimerComplete(): void {
    if (this.basketService.getNumberOfTires() !== 0 && this.isInitialized) {
      this.router.navigate(['/']);
      this.timerCompleteSubject.next(); // Émet le signal que le minuteur est terminé
      this.basketService.wipe().subscribe();
    }
    this.isInitialized = false;
  }
}
