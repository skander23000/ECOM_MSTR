import { Injectable } from '@angular/core';
import { BehaviorSubject, interval, Observable, Subscription } from 'rxjs';
import { BasketService } from '../basket.service';

@Injectable({
  providedIn: 'root'
})
export class FrontTimerService {
  private initialTime: number = 30 * 60; // 30 minutes en secondes
  private remainingTime: number = this.initialTime;
  private timerSubscription!: Subscription;

  private timerSubject: BehaviorSubject<number> = new BehaviorSubject(this.remainingTime);

  constructor(private basketService: BasketService) {
    this.startTimer();
  }

  // Démarre le minuteur
  private startTimer(): void {
    this.timerSubscription = interval(1000).subscribe(() => {
      this.remainingTime--;

      if (this.remainingTime <= 0) {
        this.remainingTime = 0;
        this.timerSubscription.unsubscribe();
        this.onTimerComplete();  // Appelle les fonctions lorsque le minuteur est terminé
      }

      this.timerSubject.next(this.remainingTime);  // Met à jour l'état du minuteur
    });
  }

  // Récupère l'état du minuteur sous forme d'Observable
  getTimerState(): Observable<number> {
    return this.timerSubject.asObservable();
  }

  // Ajoute du temps au minuteur
  addTime(seconds: number): void {
    this.remainingTime += seconds;
    if (!this.timerSubscription || this.timerSubscription.closed) {
      this.startTimer();  // Redémarre si le minuteur est arrêté
    }
  }

  addActivity(): void{
    this.addTime(5 * 60)
  }

  // Fonction appelée lorsque le minuteur atteint zéro
  private onTimerComplete(): void {
    this.basketService.wipe()
  }

  // Reset le minuteur à son état initial
  resetTimer(): void {
    this.remainingTime = this.initialTime;
    if (!this.timerSubscription || this.timerSubscription.closed) {
      this.startTimer();  // Redémarre si le minuteur est arrêté
    }
    this.timerSubject.next(this.remainingTime);
  }
}
