export interface ApiError {
  timestamp: string;
  status: number;
  error: string;
  code: string;
  message: string;
  path: string;
  violations?: Violation[];
}

export interface Violation {
  field: string;
  message: string;
}
