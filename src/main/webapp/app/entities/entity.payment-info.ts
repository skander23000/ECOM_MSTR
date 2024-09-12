export interface PaymentInfo {
  cardOwner?: string | null;
  cardNumber?: string | null;
  postCode?: string | null;
  expirationDate?: string | null;
  securityCode?: string | null;
  address?: string | null;
  city?: string | null;
}
