export interface ITireBrand {
  id: number;
  name?: string | null;
  logoUrl?: string | null;
}

export type NewTireBrand = Omit<ITireBrand, 'id'> & { id: null };
