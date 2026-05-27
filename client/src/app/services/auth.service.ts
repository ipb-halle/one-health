import axios from 'axios';

export interface LoginRequest {
    username: string;
    password: string;
}

export interface User {
    id: string;
    username: string;
    email?: string;
    role: 'VIEWER' | 'CURATOR' | 'ADMIN';
    enabled: boolean;
}

export interface LoginResponse {
    token: string;
    expiresInSeconds: number;
    user: User;
}

class AuthService {
    async login(payload: LoginRequest): Promise<LoginResponse> {
        const res = await axios.post<LoginResponse>('/auth/login', payload);
        return res.data;
    }

    logout() {
        localStorage.removeItem('token');
        localStorage.removeItem('user');
    }

    setSession(data: LoginResponse) {
        localStorage.setItem('token', data.token);
        localStorage.setItem('user', JSON.stringify(data.user));
    }

    getToken(): string | null {
        return localStorage.getItem('token');
    }

    getUser(): User | null {
        const raw = localStorage.getItem('user');
        return raw ? JSON.parse(raw) : null;
    }

    isAuthenticated(): boolean {
        return !!this.getToken();
    }
}

export const authService = new AuthService();