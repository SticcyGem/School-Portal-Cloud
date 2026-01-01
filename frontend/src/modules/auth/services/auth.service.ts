import {post} from '../../../lib/api.ts';
import type {AuthResponse, LoginCredentials} from '../models/auth.types.ts';

const COOKIE_OPTS = '; path=/; SameSite=Lax; Secure';

export class AuthService {
    static async login(credentials: LoginCredentials): Promise<AuthResponse> {
        return post<AuthResponse>('/api/v1/auth/login', {
            email: credentials.email,
            password: credentials.password
        });
    }

    static saveSession(data: AuthResponse) {
        localStorage.setItem('auth_token', data.token);
        document.cookie = `auth_token=${data.token}${COOKIE_OPTS}`;
        const role = data.roles?.length ? data.roles[0] : 'STUDENT';
        document.cookie = `user_role=${role}${COOKIE_OPTS}`;
    }
}