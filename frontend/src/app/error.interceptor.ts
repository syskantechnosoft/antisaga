import { HttpInterceptorFn, HttpErrorResponse } from '@angular/common/http';
import { catchError, throwError } from 'rxjs';
import { inject } from '@angular/core';
import { ToastService } from './toast.service';

export const ErrorInterceptor: HttpInterceptorFn = (req, next) => {
    const toast = inject(ToastService); // Inject ToastService in functional interceptor

    return next(req).pipe(
        catchError((error: HttpErrorResponse) => {
            let errorMessage = 'An unknown error occurred!';

            if (error.error instanceof ErrorEvent) {
                // Client-side error
                errorMessage = `Error: ${error.error.message}`;
            } else {
                // Server-side error
                if (error.status === 0) {
                    errorMessage = 'Cannot connect to Server. Please check if backend is running.';
                } else {
                    errorMessage = `Error: ${error.message}`;
                    if (error.error && error.error.message) {
                        errorMessage = error.error.message;
                    }
                }
            }

            toast.show(errorMessage, 'error');
            return throwError(() => error);
        })
    );
};
