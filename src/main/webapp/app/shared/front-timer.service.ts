import { Injectable } from '@angular/core';
import { BehaviorSubject, interval, Observable, Subject, Subscription } from 'rxjs';
import { BasketService } from '../basket.service';
import { Router } from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class FrontTimerService {
  private initialTime = 20; // 30 * 60; // 30 minutes en secondes
  private remainingTime: number = this.initialTime;
  private timerSubscription!: Subscription;
  private isInitialized = false; // Vérifie si le timer est initialisé
  private checkNumber = 0;

  private timerSubject: BehaviorSubject<number>;
  private timerCompleteSubject: Subject<void> = new Subject<void>(); // Sujet pour la notification

  constructor(
    private basketService: BasketService,
    private router: Router,
  ) {
    this.startTimer();
    this.timerSubject = new BehaviorSubject(this.remainingTime);
  }

  // Méthode pour démarrer le minuteur, qui ne s'exécute qu'une seule fois
  startTimer(): void {
    if (!this.isInitialized) {
      // Si le timer n'est pas encore initialisé
      this.isInitialized = true;
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

  getIsInitialized(): boolean {
    return this.isInitialized;
  }

  // Récupère l'état du minuteur sous forme d'Observable
  getTimerState(): Observable<number> {
    return this.timerSubject.asObservable();
  }

  // Ajoute du temps au minuteur
  addTime(seconds: number): void {
    this.remainingTime += seconds;
  }

  addActivity(): void {
    if (this.checkNumber >= 5) {
      this.resetTimer();
      this.checkNumber = 0;
    } else {
      this.checkNumber++;
    }
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

  // Fonction appelée lorsque le minuteur atteint zéro
  private onTimerComplete(): void {
    this.isInitialized = false;
    this.router.navigate(['/']);
    this.basketService.wipe().subscribe();
    this.timerCompleteSubject.next(); // Émet le signal que le minuteur est terminé
  }
}
