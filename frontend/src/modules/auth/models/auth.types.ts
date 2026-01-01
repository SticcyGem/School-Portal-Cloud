export interface AuthResponse {
    token: string;
    roles: string[];
    account_id: string;
    profile: any;
}

export interface BackendError {
    status: number;
    error: string;
    message: string;
    fields?: Record<string, string>;
}

export interface LoginCredentials {
    email: string;
    password: string;
}