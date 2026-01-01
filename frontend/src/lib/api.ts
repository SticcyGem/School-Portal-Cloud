export const API_BASE = import.meta.env.PUBLIC_API_BASE_URL || 'http://localhost:8080';

class ApiError extends Error {
    public status: number;
    public fields?: Record<string, string>;

    constructor(message: string, status: number, fields?: Record<string, string>) {
        super(message);
        this.status = status;
        this.fields = fields;
        Object.setPrototypeOf(this, ApiError.prototype);
    }
}

export async function post<T>(endpoint: string, body: Record<string, any>): Promise<T> {
    let response;
    try {
        response = await fetch(`${API_BASE}${endpoint}`, {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(body),
        });
    } catch (networkError) {
        console.error("Network Error:", networkError);
        throw new Error("Unable to connect to the server. Please try again later.");
    }

    const data = await response.json();

    if (!response.ok) {
        throw new ApiError(
            data.message || 'An unexpected error occurred',
            response.status,
            data.fields
        );
    }

    return data;
}