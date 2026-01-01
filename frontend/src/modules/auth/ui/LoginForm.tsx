import React, { useState } from 'react';
import { AuthService } from '../services/auth.service.ts';
import type { BackendError } from '../models/auth.types.ts';

export const LoginForm = () => {
    const [email, setEmail] = useState('');
    const [password, setPassword] = useState('');
    const [isLoading, setIsLoading] = useState(false);

    // General Errors
    const [globalError, setGlobalError] = useState<string | null>(null);

    // Field Errors
    const [fieldErrors, setFieldErrors] = useState<Record<string, string>>({});

    // Animation State
    const [isShaking, setIsShaking] = useState(false);

    const triggerShake = () => {
        setIsShaking(true);
        setTimeout(() => setIsShaking(false), 400);
    };

    const handleSubmit = async (e: React.FormEvent) => {
        e.preventDefault();
        setGlobalError(null);
        setFieldErrors({});

        let hasError = false;
        const newFieldErrors: Record<string, string> = {};

        if (!email) {
            newFieldErrors.email = "Email is required";
            hasError = true;
        }
        if (!password) {
            newFieldErrors.password = "Password is required";
            hasError = true;
        }

        if (hasError) {
            setGlobalError("Invalid email or password");
            setFieldErrors(newFieldErrors);
            triggerShake();
            return;
        }

        setIsLoading(true);

        try {
            const data = await AuthService.login({ email, password });
            AuthService.saveSession(data);
            window.location.href = '/dashboard';
        } catch (error: any) {
            console.error("Login Error:", error);

            if (error.message && error.message.includes('Unable to connect')) {
                setGlobalError('Server unreachable. Please try again later.');
            } else {
                const backendError = error as BackendError;
                setGlobalError('Invalid email or password');

                if (backendError.fields) {
                    setFieldErrors(backendError.fields);
                } else {
                    setFieldErrors({email: 'error', password: 'error'});
                }
            }
            triggerShake();
            setPassword('');
        } finally {
            setIsLoading(false);
        }
    };

    const getInputClass = (fieldName: string) => {
        const baseClass = "w-full p-[10px] text-sm border rounded-[15px] outline-none transition-all duration-300 focus:ring-1";
        const errorClass = "border-red-600 text-red-600 focus:ring-red-600 placeholder-red-400";
        const normalClass = "border-plm-gold text-text-gray focus:border-plm-navy focus:text-black focus:ring-plm-navy";

        const hasError = fieldErrors[fieldName] || (Object.keys(fieldErrors).length > 0 && fieldErrors['all']);

        return `${baseClass} ${hasError ? errorClass : normalClass} ${fieldName === 'email' ? 'lg:pr-10' : 'pr-10'}`;
    };

    return (
        <div className={isShaking ? "animate-shake" : ""}>
            <form onSubmit={handleSubmit} className="w-full">
                <div className="mb-[15px] w-full">
                    <input
                        type="text"
                        name="email"
                        placeholder="Email"
                        aria-label="Email"
                        value={email}
                        onChange={(e) => setEmail(e.target.value)}
                        className={getInputClass('email')}
                    />
                </div>

                <div className="mb-[15px] w-full relative">
                    <input
                        type="password"
                        name="password"
                        placeholder="Password"
                        aria-label="Password"
                        value={password}
                        onChange={(e) => setPassword(e.target.value)}
                        className={getInputClass('password')}
                    />
                </div>

                <div className="flex justify-center mt-2.5">
                    <button
                        type="submit"
                        disabled={isLoading}
                        className="bg-plm-navy text-white px-[30px] py-2 border-none rounded-[15px] font-bold text-sm cursor-pointer transition-colors duration-300 hover:bg-[#24286b] disabled:opacity-50 disabled:cursor-not-allowed"
                    >
                        {isLoading ? 'Logging in...' : 'Log In'}
                    </button>
                </div>

                <a href="/forgot-password" className="block text-center mt-2.5 text-plm-navy no-underline text-sm font-century hover:underline">
                    Forgot Password?
                </a>

                {globalError && (
                    <p className="absolute bottom-[15px] lg:bottom-[25px] left-0 w-full text-center text-sm text-error-red font-sans font-bold">
                        {globalError}
                    </p>
                )}
            </form>
        </div>
    );
};